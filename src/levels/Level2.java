package levels;
import static htmlMangle.Walking.Option.*;

public class Level2 implements Level{
  public String fileName(){ return "Level2"; }
  public String of(){
    return new htmlMangle.Walking(fileName(),33,3)
    // selected, start, end, option
    .question("""
      @[/*
      This is the walking minigame
      Select all the code and press 'comment'.
      Indeed, this code is a multiline comment.
      Text useful for explanation but
      ignored in execution@@.
      */]@""",Comment)
    .question("""
      @[//This is a @@single line comment]@
      //Select all and only the line with
      //the marked character. 
      """,Comment)
    .question("""
      @[No@@rth]@
      //In this kind of puzzle, we select the
      //smallest self contained unit of code
      //around the marked character. 
      //This time it is an object literal. 
      """,ObjectLiteral)
    .question("@[+%/-@@-%--]@",Error)
    .question("""
      //Text +%/--%-- outside of comments
      //is an error. When there is an error,
      //does not matter what you select.
      @[Just press @@error :-)]@ 
      """,Error)
    .question("""
      @[//@@Ok, if you answer all correct, the]@
      //next level button will appear after you
      //answer this one.
      //Otherwise, you will be asked to do      
      //more questions and maybe also to redo
      //some of the past questions.
      +%/--%--
      """,Comment)
    .question("""
      @[//@@Why the question before was a]@
      //comment and not error?
      //because the text around the marked character
      //was in a well formed comment.
      //Error outside the smallest valid syntactical
      //unit do not impact the answer
      +%/--%--
      """,Comment)
    .question("""
      //Ok, back to some questions.                        
      //This is a method call!
      @[East@@.turn]@
      """, MethodCall)
    .build(); } }