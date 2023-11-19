package processor;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;
import model.Edge;
import model.Vertex;
import model.CouplingGraph;
import parsers.SpoonParser;

public class SpoonCouplingAnalyzer {

    private static CouplingGraph couplingGraph = new CouplingGraph();
    private static SpoonParser parserSpoon;

    public SpoonCouplingAnalyzer(String path) throws IOException {
        launchAnalyze(path);
    }

    public CouplingGraph getCouplingGraph() {
        return couplingGraph;
    }

    private static void launchAnalyze(String path) throws IOException {
        parserSpoon = new SpoonParser(path);
        CtModel model = parserSpoon.parseProject();

        for (CtClass<?> ctClass : model.getElements(new TypeFilter<>(CtClass.class))) {
			String className = ctClass.getQualifiedName();


			Vertex vertexStart = getInformationAboutVertex(ctClass);

			// Filter constructor calls within this class
			List<CtConstructorCall<?>> constructorCalls = ctClass.getElements(new TypeFilter<>(CtConstructorCall.class));
			//System.out.println("Number of Constructor Calls in " + className + ": " + constructorCalls.size());

			for (CtConstructorCall<?> constructorCall : constructorCalls) {
				// Retrieve the class of the constructor being called
				CtTypeReference<?> constructorClassRef = constructorCall.getExecutable().getDeclaringType();

				// Check if the constructor class is part of the Java standard packages
				if (constructorClassRef == null || constructorClassRef.getQualifiedName().startsWith("java.")) {
					continue;  // Skip this constructor call
				}

				// Print the qualified name of the constructor class
				String constructorClassName = constructorClassRef != null ? constructorClassRef.getQualifiedName() : "Unknown";
				//System.out.println("Constructor Class: " + constructorClassName);

				// Print the source code of the constructor call
				//System.out.println("Constructor Call: " + constructorCall.toString());
				//System.out.println("---------------------------------------------------");
				


				CtType<?> ctTypeClassConstructor = constructorClassRef.getTypeDeclaration();

				if (ctTypeClassConstructor != null) {
					analyze(ctTypeClassConstructor, vertexStart);
				}

			}

			// Filter method calls within this class
			List<CtInvocation<?>> methodInvocations = ctClass.getElements(new TypeFilter<>(CtInvocation.class));
			for(CtInvocation<?> methodInvocation  : methodInvocations) {
				CtExecutableReference<?> executable = methodInvocation.getExecutable();
				CtTypeReference<?> declaringTypeRef = executable != null ? executable.getDeclaringType() : null;
				
				if(declaringTypeRef == null) {
					declaringTypeRef = methodInvocation.getExecutable().getType();
				}
				
				// Filter method without a declaringTypeRef like save in the implementation of CRUD
				if(declaringTypeRef == null) {
					if (methodInvocation.getTarget() instanceof CtVariableAccess) {
				        CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) methodInvocation.getTarget();
				        CtVariableReference<?> variable = variableAccess.getVariable();
				        declaringTypeRef = variable.getType();
					}
				}
				
				if(declaringTypeRef == null || declaringTypeRef.getQualifiedName().startsWith("java.") || declaringTypeRef.getQualifiedName().startsWith("javax")) {
					System.err.println(methodInvocation.getExecutable());
					//System.out.println(methodInvocation.getTarget());
					if (methodInvocation.getTarget() instanceof CtVariableAccess) {
			        CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) methodInvocation.getTarget();
			        CtVariableReference<?> variable = variableAccess.getVariable();
			        System.out.println(variable.getType());
					}
					continue;
				}

				// Check if the declaring type of the method is different from the current class
				if (!declaringTypeRef.getQualifiedName().equals(className)) {

					CtType<?> ctTypeClassMethod = declaringTypeRef.getTypeDeclaration();

					if (ctTypeClassMethod != null) {
						analyze(ctTypeClassMethod, vertexStart);
					}

				}
			}
		}
		couplingGraph.getListEdge().stream().filter(edge -> edge.getCouplageMetric() > 0.00 ) 
        .collect(Collectors.toCollection(ArrayList::new));

	}

	public static Vertex getInformationAboutVertex(CtClass<?> ctClass) {
		String className = ctClass.getQualifiedName();
		Vertex vertex = couplingGraph.findExistingVertex(className);
		// Count the number of constructors in the class
		int numberOfConstructorInClass = ctClass.getConstructors().size();

		if (vertex == null) {
			// Count the number of methods in the class
			int numberOfMethodInClass = ctClass.getMethods().size();
			
			// Create and add the new vertex
			vertex = new Vertex(className, numberOfConstructorInClass, numberOfMethodInClass);
			couplingGraph.addVertex(vertex);
		}
		vertex.setNumberOfConstructorInClass(numberOfConstructorInClass);
		return vertex;
	}

	public static Vertex getInformationAboutVertex(CtType<?> ctType) {
		String className = ctType.getQualifiedName();
		Vertex vertex = couplingGraph.findExistingVertex(className);
		// Count the number of constructors in the class

		if (vertex == null) {
			// Count the number of methods in the class
			int numberOfMethodInClass = ctType.getMethods().size();

			// Create and add the new vertex
			vertex = new Vertex(className, numberOfMethodInClass);
			couplingGraph.addVertex(vertex);
		}
		return vertex;
	}
	private static void analyze(CtType<?> ctTypeClassMethod, Vertex vertexStart) {
		Vertex vertexEnd = getInformationAboutVertex(ctTypeClassMethod);
		addEdgeToTheGraphe(vertexStart, vertexEnd);
	}

	public static void addEdgeToTheGraphe(Vertex vertexStart, Vertex vertexEnd) {
		//Eliminate edge on the the same class (when method is call in main and generique)
		if(vertexStart.getName().equals(vertexEnd.getName())) {return;}
		Edge existingEdge = couplingGraph.findExistingEdge(vertexStart.getName(), vertexEnd.getName());
		if (existingEdge != null) {
			// Edge already existing adding metric
			existingEdge.incrementnombreAppelEntreLesDeuxClasses();
		} else {
			// Create a new edge with initial metric
			Edge newEdge = new Edge(vertexStart, vertexEnd);
			couplingGraph.getListEdge().add(newEdge);
		}
	}


}