package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CollectText {
  public static void main(String[] args) throws IOException, URISyntaxException {
    var workingDir = Path.of("C:\\Users\\sonta\\Documents\\GitHub\\ZeroToHero\\src");
    Path levels= workingDir.toAbsolutePath().resolve("assets");
    Path resources= workingDir.toAbsolutePath().resolve("resources");
    Path walking= resources.resolve("walking");
    Path directInstructions= resources.resolve("directInstructions");

    Path outputFile= workingDir.toAbsolutePath().resolve("assets", "collectedText","collectedText.txt");
    List<Path> allFiles=List.of(
      levels.resolve("level1","Level1.html"),
      levels.resolve("level2","Level2.html"),
      resources.resolve("BaseJs.js"),
      resources.resolve("BaseStyle.css"),
      resources.resolve("RoundButtonsStyle.css"),
      directInstructions.resolve("DirectInstructions.css"),
      directInstructions.resolve("DirectInstructions.js"),
      walking.resolve("GameOptions.js"),
      walking.resolve("Question.js"),
      walking.resolve("Walking.js"),
      walking.resolve("Walking.css")
      );
    String intro="""
    I'm making a browser based game to teach programming.
    Here are the relevant files I have
    """;
    String outro="""
    -------------------
    As you can see, my formatting and coding style are quite peculiar.
    I need that becuase I'm half blind and this style helps me to keep enough
    code on screen so that I can reason about it.
    As you can see, I"m also limiting my usage of Js to mostly fat arrows and FP style.
    When suggesting more code, keep consitency with this style.
    """;
    try (var writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE)){
      writer.write(intro);
      for (Path file : allFiles) {
        writer.write("\n//---File " + workingDir.relativize(file)+"\n");
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
