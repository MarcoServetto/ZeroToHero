package levels;
import static htmlMangle.Walking.Option.*;

public class Level3 implements Level{
  public String fileName(){ return "Level3"; }
  public String of(){
    return new htmlMangle.Walking(fileName(),25,5)
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
    .question("@[So@@uth]@.turn", ObjectLiteral)
    .question("@[No@@rth:]@", Error)
    .question("Direction:{.turn:@[Dir@@ection]@,}", Type)
    .question("North:Direction{@[.turn@@->East,]@}", MethodDeclaration)
    .question("North:@[Dire@@ction]@{.turn->East,}", Type)
    .question("South:@[Dire@@ction]@{.turn->West,}", Type)
    .question("@[West:Direction{.t@@urn-->North,}]@", Error)
    .question("West:Direction{@[.t@@urn->North]@}", MethodDeclaration)
    .build(); } }