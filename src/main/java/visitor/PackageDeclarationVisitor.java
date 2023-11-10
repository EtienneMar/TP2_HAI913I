package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

/*
 * cette classe est un visiteur qui visite les déclarations de type package et avec on récupere le nom du package.
 */
public class PackageDeclarationVisitor extends ASTVisitor {
	String packageName = "";

	/*
	 * retourne vrai si le node visité est une déclaration de type package, recupere
	 * le nom du package.
	 */
	public boolean visit(PackageDeclaration node) {
		// Extraire le nom du package
		packageName = node.getName().getFullyQualifiedName();
		return super.visit(node);
	}

	/*
	 * getter on a besoin que du nom du package
	 */
	public String getPackageName() {
		return packageName;
	}

	/*
	 * affiche le nom du package
	 */
	public void printPackageName() {
		System.out.println("\n\nNOM DU PACKAGE : " + getPackageName() + "\n");
	}

}
