package visitor;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class ImportDeclarationVisitor extends ASTVisitor {
    private final List<ImportDeclaration> projectImports = new ArrayList<>();
    private final ArrayList<String> packageDotClassName = new ArrayList<>();
    private String projectPrefix = null; // Préfixe du projet
	private final HashMap<String, String> classImportMap = new HashMap<>();



	public ImportDeclarationVisitor(String projetPrefix, Set<String> listJavaFile) {
		super();
		this.setProjectPrefix(projetPrefix);
		this.setPackageDuProjet(listJavaFile);
	}


    @Override
    public boolean visit(ImportDeclaration node) {
        String importName = node.getName().getFullyQualifiedName();
        // Vérifie si l'importation commence par le préfixe du projet
        if (importName.startsWith(projectPrefix)) {
            projectImports.add(node);
            String className = getLastSegment(importName);
            classImportMap.put(className.toUpperCase(), importName);
        }else {  
        	for(String packageDotClassName : packageDotClassName) {
        		packageDotClassName.startsWith(importName);
        		classImportMap.put(getLastSegment(packageDotClassName).toUpperCase(),
        							packageDotClassName);
        	}
        }
        return super.visit(node);
    }

    public List<ImportDeclaration> getProjectImports() {
        return projectImports;
    }
    
	private String getLastSegment(String input) {
	    if (input == null) {
	        return null;
	    }

	    int lastDotIndex = input.lastIndexOf('.');
	    
	    return (lastDotIndex >= 0 && lastDotIndex < input.length() - 1) ? input.substring(lastDotIndex + 1) : input;
	}
	
	private String getFirstSegment(String input) {
	    if (input == null) {
	        return null;
	    }

	    int lastDotIndex = input.lastIndexOf('.');
	    
	    return (lastDotIndex >= 0) ? input.substring(0, lastDotIndex) : input;
	}

	public HashMap<String, String> getClassImportMap() {
		return classImportMap;
	}

    public String getProjectPrefix() {
		return projectPrefix;
	}

	public void setProjectPrefix(String projectPrefix) {
        String filePath = "C:\\Users\\33683\\Desktop\\Master_ICO_M1\\S8\\Structure_De_Donnee\\TP3\\src";
        Path path = FileSystems.getDefault().getPath(filePath);

        // Récupérer le dernier élément du chemin (TP3)
        //String lastElement = path.getFileName().toString();
	
        // Récupérer l'élément avant-dernier (ras) si possible
        if (path.getNameCount() >= 2) {
        	this.projectPrefix = path.getName(path.getNameCount() - 2).toString();
            //System.out.println("Dernier élément : " + lastElement);
            //System.out.println("Élément avant-dernier : " + avantDernier);
        } else {
            //System.out.println("Chemin trop court pour obtenir l'élément avant-dernier.");
        }
	}
	
	public List<String> getPackageDuProjet() {
		return packageDotClassName;
	}

	public void setPackageDuProjet(Set<String> listJavaFile) {
		this.packageDotClassName.addAll(listJavaFile);
	}
}
