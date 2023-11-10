package processor;

import java.io.IOException;

/*
 *  Classe abstraite Processor pour l'utiliser dans un processor de type Parser
 */
public abstract class Processor<T> {

    /*
     * attributs protéges pour un parser donnée qui permet de parser un projet
     */
    protected T parser;
    // private String projectPath;
    // private String sourcePath;

    /*
     * constructeur de la classe Processor qui prend en parametre le chemin du
     * projet à parser
     */
    public Processor(String projectPath) {
        this.setParser(projectPath);
    }

    /*
     * constructeur de la classe Processor
     */
    public Processor(T parser) {
        this.parser = parser;

    }

    /*
     * methode pour retourner le parser
     */
    public T getParser() {
        return parser;
    }

    /*
     * methode abstraite pour initialiser le parser
     */
    public abstract void setParser(String projectPath);

    /*
     * methode abstraite pour initialiser le parser
     */
    public abstract void process() throws IOException;

}
