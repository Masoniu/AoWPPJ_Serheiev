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

// Ціль називається "aggregate" і запускається на фазі компіляції
@Mojo(name = "aggregate", defaultPhase = LifecyclePhase.COMPILE)
public class Aggregate extends AbstractMojo {

    // Автоматично бере шлях до папки з вихідним кодом проєкту, де запускається плагін
    @Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true)
    private File sourceDirectory;

    // Шлях, куди буде збережено результат (у папку target)
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

            getLog().info("Збираємо Java файли у: " + outputFile.getAbsolutePath());

            try (Stream<Path> paths = Files.walk(sourceDirectory.toPath())) {
                List<Path> javaFiles = paths
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .collect(Collectors.toList());

                for (Path javaFile : javaFiles) {
                    String content = "\n\n// --- File: " + javaFile.getFileName() + " ---\n";
                    content += new String(Files.readAllBytes(javaFile));
                    Files.write(outputFile.toPath(), content.getBytes(), StandardOpenOption.APPEND);
                }
                getLog().info("Успішно зібрано файлів: " + javaFiles.size());
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Помилка під час збору файлів", e);
        }
    }
}