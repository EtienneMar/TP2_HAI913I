package model;

public class Vertex extends DendoNode {

	/**
	 * Attribut
	 * 
	 * {@code private int nbAppel = 1} 
	 * nbAppel représente le nombre de fois que la méthode est appelée 
	 * dans une autre méthode 
	 */

	private String name; 
	private int numberOfConstructorInClass; 
	private int numberOfMethodInClass;
	/**
	 * Constructeur 
	 */
	
	public Vertex(String name) {
		super(name);
		setName(name);
	}

	public Vertex(String name, int numberOfConstructorInClass, int numberOfMethodInClass) {
		super(name);
		setName(name);
		setNumberOfConstructorInClass(numberOfConstructorInClass);
		setNumberOfMethodInClass(numberOfMethodInClass);
	}
	
	public Vertex(String name, int numberOfMethodInClass) {
		super(name);
		setName(name);
		setNumberOfConstructorInClass(0);
		setNumberOfMethodInClass(numberOfMethodInClass);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfConstructorInClass() {
		return numberOfConstructorInClass;
	}

	public void setNumberOfConstructorInClass(int numberOfConstructorInClass) {
		this.numberOfConstructorInClass = numberOfConstructorInClass;
	}

	public int getNumberOfMethodInClass() {
		return numberOfMethodInClass;
	}

	public void setNumberOfMethodInClass(int numberOfMethodInClass) {
		this.numberOfMethodInClass = numberOfMethodInClass;
	}

	public int getTolalAmountMethods() {
		return numberOfConstructorInClass + numberOfMethodInClass;
	}

	@Override
	public String toString() {
		return "Vertex [name=" + name + ", numberOfConstructorInClass=" + numberOfConstructorInClass
				+ ", numberOfMethodInClass=" + numberOfMethodInClass;
	}
	
}