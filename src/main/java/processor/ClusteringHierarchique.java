package processor;

import model.Vertex;
import model.Edge;
import model.CouplingGraph;
import java.util.List;
import java.util.PriorityQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ClusteringHierarchique {

    private static class Cluster {
        Set<Vertex> vertices;

        Cluster(Vertex v) {
            this.vertices = new HashSet<>();
            this.vertices.add(v);
        }

        void merge(Cluster other) {
            this.vertices.addAll(other.vertices);
        }
    }

    public static Cluster clusteringHierarchique(CouplingGraph graph) {
        List<Cluster> clusters = new ArrayList<>();
        for (Vertex v : graph.getListVertex()) {
            clusters.add(new Cluster(v));
        }

        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparing(Edge::getCouplageMetric));
        edgeQueue.addAll(graph.getListEdge());

        while (clusters.size() > 1) {
            Edge edge = edgeQueue.poll(); // Récupère l'arête avec la plus petite métrique de couplage
            Cluster cluster1 = findClusterContainingVertex(clusters, edge.getVertexOrigin());
            Cluster cluster2 = findClusterContainingVertex(clusters, edge.getVertexFinal());

            if (cluster1 != null && cluster2 != null && !cluster1.equals(cluster2)) {
                cluster1.merge(cluster2);
                clusters.remove(cluster2);
                // Mise à jour des arêtes et de la file de priorité si nécessaire
            }
        }
        return clusters.get(0); // Le dendrogramme final
    }

    private static Cluster findClusterContainingVertex(List<Cluster> clusters, Vertex vertex) {
        for (Cluster cluster : clusters) {
            if (cluster.vertices.contains(vertex)) {
                return cluster;
            }
        }
        return null;
    }

    public static void main(String[] args) throws NullPointerException, IOException {
        // Exemple d'utilisation
    	CouplingAnalyzeJDT couplingAnalyze = new CouplingAnalyzeJDT("C:\\Users\\33683\\Desktop\\Master_ICO_M1\\S8\\Structure_De_Donnee\\TP3");
        CouplingGraph graph = couplingAnalyze.getCouplingGraphe();
        		// initialiser avec le graphe de couplage
        Cluster dendro = clusteringHierarchique(graph);
        // Utiliser le dendrogramme résultant
    }
}

