package levels.day04;

import java.util.function.Function;

import main.Days;
import htmlMangle.Forest;

public class ForestIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new Forest(name, """
//This is the forest minigame.
//Click on the path to walk and
//collect the code on the path.
//Reach the end (green) with
//complete correct code
/*
Panic: It's quite easy to get
lost here. Make sure to 
remember your directions!
*/

Direction:""", "{ .turn: Direction; }").background(Forest.Background.DAWN)
      .addNode(34, 20)
      .addNode(26, 56)
      .addFinishNode(82, 60)
      .connect(0, 1, "{ .turn: ", 34, 40, 15)
      .connect(1, 2, "Direction; }", 30, 62, 20)
      .build();
    }
  }