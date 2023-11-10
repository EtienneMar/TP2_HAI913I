package visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/*
 * cette classe est un visiteur qui visite les déclarations de type variable, et avec on récupere le liste des variables.
 */
public class VariableDeclarationFragmentVisitor extends ASTVisitor {
	private List<VariableDeclarationFragment> variables = new ArrayList<VariableDeclarationFragment>();/*
																										 * liste de
																										 * variables
																										 */

	/*
	 * retourne vrai si le node visité est une déclaration de type variable, ajoute
	 * le node visité à la liste des variables.
	 */
	public boolean visit(VariableDeclarationFragment node) {
		variables.add(node);
		return super.visit(node);
	}

	/*
	 * getter de la liste de variables
	 */
	public List<VariableDeclarationFragment> getVariables() {
		return variables;
	}
}
