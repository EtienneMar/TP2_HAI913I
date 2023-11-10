package ui.paramater;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

public class MyViewParameter {

	public MyViewParameter() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final int size_Font = 18;
	private static final String style_Font = "Arial";
	private static final Font my_Font_Style = new Font(style_Font, Font.PLAIN, size_Font);
	private static final Font my_Font_Style_Title = new Font(style_Font, Font.PLAIN, 24);

	
    
 // Définissez les pourcentages de taille relatifs
	private double pourcentageLargeur = 0.8;  // 50 % de la largeur de l'écran
	private double pourcentageHauteur = 0.9;  // 40 % de la hauteur de l'écran

    // Obtenez les dimensions de l'écran
    Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
    int largeurEcran = tailleEcran.width;
    int hauteurEcran = tailleEcran.height;

    // Calculez les dimensions de la fenêtre en fonction des pourcentages
    private int largeurFenetre = (int) (largeurEcran * pourcentageLargeur);
    private int hauteurFenetre = (int) (hauteurEcran * pourcentageHauteur);

    // Centrez la fenêtre à l'écran
    private int xFenetre = (largeurEcran - largeurFenetre) / 2;
    private int yFenetre = (hauteurEcran - hauteurFenetre) / 2;

 // Ajoutez un bouton à la fenêtre avec une taille relative
    private double pourcentageLargeurBouton = 0.3;  // 20 % de la largeur de la fenêtre
    private double pourcentageHauteurBouton = 0.08;  // 10 % de la hauteur de la fenêtre
    private int largeurBouton = (int) (largeurFenetre * pourcentageLargeurBouton);
    private int hauteurBouton = (int) (hauteurFenetre * pourcentageHauteurBouton);
    private int xBouton = (largeurFenetre - largeurBouton) / 8;
    private int yBouton = (hauteurFenetre - hauteurBouton) / 8;
    
	public double getPourcentageLargeur() {
		return pourcentageLargeur;
	}
	public void setPourcentageLargeur(double pourcentageLargeur) {
		this.pourcentageLargeur = pourcentageLargeur;
	}
	public double getPourcentageHauteur() {
		return pourcentageHauteur;
	}
	public void setPourcentageHauteur(double pourcentageHauteur) {
		this.pourcentageHauteur = pourcentageHauteur;
	}
	public Dimension getTailleEcran() {
		return tailleEcran;
	}
	public void setTailleEcran(Dimension tailleEcran) {
		this.tailleEcran = tailleEcran;
	}
	public int getLargeurEcran() {
		return largeurEcran;
	}
	public void setLargeurEcran(int largeurEcran) {
		this.largeurEcran = largeurEcran;
	}
	public int getHauteurEcran() {
		return hauteurEcran;
	}
	public void setHauteurEcran(int hauteurEcran) {
		this.hauteurEcran = hauteurEcran;
	}
	public int getLargeurFenetre() {
		return largeurFenetre;
	}
	public void setLargeurFenetre(int largeurFenetre) {
		this.largeurFenetre = largeurFenetre;
	}
	public int getHauteurFenetre() {
		return hauteurFenetre;
	}
	public void setHauteurFenetre(int hauteurFenetre) {
		this.hauteurFenetre = hauteurFenetre;
	}
	public int getxFenetre() {
		return xFenetre;
	}
	public void setxFenetre(int xFenetre) {
		this.xFenetre = xFenetre;
	}
	public int getyFenetre() {
		return yFenetre;
	}
	public void setyFenetre(int yFenetre) {
		this.yFenetre = yFenetre;
	}
	public double getPourcentageLargeurBouton() {
		return pourcentageLargeurBouton;
	}
	public void setPourcentageLargeurBouton(double pourcentageLargeurBouton) {
		this.pourcentageLargeurBouton = pourcentageLargeurBouton;
	}
	public double getPourcentageHauteurBouton() {
		return pourcentageHauteurBouton;
	}
	public void setPourcentageHauteurBouton(double pourcentageHauteurBouton) {
		this.pourcentageHauteurBouton = pourcentageHauteurBouton;
	}
	public int getLargeurBouton() {
		return largeurBouton;
	}
	public void setLargeurBouton(int largeurBouton) {
		this.largeurBouton = largeurBouton;
	}
	public int getHauteurBouton() {
		return hauteurBouton;
	}
	public void setHauteurBouton(int hauteurBouton) {
		this.hauteurBouton = hauteurBouton;
	}
	public int getxBouton() {
		return xBouton;
	}
	public void setxBouton(int xBouton) {
		this.xBouton = xBouton;
	}
	public int getyBouton() {
		return yBouton;
	}
	public void setyBouton(int yBouton) {
		this.yBouton = yBouton;
	}
	public static int getSizeFont() {
		return size_Font;
	}
	public static String getStyleFont() {
		return style_Font;
	}
	public static Font getMyFontStyle() {
		return my_Font_Style;
	}
	public static Font getMyFontStyleTitle() {
		return my_Font_Style_Title;
	}
    
    
    
   

	
	
}
