package model;

import java.util.ArrayList;
import java.util.List;

public class DendoNode {

	String dendoName; // Utilisé si c'est une feuille
	DendoNode left; // Enfant gauche (peut être un autre DendrogramNode ou une feuille)
	DendoNode right; // Enfant droit (peut être un autre DendrogramNode ou une feuille)
	Edge edge; // L'arête qui a formé ce cluster (nœud), si applicable
	float metriqueCouplage; // Métrique de couplage du cluster

	// Constructeur pour les feuilles
	public DendoNode(String name) {
		this.dendoName = name;
		this.left = null;
		this.right = null;
		this.edge = null;
		this.metriqueCouplage = 0; // Métrique pour une feuille est 0
	}

	// Constructeur pour un cluster formé d'une Edge (deux Vertex)
	public DendoNode(Edge edge) {
		this.dendoName = null;
		//this.left = new DendrogrammeNode(edge.getVertexOrigin()); // Vertex A comme enfant gauche
		//this.right = new DendrogrammeNode(edge.getVertexFinal()); // Vertex B comme enfant droit
		this.edge = edge;
		this.metriqueCouplage = edge.getCouplageMetric(); // Métrique de l'Edge
	}

	// Constructeur pour les nœuds internes (combinant clusters/feuilles)
	public DendoNode(DendoNode nodeOrigin, DendoNode nodeFinal, float metriqueCouplage) {
		this.dendoName = nodeOrigin.getDendoName() + "|" + nodeFinal.getDendoName();
		this.left = nodeOrigin;
		this.right = nodeFinal;
		this.edge = null; // Pas d'Edge directe pour un nœud interne
		this.metriqueCouplage = metriqueCouplage; // Métrique pour le cluster formé
	}
	
	/*
    // Méthode pour vérifier si un Vertex est contenu dans le DendoNode
    public boolean containsVertex(Vertex vertex) {
    	ArrayList<String> a = new ArrayList<>();
        // Vérifiez si le vertex est le vertex de la feuille
        if (dendoName != null && dendoName.equals(vertex.getName())) {
            return true;
        }

        // Vérifiez dans les sous-nœuds gauche et droit
        if (left != null && left.containsVertex(vertex)) {
        	System.out.println("GAUCHE : " + left.getDendoName());
        	a.add(dendoName);
            return true;
        }
        if (right != null && right.containsVertex(vertex)) {
        	System.out.println("DROIT : "+ right);
        	a.add(dendoName);
            return true;
        }

        return false; // Le Vertex n'est pas trouvé dans ce nœud ou ses sous-nœuds
    }*/
	
	public List<DendoNode> containsVertex(Vertex vertex) {
	    List<DendoNode> matchingNodes = new ArrayList<>();

	    // Vérifiez si le vertex est le vertex de la feuille
	    if (dendoName != null && dendoName.contains(vertex.getName())) {
	        matchingNodes.add(this);
	    }

	    // Chercher dans le sous-nœud gauche
	    if (left != null) {
	        matchingNodes.addAll(left.containsVertex(vertex));
	    }

	    // Chercher dans le sous-nœud droit
	    if (right != null) {
	        matchingNodes.addAll(right.containsVertex(vertex));
	    }

	    return matchingNodes; // Retourne la liste de nœuds correspondants
	}



	/*
    public DendoNode containsVertex(Vertex vertex) {
        DendoNode result = null;

        // Vérifiez si le vertex est le vertex de la feuille
        if (dendoName != null && dendoName.equals(vertex.getName())) {
            result = this;
        }

        // Vérifiez dans le sous-nœud gauche
        if (left != null) {
            DendoNode leftResult = left.containsVertex(vertex);
            if (leftResult != null && (result == null || leftResult.getDendoName().length() > result.getDendoName().length())) {
                result = leftResult;
            }
        }

        // Vérifiez dans le sous-nœud droit
        if (right != null) {
            DendoNode rightResult = right.containsVertex(vertex);
            if (rightResult != null && (result == null || rightResult.getDendoName().length() > result.getDendoName().length())) {
                result = rightResult;
            }
        }

        
        return result; // Retourne le DendoNode trouvé ou null si aucun n'est trouvé
    }
	*/
    
	public float getMetriqueCouplage() {
		return metriqueCouplage;
	}

	public void setMetriqueCouplage(float metriqueCouplage) {
		this.metriqueCouplage = metriqueCouplage;
	}
	

	public String getDendoName() {
		return dendoName;
	}

	public void setVertexNameOrigin(String vertexNameOrigin) {
		this.dendoName = vertexNameOrigin;
	}

	public DendoNode getLeft() {
		return left;
	}

	public void setLeft(DendoNode left) {
		this.left = left;
	}

	public DendoNode getRight() {
		return right;
	}

	public void setRight(DendoNode right) {
		this.right = right;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	@Override
	public String toString() {
		return "DendoNode [vertexNameOrigin=" + dendoName + ", left=" + left + ", right=" + right + ", edge="
				+ edge + ", metriqueCouplage=" + metriqueCouplage + "]";
	}



}
