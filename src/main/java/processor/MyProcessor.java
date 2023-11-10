
package processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.CompilationUnit;

import parsers.EclipseJDTParser;
import ui.controller.Resultat;
import visitor.ClassInterfaceVisitor;
import visitor.MethodDeclarationVisitor;
import visitor.PackageDeclarationVisitor;

/*
 * Classe MyProcessor qui hérite de la classe Processor et qui permet de parser un projet avec le parser EclipseJDTParser et de faire des statistiques sur le projet parsé 
 */
public class MyProcessor extends Processor<EclipseJDTParser> {

    // private int countPackage = 0;

    /*
     * constructeur de la classe MyProcessor qui prend en parametre le chemin du
     * projet à parser
     */
    public MyProcessor(String path) {
        super(path);
    }

    /*
     * methode overrider de la classe Processor pour initialiser ler parser et qui
     * fait appel à la methode parseProject() du parser EclipseJDTParser
     */
    @Override
    public void process() throws IOException {
        EclipseJDTParser parser = new EclipseJDTParser(
                "");
        // parser.configure();
        parser.parseProject();
        // System.out.println(parser.parseProject());
    }

    /*
     * methode overrider de la classe Processor pour initialiser le parser avec un
     * try catch pour gérer les exceptions. A l'intérieur un parser EclipseJDTParser
     * y est instancié avec le chemin du projet à parser . Deux exceptions y sont
     * levées au cas où le chemin du projet est null ou si le fichier n'est pas
     * trouvé
     */
    @Override
    public void setParser(String projectPath) {
        try {
            parser = new EclipseJDTParser(projectPath);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * methode privée pour parcourir le projet avec la méthode parseProject() du
     * parser EclipseJDTParser et pour chaque CompilationUnit on fait appel à la
     * methode accept() qui prend en parametre un visitor qui est de type T étends
     * ASTVisitor
     */

   

    /*
     * methode pour compter le nombre de packages dans un projet donnée. Pour éviter
     * les doublons on utilise un HashSet qui ne prend pas en compte les doublons et
     * on retourne la taille de ce HashSet.
     * On fait à notre processCompilationUnitsWithVisito() avec le visitor de type
     * PackageDeclarationVisitor. Ce dernier permet de récupérer le nom du package
     * et de l'ajouter au HashSet.
     */
    public int countPackagesInProject() throws IOException {
        HashSet<String> packageSet = new HashSet<>();
        for (CompilationUnit cu : parser.parseProject()) {
            PackageDeclarationVisitor visitor = new PackageDeclarationVisitor();
            cu.accept(visitor);
            packageSet.add(visitor.getPackageName());
        }
        return packageSet.size();
    }

    /*
     * Méthode pour compter le nombre de classes dans un projet donnée. Pareil que
     * la méthode countPackagesInProject() mais avec un ClassInterfaceVisitor qui
     * permet de récupérer le nom de la classe nous avons procéder de la même
     * manière pour eviter les doublons et retourner le nombre de classes trouvées
     * dans le projet.
     */
    public int countClassesInProject() throws IOException {
        HashSet<String> classSet = new HashSet<>();
        for (CompilationUnit cu : parser.parseProject()) {
            ClassInterfaceVisitor visitor = new ClassInterfaceVisitor();
            cu.accept(visitor);
            System.out.println(visitor.getClassName());
            if (visitor.getClassName() != null) {
                classSet.add(visitor.getClassName());
            }
        }
        return classSet.size();

    }

    /*
     * Méthode pour compter le nombre de méthodes qu'il y' a dans le projet. Quasi
     * similaire aux deux méthodes précédentes mais avec un
     * MethodDeclarationVisitor, un compteur val est initialisé à 0 car on suppose
     * que dans un projet donné il peut avoir un doublon de méthode même
     * nomenclature mais qui ne sont pas dans la même classe. Le parcourt reste le
     * meme que les deux précédents avec un visitor MethodDeclarationVisitor qui
     * permet de récupérer le nom de la méthode et de l'ajouter au compteur val. On
     * retourne le compteur val.
     */

    public int countMethodInProject() throws IOException {
        int val = 0;
        for (CompilationUnit cu : parser.parseProject()) {
            MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
            cu.accept(visitor);
            val += visitor.getMethods().size();

        }
        return val;
    }

    /*
     * methode pour calculer le nombre moyen de méthodes par classe dans un projet
     * donné. On fait appel à la methode countMethodInProject() et
     * countClassesInProject() pour avoir le nombre de méthodes et le nombre de
     * classes qu'il y' a dans le projet. On retourne le résultat de la division des
     * deux.
     */
    public int meanMethodsPerClass() throws IOException {
        return countMethodInProject() / countClassesInProject();
    }

    /*
     * methode pour compter le nombre de linges de code que contient un projet
     * donné. Pour se faire on initialise un compteur val initialisé à 0, on fait
     * appel à la methode parseProject() du parser EclipseJDTParser pour parcourir
     * le projet. Pour chaque CompilationUnit on fait appel à la methode accept()
     * qui prend en parametre un visitor qui est ici un ClassInterfaceVisitor car on
     * suppose qu'on commence à coder à partir de la classe. Ce
     * dernier permet de récupérer le nombre de lignes de code et de l'ajouter au
     * compteur val et on retourne le copmteur val.
     */

    public int countLinesCodeInProject() throws IOException {
        int val = 0;
        for (CompilationUnit cu : parser.parseProject()) {
            ClassInterfaceVisitor visitor = new ClassInterfaceVisitor();
            cu.accept(visitor);
            val += visitor.getLinesOfCode();

        }
        return val;
    }

    /*
     * methode pour calculer le nombre moyen d'attributs qu'il y'a dans une classe
     * dans un projet donné. Pour se faire on initialise un compteur val initialisé
     * à 0, on fait appel à la methode parseProject() du parser EclipseJDTParser
     * pour parcourir le projet. Pour chaque CompilationUnit on fait appel à la
     * methode accept() qui prend en parametre un visitor qui est ici un
     * ClassInterfaceVisitor. Ce dernier permet de récupérer le nombre d'attributs
     * et de l'ajouter au compteur val et on retourne le copmteur val.
     */

    public int meanFieldPerClass() throws IOException {

        int field = 0;
        for (CompilationUnit cu : parser.parseProject()) {
            ClassInterfaceVisitor visitor = new ClassInterfaceVisitor();
            cu.accept(visitor);
            field += visitor.getAttributeCount();
        }
        return field / countClassesInProject();
    }

    /*
     * methode pour calculer le nombre moyen de lignes de code par méthode dans un
     * projet donné. on retourne la division du nombre de lignes de code par le
     * nombre de méthodes qu'il y'a dans le projet.
     */
    public int meanLinesPerMethod() throws IOException {
        return countLinesCodeInProject() / countMethodInProject();
    }

    /*
     * methode pour calculer les 10% des classes qui ont le plus de méthodes.nous
     * avons crée une classe Résultat dans le controller pour stocker les résultats
     * de calculs. On instancie une objet de type résulat, on parse le projet, on
     * parcourt avec les visitors de classe et methode on ajoute le nom de la classe
     * et le nombre de méthodes dans l'objet résultats. Et puis on a fait un Map de
     * type String, Integer pour stocker les résultats. On a fait un stream pour 
     * trier les résultats par ordre décroissant et on a fait un collect pour
     * récupérer les N premiers éléments. On a fait un résultat final pour stocker
     * les résultats et on retourne le résultat final.
     */
    public Resultat getTopClassesByMethodsCount() throws IOException {
        Resultat resultat = new Resultat();
        for (CompilationUnit cu : parser.parseProject()) {
            ClassInterfaceVisitor visitor = new ClassInterfaceVisitor();
            MethodDeclarationVisitor visitor2 = new MethodDeclarationVisitor();

            cu.accept(visitor);
            cu.accept(visitor2);
            resultat.addResultat(visitor.getClassName(), visitor2.getMethods().size());//

        }
        Map<String, Integer> sortedMap = resultat.getResultats().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));


        Long N = Math.round(0.1 * countClassesInProject());
        Map<String, Integer> firstNElements = sortedMap.entrySet()
                .stream()
                .limit(N)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        Resultat resultatFinal = new Resultat();
        resultatFinal.addAllResultat(firstNElements);
        return resultatFinal;

    }

