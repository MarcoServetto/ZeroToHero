package levels.day03;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class ClimbBase implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,directions)
    .question("""
      This is the climbing minigame.
      Highlight/Select all the code below.
      Then click on the first rock on the right of this text
                                                                             click      |➡️
                                                                             on the     |➡️
          It works because North.turn reduces to East in one step            rock       |➡️      
      """,
      "@[North.turn]@",
      List.of("East","North",".turn","Direction"),
      0)
    .question("""
      Here Highlight/Select only the first operation.
      Select the rock with the correct result
      """,
      "@[East.turn]@.turn",
      List.of("Direction","South","North","Panic","<completed>"),
      1)
    .question("""
      Great, now select the next reduction step!
      """,
      "##@[South.turn]@",
      List.of("Direction","South","North","West","<completed>"),
      3)
    .question("""
      This is it. We have reduced `East.turn.turn`
      Now just select <completed>
      """,
      "##@[West]@",
      List.of("Direction","South","North","West","<completed>"),
      4)
    .build(); }
  public static final String directions= """
//Below here is all the relevant code. Scroll down to see it all!
Direction: {
  .turn: Direction;
  .reverse: Direction -> this.turn.turn;
  }
North: Direction { East  }
East:  Direction { South }
South: Direction { West  }
West:  Direction { North }
Archers: { #(heading: Direction, aiming:  Direction): Archer ->
  Archer: {
    .heading: Direction -> heading;
    .aiming:  Direction -> aiming;
    }
  }
""";
  
  public static final String directionsAimTo= """
//Now with two more methods!
Direction: {
  .turn: Direction;
  .reverse: Direction -> this.turn.turn;
  }
North: Direction { East  }
East:  Direction { South }
South: Direction { West  }
West:  Direction { North }
Archers: { #(heading: Direction, aiming:  Direction): Archer ->
  Archer: {
    .heading: Direction -> heading;
    .aiming:  Direction -> aiming;
    .aimTo(d:  Direction):  Archer -> Archers#(heading, d);
    .headTo(d: Direction): Archer -> Archers#(d, aiming);
    }
  }
""";
}