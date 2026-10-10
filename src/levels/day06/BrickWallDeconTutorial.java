package levels.day06;

import static htmlMangle.BrickWallDeconstruction.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWallDeconstruction;
import mainZeroToHero.Days;

public class BrickWallDeconTutorial implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    BrickWallDeconstruction bw= new BrickWallDeconstruction(name)
      .background(Prairie, 1)
      .addImmovable(0, "Direction:{.turn:Direction")
      .addReplaceable(0, ";", "")
      .addImmovable(0, "}")
      .newRow()
      .addImmovable(0, "North:Direction{East}")
      .newRow()
      .addImmovable(0, "South:Direction{")
      .addReplaceable(0, ".turn:Direction->","")
      .addImmovable(0, "West}")
      .newRow()
      .addImmovable(0, "West:Direction{North}")
      .newRow()
      .addImmovable(0, "East:Direction{South")
      .addReplaceable(0, ";", "")
      .addImmovable(0, "}")
      .addToPile(0, true, "3")
      .addToPile(0, true, "*")
      .addToPile(5, true, "length");
    return bw.build();
    }
  }