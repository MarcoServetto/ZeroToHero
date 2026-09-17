package levels.day03;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingBoomerangParam implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,20)
    // selected, start, end, option
    .question("""
      @[Boomerang@@: {
        .throw(dir: Direction): Direction -> dir.reverse
        }]@
      """,TypeDeclaration,"top level code")
    .question("""
      Boomerang: {
        @[.throw(dir: Direction): Direction @@-> dir.reverse]@
        }
      """,MethodDeclaration,"top level code")
    .question("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[dir.r@@everse]@
        }
      """,MethodCall,"top level code")
    .question("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[d@@ir]@.reverse
        }
      """,Parameter,"top level code")
    .question("""
      @[Boomerang: {
        .throw(dir: Direction): Direction -> dir.reverse
        @@}]@
      """,TypeDeclaration,"top level code")
    .question("""
      Boomerang: {
        @[.throw(dir: Direction)@@: Direction -> dir.reverse]@
        }
      """,MethodDeclaration,"top level code")
    .question("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[dir@@.reverse]@
        }
      """,MethodCall,"top level code")
    .question("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[@@dir]@.reverse
        }
      """,Parameter,"top level code")
    .question("""
      @[Boom@@erang: {
        .throw(dir: Direction): Direction -> dir.reverse
        }]@
      """,TypeDeclaration,"top level code")
    .question("""
      Boomerang: {
        .throw(dir: @[Dir@@ection]@): Direction -> dir.reverse
        }
      """,Type,"top level code")
    .question("""
      Boomerang: {
        .throw(dir: Direction): @[Dir@@ection]@ -> dir.reverse
        }
      """,Type,"top level code")
    .question("""
      Boomerang: {
        @[.throw(d@@ir: Direction): Direction -> dir.reverse]@
        }
      """,MethodDeclaration,"top level code")
    .build(); } }