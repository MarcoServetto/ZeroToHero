package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingBack implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,25)
    // selected, start, end, option
    .questionMethodBody("@[th@@is]@.turn.turn",Parameter)
    .questionMethodBody("@[this.@@turn]@.turn",MethodCall)
    .questionMethodBody("@[this.turn.t@@urn]@",MethodCall)
    .questionTopLevel("""
      Direction:{
        .turn: Direction;
        @[.reverse: Direction @@-> this.turn.turn;]@
        }
      //remember, if there is the ->
      //then it is not a 'method call'
      """,MethodDeclaration)
    .questionTopLevel("""
        Direction:{
          .turn: Direction;
          .reverse: Direction -> @[th@@is]@.turn.turn;
          }
        //Writing code on multiple lines in this way
        //is called indentation. Indentation makes
        //code more readable.
        """,Parameter)
    .questionTopLevel("@[No@@rth:{East}]@",TypeDeclaration)
    .questionTopLevel("North:{@[Ea@@st]@}",ObjectLiteral)
    .questionTopLevel("North:{.turn->@[Ea@@st]@}",ObjectLiteral)
    .errorTopLevel("@[.north{East@@}]@",
      "Top level code starts with Uppercase!")
    .questionTopLevel("""
      //Single line comments start with two
      @[//slash (/) and en@@d with the end of line]@
      """,Comment)
    .questionMethodBody("""
      /*Multi line comments start with slash star
      and end with star slash*/@[Ea@@st]@
      //Code can be around them!
      """,ObjectLiteral)
    .errorTopLevel("""
      /*Any text inside comments is ok*/
      But outside @[@@]@ it is not!
      """,
      "Plain English need to be inside comments")
    .build(); } }
