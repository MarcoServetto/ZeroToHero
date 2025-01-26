package levels;
import static htmlMangle.Walking.Option.*;

public class Level8 implements Level{
  public String fileName(){ return "Level8"; }
  public String of(){
    return new htmlMangle.Walking(fileName(),25,9)
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