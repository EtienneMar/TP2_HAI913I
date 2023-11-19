package dendogramme;


import java.awt.Graphics;

import javax.swing.JPanel;

import model.DendoNode;

public class DendrogramPanel extends JPanel {
	private Dendogramme dendogramme;

	public DendrogramPanel(Dendogramme dendogramme) {
		this.dendogramme = dendogramme;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Logique pour dessiner le dendrogramme
		drawDendogramme(g, dendogramme.getDendogramme().get(0), 10, 10); // Commencez à dessiner à partir de (10,10)
	}

	private void drawDendogramme(Graphics g, DendoNode node, int x, int y) {
		if (node == null) return;

		// Dessiner le nœud actuel
		g.drawString(node.getDendoName(), x, y);

		// Dessiner les branches vers les nœuds enfants si existants
		if (node.getLeft() != null) {
			drawDendogramme(g, node.getLeft(), x - 20, y + 20); // Décalage pour le nœud gauche
		}
		if (node.getRight() != null) {
			drawDendogramme(g, node.getRight(), x + 20, y + 20); // Décalage pour le nœud droit
		}
	}
}
