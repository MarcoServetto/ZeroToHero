package levels.day06;

import static htmlMangle.BrickWall.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWall;
import mainZeroToHero.Days;

public class BrickWallTutorial2 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name, """
I: Hour{ II }
II: Hour{ III }
III: Hour{ IV }""")
      .background(Tutorial, 2)
      .addImmovable(2, "I: Hour{")
      .addImmovable(6, "}")
      .addToPile(0, true, "I")
      .addToPile(3, true, "ur")
      .addToPile(5, true, "I")
      .addToPile(4, true, "IV")
      .addToPile(5, true, "Ho")
      .addToPile(5, true, "I")
      .newRow()
      .addImmovable(2, "II:")
      .addMovable(3, "V")
      .addImmovable(3, "{ III }")
      .addToPile(0, false, "//A broken name works only if its pieces touch.")
      .newRow()
      .addImmovable(2, "III: Hour{")
      .addImmovable(6, "}")
      .addToPile(0, false, "//Bricks in the way? Move them out!")
      .newRow()
      .addToPile(0, false, "//Too many bricks? The needle leans right.")
      .build();
    }
  }