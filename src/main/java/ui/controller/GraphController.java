package ui.controller;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import model.Edge;
import model.Vertex;
import parsers.EclipseJDTParser;
import processor.CouplingAnalyze;

public class GraphController {

	public static EclipseJDTParser parserEclipse;
	private static CouplingAnalyze couplingGraphe; 

	public void GraphPanel(String path) throws IOException {

		couplingGraphe = new CouplingAnalyze(path);

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

				for(Vertex vertex : couplingGraphe.getCouplingGraphe().getListVertex()) {
					Object vertexOriginGraph = graph.insertVertex(parent, vertex.getName(), vertex.getName(), 20, 20, 80, 30);
					mxRectangle dimensions = graph.getPreferredSizeForCell(vertexOriginGraph);

					// Msj à jour les dimensions du vertex
					graph.resizeCell(vertexOriginGraph, dimensions);
				}

				// Afficher tous les edge existants
				ArrayList<Object> cellList = new ArrayList<>(Arrays.asList(graph.getChildCells(parent)));
				cellList.stream().filter(cell -> graph.getModel().isVertex(cell));

				for (Edge edge : couplingGraphe.getCouplingGraphe().getListEdge()) {
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

}
