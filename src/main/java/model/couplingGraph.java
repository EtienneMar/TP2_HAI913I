package model;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class couplingGraph {

	/**
	 * Attribut
	 */
	private Set<String> listFileAnalyze = new HashSet<>();
	private Set<Vertex> listVertex = new HashSet<>();
	private Set<Edge> listEdge = new HashSet<>();
	/**
	 * Constructeur vide
	 */

	public couplingGraph() {}

	public Set<Vertex> getListVertex() {
		return listVertex;
	}

	public void setListVertex(Set<Vertex> listVertex) {
		this.listVertex = listVertex;
	}

	public void addVertex(Vertex vertexToAdd) {
		for (Vertex vertex : getListVertex()) {
			if (vertex.getName().equals(vertexToAdd.getName())) {
				return;
			}
		}
		getListVertex().add(vertexToAdd);
	}



	public Set<Edge> getListEdge() {
		return listEdge;
	}

	public void setListEdge(Set<Edge> listEdge) {
		this.listEdge = listEdge;
	}

	public Edge findExistingEdge(String vertexStart, String vertexEndName) {
		for (Edge edge : listEdge) {
			if (edge.isBetweenVertices(vertexStart, vertexEndName)) {
				return edge;
			}
		}
		return null; // Aucune arête n'a été trouvée
	}

	public Vertex findExistingVertex(String vertexName) {
		for (Vertex vertex : listVertex) {
			if (vertex.getName().equals(vertexName)) {
				return vertex;
			}
		}
		return null;
	}

	public Boolean isExistingVertex(String vertexName) {
		for (Vertex vertex : listVertex) {
			if (vertex.getName().equals(vertexName)) {
				return true;
			}
		}
		return false;
	}

	public Set<String> getListFileAnalyze() {
		return listFileAnalyze;
	}

	public void setListFileAnalyze(List<File> listFileAnalyze) {
		this.listFileAnalyze = addAllFileAnalyze(listFileAnalyze);
	}

	public Set<String> addAllFileAnalyze (List<File> javaFiles) {
		Set<String> listFileAnalyze = new HashSet<>();
		for(File file : javaFiles) {
			String pathFileToAnalyze = file.getParentFile().getName() +"."+ file.getName().replaceAll("(?i).java", "");
			listFileAnalyze.add(pathFileToAnalyze);
		}
		return listFileAnalyze;
	}


	public boolean isFileHasToBeAnalyze (String packageName) {
		return getListFileAnalyze().contains(packageName);
	}

	public float getMetricCouplageByName (String vertexStart, String vertexEnd) {
		Edge edge = findExistingEdge(vertexStart, vertexEnd);
		if (edge == null) {
			return -1;
		}else {
			return edge.getCouplageMetric();
		}
	}
}



