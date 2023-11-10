package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import org.apache.commons.io.FileUtils;

public class EclipseJDTParser extends Parser<ASTParser> {



	/**
	 * Constructeur
	 * 	
	 * @param projectPath récupère le Path du projet spécifier par l'utilisateur dans l'UI
	 * @throws NullPointerException @see Parser#getJrePath()
	 * @throws FileNotFoundException @see Parser#setProjectPath()
	 */

	public EclipseJDTParser(String projectPath) throws NullPointerException, FileNotFoundException {
		super(projectPath);
	}


	/**
	 * 
	 *Implémente le pattern Facade pour instancier un EclipseJDTParser (AST parser) 
	 * par défaut avec des options personnalisées.
	 * 
	 * @param level Le niveau de la version spécifique du langage de la Java Language Specification.
	 * @param kind Le type de l'AST parser (par exemple, {@link ASTParser#K_COMPILATION_UNIT}).
	 * @param resolveBindings Spécifie si les résolutions de liens (bindings) doivent être activées.
	 *  	  Si activée l'ASTParser peut fournir des informations détaillées sur les éléments du code source, classes,méthodes,champs ect..
	 * @param bindingsRecovery Spécifie si la récupération des liens (bindings) doit être activée.
	 * 		  Si activée ATParser produit une représentation partielle de l'AST même si certaines parties du code source 
	 * 		  contiennent des erreurs de résolution de liens
	 * @param encoding Le type d'encodage du parser (par exemple, "UTF8", "ACSSI", etc.).
	 */


	public void defaultSetterParser(int level, int kind, boolean resolveBindings, boolean bindingsRecovery,
			String encoding) {
		parserType = ASTParser.newParser(level);
		parserType.setUnitName("");
		parserType.setKind(kind);
		parserType.setCompilerOptions(JavaCore.getOptions());
		parserType.setEnvironment(new String[] { getJrePath() },
				new String[] { getProjectPath() },
				new String[] { encoding }, true);
		parserType.setResolveBindings(resolveBindings);
		parserType.setBindingsRecovery(bindingsRecovery);
	}


	/**
	 * Implémentation de la méthode configure de la classe mère parser pour configure EclipseJDTParser
	 * @see Parser#configure() 
	 * Elle appelle la configuration par défaut de l'AST Parser défini dans la méthode defaultSetterParser.
	 */

	@Override
	public void configure() {
		defaultSetterParser(AST.JLS4, ASTParser.K_COMPILATION_UNIT, true, true, "UTF-8");
	}


	/**
	 * Méthode pour analyser un fichier Java spécifié en tant que source d'analyse dans l'AST Parser.
	 * 
	 * @param file Le chemin du fichier Java à ajouter en tant que source d'analyse dans l'AST Parser.
	 * @return L'objet CompilationUnit représentant la structure de l'AST (Abstract Syntax Tree) résultant de l'analyse du fichier.
	 * @throws IOException Si une erreur d'entrée/sortie se produit lors de la lecture du fichier.
	 */
	public CompilationUnit parse(File file) throws IOException {
		parserType.setSource(FileUtils.readFileToString(file, Charset.defaultCharset()).toCharArray());
		return (CompilationUnit) parserType.createAST(null);
	}

	/**
	 * Analyse tous les fichiers Java du projet en cours et retourne une liste de CompilationUnits.
	 * 
	 * @return Une liste de CompilationUnits, une pour chaque fichier Java analysé.
	 * @throws IOException Si une erreur d'entrée/sortie se produit lors de la lecture d'un fichier.
	 * @throws FileNotFoundException Si un fichier Java du projet n'est pas trouvé.
	 */
	public List<CompilationUnit> parseProject() throws IOException, FileNotFoundException {


		List<CompilationUnit> cUnits = new ArrayList<>();

		for (File sourceFile : listJavaProjectFiles())
			cUnits.add(parse(sourceFile));

		return cUnits;
	}

	/**
	 * Analyse le fichier Java spécifié et renvoie un objet CompilationUnit représentant l'Arbre Syntaxique Abstrait (AST) du fichier analysé.
	 * Si le fichier est un fichier Java valide avec une extension ".java"

	 * @param file Le fichier Java à analyser. Il devrait avoir l'extension '.java'.
	 * @return Un objet CompilationUnit représentant l'AST du fichier analysé.
	 * @throws IOException En cas d'erreur d'entrée/sortie lors de la lecture du fichier.
	 */

	public CompilationUnit parseWithVerify(File file) throws IOException {
	    if (!isValidJavaFile(file)) {
	        throw new IOException("The specified file is not a valid Java file with a '.java' extension: " + file.getAbsolutePath());
	    }
	    return parse(file);
	}
	
	//Vérifier si le fichier est un fichier Java valide  se terminant par l'extension '.java' 
	public boolean isValidJavaFile(File file) {
	    return file != null && file.isFile() && file.getName().endsWith(".java");
	}


}
