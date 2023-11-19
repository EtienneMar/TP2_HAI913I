package processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import model.Edge;
import model.Vertex;
import model.CouplingGraph;
import parsers.EclipseJDTParser;
import visitor.ConstructorInvocationVisitor;
import visitor.ImportDeclarationVisitor;
import visitor.MethodDeclarationVisitor;
import visitor.MethodInvocationVisitor;

public class CouplingAnalyzeJDT {

    // Déclaration des attributs de la classe
    private static CouplingGraph couplingGraphe = new CouplingGraph();
    private static EclipseJDTParser parserEclipse;
    private static Vertex vertexEnd;

    // Constructeur qui initie l'analyse du couplage
    public CouplingAnalyzeJDT(String path) throws NullPointerException, IOException {
        launchAnalyze(path);
    }

    // Méthode pour récupérer le graphe de couplage
    public CouplingGraph getCouplingGraphe() {
        return couplingGraphe;
    }

    // Méthode pour définir le graphe de couplage
    public void setCouplingGraphe(CouplingGraph couplingGraphe) {
        CouplingAnalyzeJDT.couplingGraphe = couplingGraphe;
    }

    // Méthode principale pour lancer l'analyse du couplage
    private static void launchAnalyze(String path) throws FileNotFoundException, IOException {
        parserEclipse = new EclipseJDTParser(path);
        List<CompilationUnit> cus = parserEclipse.parseProject();

        Set<String> listJavaFile = new HashSet<>();
        for (CompilationUnit cu : cus) {
            String packageDotClassName = extractPackageNameDotClassName(cu);
            if(packageDotClassName == null) {continue;}
            listJavaFile.add(packageDotClassName);
        }

        ImportDeclarationVisitor importDeclarationVisitor = new ImportDeclarationVisitor(path, listJavaFile);

        for (CompilationUnit cu : cus) {
            String packageName = extractPackageNameDotClassName(cu);
            if(packageName == null) {continue;}
            Vertex vertexStart = getInformationAboutVertex(cu, packageName);

            cu.accept(importDeclarationVisitor);
            HashMap<String, String> classMap = importDeclarationVisitor.getClassImportMap();
            ConstructorInvocationVisitor constructorInvocationVisitor = new ConstructorInvocationVisitor();
            cu.accept(constructorInvocationVisitor);
            visitConstrutor(cu, classMap, vertexStart);
            visitMethodInvocation(cu, classMap, vertexStart);
        }
    }

    // Méthodes pour la visite des constructeurs et invocations de méthodes
    private static void visitConstrutor(CompilationUnit cu, HashMap<String, String> classImportMap, Vertex vertexStart) {
        ConstructorInvocationVisitor constructorInvocationVisitor = new ConstructorInvocationVisitor();
        cu.accept(constructorInvocationVisitor);
        for(ClassInstanceCreation classInstanceCreation : constructorInvocationVisitor.getMethods()){
            if (classInstanceCreation.getType().isPrimitiveType() || classInstanceCreation.getType().isArrayType()) {
                continue;
            } else {
                String vertexName = classInstanceCreation.getType().resolveBinding().getName();
                analyze(vertexName, cu, vertexStart, classImportMap);
            }
        }
    }

    private static void visitMethodInvocation(CompilationUnit cu, HashMap<String, String> classImportMap, Vertex vertexStart) {
        MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
        cu.accept(methodInvocationVisitor);
        Iterator<MethodInvocation> iterator = methodInvocationVisitor.getMethods().iterator();

        while (iterator.hasNext()) {
            MethodInvocation methodInvocation = iterator.next();
            String vertexName = resolveVertexNameMethodInvocation(methodInvocation);
            if (vertexName != null && analyze(vertexName, cu, vertexStart, classImportMap)) {
                iterator.remove();
            }
        }
    }

    // Méthode d'analyse des invocations pour déterminer le couplage
    private static boolean analyze(String vertexName, CompilationUnit cu, Vertex vertexStart, HashMap<String, String> classMap) {
        String vertexNameCapitalized = vertexName.toUpperCase();
        String correspondingClass = classMap.get(vertexNameCapitalized);
        if(correspondingClass != null) {
            vertexEnd = getInformationAboutVertex(cu, correspondingClass);
            addEdgeToTheGraphe(vertexStart, vertexEnd);
            return true;
        }
        return false;
    }

    // Méthodes pour résoudre le nom du vertex lors d'une invocation de méthode
    private static String resolveVertexNameMethodInvocation(MethodInvocation methodInvocation) {
        // Différentes manières de résoudre le nom du vertex
        if (methodInvocation.getExpression() != null && methodInvocation.getExpression().resolveTypeBinding() != null) {
            return methodInvocation.getExpression().resolveTypeBinding().getName();
        }
        else if (methodInvocation.resolveMethodBinding() != null) {
            return methodInvocation.resolveMethodBinding().getDeclaringClass().getTypeDeclaration().getQualifiedName();
        }
        else if (methodInvocation.getExpression() != null && methodInvocation.getExpression().toString() != null) {
            return methodInvocation.getExpression().toString();
        }
        else if (methodInvocation.getName() != null) {
            return methodInvocation.getName().toString();
        }
        return null;
    }

    // Méthodes pour extraire des informations sur les packages et les classes
    public static String extractPackageNameDotClassName(CompilationUnit cu) {
        String packageDotClassName = extractPackageName(cu);
        if(packageDotClassName == null) {return null;}
        String className = extractClassName(cu);
        if(className == null) {return null;}
        packageDotClassName += "." + extractClassName(cu);
        return packageDotClassName;
    }

    public static String extractPackageName(CompilationUnit cu) {
        return cu.getPackage().getName().getFullyQualifiedName();
    }

    public static String extractClassName(CompilationUnit cu) {
        for (Object type : cu.types()) {
            if (type instanceof TypeDeclaration) {
                TypeDeclaration typeDeclaration = (TypeDeclaration) type;
                return typeDeclaration.getName().getFullyQualifiedName();
            }
        }
        return null;
    }

    // Méthode pour obtenir des informations sur un vertex spécifique
    public static Vertex getInformationAboutVertex(CompilationUnit cu, String packageName) {
        Vertex vertex = couplingGraphe.findExistingVertex(packageName);
        if(vertex == null) {
            MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
            cu.accept(methodDeclarationVisitor);
            int numberOfMethodInClass = methodDeclarationVisitor.getMethods().size();
            int numberOfConstructorInClass = methodDeclarationVisitor.getConstructors().size();
            vertex = new Vertex(packageName, numberOfConstructorInClass, numberOfMethodInClass);
            couplingGraphe.addVertex(vertex);
            return vertex;
        } else {
            return vertex;
        }
    }

    // Méthode pour ajouter ou mettre à jour une edge dans le graphe de couplage
    public static void addEdgeToTheGraphe(Vertex vertexStart, Vertex vertexEnd) {
        if(vertexStart.getName().equals(vertexEnd.getName())) {return;}
        Edge existingEdge = couplingGraphe.findExistingEdge(vertexStart.getName(), vertexEnd.getName());
        if (existingEdge != null) {
            existingEdge.incrementnombreAppelEntreLesDeuxClasses();
        } else {
            Edge newEdge = new Edge(vertexStart, vertexEnd);
            couplingGraphe.getListEdge().add(newEdge);
        }
    }

}
