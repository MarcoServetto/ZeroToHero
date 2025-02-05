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
    .question("@[North{Ea@@st}]@",Error)
    .build(); } }