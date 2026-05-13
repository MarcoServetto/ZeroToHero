package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, "Direction:", "{ .turn: Direction; }")
      .addNode(34, 20)
      .addNode(26, 56)
      .addFinishNode(82, 60)
      .connect(0, 1, "{ .turn: ", 34, 40, 28, 7)
      .connect(1, 2, "Direction; }", 30, 62)
      .build();
    }
  }