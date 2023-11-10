package model;

public class Edge {
	
	private Vertex VertexA;
	private Vertex VertexB;
	private int nombreAppelEntreLesDeuxClasses = 1;
	private float couplageMetric;
	
	public Edge(Vertex vertexOrigin, Vertex vertexFinal) {
		this.setVertexOrigin(vertexOrigin);
		this.setVertexFinal(vertexFinal);
		this.setCouplageMetric();
	}
	
	public Vertex getVertexOrigin() {
		return VertexA;
	}
	public void setVertexOrigin(Vertex vertexOrigin) {
		VertexA = vertexOrigin;
	}
	public Vertex getVertexFinal() {
		return VertexB;
	}
	public void setVertexFinal(Vertex vertexFinal) {
		VertexB = vertexFinal;
	}
	public float getCouplageMetric() {
		return couplageMetric;
	}
	public void setCouplageMetric() {
		int nombreRelationsBinaires = VertexA.getTolalAmountMethods()*VertexB.getTolalAmountMethods();
		if (nombreRelationsBinaires > 0 ) {
			// Utilisation de Math.round pour tronquer à trois chiffres après la virgule
			float metricdeCouplage = ((float) nombreAppelEntreLesDeuxClasses / nombreRelationsBinaires);
			this.couplageMetric = (float) (Math.round(metricdeCouplage * 100.0) / 100.0);
		}
	} 
	
	public int getNombreAppelEntreLesDeuxClasses() {
		return nombreAppelEntreLesDeuxClasses;
	}

	public void setNombreAppelEntreLesDeuxClasses(int nombreAppelEntreLesDeuxClasses) {
		this.nombreAppelEntreLesDeuxClasses = nombreAppelEntreLesDeuxClasses;
	}
	
	public void incrementnombreAppelEntreLesDeuxClasses() {
		this.nombreAppelEntreLesDeuxClasses++;
		this.setCouplageMetric();;
	}
	
	public boolean isBetweenVertices(String vertexStartName, String vertexEndName) {
	    return (VertexA.getName().equals(vertexStartName) && VertexB.getName().equals(vertexEndName)) ||
	           (VertexA.getName().equals(vertexEndName) && VertexB.getName().equals(vertexStartName));
	}

	@Override
	public String toString() {
		return "Vertex A : " + VertexA.getName() + ", Vertex B: " + VertexB.getName() + ", nbrApp="
				+ nombreAppelEntreLesDeuxClasses + "metrique : " + getCouplageMetric();
				
	}
	
}
