package levels.day01;

import java.util.function.Function;

import main.Days;

public class ExplainReverse implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name)
    // topStart/end   leftStart/end
    .image(6)
      .area(30, 95,     3, 48, """
  Direction: {
    .turn: Direction,
    .reverse: Direction-> /*[*/this.turn.turn/*]*/,
    }
  North: Direction { East,  }
  East:  Direction { South, }
  South: Direction { West,  }
  West:  Direction { North, }
  """)
    .image(5)
    .build(); } }