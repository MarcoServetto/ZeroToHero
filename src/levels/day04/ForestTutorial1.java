package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestTutorial1 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//Go on the same path again to collect
//the same code multiple times
""", "Direction:{ .turn: Direction; }")
      .addNode(10, 10)
      .addNode(50, 10)
      .addFinishNode(50, 50)
      .connect(0, 1, "Direction", 55, 20, 30, 7)
      .connect(1, 0, ":{ .turn: ", 5, 20, 28, 7)
      .connect(1, 2, "; }", 30, 62)
      .build();
    }
  }
