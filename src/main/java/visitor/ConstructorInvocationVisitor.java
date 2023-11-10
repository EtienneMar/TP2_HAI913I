package visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

/*
 * cette classe est un visiteur qui visite les invocations(appel) de  constructeurs  et  récupére la liste des constructeurs et la liste des constructeurs super.
 */
public class ConstructorInvocationVisitor extends ASTVisitor {
	List<ClassInstanceCreation> constructors = new ArrayList<ClassInstanceCreation>();
	List<SuperConstructorInvocation> superConstructors = new ArrayList<SuperConstructorInvocation>();

	/*
	 * retourne vrai si le node visité est une invocation de constructeur
	 * (classInstanceCreation),
	 * ajoute le node visité à la liste des constructeurs.
	 */
	public boolean visit(ClassInstanceCreation node) {

		constructors.add(node);
		return super.visit(node);
	}

	/*
	 * retourne vrai si le node visité est une invocation de constructeur
	 * super (superConstructorInvocation),ajoute le node visité à la liste des
	 * constructeurs super.
	 */
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		superConstructors.add(node);
		return super.visit(node);
	}

	/*
	 * les getters des deux listes de constructeurs et de constructeurs super et les
	 * retorunes en tant que méthodes. on considére les constructeurs et les
	 * constructeurs super comme des méthodes.
	 */
	public List<ClassInstanceCreation> getMethods() {
		return constructors;
	}

	public List<SuperConstructorInvocation> getSuperMethod() {
		return superConstructors;
	}

}
