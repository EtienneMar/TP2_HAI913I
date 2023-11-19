package dendogramme;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import model.DendoNode;
import model.Edge;
import model.Vertex;
import model.CouplingGraph;
import processor.CouplingAnalyzeJDT;

public class MainTest {

	public static class EdgeComparator implements Comparator<Edge> {
		@Override
		public int compare(Edge edge1, Edge edge2) {
			double couplage1 = edge1.getCouplageMetric();
			double couplage2 = edge2.getCouplageMetric();

			if (couplage1 < couplage2) {
				return 1;
			} else if (couplage1 > couplage2) {
				return -1;
			} else {
				return edge1.isBetweenVertices(edge2.getVertexFinal().getName(), edge2.getVertexOrigin().getName()) ? 0 : 1;
			}
		}
	}

	public static void main(String[] args) throws NullPointerException, IOException {
		CouplingAnalyzeJDT couplingAnalyze = new CouplingAnalyzeJDT("C:\\\\\\\\Users\\\\\\\\33683\\\\\\\\Desktop\\\\\\\\Master_ICO_M1\\\\\\\\S7\\\\\\\\Architecture_logicielle_distribuee\\\\\\\\1_Projet\\\\\\\\Architecture_Logicielle_Distribuee\\\\\\\\Projet_Rest\\\\\\\\Question_1_a_2\\\\\\\\tp3.rest.hotel");
		//C:\\\\Users\\\\33683\\\\Desktop\\\\Master_ICO_M1\\\\S8\\\\Structure_De_Donnee\\\\TP5
		//C:\\Users\\33683\\Desktop\\Master_ICO_M1\\S8\\Structure_De_Donnee\\TP4
		//C:\\\\Users\\\\33683\\\\Desktop\\\\Master_ICO_M1\\\\S7\\\\Architecture_logicielle_distribuee\\\\1_Projet\\\\Architecture_Logicielle_Distribuee\\\\Projet_Rest\\\\Question_1_a_2\\\\tp3.rest.hotel
		CouplingGraph couplingGraphe = couplingAnalyze.getCouplingGraphe();
		//System.out.println(couplingAnalyze.getCouplingGraphe().getListEdge().size());
		Set<Vertex> listVertex = couplingGraphe.getListVertex();
		Set<Edge> listEdge = couplingGraphe.getListEdge();

		TreeSet<Edge> sortedEdges = new TreeSet<>(new EdgeComparator());
		sortedEdges.addAll(listEdge);

		Dendogramme dendogramme = new Dendogramme();
		for (Vertex vertex : listVertex) {
			dendogramme.addNode(new DendoNode(vertex.getName()));
		}

		System.out.println(sortedEdges.size());
		System.out.println("******************************");
		//dendogramme.getDendogramme().forEach(node -> System.out.println(node.getDendoName()));
		System.out.println("******************************");
		
		

		while (!sortedEdges.isEmpty()) {
			System.out.println(sortedEdges.size());
			Edge edge = sortedEdges.pollFirst(); // Récupère et supprime le premier élément
			if (edge != null) {
				System.out.println("Actuelle arc  : " + edge);
				DendoNode nodeOrigin = dendogramme.isExistingNodeLeft(edge.getVertexOrigin());
				DendoNode nodeFinal = dendogramme.isExistingNodeRight(edge.getVertexFinal());
				System.out.println("nodeOrigin :" + nodeOrigin.getDendoName());
				System.out.println("nodeFinal :" + nodeFinal.getDendoName());
				//System.out.println(nodeFinal.getDendoName());
				//System.out.println(nodeOrigin.getDendoName());
				DendoNode dendoNode = new DendoNode(nodeOrigin, nodeFinal, edge.getCouplageMetric());
				dendogramme.addNode(dendoNode);
				dendogramme.removeNode(nodeOrigin);
				dendogramme.removeNode(nodeFinal);
				//sortedEdges.remove(edge);
				//System.out.println(sortedEdges);
			}
			/* dendogramme.getDendogramme().stream()
             .filter(node -> node.getMetriqueCouplage() != 0)
             .forEach(node -> System.out.println(node.getMetriqueCouplage()));*/
		}
		dendogramme.removeZeroMetricNode();
		System.out.println("******************************");
		dendogramme.getDendogramme().forEach(node -> System.out.println(node.getMetriqueCouplage()));
		System.out.println("******************************");

		System.out.println(calculateDepthAndPrintMetric(dendogramme.getDendogramme().get(0), 0));
		//printDendogrammeNodes(dendogramme.getDendogramme().get(0));

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("DendoViewer");
			frame.setBounds(100, 100, 1000, 800);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			mxGraph graph = new mxGraph();
			graph.setCellsEditable(false);
			graph.setCellsMovable(true);
			graph.setCellsResizable(false);
			graph.setDropEnabled(false);
			graph.setSplitEnabled(false);
			Object parent = graph.getDefaultParent();

			graph.getModel().beginUpdate();
			try {


				addDendogrammeToGraph(graph, parent, dendogramme.getDendogramme().get(0));
				

				mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false);
				//layout.setOrientation(SwingConstants.NORTH);
				//layout.setIntraCellSpacing(100);
				//layout.setInterRankCellSpacing(150);
				layout.execute(parent);
			} finally {
				graph.getModel().endUpdate();
			}

