package model;

public class DendrogrammeNode {
    Vertex vertex; // Utilisé si c'est une feuille
    DendrogrammeNode left; // Enfant gauche (peut être un autre DendrogramNode ou une feuille)
    DendrogrammeNode right; // Enfant droit (peut être un autre DendrogramNode ou une feuille)
    Edge edge; // L'arête qui a formé ce cluster (nœud), si applicable
    float metriqueCouplage; // Métrique de couplage du cluster

    // Constructeur pour les feuilles
    public DendrogrammeNode(Vertex vertex) {
        this.vertex = vertex;
        this.left = null;
        this.right = null;
        this.edge = null;
        this.metriqueCouplage = 0; // Métrique pour une feuille est 0
    }

    // Constructeur pour un cluster formé d'une Edge (deux Vertex)
    public DendrogrammeNode(Edge edge) {
        this.vertex = null;
        this.left = new DendrogrammeNode(edge.getVertexOrigin()); // Vertex A comme enfant gauche
        this.right = new DendrogrammeNode(edge.getVertexFinal()); // Vertex B comme enfant droit
        this.edge = edge;
        this.metriqueCouplage = edge.getCouplageMetric(); // Métrique de l'Edge
    }

    // Constructeur pour les nœuds internes (combinant clusters/feuilles)
    public DendrogrammeNode(DendrogrammeNode left, DendrogrammeNode right, float metriqueCouplage) {
        this.vertex = null;
        this.left = left;
        this.right = right;
        this.edge = null; // Pas d'Edge directe pour un nœud interne
        this.metriqueCouplage = metriqueCouplage; // Métrique pour le cluster formé
    }

	@Override
	public String toString() {
		String toReturn = null;
		if(vertex != null) {
			toReturn = vertex.getName(); 
		}
		return toReturn;
	}
    
    
}
