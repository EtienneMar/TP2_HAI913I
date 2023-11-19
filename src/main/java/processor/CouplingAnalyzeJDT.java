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

	private static CouplingGraph couplingGraphe = new CouplingGraph();
	private static EclipseJDTParser parserEclipse;
	private static Vertex vertexEnd; 

	public CouplingAnalyzeJDT(String path) throws NullPointerException, IOException {
		launchAnalyze(path);
	}

	public CouplingGraph getCouplingGraphe() {
		return couplingGraphe;
	}

	public  void setCouplingGraphe(CouplingGraph couplingGraphe) {
		CouplingAnalyzeJDT.couplingGraphe = couplingGraphe;
	}

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
		//System.out.println(couplingGraphe.getListFileAnalyze());
		
		for (CompilationUnit cu : cus) {
			//Récupérer le nom du package
			String packageName = extractPackageNameDotClassName(cu);
			if(packageName == null) {continue;}
			Vertex vertexStart = getInformationAboutVertex(cu, packageName);
			
			
			cu.accept(importDeclarationVisitor);
			//System.out.println(importDeclarationVisitor.getClassImportMap());
			HashMap<String, String> classMap = importDeclarationVisitor.getClassImportMap();
			ConstructorInvocationVisitor constructorInvocationVisitor = new ConstructorInvocationVisitor();
			cu.accept(constructorInvocationVisitor);
			//System.out.println(packageName +" : " + constructorInvocationVisitor.getMethods().size());
			//System.out.println(classMap);
			visitConstrutor(cu, classMap, vertexStart);
			visitMethodInvocation(cu, classMap, vertexStart);
		}
	}

	private static void visitConstrutor(CompilationUnit cu, HashMap<String, String> classImportMap, Vertex vertexStart) {
		ConstructorInvocationVisitor constructorInvocationVisitor = new ConstructorInvocationVisitor();
		cu.accept(constructorInvocationVisitor);
		//System.out.println(constructorInvocationVisitor.getMethods().isEmpty());
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