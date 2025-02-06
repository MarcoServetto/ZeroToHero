package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import main.Days;

public class Walking1 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,33)
    // selected, start, end, option
    .question("""
      @[/*
      This is the walking minigame
      Select all the code and press 'comment'.
      This text -is- the code!
      Indeed, this code is a multiline comment.
      Text useful for explanation but
      ignored in execution@@.
      */]@""",Comment)
    .question("""
      @[//This is a @@single line comment]@
      //Select all and only the line with
      //the highlighted character.
      //The highlighted character is
      //the currently selected character.
      """,Comment)
    .question("""
      @[No@@rth]@
      //In this kind of puzzle, we select the
      //smallest self contained unit of code
      //around the highlighted/selected character. 
      //This time it is an object literal. 
      """,ObjectLiteral)
    .question("@[+%/-@@-%--]@",Error)
    .question("""
      //Text +%/--%-- outside of comments
      //is an error. When there is an error,
      //it does not matter what you select.
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
      //because the text around the highlighted character
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
    .question("""
        @[North.@@turn]@
        """, MethodCall)
    .question("""
        @[South t@@urn]@
        //Note the missing dot!
        """, Error)
    .question("""
        @[/*This is a multiline comment.
        This is the last question of this batch,
        after, they will just repeat.
        If you have completed the level, you should
        see the next level button on the right@@!
        */]@
        """, Comment)
    .build(); } }