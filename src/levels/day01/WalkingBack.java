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
        .turn: Direction,
        @[.reverse: Direction @@-> this.turn.turn,]@
        }
      """,MethodDeclaration)
    .question("""
        Direction:{ 
          .turn: Direction,
          .reverse: Direction -> @[th@@is]@.turn.turn,
          }
        """,Parameter)
    .question("@[No@@rth:{East}]@",TypeDeclaration)
    .question("North:{@[Ea@@st]@}",ObjectLiteral)
    .question("North:{.turn->@[Ea@@st]@}",ObjectLiteral)
    .question("@[North{East@@}]@",Error)
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