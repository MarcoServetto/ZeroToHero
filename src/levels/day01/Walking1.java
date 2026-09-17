package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class Walking1 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,33)
    // selected, start, end, option
    .questionTopLevel("""
      // You need to do two actions:
      //(1) Select the correct text
      //(2) press the correct button

      @[//Select all and only this lin@@e]@

      //Press the button 'comment'
      """,Comment,true)
    .questionTopLevel("""
      @[/*
      This is the walking minigame
      Select all the code and press 'comment'.
      This text -is- the code; including / and *.
      Indeed, this code is a multiline comment.
      Text useful for explanation but
      ignored in execution@@.
      */]@""",Comment)
    .questionTopLevel("""
      @[//This is a @@single line comment]@
      //Select all and only the line with
      //the highlighted character and press 'comment'.
      //The highlighted character is
      //the currently selected character.
      //Valid selections must include the highlighted character.
      """,Comment)
    .questionMethodBody("""
      @[No@@rth]@
      //In this kind of puzzle, we select the
      //smallest self contained unit of code
      //around the highlighted/selected character.
      //This time it is an object literal.
      """,ObjectLiteral)
    .errorTopLevel("@[+%/-@@-%--]@",
      "Text +%/--%-- is not valid code, just gibberish")
    .errorTopLevel("""
      //Text +%/--%-- outside of comments
      //is an error. When there is an error,
      //it does not matter what you select.
      @[Just press @@error :-)]@
      """,
      "the text `Just press error :-)` is English, Not code.\n English need to go in comments")
    .questionTopLevel("""
      @[//@@Ok, if you answered all correct, the]@
      //next level button will appear after you
      //answer this one.
      //Otherwise, you will be asked to do
      //more questions and maybe also to redo
      //some of the past questions.
      //+%/--%--
      """,Comment)
    .questionTopLevel("""
      @[//@@Why the question before was a]@
      //comment and not error?
      //because the creazy +%/--%-- text was in a comment.
      """,Comment)
    .questionMethodBody("""
      //Ok, back to some questions.
      //This is a method call!
      @[East@@.turn]@
      """, MethodCall)
    .questionMethodBody("""
        @[North.@@turn]@
        """, MethodCall)
    .errorMethodBody("""
        @[South t@@urn]@
        //Note the missing dot!
        """,
      "The method name is `.turn`, not `turn`")
    .errorTopLevel("""
      @[Mistakes are not just welcome,
      Mistakes are necessary.
      Do mistakes on purpose in
      order to experiment@@.]@
      """,
      "this is plain English not in a comment")
    .questionTopLevel("""
        @[/*This is a multiline comment.
        This is the last question of this batch,
        after, they will just repeat.
        If you have completed the level, you should
        see the next level button on the right@@!
        */]@
        """, Comment)
    .build(); } }
