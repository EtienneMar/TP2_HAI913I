package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/*
 * Cette classe est un visiteur qui visite les déclarations de type  classe et interface: compte le nombre de lignes de code, le nombre d'attribut, verifie si le node est une classe ou une interface et le recupere son nom et son code source.
 */
public class ClassInterfaceVisitor extends ASTVisitor {

    String className = null;
    int linesOfCode = 0;
    int attributeCount = 0;
    String javaCode = "";
    boolean isClass = false;
    boolean isInterface = false;

    /*
     * cette methode visit prend en parametre un node de type TypeDeclaration et
     * verifie si le node est une classe ou une interface. Si c'est une classe, on
     * recupere son nom, son code source et on compte le nombre de lignes de code.
     * Si c'est une interface, on recupere son code source et on compte le nombre de
     * lignes de code et puis retourne true si le node a été visité.
     */
    public boolean visit(TypeDeclaration node) {
        if (!node.isInterface()) {
            className = node.getName().getFullyQualifiedName();
            javaCode = node.toString();
            linesOfCode = countLinesOfCode(javaCode);
            isClass = true;
        }
        if (node.isInterface()) {
            javaCode = node.toString();
            linesOfCode = countLinesOfCode(javaCode);
            isInterface = true;

        }
        return super.visit(node);
    }

    /*
     * cette methode visit prend en parametre un node de type FieldDeclaration pour
     * visiter les déclarations de champ et compte chaque déclaration de champ comme
     * un attribut.
     */
    public boolean visit(FieldDeclaration node) {
        // Comptez chaque déclaration de champ comme un attribut
        attributeCount++;
        return super.visit(node);
    }

    /*
     * Cette partie comporte nos getters pour les attributs de la classe
     */

    public int getAttributeCount() {
        return attributeCount;
    }

    public String getClassName() {
        return className;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    /*
     * cette methode nous permet de compter le nombre de lignes de code quand notre
     * visiteur visite un node de type TypeDeclaration. Elle prend en parametre une
     * chaine de caractere javaCode et retourne le nombre de lignes de code. Dans la
     * logique métier de cette méthode nous avons déclaré un tableau de chaine de
     * carctere qui stocke le code source avec une expression régulière qui ignore
     * le retour à la ligne et une variable compteur lineCount. Puis on boucle dans
     * le tableau de chaine de caractere pour compter le nombre de lignes de code en
     * ignorant les lignes vides et les commentaires et retourner le nombre de
     * lignes de ce type de declaration.
     */
    public int countLinesOfCode(String javaCode) {
        // javaCode = javaCode.replaceAll(
        // "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 " );
        String[] lines = javaCode.split("\r\n|\r|\n"); // Ignorer le retour à la ligne sur mac et windows
        int lineCount = 0;
        for (String line : lines) {
            // Ignorer les lignes vides et les commentaires
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("//")) {
                lineCount++;
            }
        }
        return lineCount;
    }

	public boolean getIsClass() {
		return isClass;
	}

	public void setClass(boolean isClass) {
		this.isClass = isClass;
	}

	public boolean getIsInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

}
