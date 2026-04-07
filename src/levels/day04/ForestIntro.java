package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
	return new htmlMangle.Forest("Hello")
	  .addNode(25, 60)
	  .addNode(75, 30)
	  .addDirected(25, 60, 75, 30, "North:Direction{}")
	  .addDirected(25, 60, 75, 30, "South:Direction{}")
	  .addDirected(25, 60, 75, 30, "East:Direction{}")
	  .addDirected(25, 60, 75, 30, "West:Direction{}")
	  .addDirected(25, 60, 75, 30, "Direction{}")
	  //.addDirected(75, 30, 25, 60, "Direction{}")
	  .build();
    }
  }