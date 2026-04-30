package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
	return new htmlMangle.Forest("Direction:", "Direction:{ .turn: Direction; }")
	  .addNode(34, 20)
	  .addNode(26, 56)
	  .addNode(82, 60)
	  .connect(34, 20, 26, 56, "{ .turn: ")
	  .connect(26, 56, 82, 60, "Direction; }")
	  .build();
    }
  }