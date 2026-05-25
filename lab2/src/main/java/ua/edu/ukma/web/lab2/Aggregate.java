package ua.edu.ukma.web.lab2;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "aggregate", defaultPhase = LifecyclePhase.COMPILE)
public class Aggregate extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true)
    private File sourceDirectory;
    @Parameter(defaultValue = "${project.build.directory}/aggregated-sources.txt")
    private File outputFile;

    public void execute() throws MojoExecutionException {
        if (!sourceDirectory.exists()) {
            getLog().warn("Source directory does not exist: " + sourceDirectory.getAbsolutePath());
            return;
        }

        try {
            outputFile.getParentFile().mkdirs();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();

            getLog().info("Aggregating Java files in: " + outputFile.getAbsolutePath());

            try (Stream<Path> paths = Files.walk(sourceDirectory.toPath())) {
                List<Path> javaFiles = paths
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .collect(Collectors.toList());

                for (Path javaFile : javaFiles) {
                    String content = "\n\nFile: " + javaFile.getFileName() + "\n";
                    content += new String(Files.readAllBytes(javaFile));
                    Files.write(outputFile.toPath(), content.getBytes(), StandardOpenOption.APPEND);
                }
                getLog().info("Successfully aggregated files: " + javaFiles.size());
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error during aggregation", e);
        }
    }
}