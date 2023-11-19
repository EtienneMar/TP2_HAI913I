package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtType;

public class SpoonParser extends Parser<Launcher> {

    /**
     * Constructor
     *
     * @param projectPath the path of the project specified by the user.
     * @throws NullPointerException
     * @throws FileNotFoundException
     */
    public SpoonParser(String projectPath) throws NullPointerException, FileNotFoundException {
        super(projectPath);
    }

    @Override
    public void configure() {
        parserType = new Launcher();
        parserType.getEnvironment().setNoClasspath(true);
        parserType.addInputResource(getProjectPath());
    }

    /**
     * Parse all Java files in the project and return the model.
     *
     * @return CtModel representing the Spoon model of the project.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public CtModel parseProject() throws IOException, FileNotFoundException {
        configure();
        parserType.buildModel();
        return parserType.getModel();
    }

    /**
     * Parse a single Java file and return its types.
     *
     * @param file the Java file to parse.
     * @return List of CtType representing types in the file.
     * @throws IOException
     */
    public List<CtType<?>> parseFile(File file) throws IOException {
        Launcher singleFileLauncher = new Launcher();
        singleFileLauncher.getEnvironment().setNoClasspath(true);
        singleFileLauncher.addInputResource(file.getAbsolutePath());
        singleFileLauncher.buildModel();
        return new ArrayList<>(singleFileLauncher.getModel().getAllTypes());
    }

    @Override
    public void configure(String string) {
        // This method can be used to configure Spoon based on specific requirements.
    }
}
