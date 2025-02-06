package levels.day01;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import main.Days;

public class Walking2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,33)
    // selected, start, end, option
    .question("""
      //Going forward the text will help
      //you less and less.
      //Learn from your mistakes and experiment!
      @[Ea@@st]@
      """,ObjectLiteral)
    .question("""
      Direction:{}
      @[North@@:Direction{}]@
      South:Direction{}
      //What is this? we mentioned in
      //the story before!
      """,TypeDeclaration)
    .question("""
      Direction:{}
      North:Direction{}
      @[South@@:Direction{}]@
      //Can you solve it now?
      """,TypeDeclaration)    
    .question("@[South@@.turn]@", MethodCall)
    .question("@[So@@uth]@.turn\n//Remember, you need to select the smallest\n//syntactical unit around the highlighted character", ObjectLiteral)
    .question("@[Ea@@st]@.turn", ObjectLiteral)
    .question("@[North@@:]@", Error)
    .question("Direction:{.turn:@[Dir@@ection]@,}", Type)
    .question("North:Direction{@[.turn@@->East,]@}\n//The arrow (->) means 'method body is here'", MethodDeclaration)
    .question("North:@[Dire@@ction]@{.turn->East,}\n//If you are stuck, make a mistake and read the error!", Type)
    .question("South:@[Dire@@ction]@{.turn->West,}\n//Learn by trial and error!", Type)
    .question("@[West:Direction{.t@@urn-->North,}]@", Error)
    .question("West:Direction{@[.t@@urn->North]@}", MethodDeclaration)
    .question("West:Direction{.turnTwice->@[West.turn@@.turn]@}", MethodCall)
    .question("West:Direction{.turnTwice->@[West@@.turn]@.turn}", MethodCall)
    .question("West:Direction{@[.turnTwi@@ce->West.turn.turn]@}", MethodDeclaration)
    .question("North: Direction{ .turnTwice->@[North.turn@@.turn,]@ }", MethodCall)
    .question("North:Direction{ .turnTwice -> @[North@@.turn]@.turn }", MethodCall)
    .question("North:Direction { @[.turnTwi@@ce -> North.turn.turn,]@ }", MethodDeclaration)
    .build(); } }