    public Resultat getTopClassesByFieldsCount() throws IOException {
        Resultat resultat = new Resultat();
        for (CompilationUnit cu : parser.parseProject()) {
            ClassInterfaceVisitor visitor = new ClassInterfaceVisitor();
            cu.accept(visitor);
            resultat.addResultat(visitor.getClassName(), visitor.getAttributeCount());//

        }
        Map<String, Integer> sortedMap = resultat.getResultats().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        Long N = Math.round(0.1 * countClassesInProject());
        Map<String, Integer> firstNElements = sortedMap.entrySet()
                .stream()
                .limit(N)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        Resultat resultatFinal = new Resultat();
        resultatFinal.addAllResultat(firstNElements);
        return resultatFinal;

    }

    // Question 10

    public Resultat getTopClassByMethodsAndField() throws IOException {
        Resultat resultat = new Resultat();
        getTopClassesByMethodsCount().getKeys().forEach(e -> {
            try {
                if (getTopClassesByFieldsCount().getKeys().contains(e)) {
                    resultat.addResultat(e, 0);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return resultat;
    }

    // Queston 11 hashset

    public Resultat getTopClassWithXGivenMethods(int x) throws IOException {
        Resultat resultat = new Resultat();
        for (CompilationUnit cu : parser.parseProject()) {
            ClassInterfaceVisitor visitor = new ClassInterfaceVisitor();
            MethodDeclarationVisitor visitor2 = new MethodDeclarationVisitor();
            cu.accept(visitor);
            cu.accept(visitor2);
            if (visitor2.getMethods().size() > x) {
                resultat.addResultat(visitor.getClassName(), 0);
            }
        }
        return resultat;
    }

    // 12 Map<String, Integer>
    // Question 12 : 10% methodes avec le plus de lignes de code

    public Resultat getTopMethodsByLinesCode() throws IOException {
        Resultat resultat = new Resultat();
        for (CompilationUnit cu : parser.parseProject()) {
            MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
            cu.accept(visitor);
            resultat.addAllResultat(visitor.getMethodsLines());
        }

        Map<String, Integer> sortedMap = resultat.getResultats().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        Long N = Math.round(0.1 * countMethodInProject());
        Map<String, Integer> firstNElements = sortedMap.entrySet()
                .stream()
                .limit(N)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        Resultat resultatFinal = new Resultat();
        resultatFinal.addAllResultat(firstNElements);
        return resultatFinal;
    }

    // 13 probablement un Map<String, Integer>

    public Resultat getTopMethodsByParameters() throws IOException {
        Resultat resultat = new Resultat();
        for (CompilationUnit cu : parser.parseProject()) {
            MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
            cu.accept(visitor);
            resultat.addAllResultat(visitor.getMethodsParameters());
        }

        Map<String, Integer> sortedMap = resultat.getResultats().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        System.out.println("SORTED" + sortedMap.toString());

        Map<String, Integer> firstNElements = sortedMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(sortedMap.values().iterator().next()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        System.out.println("RES" + firstNElements.toString());
        Resultat resultatFinal = new Resultat();
        resultatFinal.addAllResultat(firstNElements);
        return resultatFinal;
    }
    
}