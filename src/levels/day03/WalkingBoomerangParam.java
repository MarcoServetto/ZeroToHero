package levels.day03;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingBoomerangParam implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,20)
    // selected, start, end, option
    .questionTopLevel("""
      @[Boomerang@@: {
        .throw(dir: Direction): Direction -> dir.reverse
        }]@
      """,TypeDeclaration)
    .questionTopLevel("""
      Boomerang: {
        @[.throw(dir: Direction): Direction @@-> dir.reverse]@
        }
      """,MethodDeclaration)
    .questionTopLevel("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[dir.r@@everse]@
        }
      """,MethodCall)
    .questionTopLevel("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[d@@ir]@.reverse
        }
      """,Parameter)
    .questionTopLevel("""
      @[Boomerang: {
        .throw(dir: Direction): Direction -> dir.reverse
        @@}]@
      """,TypeDeclaration)
    .questionTopLevel("""
      Boomerang: {
        @[.throw(dir: Direction)@@: Direction -> dir.reverse]@
        }
      """,MethodDeclaration)
    .questionTopLevel("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[dir@@.reverse]@
        }
      """,MethodCall)
    .questionTopLevel("""
      Boomerang: {
        .throw(dir: Direction): Direction -> @[@@dir]@.reverse
        }
      """,Parameter)
    .questionTopLevel("""
      @[Boom@@erang: {
        .throw(dir: Direction): Direction -> dir.reverse
        }]@
      """,TypeDeclaration)
    .questionTopLevel("""
      Boomerang: {
        .throw(dir: @[Dir@@ection]@): Direction -> dir.reverse
        }
      """,Type)
    .questionTopLevel("""
      Boomerang: {
        .throw(dir: Direction): @[Dir@@ection]@ -> dir.reverse
        }
      """,Type)
    .questionTopLevel("""
      Boomerang: {
        @[.throw(d@@ir: Direction): Direction -> dir.reverse]@
        }
      """,MethodDeclaration)
    .build(); } }