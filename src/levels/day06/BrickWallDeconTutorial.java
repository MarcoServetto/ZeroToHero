package levels.day06;

import static htmlMangle.BrickWallDeconstruction.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWallDeconstruction;
import mainZeroToHero.Days;

public final class BrickWallDeconTutorial implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name) {
    return new BrickWallDeconstruction(name)
      .background(Tutorial, 1)
      .addImmovable(0, "Direction:{.turn:Direction")
      .addReplaceable(0, ";", "")
      .addImmovable(0, "}")
      .newRow()
      .addImmovable(0, "North:Direction{East}")
      .newRow()
      .addImmovable(0, "South:Direction{")
      .addReplaceable(0, ".turn:Direction->", "")
      .addImmovable(0, "West}")
      .newRow()
      .addImmovable(0, "West:Direction{North}")
      .newRow()
      .addImmovable(0, "East:Direction{South")
      .addReplaceable(0, ";", "")
      .addImmovable(0, "}")
      .addToPile(0, false, "//This is the space for the code we collect")
      .newRow()
      .addToPile(0, false, "//drag and drop redundant code on top of this text")
      .build();
    }
  }