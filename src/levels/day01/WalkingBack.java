package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import main.Days;

public class WalkingBack implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,25)
    // selected, start, end, option
    .question("@[th@@is]@.turn.turn",Parameter)
    .question("@[this.@@turn]@.turn",MethodCall)
    .question("@[this.turn.t@@urn]@",MethodCall)
    .question("""
      Direction:{ 
        .turn: Direction;
        @[.reverse: Direction @@-> this.turn.turn;]@
        }
      //remember, if there is the ->
      //then it is not a 'method call'
      """,MethodDeclaration)
    .question("""
        Direction:{ 
          .turn: Direction;
          .reverse: Direction -> @[th@@is]@.turn.turn;
          }
        //Writing code on multiple lines in this way
        //is called indentation. Indentation makes
        //code more readable.
        """,Parameter)
    .question("@[No@@rth:{East}]@",TypeDeclaration)
    .question("North:{@[Ea@@st]@}",ObjectLiteral)
    .question("North:{.turn->@[Ea@@st]@}",ObjectLiteral)
    .question("@[.north{East@@}]@",Error)
    .question("""
      //Single line comments start with two
      @[//slash (/) and en@@d with the end of line]@ 
      """,Comment)
    .question("""
      /*Multi line comments start with slash star
      and end with star slash*/@[Ea@@st]@
      //Code can be around them! 
      """,ObjectLiteral)
    .question("""
      /*Any text inside comments is ok*/
      But outside @[@@]@ it is not!
      """,Error)
    .build(); } }