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
    Path levels= workingDir.toAbsolutePath().resolve("assetsDest");
    Path resources= workingDir.toAbsolutePath().resolve("resources");
    Path walking= resources.resolve("walking");
    Path directInstructions= resources.resolve("directInstructions");
    Path gather= resources.resolve("gather");

    Path outputFile= workingDir.toAbsolutePath().resolve("assets", "collectedText","collectedText.txt");
    List<Path> allFiles=List.of(
      levels.resolve("Level1","Level1.html"),
      levels.resolve("Level2","Level2.html"),
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
        
    Note how we only use percentage and ex, and we avoid px at all costs. This allows for the pages to be scalable.
    
    Note how we consistenty use the html 'hidden' to make stuff appear and disappear.
    
    As you can see, I"m also limiting my usage of Js to mostly fat arrows and FP style.
    When suggesting more code, keep consitency with this style.
    
    Style: only fat arrows, closure based objects, no use of 'this'.
    Indentation: note the position of the closed curlies. This used to be called banner style.
    if (cond){
      bbbb
      bbbb
      }//Yes: banner style
    if (cond){
      bbbb
      bbbb
    }//No: other style
    Ifs: note how we often declare a const to store the condition with a descriptive name
    That is:
    --Bad version below:
    const cond = ...;
    if (cond){// If drop on slot:
      ..
      }//BAD CODE: using comment for describing behaviour
    --Good version below:
    const dropOnSlot = ...;
    if (dropOnSlot){
      ..
      }//GOOD CODE: using variable name for describing behaviour
    -----
    note how we use curlies all of the times and we keep short ifs on one line (but with curlies)
    We consistently add curlies on if-for-while bodies, but we omit any other kind of redundant curlies.
        
    Compact code: always write the most compact and correct code possible.
    For example, avoid code like "if (cond){ return true; } else { return false; }"
    
    Normalized ifs:
      When possible, avoid if-else and favour if-return.
      To reduce indented parts of code, normalize the logic so that the
      if-return part is the shorter.
      
    Normalized method form:
    When possible, fit method bodies in one of those two cathegories:
    - Simple delegation: this method can fit in one line
    - guards,computation,result
      guards: at the start of the method we write a bunch of 'if-error' or 'if-return'
      to exclude cases where the code should not be called or the result is obvious.
      computation: this is the center part, here we can use some for/stream/while/loop
        however, keep it simple and delagate to another method if needed.
      result: this part computes the result using information produced in the computation.
        Note: it is perfectly fine to use early returns when more compact.
    Use of spaces around '='.
    We distinguish update "a = b" from initialization "a= b".
    Thus,
    const x= 3; //Good
    const x = 3; //Bad: this is initialization
    let x= 3;//Good, still initialization    
    if (cond){ x = 3; }//Good, this is update
    ----------------
    At this point I think I want to make sure that every time we go to the next level, there is some cool
    animation showing that the game is progressing.
    Since it is always pressing a specific button that goes to the next level,
    we should be able to add some specific code to BaseJs in order
    to get the animation working on all level changes, for all kinds of levels. 
    Can you design code in this sense?
    -Think carefully what a good 'jump to next level' animation can be.
    The player do not really knows about 'level numbers', they
    think in order of 'game days' and each day is made by a number of levels,
    for example day1 is 9 levels.
    - I can make new images with transparency if you give me a clear description of the image.
     Images may or may not be appropriate for this animation.
    - The animation should last between 3 and 15 seconds, but you probably know
     better than me what an appropriate time is.
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
