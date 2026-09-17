package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class Walking2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,33)
    // selected, start, end, option
    .questionMethodBody("""
      //Going forward the text will help
      //you less and less.
      //Learn from your mistakes and experiment!
      @[Ea@@st]@
      """,ObjectLiteral)
    .questionTopLevel("""
      Direction:{}
      @[North@@:Direction{}]@
      South:Direction{}
      //What is this? we mentioned in
      //the story before!
      """,TypeDeclaration)
    .questionTopLevel("""
      Direction:{}
      North:Direction{}
      @[South@@:Direction{}]@
      //Can you solve it now?
      """,TypeDeclaration)
    .questionMethodBody("@[South@@.turn]@", MethodCall)
    .questionMethodBody("@[So@@uth]@.turn\n//Remember, you need to select the smallest\n//syntactical unit around the highlighted character", ObjectLiteral)
    .questionMethodBody("@[Ea@@st]@.turn", ObjectLiteral)
    .errorTopLevel("@[North@@:]@",
      "`:` must be followed by someting")
    .questionTopLevel("Direction:{.turn:@[Dir@@ection]@;}\n"
      +"//Direction here is in a 'role' that\n//is not object literal", Type)
    .questionTopLevel("North:Direction{@[.turn@@->East;]@}\n//The arrow (->) means 'method body is here'", MethodDeclaration)
    .questionTopLevel("North:@[Dire@@ction]@{.turn->East;}\n//If you are stuck, make a mistake and see the solution!", Type)
    .questionTopLevel("South:@[Dire@@ction]@{.turn->West;}\n//Learn by trial and error!", Type)
    .errorTopLevel("@[West:Direction{.t@@urn-->North;}]@",
      "methods use '->', not '-->'")
    .questionTopLevel("West:Direction{@[.t@@urn->North]@}", MethodDeclaration)
    .questionTopLevel("West:Direction{.turnTwice->@[West.turn@@.turn]@}", MethodCall)
    .questionTopLevel("West:Direction{.turnTwice->@[West@@.turn]@.turn}", MethodCall)
    .questionTopLevel("West:Direction{@[.turnTwi@@ce->West.turn.turn]@}", MethodDeclaration)
    .questionTopLevel("North: Direction{ .turnTwice->@[North.turn@@.turn]@; }", MethodCall)
    .questionTopLevel("North:Direction{ .turnTwice -> @[North@@.turn]@.turn }", MethodCall)
    .questionTopLevel("North:Direction { @[.turnTwi@@ce -> North.turn.turn;]@ }", MethodDeclaration)
    .questionMethodBody("@[South@@.turn]@.turn.turn", MethodCall)
    .questionMethodBody("@[South.turn@@.turn]@.turn", MethodCall)
    .questionMethodBody("@[South.turn.turn@@.turn]@", MethodCall)
    .build(); } }
