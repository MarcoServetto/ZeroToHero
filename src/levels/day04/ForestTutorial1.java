package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestTutorial1 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//Go on the same path again to collect
//the same code multiple times
""", "Direction:{ .turn: Direction; }")
      .addNode(15, 15)
      .addNode(55, 15)
      .addFinishNode(55, 55)
      .connect(0, 1, "Direction",  22,  4, 30, 7)
      .connect(1, 0, ":{ .turn: ", 22, 20, 28, 7)
      .connect(1, 2, "; }", 40, 42)
      .build();
    }
  }