			frame.add(new com.mxgraph.swing.mxGraphComponent(graph));
			frame.setVisible(true);
		});
	}
	
	private static Object addDendogrammeToGraph(mxGraph graph, Object parent, DendoNode node) {
	    if (node == null) {
	        return null;
	    }
	    
	    Object currentVertex = null;
	    if (node.getMetriqueCouplage() == 0.0) {
	    	currentVertex = graph.insertVertex(parent, null, node.getDendoName(), 0, 0, 80, 30);
	    }else {
	    	currentVertex = graph.insertVertex(parent, null, node.getMetriqueCouplage(), 0, 0, 80, 30);
	    }
	      

	    // Si le nœud a des enfants, ajoutez des arêtes et appelez cette méthode récursivement
	    if (node.getLeft() != null) {
	        Object leftChild = addDendogrammeToGraph(graph, parent, node.getLeft());
	        graph.insertEdge(parent, null, "", currentVertex, leftChild);
	    }
	    if (node.getRight() != null) {
	        Object rightChild = addDendogrammeToGraph(graph, parent, node.getRight());
	        graph.insertEdge(parent, null, "", currentVertex, rightChild);
	    }

	    return currentVertex;
	}

	   private static void printDendogrammeNodes(DendoNode node) {
	        if (node == null) {
	            return;
	        }

	        // Afficher le nom du nœud actuel
	        System.out.println("Node: " + node.getDendoName() + " - Métrique: " + node.getMetriqueCouplage());

	        // Parcourir récursivement les nœuds enfants
	        if (node.getLeft() != null) {
	            System.out.println("  Going left from " + node.getDendoName());
	            printDendogrammeNodes(node.getLeft());
	        }
	        if (node.getRight() != null) {
	            System.out.println("  Going right from " + node.getDendoName());
	            printDendogrammeNodes(node.getRight());
	        }
	    }
	   
	   private static int calculateDepthAndPrintMetric(DendoNode node, int depth) {
		    if (node == null) {
		        return depth;
		    }

		    // Afficher la métrique de couplage du nœud actuel et sa profondeur
		    System.out.println("Depth: " + depth + ", Metric: " + node.getDendoName());

		    // Calculer la profondeur des sous-arbres gauche et droit
		    int leftDepth = calculateDepthAndPrintMetric(node.getLeft(), depth + 1);
		    int rightDepth = calculateDepthAndPrintMetric(node.getRight(), depth + 1);

		    // Retourner la plus grande profondeur des deux sous-arbres
		    return Math.max(leftDepth, rightDepth);
		}


}
