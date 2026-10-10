package levels.day06;

import static htmlMangle.BrickWallDeconstruction.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWallDeconstruction;
import mainZeroToHero.Days;

public class BrickWallDeconTutorial2 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWallDeconstruction(name)
      .background(Tutorial, 2)
      .addMovable(0, "Direction:")
      .addImmovable(0, "{.turn:Direction}")
      .newRow()
      .addImmovable(0, "North:Direction{")
      .addReplaceable(0, ".turn:Direction->East","East")
      .addMovable(0, "}")
      .newRow()
      .addImmovable(0, "South:Direction{")
      .addReplaceable(0, ".turn:Direction->","")
      .addImmovable(0, "West}")
      .newRow()
      .addImmovable(0, "West:Direction{North}")
      .newRow()
      .addImmovable(0, "East:Direction{South}")
      .addToPile(0, true, "Ea")
      .addToPile(5, true, "st")
      .newRow()
      .addToPile(0, false, "//the two bricks above can be pushed into the wall")
      .build();
    }
  }