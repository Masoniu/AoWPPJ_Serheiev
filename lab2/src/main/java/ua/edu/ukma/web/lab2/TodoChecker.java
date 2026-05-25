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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "check-todos", defaultPhase = LifecyclePhase.VERIFY)
public class TodoChecker extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true)
    private File sourceDirectory;

    public void execute() throws MojoExecutionException {
        if (!sourceDirectory.exists()) {
            return;
        }

        getLog().info("Скануємо код на наявність TODO...");
        int todoCount = 0;

        try (Stream<Path> paths = Files.walk(sourceDirectory.toPath())) {
            List<Path> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            for (Path javaFile : javaFiles) {
                List<String> lines = Files.readAllLines(javaFile);
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("TODO")) {
                        getLog().warn("Знайдено TODO у " + javaFile.getFileName() + " на рядку " + (i + 1));
                        todoCount++;
                    }
                }
            }

            if (todoCount > 0) {
                getLog().warn("Всього знайдено TODO: " + todoCount + ". Будь ласка, виправте їх.");
            } else {
                getLog().info("Код чистий! TODO не знайдено.");
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Помилка під час сканування", e);
        }
    }
}