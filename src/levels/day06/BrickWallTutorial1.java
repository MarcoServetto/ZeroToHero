package levels.day06;

import static htmlMangle.BrickWall.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWall;
import mainZeroToHero.Days;

public class BrickWallTutorial1 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name, """
Hour: { .succ: Hour }""")
      .background(Tutorial, 1)
      .addImmovable(2, "Hour:")
      .addImmovable(9, "Hour")
      .addToPile(0, true, "}")
      .addToPile(3, true, "Hour")
      .addToPile(5, true, "Direction")
      .addToPile(10, true, ".succ: ")
      .addToPile(14, true, "{")
      .newRow()
      .addToPile(0, false, "//Drag the bricks above here into the wall.")
      .newRow()
      .addToPile(0, false, "//The dial needle shows how much code is missing")
      .newRow()
      .addToPile(0, false, "//by weighing the wall.")
      .newRow()
      .addToPile(0, false, "//When the needle is in the green, press 🎉")
      .build();
    }
  }