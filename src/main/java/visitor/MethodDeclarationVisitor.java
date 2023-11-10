package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/*
 * cette classe est un visiteur qui visite les déclarations de type méthode, et avec on récupere le liste des méthodes, le nombre de lignes de code de chaque méthode et le nombre de paramètres de chaque méthode et le code source de chaque méthode.
 */
public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>(); // liste des methodes
	List<MethodDeclaration> constructors = new ArrayList<>(); //Liste Constructor
	HashMap<String, Integer> methodsLines = new HashMap<String, Integer>(); // nombre de lignes de code de chaque methode
	HashMap<String, Integer> methodsParamaters = new HashMap<String, Integer>(); // nombre de parametres de chaque methode
	String methodCode = ""; // code source de chaque methode

	/*
	 * retourne vrai si le node visité est une déclaration de méthode, ajoute le
	 * node visité à la liste des méthodes, recupere le code source de chaque
	 * methode, met à jour la méthode setMethodLines et setMethodsParameters.
	 */
	public boolean visit(MethodDeclaration node) {

		methodCode = node.toString();
		if(node.isConstructor()) {
			constructors.add(node); 
		}else {
			methods.add(node);
			setMethodsLines();
			setMethodsParameters();
		}
		return super.visit(node);
	}

	/*
	 * retourne la liste des méthodes si le node visité est une déclaration de
	 * méthode.
	 */
	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	/*
	 * cette methode met à jour la hashmap methodsLines qui en a comme clé le nom de
	 * la methode et comme valeur le nombre de lignes de code de chaque methode en
	 * parcourant la liste des méthodes de type MethodDeclaration.
	 */
	public void setMethodsLines() {
		for (MethodDeclaration method : getMethods()) {
			methodsLines.put(method.getName().toString(), countLinesOfMethod(method.toString()));//
		}
	}

	/*
	 * retourne la hashmap methodsLines qui en a comme clé le nom de la methode et
	 * comme valeur le nombre de lignes de code de chaque methode.
	 */
	public HashMap<String, Integer> getMethodsLines() {
		return methodsLines;
	}

	/*
	 * cette methode met à jour la hashmap methodsParamaters qui en a comme clé le
	 * nom de la methode et comme valeur le nombre de parametres de chaque methode
	 * en parcourant la liste des méthodes de type MethodDeclaration.
	 */
	public void setMethodsParameters() {
		for (MethodDeclaration method : getMethods()) {
			methodsParamaters.put(method.getName().toString(), method.parameters().size());//
		}
	}

	public HashMap<String, Integer> getMethodsParameters() {
		return methodsParamaters;
	}
	

	public List<MethodDeclaration> getConstructors() {
		return constructors;
	}

	public void setConstructors(List<MethodDeclaration> constructors) {
		this.constructors = constructors;
	}

	/*
	 * permet d'afficher le nom de la methode, le nombre de parametres, le type de
	 * retour et le nombre de lignes de code de chaque methode.
	 */
	public void printMethodsNames() {
		System.out.println("NOM | NB PARAM| TYPE RETOUR | NB LINE");
		for (MethodDeclaration method : getMethods()) {
			System.out.println(method.getName()
					+ " | " + method.parameters().size()
					+ " | " + method.getReturnType2()
					+ " | " + countLinesOfMethod(method.toString()));
		}
	}

	/*
	 * Affiche le nombre de méthodes.
	 */
	public void printMethodCount() {
		System.out.println("- NB OF METHODS : " + getMethods().size());
	}

	/*
	 * cette methode permet de compter le nombre de lignes de code de chaque methode
	 * passée en parametre. Pour se faire on initiliase un tableau de chaine de
	 * caracteres qui stocke le code source de la methode passée en parametre sans
	 * le retour chariot sur windows, linux et mac. Puis on déclare une variable
	 * lineCount qui compte le nombre de lignes de code de la methode en parcourant
	 * le tableau de chaine de caracteres. On ignore les lignes vides et les
	 * commentaires et retourne le compteur.
	 */

	public int countLinesOfMethod(String methodCode) {
		// javaCode = javaCode.replaceAll(
		// "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 " );
		String[] lines = methodCode.split("\r\n|\r|\n");
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