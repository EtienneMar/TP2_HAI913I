package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportDeclarationVisitor extends ASTVisitor {
    private final List<ImportDeclaration> projectImports = new ArrayList<>();
    private String projectPrefix = null; // Préfixe du projet


	public ImportDeclarationVisitor(String projetPrefix) {
		super();
		this.setProjectPrefix(projetPrefix);
	}

	private final HashMap<String, String> classImportMap = new HashMap<>();

    @Override
    public boolean visit(ImportDeclaration node) {
        String importName = node.getName().getFullyQualifiedName();
        
        // Vérifie si l'importation commence par le préfixe du projet
        if (importName.startsWith(projectPrefix)) {
        	
            projectImports.add(node);
            String className = getLastSegment(importName);
            classImportMap.put(className.toUpperCase(), importName);
            
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

	public HashMap<String, String> getClassImportMap() {
		return classImportMap;
	}

    public String getProjectPrefix() {
		return projectPrefix;
	}

	public void setProjectPrefix(String projectPrefix) {
		this.projectPrefix = getLastSegment(projectPrefix);
	}
}
