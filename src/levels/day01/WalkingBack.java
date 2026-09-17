package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingBack implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,25)
    // selected, start, end, option
    .question("@[th@@is]@.turn.turn",Parameter,"method body")
    .question("@[this.@@turn]@.turn",MethodCall,"method body")
    .question("@[this.turn.t@@urn]@",MethodCall,"method body")
    .question("""
      Direction:{
        .turn: Direction;
        @[.reverse: Direction @@-> this.turn.turn;]@
        }
      //remember, if there is the ->
      //then it is not a 'method call'
      """,MethodDeclaration,"top level code")
    .question("""
        Direction:{
          .turn: Direction;
          .reverse: Direction -> @[th@@is]@.turn.turn;
          }
        //Writing code on multiple lines in this way
        //is called indentation. Indentation makes
        //code more readable.
        """,Parameter,"top level code")
    .question("@[No@@rth:{East}]@",TypeDeclaration,"top level code")
    .question("North:{@[Ea@@st]@}",ObjectLiteral,"top level code")
    .question("North:{.turn->@[Ea@@st]@}",ObjectLiteral,"top level code")
    .error("@[.north{East@@}]@",
      "Top level code starts with Uppercase!","top level code")
    .question("""
      //Single line comments start with two
      @[//slash (/) and en@@d with the end of line]@
      """,Comment,"comment")
    .question("""
      /*Multi line comments start with slash star
      and end with star slash*/@[Ea@@st]@
      //Code can be around them!
      """,ObjectLiteral,"method body")
    .error("""
      /*Any text inside comments is ok*/
      But outside @[@@]@ it is not!
      """,
      "Plain English need to be inside comments","not code")
    .build(); } }
