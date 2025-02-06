package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import htmlMangle.Escape;
import resources.File;

public class CollectText {
  public static void main(String[] args) throws IOException, URISyntaxException {
    var workingDir = Path.of("C:\\Users\\sonta\\Documents\\GitHub\\ZeroToHero\\src");
    Path levels= workingDir.toAbsolutePath().resolve("assetsDest");
    Path resources= workingDir.toAbsolutePath().resolve("resources");
    Path walking= resources.resolve("walking");
    Path directInstructions= resources.resolve("directInstructions");
    Path gather= resources.resolve("gather");
    Path book= resources.resolve("book");

    Path outputFile= workingDir.toAbsolutePath().resolve("assets", "collectedText","collectedText.txt");
    List<Path> allFiles=List.of(
      levels.resolve("Level101","Level101.html"),
      levels.resolve("Level102","Level102.html"),
      levels.resolve("Level106","Level106.html"),
      levels.resolve("Level201","Level201.html"),
      resources.resolve("BaseJs.js"),
      resources.resolve("BaseStyle.css"),
      resources.resolve("RoundButtonsStyle.css"),
      directInstructions.resolve("DirectInstructions.css"),
      directInstructions.resolve("DirectInstructions.js"),
      walking.resolve("GameOptions.js"),
      walking.resolve("Question.js"),
      walking.resolve("Walking.js"),
      walking.resolve("Walking.css"),
      gather.resolve("Gather.js"),
      gather.resolve("CodeCard.js"),
      gather.resolve("Gather.css"),
      book.resolve("Book.css"),
      book.resolve("Book.js")
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
    I'm now fixing the book.Right now the green flash is used to notify the end of a level, but I'm not so happy with it.
    - The book used to work using a different format for the metadata, the new one separe the options with |#| instead of comma
    - content of the book has been produced by a markdown to html converter;
      The resulting html formatting does not seem to work, we should have some css style for stuff under .column
    - The text to replace is sometimes inserted in code, and the markdown translation 'escapes' the tags. 
    That is, this was part of the original book source:    
    -----
    We can represent an Archer as follows:    
    ```
      Archer:{
        .heading: Direction,
        <#[0|#|.aiming:Direction,|#|eating tomatoes|#|.shoot->North|#|.aiming->this]#>
        }
    ```
    -----
    How to handle this issue?
    I'm doing the markdown conversion from Java using
    com.vladsch.flexmark.html.HtmlRenderer
    and the following code:
    
      static private final String middleTag= "|#|";
  static private final String holeStart= "<span class=\"hole\" data-correct=\"";
  static private final String holeMiddle= "\" data-options=\"";
  static private final String holeChar= "■";
  static private final String holeEnd1= "\">";
  static private final String holeEnd2= "</span>";
  static private final Pattern pattern = Pattern.compile("<#\\[(.*?)\\]#>");
  
  private String htmlHole(int solution, List<String> elems){
    String first= Escape.escapeForHtmlAttribute(elems.get(solution));
    int maxSize= elems.stream().mapToInt(String::length).max().getAsInt();
    String options= elems.stream()
      .map(Escape::escapeForHtmlAttribute)
      .collect(Collectors.joining(middleTag));
    return holeStart + first + holeMiddle + options
      + holeEnd1 + holeChar.repeat(maxSize) + holeEnd2;
    }  
  private String allHtmlHoles(String text){
    //find all openTag text closeTag
    //replace it with htmlHole(first,elems), for example <#[1|#|abc|#|dfg]#>
    //would be replaced with the result of htmlHole(1,List.of("abc","dfg")) 
    Matcher matcher = pattern.matcher(text);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      String content = matcher.group(1); // The part inside <#[ ... ]#> 
      String[] parts = content.split("\\Q"+middleTag+"\\E");
      int solutionIndex = Integer.parseInt(parts[0]);
      List<String> elems = List.of(parts);
      String replacement = htmlHole(solutionIndex, elems.subList(1,elems.size()));
      matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
      }
    matcher.appendTail(sb);
    return sb.toString();
    }
  private String generate(String text){
    String htmlText=allHtmlHoles(text);
    List<Parser.ParserExtension> extensions = List.of(TablesExtension.create());
    Parser parser = Parser.builder().extensions(extensions).build();
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    return renderer.render(parser.parse(htmlText));    
    }
  public String build(){
    return name.htmlNextLevel(File.Book_html.text)
      .replace("[###BODY_LEFT###]", generate(left))
      .replace("[###BODY_RIGHT###]", generate(right));
    }
    
  I could imagine to either personalize/customize the markdown engine or
  to make a pre pass when I replace all open/close ``` with some html tags
  styled to display the content mostly like code, but still allowing inned html stuff 
  What would you suggest?
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
