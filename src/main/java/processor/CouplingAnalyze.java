package processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import model.Edge;
import model.Vertex;
import model.couplingGraph;
import parsers.EclipseJDTParser;
import visitor.ConstructorInvocationVisitor;
import visitor.MethodDeclarationVisitor;
import visitor.MethodInvocationVisitor;

public class CouplingAnalyze {

	private static couplingGraph couplingGraphe = new couplingGraph();
	private static EclipseJDTParser parserEclipse;

	public CouplingAnalyze(String path) throws NullPointerException, IOException {
		launchAnalyze(path);

	}
	
	public couplingGraph getCouplingGraphe() {
		return couplingGraphe;
	}

	public  void setCouplingGraphe(couplingGraph couplingGraphe) {
		CouplingAnalyze.couplingGraphe = couplingGraphe;
	}

	private static void launchAnalyze(String path) throws NullPointerException, IOException {

		parserEclipse = new EclipseJDTParser(path);
		List<File> javaFiles = parserEclipse.listJavaProjectFiles();
		couplingGraphe.setListFileAnalyze(javaFiles);

		for (File content : javaFiles) {
			parserEclipse.configure();
			CompilationUnit cu= parserEclipse.parse(content);

			//Récupérer le nom du package
			String packageName = extractPackageNameDotClassName(cu);
			Vertex vertexStart = getInformationAboutVertex(cu, packageName);

			String vertexName = null;
			Vertex vertexEnd = null;
			//Récupére le nombre d'invocation de constructeur de classe extérieur dans une classe
			ConstructorInvocationVisitor constructorInvocationVisitor = new ConstructorInvocationVisitor();
			cu.accept(constructorInvocationVisitor);
			for(ClassInstanceCreation classInstanceCreation : constructorInvocationVisitor.getMethods()){

				//Chercher qu'est ce que ça gère ? 
				if(classInstanceCreation.resolveTypeBinding() == null && classInstanceCreation.getExpression() == null) {
					vertexName = classInstanceCreation.getType().resolveBinding().getQualifiedName();
					if(couplingGraphe.isFileHasToBeAnalyze(vertexName)) {
						vertexEnd = getInformationAboutVertex(cu, vertexName);
						addEdgeToTheGraphe(vertexStart, vertexEnd);
					}
				}else if (classInstanceCreation.getExpression() == null) {

					vertexName = classInstanceCreation.resolveTypeBinding().getBinaryName();
					if(couplingGraphe.isFileHasToBeAnalyze(vertexName)) {
						vertexEnd = getInformationAboutVertex(cu, vertexName);
						addEdgeToTheGraphe(vertexStart, vertexEnd);
					}	
				}
			}

			//Récupérer le nombre d'invocation de méthode de classe extérieur dans une classe
			MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
			cu.accept(methodInvocationVisitor);
			for(MethodInvocation methodInvocation : methodInvocationVisitor.getMethods()) {
				// Expression explicite
				if (methodInvocation.getExpression() != null) {
					if (methodInvocation.getExpression().resolveTypeBinding() != null) {
						vertexName = methodInvocation.getExpression().resolveTypeBinding().getTypeDeclaration().getQualifiedName();
						if(couplingGraphe.isFileHasToBeAnalyze(vertexName)) {
							vertexEnd = getInformationAboutVertex(cu, vertexName);
							addEdgeToTheGraphe(vertexStart, vertexEnd);
						}
					}
					//expression non explicite
				} else if (methodInvocation.resolveMethodBinding() != null) {
					vertexName = methodInvocation.resolveMethodBinding().getDeclaringClass().getTypeDeclaration().getQualifiedName();
					if(couplingGraphe.isFileHasToBeAnalyze(vertexName)) {
						vertexEnd = getInformationAboutVertex(cu, vertexName);
						addEdgeToTheGraphe(vertexStart, vertexEnd);
					}
				}
			}
		}
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