package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnumDeclaration;

/*
 * cette classe est un visiteur qui visite les déclarations de type enum et avec on  récupere le nombre de lignes de code, le code source et vérifie si c'est énum.
 */
public class EnumVisitor extends ASTVisitor {
	int linesOfCode = 0;
	String javaCode = "";
	String enumName = null;

	/*
	 * retourne vrai si le node visité est une déclaration de type enum, recupere le
	 * code source , le nombre de lignes de code et le nom de l'enum.
	 */
	public boolean visit(EnumDeclaration node) {
		javaCode = node.toString();
		linesOfCode = countLinesOfCode(javaCode);
		enumName = node.getName().toString();
		return super.visit(node);
	}

	/*
	 * getter et setter
	 */

	public String getEnumName() {
		return enumName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	public void setLinesOfCode(int linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

	/*
	 * end getter et setter
	 */

	/*
	 * cette méthode compte de la nombre de lignes de code qu'il y a dans le code
	 * source de la déclaration de type enum avec un tableau qui stocke les lignes
	 * sans le retour chariot, un compteur pour compter le nombre de lignes contenus
	 * dans ce tableau en ignorant les lignes vides et les commentaires.
	 */
	public int countLinesOfCode(String javaCode) {
		// javaCode = javaCode.replaceAll(
		// "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 " );
		String[] lines = javaCode.split("\r\n|\r|\n");
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
}
