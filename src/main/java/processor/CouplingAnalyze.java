package processor;

import java.io.File;
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
import model.couplingGraph;
import parsers.EclipseJDTParser;
import visitor.ConstructorInvocationVisitor;
import visitor.ImportDeclarationVisitor;
import visitor.MethodDeclarationVisitor;
import visitor.MethodInvocationVisitor;

public class CouplingAnalyze {

	private static couplingGraph couplingGraphe = new couplingGraph();
	private static EclipseJDTParser parserEclipse;
	private static Vertex vertexEnd; 

	public CouplingAnalyze(String path) throws NullPointerException, IOException {
		launchAnalyze(path);
	}

	public couplingGraph getCouplingGraphe() {
		return couplingGraphe;
	}

	public  void setCouplingGraphe(couplingGraph couplingGraphe) {
		CouplingAnalyze.couplingGraphe = couplingGraphe;
	}

	private static void launchAnalyze(String path) throws FileNotFoundException, IOException {

		
		parserEclipse = new EclipseJDTParser(path);
		List<CompilationUnit> cus = parserEclipse.parseProject();
		
		
		Set<String> fileToAnalyze = new HashSet<>();
		for (CompilationUnit cu : cus) {
			String packageName = extractPackageNameDotClassName(cu);
			fileToAnalyze.add(packageName);
		}
		couplingGraphe.setListFileAnalyze(fileToAnalyze);
		System.out.println(fileToAnalyze.toString());
		
		for (CompilationUnit cu : cus) {
			//Récupérer le nom du package
			String packageName = extractPackageNameDotClassName(cu);
			Vertex vertexStart = getInformationAboutVertex(cu, packageName);

			ImportDeclarationVisitor importDeclarationVisitor = new ImportDeclarationVisitor(path);
			cu.accept(importDeclarationVisitor);
			HashMap<String, String> classImportMap = importDeclarationVisitor.getClassImportMap();

			visitConstrutor(cu, classImportMap, vertexStart);
			visitMethodInvocation(cu, classImportMap, vertexStart);


		}
	}



	private static void visitConstrutor(CompilationUnit cu, HashMap<String, String> classImportMap, Vertex vertexStart) {
		ConstructorInvocationVisitor constructorInvocationVisitor = new ConstructorInvocationVisitor();
		cu.accept(constructorInvocationVisitor);
		for(ClassInstanceCreation classInstanceCreation : constructorInvocationVisitor.getMethods()){
			if (classInstanceCreation.getType().isPrimitiveType() || classInstanceCreation.getType().isArrayType()) {
				continue;
			}else {
				String vertexName = classInstanceCreation.getType().resolveBinding().getName();
				analyze(vertexName, cu, vertexStart, classImportMap);
			}
		}
	}

	private static void visitMethodInvocation(CompilationUnit cu, HashMap<String, String> classImportMap, Vertex vertexStart) {
		//Récupérer le nombre d'invocation de méthode de classe extérieur dans une classe
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
		cu.accept(methodInvocationVisitor);
		Iterator<MethodInvocation> iterator = methodInvocationVisitor.getMethods().iterator();
		//List<MethodInvocation> allMethodInvocations = new ArrayList<>(methodInvocationVisitor.getMethods());


		while (iterator.hasNext()) {
			MethodInvocation methodInvocation = iterator.next();
            String vertexName = resolveVertexNameMethodInvocation(methodInvocation);
            if (vertexName != null && analyze(vertexName, cu, vertexStart, classImportMap)) {
                iterator.remove();
            }
		}
		//System.out.println("Qui reste t'il ? : " );
		//allMethodInvocations.forEach(m -> System.out.println(m.toString()));
	}

	private static boolean analyze(String vertexName, CompilationUnit cu, Vertex vertexStart, HashMap<String, String> classImportMap) {
		String correspondingClass = classImportMap.get(vertexName.toUpperCase());
		if(correspondingClass != null && couplingGraphe.isFileHasToBeAnalyze(correspondingClass)) {
			vertexEnd = getInformationAboutVertex(cu, correspondingClass);
			addEdgeToTheGraphe(vertexStart, vertexEnd);
			return true;
		}
		return false;
	}

	private static String resolveVertexNameMethodInvocation(MethodInvocation methodInvocation) {

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

	public static String extractPackageNameDotClassName(CompilationUnit cu) {
		String packageName = cu.getPackage().getName().getFullyQualifiedName();

		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) type;
				packageName += "." + typeDeclaration.getName().getFullyQualifiedName();
			}
		}

		return packageName;
	}

	public static Vertex getInformationAboutVertex(CompilationUnit cu, String packageName) {
		Vertex vertex = couplingGraphe.findExistingVertex(packageName);
		if(vertex == null) {
			//Récupérer le nombre de méthode possible de la classe A pour la relation binaire
			MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
			cu.accept(methodDeclarationVisitor); 
			int numberOfMethodInClass = methodDeclarationVisitor.getMethods().size();
			//System.out.println("nbr methode" + numberOfMethodInClass);

			//Récupérer le nombre de constructeur de la classe
			int numberOfConstructorInClass = methodDeclarationVisitor.getConstructors().size();
			//System.out.println("nbr constructor" + numberOfConstructorInClass);
			vertex = new Vertex(packageName, numberOfConstructorInClass, numberOfMethodInClass);
			couplingGraphe.addVertex(vertex);
			return vertex;
		}else {
			return vertex;
		}
	}

	public static void addEdgeToTheGraphe(Vertex vertexStart, Vertex vertexEnd) {
		//Eliminate edge on the the same class (when method is call in main and generique)
		if(vertexStart.getName().equals(vertexEnd.getName())) {return;}
		Edge existingEdge = couplingGraphe.findExistingEdge(vertexStart.getName(), vertexEnd.getName());
		if (existingEdge != null) {
			// Une arête existe déjà entre vertex et callingVertex, ajoutez simplement le couplage
			existingEdge.incrementnombreAppelEntreLesDeuxClasses();
		} else {
			// Créez une nouvelle arête avec le couplage initial
			Edge newEdge = new Edge(vertexStart, vertexEnd);
			couplingGraphe.getListEdge().add(newEdge);
		}
	}



}