package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
	return new htmlMangle.Forest("North:Direction", "North:Direction{}")
	  .addNode(0, 0)
	  .addNode(20, 20)
	  .addNode(40, 40)
	  .addNode(50, 20)
	  .addNode(90, 20)
	  .addNode(100, 30)
	  .addNode(50, 50)
	  .addNode(100, 100)
	  .connect(0, 0, 20, 20, "{}")
	  .connect(0, 0, 20, 20, "Direction")
	  .connect(0, 0, 20, 20, ":")
	  .connect(20, 20, 50, 20, "North:Direction{}")
	  .connect(50, 20, 90, 20, "North:Direction{}")
	  .connect(50, 20, 90, 20, "East:Direction{}")
	  .build();
    }
  }