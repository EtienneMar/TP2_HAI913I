package visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

/*
 * cette classe est un visiteur qui visite les invocations(appel) de  méthodes  et avec on  récupére la liste des méthodes et la liste des méthodes super.
 */
public class MethodInvocationVisitor extends ASTVisitor {
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();// liste de méthodes
	List<SuperMethodInvocation> superMethods = new ArrayList<SuperMethodInvocation>();// liste de méthodes héritées

	/*
	 * retourne vrai si le node visité est une invocation de méthode
	 * (MethodInvocation),ajoute le node visité à la liste des méthodes.
	 */
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}

	/*
	 * retourne vrai si le node visité est une invocation de méthode héritée
	 * (SuperMethodInvocation),ajoute le node visité à la liste de méthodes
	 * héritées.
	 */
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethods.add(node);
		return super.visit(node);
	}

	/*
	 * retourne la liste de méthodes invokées.
	 */
	public List<MethodInvocation> getMethods() {
		return methods;
	}

	/*
	 * retourne la liste de méthodes héritées (invokées) .
	 */
	public List<SuperMethodInvocation> getSuperMethod() {
		return superMethods;
	}

}
