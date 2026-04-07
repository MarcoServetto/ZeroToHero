package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
	return new htmlMangle.Forest("Hello")
	  .addNode(50, 50, "North: Direction {}")
	  .addNode(75, 30, "South: Direction {}")
	  .addUndirected(50, 50, 75, 30)
	  .build();
    }
  }