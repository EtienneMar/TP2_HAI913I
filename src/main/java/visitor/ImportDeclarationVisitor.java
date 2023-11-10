package visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;

/*
 * cette classe est un visiteur qui visite les déclarations de type import qui récupere la liste des imports.
 */
public class ImportDeclarationVisitor extends ASTVisitor {
    List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();// liste des imports

    /*
     * retourne vrai si le node visité est une déclaration de type import, ajoute le
     * node visité à la liste des imports.
     */
    @Override
    public boolean visit(ImportDeclaration node) {
        imports.add(node);
        return super.visit(node);
    }

    /*
     * getter de la liste des imports
     */
    public List<ImportDeclaration> getImports() {
        return imports;
    }

    /*
     * affiche la liste des imports
     */
    @Override
    public String toString() {
        return "ImportDeclarationVisitor [imports=" + imports + "]";
    }

}
