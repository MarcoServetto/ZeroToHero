package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class Walking2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,33)
    // selected, start, end, option
    .question("""
      //Going forward the text will help
      //you less and less.
      //Learn from your mistakes and experiment!
      @[Ea@@st]@
      """,ObjectLiteral,"method body")
    .question("""
      Direction:{}
      @[North@@:Direction{}]@
      South:Direction{}
      //What is this? we mentioned in
      //the story before!
      """,TypeDeclaration,"top level code")
    .question("""
      Direction:{}
      North:Direction{}
      @[South@@:Direction{}]@
      //Can you solve it now?
      """,TypeDeclaration,"top level code")
    .question("@[South@@.turn]@", MethodCall,"method body")
    .question("@[So@@uth]@.turn\n//Remember, you need to select the smallest\n//syntactical unit around the highlighted character", ObjectLiteral,"method body")
    .question("@[Ea@@st]@.turn", ObjectLiteral,"method body")
    .error("@[North@@:]@",
      "`:` must be followed by someting","top level code")
    .question("Direction:{.turn:@[Dir@@ection]@;}\n"
      +"//Direction here is in a 'role' that\n//is not object literal", Type,"top level code")
    .question("North:Direction{@[.turn@@->East;]@}\n//The arrow (->) means 'method body is here'", MethodDeclaration,"top level code")
    .question("North:@[Dire@@ction]@{.turn->East;}\n//If you are stuck, make a mistake and see the solution!", Type,"top level code")
    .question("South:@[Dire@@ction]@{.turn->West;}\n//Learn by trial and error!", Type,"top level code")
    .error("@[West:Direction{.t@@urn-->North;}]@",
      "methods use '->', not '-->'","top level code")
    .question("West:Direction{@[.t@@urn->North]@}", MethodDeclaration,"top level code")
    .question("West:Direction{.turnTwice->@[West.turn@@.turn]@}", MethodCall,"top level code")
    .question("West:Direction{.turnTwice->@[West@@.turn]@.turn}", MethodCall,"top level code")
    .question("West:Direction{@[.turnTwi@@ce->West.turn.turn]@}", MethodDeclaration,"top level code")
    .question("North: Direction{ .turnTwice->@[North.turn@@.turn]@; }", MethodCall,"top level code")
    .question("North:Direction{ .turnTwice -> @[North@@.turn]@.turn }", MethodCall,"top level code")
    .question("North:Direction { @[.turnTwi@@ce -> North.turn.turn;]@ }", MethodDeclaration,"top level code")
    .question("@[South@@.turn]@.turn.turn", MethodCall,"method body")
    .question("@[South.turn@@.turn]@.turn", MethodCall,"method body")
    .question("@[South.turn.turn@@.turn]@", MethodCall,"method body")
    .build(); } }
