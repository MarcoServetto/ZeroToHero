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
      //levels.resolve("Level201","Level201.html"),
      resources.resolve("BaseJs.js"),
      resources.resolve("BaseStyle.css"),
      resources.resolve("RoundButtonsStyle.css"),
      directInstructions.resolve("DirectInstructions.css"),
      directInstructions.resolve("DirectInstructions.js"),
      walking.resolve("GameOptions.js"),
      resources.resolve("Question.js"),
      resources.resolve("Score.js"),
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
    
    Offensive programming:
    As much as possible, we follow offensive programming where we
    check that anything exists and has the expected shape, and throw errors
    if this is not the case. We can do this with 'Utils.checkExists' and other
    methods. This is in contrast with many common JS patterns where
    a reasonable default is selected instead. The objective here is to fail 
    fast and fail in a very self evident way. 
    ----------------
    All code you will suggest must follow the existing conventions, including style
    and file paths (for example, using ../../resources/... )
    Yes, the current style is unusual. This is intentional.
    In particular, everyhing need to stay in the gameArea component
    styled (very unusually) in the base css.
    Whenever possible, try to reuse existing code/css instead of repeating it.
    This may include moving css into the base css so that it can be seen in multiple
    minigames.
    ----------------
    - First, confirm that you have all the information needed to answer.
    I may have forgot to include some files. If some file or function is referenced but is 
    not included in the provided data, please tell me about it so that I can add it. Do not try
    to give a solution from incomplete data.
    
    - If you think you have all the data, try to help for a solution.
    ----------------
    Currently,
    I'm tring to design a new minigame, it will be called 'climbing.
    Climbing will teach about 'small step reduction'.
    I think it will be somehow similar to 'walking' but while walking has the graphical part 'horizontal'
    climbing will have a vertical 'gamey graphical part' in the center 'stripe' of the screen,
    and we are going to have code and other info in the left and the right.
    
    There are going to be quite a few text areas, all not editable, and all except one not selectable.

    -Description of the layout:
    
    The game area is divided in 3 equally sized column, left, center and right.
    
    # Left
    left column has two text areas left top and left bottom:
      leftTop has common code and instructions
      leftBottom shows a current expression under reduction.
      leftBottom allows selection (not edititing, just selection)
      There is no concept of the special selected character as in walking.
      The user can select any text they want.
     
     #Center
     center column has an image of a mountain wall.
     This works similarly to the image of the street in the walking, but vertically instead of horizontaly.
     The walking image tiles left to right, so it can animate left (or right) movements
     The climbing image tiles up to bottomom, so it can animate up (or down) movements.
     The walking image moves at a specified speed via css
     The climbin image moves in a much more controlled way via JS.
     Two JS function need to be defined:
     - moveUp, slowly scrolling the image down, simulating climbing up.
     This needs to scroll up about 50%.
     - fallDown, fast scrolling the image up, simulating falling.
     This neeed to scroll down all the amount that we have gone up.
     After we fully scroll down, an image of our hero hitting the ground in a
     fun/safe cartoonish way is shown.
     Then another image is shown with the stars around the head.
     This two images need to be shown using code similar to the flashGreen() function.
     
     In the center, we can also display up to 5 rocks at about 65% height from the bottom of the game area
     each rock will use an image with transparant background from a folder resources.climbing.images.Rocks
     there will be many rocks, like Rock1.png
     When the user put the mouse cursor on top of a rock, some code is shown.
     The code must visually look exactly like the other codes, so we may need to dynamically show and position
     an actual textArea with the right style. 
     
     #Right
     the right column will have a rightTop and rightBottom
     rightTop will contain another text area, with additional informations
     This text area will have a vertical scroll bar always present, and may contain
     quite many lines of text in harder exercises.     
     rightBottom will have a round point counter identical to the one of walking.
     This means that we will have to move some of the style currenty in walking
     into the base style, so that it can be reused by both. Css about the code/text areas may also
     need to be moved.
     This point just grow of 1 for every correct answer, and has a 'fall to zero' animation when we
     make a mistake.
     In climbing the questions have a precise order. Falling to zero cause the page to reload, so every
     climbing attempt will use the questions in the same order, and there is no need to keep track of the
     solved questions, just how far we are in the chain of questions.
     When all the questions are solved, we get the green flash and we move to the next level automatically.
     
     
     #The user action/interaction
     To solve a question, the user must do a two step process:
     1-select the correct piece of text from leftBottom.
     If the wrong piece of text is selected, 
     the leftBottom glowOrange (also the orange glow may need to be moved in the global css file?)
     for a second or two, then the selection is removed and the user can select again.
     After 5 wrong selection attempts, the correct selection is shown, then removed and the user can select again.
     Failing to select does NOT cause the player to fall.
     2-After a correct selection, a text area with the selected code is 'paired' with the cursor, that now can move on screen by 'dragging'
     this text on one of the rocks.
     The user must then click on the correct rock.
     If the user clicks on the correct rock, all the rocks disappear, we climb up,
     the set of rocks for the next question appear,
     and the text areas leftTop and leftBottom now display the text for the next question.
     The score animates and goes +1.
     (Note: we do not show the needed score amount, or other score informations here, just the current total score)
     If the user clicks on the wrong rock, we get the fall down animation, and after a little pause, the page reloads.
     
     #The questions format
     The questions will use the same kind of conventions of the walking questions.
     There will be (meta) data associated to the tags with id="question1" and so on.
     The data will be:
       context: the content of leftTop
       currentCode: the content of leftBottom
       selectionStart and selectionEnd: the start and end of the correct selection
       rock1Img: the url for the image for rock1
       rock1Code: the code to visualize under rock1 when the cursor is on top of rock1
       rock2Img,rock2Code etc... up to rock5 will represent the other rocks.
       Note: not all 5 rocks need to be present, any number of rocks from 1 to 5 can be present
       correctRock: the number of the correct rock.
       
     #Expected output:
     I expect you to give me the following files:
     -resources.climb.Climb.css
     -resources.climb.Climb.js
     -an example ClimbExample.html
     In addition, show what code I have to move from existing files
     in the more 'global' ones. This may include some JS too.
     
     Thanks a lot, this project does help my students to learn and to be
     happier while they do it.
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
