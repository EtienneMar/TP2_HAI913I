package parsers;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public abstract class Parser<T> {


	private static final String sourcePathForJava = File.separator + "src";
	protected String projectPath;
	protected String jrePath;

	// 
	protected T parserType; 



	/*	Constructor
	 * 
	 * Appelle des setters dans le Constructeur afin de respecter 
	 * les principes solides tout en gérant les exceptions de ces derniers. 
	 * 
	 * */

	public Parser(String projectPath) throws NullPointerException, FileNotFoundException {
		this.setProjectPath(projectPath);
		this.setJrePath();
		this.configure();
	}


	public String getProjectPath() {
		return projectPath;
	}

	public String getJrePath() {
		return jrePath;
	}



	/**
	 * Définit le chemin d'accès au répertoire du projet manuellement à l'aide d'un objet
	 * scanner. 
	 * Vérification de la validité du chemin à l'aide de {@link #verifyIfDirectoryExist(String)}.
	 * Si oui il est assignée à la variable
	 * Si non l'utilisateur doit à nouveau saisir le répertoire existant
	 * @throws FileNotFoundException 
	 * 
	 * */
	public void setProjectPath(String projectPath) throws FileNotFoundException {
		if (projectPath.endsWith(sourcePathForJava))
		{
			if(verifyIfDirectoryExist(projectPath)) {
				this.projectPath = projectPath;
			}else {throw new FileNotFoundException("NullPointerException, le dossier ne contient pas de fichier source");}
		}else {
			if(verifyIfDirectoryExist(projectPath+sourcePathForJava)) {
				this.projectPath = projectPath+sourcePathForJava;
			}else {throw new FileNotFoundException("File not found, le dossier ne contient pas de fichier source");}
		}
		System.out.println(projectPath);
	}

	/** 
	 * Initialise le path de la Java Runtime Environment (JRE) 
	 * en fonction du java.home du système utilisateur  * 
	 * 
	 * Si la propriété existe sur le système on assigne le path
	 * Sinon * @throws JRENotFoundException 
	 * 
	 * */
	public void setJrePath() throws NullPointerException {
		String jrePath = System.getProperty("java.home");

		if (jrePath != null) {
			this.jrePath = jrePath;
		}else {
			throw new NullPointerException("Attention la variable system 'java.home' "
					+ "n'est pas correctement initialisée sur votre system ou est null");
		}
	} 


	/**
	 * Récupère la liste des fichiers Java à partir d'un chemin de dossier spécifié.
	 * Si le fichier n'est pas un fichier Java il n'est pas ajouté
	 * 
	 * Si le fichier en question est un dossier, cette méthode est appelée récursivement pour
	 * ajouter la liste de fichiers Java au sein de ce dossier.
	 * 
	 * @param filePath le path du dossier où les éléments java sont à récupérer
	 * 
	 * @return javaFiles une liste de fichier Java {@code List<File>}
	 */
	private List<File> listJavaFiles(String filePath){
		File folder = new File(filePath);
		List<File> javaFiles = new ArrayList<>();

		for (File file: folder.listFiles()) {
			if (file.isDirectory())
				javaFiles.addAll(listJavaFiles(file.getAbsolutePath()));
			else if (file.getName().endsWith(".java"))
				javaFiles.add(file);
		}

		return javaFiles;
	}


	/**
	 * Appel la fonction listJavaFiles avec le path du projet spécifier par l'utilisateur
	 * 
	 * @throws FileNotFoundException Si aucun fichier Java n'a été trouvé dans le répertoire spécifiée
	 * @return Une liste de fichiers Java {@code List<File>}.
	 */

	public List<File> listJavaProjectFiles() throws FileNotFoundException{
		List<File> listJavaFiles =  listJavaFiles(getProjectPath());
		if (listJavaFiles.isEmpty()) {
			throw new FileNotFoundException("Il n'existe aucun fichier java dans le répertoire source spécifiée"); 
		}

		return listJavaFiles;

	}

	/**
	 * Vérifie si un répertoire ou fichier existe à l'emplacement spécifié.
	 *
	 * @param path Le chemin d'accès au répertoire ou fichier à vérifier.
	 * @return {@code true} si le répertoire ou fichier existe, sinon {@code false}.
	 *         Un {@link NullPointerException} peut être levé si le chemin est nul.
	 * @throws FileNotFoundException 
	 * */
	protected boolean verifyIfDirectoryExist(String path) {
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory())
		{
			return true;
		}else {return false;}
	}


	/**
	 * Configure la fonctionnalité ou le comportement du Parser.
	 * 
	 * Cette méthode doit être implémentée par les sous-classes pour définir
	 * la configuration spécifique à chaque type de Parser utilisé. 
	 */
	public abstract void configure();


	/**
	 * Implémentation de la méthode configure de la classe mère parser pour configure EclipseJDTParser
	 * @param string 
	 * @see Parser#configure() 
	 * Elle appelle la configuration par défaut de l'AST Parser défini dans la méthode defaultSetterParser.
	 */
	public abstract void configure(String string); 

}
