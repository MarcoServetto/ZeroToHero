package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FilesIn {
  private final Path root;
  private final List<String> ignorePatterns= new ArrayList<>();
  private final Map<Path, Boolean> fileGroups= new HashMap<>();
  public FilesIn(Path root){ this.root= root; }
  public FilesIn ignore(String pattern) {
    ignorePatterns.add(pattern);
    return this;
  }
  public FilesIn add(Path filePath) {
    Path absolute = filePath.toAbsolutePath().normalize();
    fileGroups.put(absolute, true);
    return this;
  }
  public FilesIn addS(Path filePath) {
    Path absolute = filePath.toAbsolutePath().normalize();
    fileGroups.put(absolute, false);
    return this;
  }

  private Set<Path> ignoredFiles(List<Path> allFiles){
    Set<Path> res= new HashSet<>();
    FileSystem fs = FileSystems.getDefault();
    for (String pattern : ignorePatterns){
      PathMatcher m= fs.getPathMatcher("glob:" + pattern);
      for (Path file : allFiles){
        if (m.matches(file.getFileName())){
          res.add(file.toAbsolutePath().normalize());
        }
      }
    }
    return Set.copyOf(res);
    }

  public Content build() throws IOException {
    List<Path> allFiles = Files.walk(root)
      .filter(Files::isRegularFile)
      .map(p -> p.toAbsolutePath().normalize())
      .toList();//wrong code but it is only an internal tool (does not close stream)
    Set<Path> relevantFiles= new HashSet<>(allFiles);
    relevantFiles.removeAll(ignoredFiles(allFiles));
    assertAllPresent(relevantFiles, fileGroups.keySet(), "Files not assigned to any group");
    assertAllPresent(fileGroups.keySet(), relevantFiles, "Files added but not found under root");    
    return new Content(root,fileGroups);
  }  
  private void assertAllPresent(Set<Path> expected, Set<Path> actual, String errorMessage){
    String missing = expected.stream()
      .map(p -> p.toAbsolutePath().normalize())
      .filter(p -> actual.stream()
        .map(Path::toAbsolutePath)
        .map(Path::normalize)
        .noneMatch(p::equals))
      .map(root::relativize)
      .map(Path::toString)
      .sorted()
      .collect(Collectors.joining("\n"));
    if (!missing.isEmpty()){
      throw new IllegalStateException(errorMessage + ":\n" + missing);
    }
  }
}

class Content {
  private final Path root;
  private final Map<Path, Boolean> fileGroups;
  public Content(Path root, Map<Path, Boolean> fileGroups){
    this.root= root;
    this.fileGroups = Map.copyOf(fileGroups);
  }
  public List<Path> getFilesForGroup(boolean group) {
    return fileGroups.entrySet().stream()
      .filter(entry -> entry.getValue() == group)
      .map(Map.Entry::getKey)
      .toList();
  }
  public void writeReport(Path outputFile, String intro, String outro) throws IOException{
    try (var writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE)){
      writer.write(intro);
      for (Path file : getFilesForGroup(true)){
        writer.write("\n//---File " + root.relativize(file)+"\n");
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
          if(line.trim().startsWith("//")){ continue; }
          writer.write(line);
          writer.newLine();
        }
      }
      writer.write(outro);
    }
  }
}
