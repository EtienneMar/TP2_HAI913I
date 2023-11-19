package dendogramme;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import model.DendoNode;
import model.Vertex;

public class Dendogramme {
	
	private ArrayList<DendoNode> dendogramme = new ArrayList<>();
	
	

	public ArrayList<DendoNode> getDendogramme() {
		return dendogramme;
	}

	public void setDendogramme(ArrayList<DendoNode> dendogramme) {
		this.dendogramme = dendogramme;
	} 
	
	public void addNode(DendoNode node) {
		if(!dendogramme.contains(node)) {
			dendogramme.add(node);
		}else {
			return;
		}
	}
	
	/*
	  public DendoNode isExistingNode(Vertex vertex) {
		DendoNode returnNode = null;
		int maxLength = 0;
	    for (DendoNode node : dendogramme) {
	    	 DendoNode foundNode = node.containsVertex(vertex);
	         if (foundNode != null) {
	             int nameLength = foundNode.getDendoName().length();
	             if (nameLength > maxLength) {
	                 returnNode = foundNode;
	                 maxLength = nameLength;
	             }
	         }
	     }
		return returnNode;
	} 
	 */
	
	public DendoNode isExistingNodeLeft(Vertex vertex) {
		DendoNode node = nodeInDendogramme(vertex);
		if(node != null) {
			return node; 
		}else {
			for (DendoNode nodeNotInDendogramme : dendogramme) {
				System.out.println(nodeNotInDendogramme.containsVertex(vertex));
				node = nodeNotInDendogramme.containsVertex(vertex).stream()
						.max(Comparator.comparingInt(containingNode -> containingNode.getDendoName().length()))
			            .orElse(null);
				
				//nodeNotInDendogramme.containsVertex(vertex).forEach(nodeA -> System.out.println(nodeA.getDendoName()));
				//node = nodeNotInDendogramme.containsVertex(vertex);
		    	if(node != null) {
		    		System.out.println(node.getDendoName());
		    		return node;
		    	}
		}
	    
	    }
	    
	    return null; // Si aucun nœud ne contient le vertex
	}
	

	public DendoNode isExistingNodeRight(Vertex vertex) {
		DendoNode node = nodeInDendogramme(vertex);
		if(node != null) {
			return node; 
		}else {
			for (DendoNode nodeNotInDendogramme : dendogramme) {
				System.out.println(nodeNotInDendogramme.containsVertex(vertex));
				node = nodeNotInDendogramme.containsVertex(vertex).stream()
						.min(Comparator.comparingInt(containingNode -> containingNode.getDendoName().length()))
			            .orElse(null);
				
				//nodeNotInDendogramme.containsVertex(vertex).forEach(nodeA -> System.out.println(nodeA.getDendoName()));
				//node = nodeNotInDendogramme.containsVertex(vertex);
		    	if(node != null) {
		    		System.out.println(node.getDendoName());
		    		return node;
		    	}
		}
	    
	    }
	    
	    return null; // Si aucun nœud ne contient le vertex
	}
	
	public DendoNode nodeInDendogramme(Vertex vertex) {
	    for (DendoNode node : dendogramme) {
	    	if(node.getDendoName().equals(vertex.getDendoName())) {
	    		return node;
	    	}
	    }
		return null;
	}
	
	// Méthode pour supprimer un nœud
    public void removeNode(DendoNode node) {
		if(dendogramme.contains(node)) {
			dendogramme.remove(node);
		}
    }
    
    public void removeZeroMetricNode() {
        this.dendogramme = dendogramme.stream()
        		 .filter(node -> node.getLeft() != null || node.getRight() != null) 
                .collect(Collectors.toCollection(ArrayList::new));
    }

	@Override
	public String toString() {
		return "Dendogramme [dendogramme=" + dendogramme + "]";
	}

	
	
	
}
