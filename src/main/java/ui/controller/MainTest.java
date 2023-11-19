package ui.controller;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import model.DendoNode;
import model.Edge;
import model.Vertex;
import parsers.EclipseJDTParser;
import parsers.SpoonParser;
import model.CouplingGraph;
import processor.CouplingAnalyzeJDT;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;
import visitor.ConstructorInvocationVisitor;
import visitor.MethodDeclarationVisitor;
import visitor.MethodInvocationVisitor;

public class MainTest {

	public static CouplingGraph couplingGraph = new CouplingGraph();

	public static void main(String[] args) throws NullPointerException, IOException {

		String path ="C:\\Users\\33683\\Desktop\\Master_ICO_M1\\S7\\Architecture_logicielle_distribuee\\1_Projet\\Projet_Rest\\Question 1 à 2\\tp3.rest.hotel\\src";

		GraphPanel(path);

			SwingUtilities.invokeLater(() -> {
				JFrame frame = new JFrame("AST Graph Viewer");
				frame.setBounds(100, 100, 1000, 800);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				mxGraph graph = new mxGraph();
				graph.setCellsEditable(false); // Désactiver l'édition
				graph.setCellsMovable(false); // Désactiver le déplacement
				graph.setCellsResizable(false); // Désactiver le redimensionnement
				graph.setDropEnabled(false); // Désactiver le glisser-déposer
				graph.setSplitEnabled(false); // Désactiver la divisio
				Object parent = graph.getDefaultParent();

				graph.getModel().beginUpdate();

				try {

					for(Vertex vertex : couplingGraph.getListVertex()) {
						Object vertexOriginGraph = graph.insertVertex(parent, vertex.getName(), vertex.getName(), 20, 20, 80, 30);
						mxRectangle dimensions = graph.getPreferredSizeForCell(vertexOriginGraph);

						// Msj à jour les dimensions du vertex
						graph.resizeCell(vertexOriginGraph, dimensions);
					}

					// Afficher tous les edge existants
					ArrayList<Object> cellList = new ArrayList<>(Arrays.asList(graph.getChildCells(parent)));
					cellList.stream().filter(cell -> graph.getModel().isVertex(cell));

					for (Edge edge : couplingGraph.getListEdge()) {
						String vertexNameA = edge.getVertexOrigin().getName();
						String vertexNameB = edge.getVertexFinal().getName();
						Object vertexA = null;
						Object vertexB = null;
						for(Object cell : cellList) {
							if(graph.getLabel(cell).equals(vertexNameA)) {
								vertexA = cell;
							}
							if(graph.getLabel(cell).equals(vertexNameB)) {
								vertexB = cell;
							}
						}
						if(vertexA != null && vertexB != null) {
							@SuppressWarnings("unused")
							Object e1 = graph.insertEdge(parent, null, edge.getCouplageMetric(), vertexA, vertexB);
						}
					}
					// Utilisez l'algorithme hierarchique pour organiser les vertex
					mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
					// orientation verticale
					layout.setOrientation(SwingConstants.NORTH);
					layout.setIntraCellSpacing(100); // Espacement entre les nœuds dans la même couche
					layout.setInterRankCellSpacing(150); // Espacement entre les couches
					layout.execute(parent);

				} finally {
					graph.getModel().endUpdate();
				}

				mxGraphComponent graphComponent = new mxGraphComponent(graph);
				graphComponent.setPreferredSize(new Dimension(800, 800)); 
				frame.getContentPane().add(graphComponent);
				frame.pack(); 
				frame.setVisible(true);
			}); 
		
	}

	public static void GraphPanel(String path) throws IOException {


		SpoonParser spoon = new SpoonParser(path);
		CtModel model = spoon.parseProject();

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
				System.out.println("Constructor Class: " + constructorClassName);

				// Print the source code of the constructor call
				System.out.println("Constructor Call: " + constructorCall.toString());
				System.out.println("---------------------------------------------------");


				CtType<?> ctTypeClassConstructor = constructorClassRef.getTypeDeclaration();

				if (ctTypeClassConstructor != null) {
					analyze(ctTypeClassConstructor, vertexStart);
				}

			}

			List<CtInvocation<?>> methodInvocations = ctClass.getElements(new TypeFilter<>(CtInvocation.class));
			for(CtInvocation<?> methodInvocation  : methodInvocations) {
				CtExecutableReference<?> executable = methodInvocation.getExecutable();
				CtTypeReference<?> declaringTypeRef = executable != null ? executable.getDeclaringType() : null;
				
				if(declaringTypeRef == null) {
					declaringTypeRef = methodInvocation.getExecutable().getType();
				}
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
		couplingGraph.getListVertex().forEach(v -> System.out.println(v));
		System.out.println(couplingGraph.getListEdge().size());

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
			// Une arête existe déjà entre vertex et callingVertex, ajoutez simplement le couplage
			existingEdge.incrementnombreAppelEntreLesDeuxClasses();
		} else {
			// Créez une nouvelle arête avec le couplage initial
			Edge newEdge = new Edge(vertexStart, vertexEnd);
			couplingGraph.getListEdge().add(newEdge);
		}
	}
}
