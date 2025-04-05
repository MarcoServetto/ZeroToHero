package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class CollectText {
  public static void main(String[] args) throws IOException, URISyntaxException {
    var content = files
      .addS(levels.resolve("Level101", "Level101.html"))
      .add(levels.resolve("Level102", "Level102.html"))
      .addS(levels.resolve("Level103", "Level103.html"))
      .addS(levels.resolve("Level104", "Level104.html"))
      .addS(levels.resolve("Level105", "Level105.html"))
      .addS(levels.resolve("Level106", "Level106.html"))
      .addS(levels.resolve("Level107", "Level107.html"))
      .addS(levels.resolve("Level108", "Level108.html"))
      .addS(levels.resolve("Level109", "Level109.html"))
      .addS(levels.resolve("Level110", "Level110.html"))
      .addS(levels.resolve("Level111", "Level111.html"))
      .addS(levels.resolve("Level112", "Level112.html"))
      .addS(levels.resolve("Level113", "Level113.html"))
      .addS(levels.resolve("Level114", "Level114.html"))
      .addS(levels.resolve("Level201", "Level201.html"))
      .addS(levels.resolve("Level202", "Level202.html"))
      .addS(levels.resolve("Level203", "Level203.html"))
      .addS(levels.resolve("Level204", "Level204.html"))
      .addS(levels.resolve("Level205", "Level205.html"))
      .addS(levels.resolve("Level301", "Level301.html"))
      .addS(levels.resolve("Level302", "Level302.html"))
      .addS(levels.resolve("Level303", "Level303.html"))
      .addS(levels.resolve("Level304", "Level304.html"))
      .addS(levels.resolve("Level305", "Level305.html"))
      .addS(levels.resolve("Level306", "Level306.html"))
      .addS(levels.resolve("Level307", "Level307.html"))
      .addS(levels.resolve("Level308", "Level308.html"))
      .addS(levels.resolve("Level309", "Level309.html"))
      
      .addS(resources.resolve("Book.html"))
      .addS(resources.resolve("Climb.html"))
      .addS(resources.resolve("DirectInstructions.html"))
      .addS(resources.resolve("Gather.html"))
      .add(resources.resolve("Walking.html"))

      .addS(htmlMangle.resolve("Book.java"))
      .addS(htmlMangle.resolve("Climb.java"))
      .addS(htmlMangle.resolve("DirectInstructions.java"))
      .addS(htmlMangle.resolve("Gather.java"))
      .add(htmlMangle.resolve("Walking.java"))

      .addS(day01.resolve("Day1Outro.java"))
      .addS(day01.resolve("ExplainGather.java"))
      .addS(day01.resolve("ExplainReverse.java"))
      .addS(day01.resolve("GatherPre1.java"))
      .addS(day01.resolve("GatherPre1b.java"))
      .addS(day01.resolve("GatherPre2.java"))
      .addS(day01.resolve("GatherPre3.java"))
      .addS(day01.resolve("GatherPreTrueExpr.java"))
      .addS(day01.resolve("GatherTrueDecl.java"))
      .addS(day01.resolve("GatherTrueExpr.java"))
      .addS(day01.resolve("IntroLevel.java"))
      .add(day01.resolve("Walking1.java"))
      .addS(day01.resolve("Walking2.java"))
      .addS(day01.resolve("WalkingBack.java"))

      .addS(day02.resolve("Archery1.java"))
      .addS(day02.resolve("Archery2.java"))
      .addS(day02.resolve("BookArchery1.java"))
      .addS(day02.resolve("BookArchery2.java"))
      .addS(day02.resolve("BookIntro.java"))
      .addS(day03.resolve("ClimbArcherAimTo.java"))
      .addS(day03.resolve("ClimbBase.java"))
      .addS(day03.resolve("ClimbBaseArcher.java"))
      .addS(day03.resolve("ClimbFood.java"))
      .addS(day03.resolve("ClimbStone.java"))
      .addS(day03.resolve("Day3Outro.java"))
      .addS(day03.resolve("GatherOnTop.java"))
      .addS(day03.resolve("WalkingBoomerangParam.java"))
      .addS(day03.resolve("WalkingExplainClimb.java"))

      .add(main.resolve("Days.java"))
      .add(main.resolve("Main.java"))
      .add(resources.resolve("File.java"))
      .add(htmlMangle.resolve("Escape.java"))
      .add(htmlMangle.resolve("Range.java"))
      
      .add(resources.resolve("BaseJs.js"))
      .add(resources.resolve("Question.js"))
      .add(resources.resolve("Score.js"))
      .addS(directInstructions.resolve("DirectInstructions.js"))
      .add(walking.resolve("GameOptions.js"))
      .add(walking.resolve("Walking.js"))
      .addS(gather.resolve("CodeCard.js"))
      .addS(gather.resolve("Gather.js"))
      .addS(book.resolve("Book.js"))
      .addS(climb.resolve("Climb.js"))

      .add(resources.resolve("BaseStyle.css"))
      .add(resources.resolve("RoundButtonsStyle.css"))
      .add(resources.resolve("FontsSetUp.css"))
      .addS(directInstructions.resolve("DirectInstructions.css"))
      .add(walking.resolve("Walking.css"))
      .addS(gather.resolve("Gather.css"))
      .addS(book.resolve("Book.css"))
      .addS(climb.resolve("Climb.css"))      
      
      .build();    
    String currentTask= """
Currently, I'm trying to get the Walking minigame to be more compact.
In particular, file GameOptions.js looks really redundant.
Can we make it so that we can read that data from the html structure?
if the html does not have all that is needed, can we add some metadata?
The goal is that in the future we can change the html and the javascript will adapt automatically
to a different set of possible answers.
""";
  content.writeReport(outputFile, intro, outro+currentTask);
  }
  private static final String intro="""
I'm making a browser based game to teach programming.
Here are the relevant files I have
""";
  private static final String outro="""
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
To reduce indented parts of code, normalize the logic so that the if-return part is the shorter.
Normalized method form:
When possible, fit method bodies in one of those two cathegories:
- Simple delegation: this method can fit in one line
- guards,computation,result
  guards: at the start of the method we write a bunch of 'if-error' or 'if-return'
  to exclude cases where the code should not be called or the result is obvious.
  computation: this is the center part, here we can use some for/stream/while/loop
  however, keep it simple and delagate to another method if needed.
- result: this part computes the result using information produced in the computation.
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
In particular, everyhing need to stay in the gameArea component styled (very unusually) in the base css.
Whenever possible, try to reuse existing code/css instead of repeating it.
This may include moving css into the base css so that it can be seen in multiple minigames.
----------------
- First, confirm that you have all the information needed to answer.
I may have forgot to include some files. If some file or function is referenced but is 
not included in the provided data, please tell me about it so that I can add it.
Do not try to give a solution from incomplete data.
- If you think you have all the data, try to help for a solution.
----------------
""";
  private static final Path workingDir = Path.of("C:\\Users\\sonta\\Documents\\GitHub\\ZeroToHero\\src");
  private static final Path levels= workingDir.toAbsolutePath().resolve("assetsDest");
  private static final Path levelsLeo= workingDir.toAbsolutePath().resolve("assetsLeo");
  private static final Path resources= workingDir.toAbsolutePath().resolve("resources");
  private static final Path walking= resources.resolve("walking");
  private static final Path directInstructions= resources.resolve("directInstructions");
  private static final Path gather= resources.resolve("gather");
  private static final Path book= resources.resolve("book");
  private static final Path outputFile= workingDir.toAbsolutePath().resolve("assets", "collectedText","collectedText.txt");

  private static final Path main = workingDir.resolve("main");
  private static final Path htmlMangle = workingDir.resolve("htmlMangle");

  private static final Path climb = resources.resolve("climb");

  private static final Path levelsDir = workingDir.resolve("levels");
  private static final Path day01 = levelsDir.resolve("day01");
  private static final Path day01Leo = levelsDir.resolve("day01Leo");
  private static final Path day02 = levelsDir.resolve("day02");
  private static final Path day03 = levelsDir.resolve("day03");

  
  private static final FilesIn files= new FilesIn(workingDir)
    .ignore("*.png")
    .ignore("*.jpg")
    .ignore("*.bmp")
    .ignore("*.pdf")
    .ignore("*.odp")
    .ignore("*.xcf")
    .ignore("*.woff2")
    .ignore("*.txt")
    .addS(workingDir.resolve("module-info.java"))
    .addS(workingDir.resolve("assets","walking","editableDiv.html"))
    .addS(day01Leo.resolve("Leo1.java"))
    .addS(day01Leo.resolve("Leo2.java"))
    .addS(day01Leo.resolve("Leo3.java"))
    .addS(day01Leo.resolve("Leo4.java"))
    .addS(levelsLeo.resolve("Level101","Level101.html"))
    .addS(levelsLeo.resolve("Level102","Level102.html"))
    .addS(levelsLeo.resolve("Level103","Level103.html"))
    .addS(levelsLeo.resolve("Level104","Level104.html"))
    .addS(main.resolve("CollectText.java"))
    .addS(main.resolve("FilesIn.java"))
    .addS(main.resolve("RealMain.java"));
}
