package levels.day03;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import main.Days;

public class WalkingExplainClimb implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name,23)
    // selected, start, end, option
    .question("""
      @[Panic shook me awake, his voice urgent.
      "Wake up! We're going to climb the hill 
      nearby to collect mushrooms. The rain last
      night should have triggered a good harvest!"
      
      I blinked, still groggy, but the excitement
      in his tone was contagious. The thought of
      fresh mushrooms, sprouting in the damp earth
      after the night's downpour,
      was enough to get me moving.@@.
      ]@""",Error)
    .question("""
        @[//Let me repeat: We need to @@CLIMB the hill nearby]@
        //Climbing is tricky, I hope you are ready!
        ]@""",Comment)
    .question("""
      As we have seen, operations move forward into results.
      For example `@[North@@.turn]@.turn` becomes `East.turn`
      that becomes `South`.      
      """,MethodCall)
    .question("""
      @[/Climbing is all about recognizing those natural
      /transitions etched in the shapes of the rocks.
      /At any moment, grab onto the rock that represents
      /a small step @@forward.]@
      """,Error)
    .question("""
      //We are near there, just a few more steps toward
      @[No@@rth]@
      """,ObjectLiteral)
    .question("""
        @[/ * Getting there@@! * /]@      
        """,Error)
    .question("""
      @[//@@/Still here? how many times did]@
      ///you stumble today?
      ///are you still sea sick?
      """,Comment)
    .question("""
      // No worry, back to walking,                        
            @[// We are near @@there!]@
      """, Comment)
    .question("""
      //Here it is the
      @[Hill@@:{}]@
      """, TypeDeclaration)
    .question("""
  Archer: {
    .heading: Direction -> heading,
    .aiming:  Direction -> aiming,
    .aimTo(d: Direction):Archer -> Archers#(heading, @[@@d]@),
    .headTo(d: Direction):Archer -> Archers#(d, aiming),
    }
""", Parameter)
    .question("""
  Archer: {
    .heading: Direction -> heading,
    .aiming:  Direction -> aiming,
    .aimTo(d: Direction):Archer -> Archers#(heading, d),
    @[.headTo(@@d: Direction):Archer -> Archers#(d, aiming),]@
    }
""", MethodDeclaration)
    .question("""
  Archer: {
    .heading: Direction -> heading,
    .aiming:  Direction -> aiming,
    .aimTo(d: Direction):Archer -> Archers#(heading, d),
    .headTo(d: @[D@@irection]@):Archer -> Archers#(d, aiming),
    }
""", Type)
    .build(); } }
