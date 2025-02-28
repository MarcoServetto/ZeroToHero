package levels.day03;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import main.Days;

public class WalkingBoomerangParam implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,20)
    // selected, start, end, option
    .question("""
      @[Boomerang@@:{ .throw(dir: Direction): Direction -> dir.reverse }]@
      """,TypeDeclaration)
    .question("""
      Boomerang:{ @[.throw(dir: Direction): Direction @@-> dir.reverse]@ }
      """,MethodDeclaration)
    .question("""
      Boomerang:{ .throw(dir: Direction): Direction -> @[dir.r@@everse]@ }
      """,MethodCall)
    .question("""
      Boomerang:{ .throw(dir: Direction): Direction -> @[d@@ir]@.reverse }
      """,Parameter)
    .question("""
      @[Boomerang:{ .throw(dir: Direction): Direction -> dir.reverse @@}]@
      """,TypeDeclaration)
    .question("""
      Boomerang:{ @[.throw(dir: Direction)@@: Direction -> dir.reverse]@ }
      """,MethodDeclaration)
    .question("""
      Boomerang:{ .throw(dir: Direction): Direction -> @[dir@@.reverse]@ }
      """,MethodCall)
    .question("""
      Boomerang:{ .throw(dir: Direction): Direction -> @[@@dir]@.reverse }
      """,Parameter)
    .question("""
      @[Boom@@erang:{ .throw(dir: Direction): Direction -> dir.reverse }]@
      """,TypeDeclaration)
    .question("""
      Boomerang:{ .throw(dir: @[Dir@@ection]@): Direction -> dir.reverse }
      """,Type)
    .question("""
      Boomerang:{ .throw(dir: Direction): @[Dir@@ection]@ -> dir.reverse }
      """,Type)
    .question("""
      Boomerang:{ @[.throw(d@@ir: Direction): Direction -> dir.reverse]@ }
      """,MethodDeclaration)
    .build(); } }