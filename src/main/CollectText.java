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
    At this point I need to add a new minigame, called 'book'.
    The idea of book is that the game area will show an image of an open book, 
    that will contain fully formatted html stuff, with images etc in a vertically scrollable
    area.
    Most of that html will be simple texts/paragraphs etc.
    The text will have some words 'obscured', the idea is that the pages of the book
    are ruined, and we need to discover the missing text.
    From the aspect of coding this minigame, the are the following challenges:
    - Get a css showing text in a way that looks like an ancient book/scroll,
    with a scroll bar in theme.
    - Think a way to represent those missing words. There are many unicode characteres that
    looks really alien, may be we can use some of those? 
    or, some unicode showing dotted lines and or dots at various height level
    animation showing that the game is progressing.
    - When the user overs the mouse on top of a 'broken word/sentence', we need to figure out
    a good 'UI' to show them a list of options.
    The list of options need to not really cover the text, otherwise, how can they
    read the context of the words around? Discuss what can be a good solution and why.
    We expect to provide about 10 different options for the replacement.
    -When the user selects one of those options, the text will now contain such word.
    This introduces a new challenge: we must ensure that the formatting somehow stays consistent even
    if the text changes. That is, the 'replacement text' may not have the same exact length of the 'hole'
    but we need to make sure that all the words that were on that line before stay on that line.
    I'm not sure how to do that in html. May be we simply end that line shortish with a manual <BR> to 
    gurantee that the line does not split when we add the replacement?
    -When the user makes the wrong selection, they get a timeout.
    They can keep reading and scrolling, but they can not provide another replacement for xx seconds.
    This timeout time need to be clearlly visible with a cool countdown.
    This timeout need to start 'quick', like 10 sec only, and become bigger if they keep making mistakes, and
    becomes shorter if they make correct answers in a row.
    -The challenge here is to design a good artimetic progression for those timeouts, something that is clear to the player
    not too punishing, but not too light either.
    For example, every mistake could be times 1.5 and every success halved? (capped to the min of 10 sec)
    But, I'm really not sure, and I would like your opinions as an expert game designer.
    What is a good progression and why? is this a good idea at all to encourage the player to not just answer randomly?    
    Overall here I'm asking for game design decisions in addition to concrete implementations.
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
