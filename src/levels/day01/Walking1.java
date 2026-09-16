package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class Walking1 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,33)
    // selected, start, end, option
    .question("""
      // You need to do two actions:
      //(1) Select the correct text
      //(2) press the correct button

      @[//Select all and only this lin@@e]@

      //Press the button 'comment'
      """,Comment,true)
    .question("""
      @[/*
      This is the walking minigame
      Select all the code and press 'comment'.
      This text -is- the code; including / and *.
      Indeed, this code is a multiline comment.
      Text useful for explanation but
      ignored in execution@@.
      */]@""",Comment)
    .question("""
      @[//This is a @@single line comment]@
      //Select all and only the line with
      //the highlighted character and press 'comment'.
      //The highlighted character is
      //the currently selected character.
      //Valid selections must include the highlighted character.
      """,Comment)
    .question("""
      @[No@@rth]@
      //In this kind of puzzle, we select the
      //smallest self contained unit of code
      //around the highlighted/selected character.
      //This time it is an object literal.
      """,ObjectLiteral)
    .error("@[+%/-@@-%--]@",
      "the symbol run +%/--%-- is not a comment, a parameter, a method call, "
      +"an object literal, a method declaration, a type declaration, or a type: "
      +"the grammar has no rule for it")
    .error("""
      //Text +%/--%-- outside of comments
      //is an error. When there is an error,
      //it does not matter what you select.
      @[Just press @@error :-)]@
      """,
      "the line 'Just press error :-)' is plain English text, not preceded "
      +"by // and not inside a /* */ block, so it is code the parser must "
      +"reject")
    .question("""
      @[//@@Ok, if you answered all correct, the]@
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
    .error("""
        @[South t@@urn]@
        //Note the missing dot!
        """,
      "'South turn' juxtaposes two identifiers separated only by whitespace: "
      +"a method call needs a dot between the receiver and the method name, "
      +"as in 'South.turn'")
    .error("""
      @[Mistakes are not just welcome,
      Mistakes are necessary.
      Do mistakes on purpose in
      order to experiment@@.]@
      """,
      "this is an English sentence with commas and a period, not preceded "
      +"by // and not inside a /* */ block, so the parser sees it as code "
      +"it cannot recognize")
    .question("""
        @[/*This is a multiline comment.
        This is the last question of this batch,
        after, they will just repeat.
        If you have completed the level, you should
        see the next level button on the right@@!
        */]@
        """, Comment)
    .build(); } }