package levels.day06;

import static htmlMangle.BrickWall.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWall;
import mainZeroToHero.Days;

public class BrickWallTutorial3 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name, """
Hour: { .succ: Hour }
I: Hour{ II }
II: Hour{ III }
III: Hour{ IV }
IV: Hour{ V }
V: Hour{ VI }
VI: Hour{ VII }
VII: Hour{ VIII }
VIII: Hour{ IX }
IX: Hour{ X }
X: Hour{ XI }
XI: Hour{ XII }
XII: Hour{ I }""")
      .background(Tutorial, 3)
      .addImmovable(2, "Hour: { .succ: Hour }")
      .newRow()
      .addImmovable(2, "I: Hour{ II }")
      .newRow()
      .addImmovable(2, "II: Hour{ III }")
      .newRow()
      .addImmovable(2, "III: Hour{ IV }")
      .newRow()
      .newRow()
      .addToPile(8, true, "}")
      .addToPile(12, true, "{")
      .newRow()
      .addToPile(8, true, "}")
      .addToPile(11, true, "XI")
      .addToPile(3, true, "}")
      .addToPile(4, true, "}")
      .addToPile(8, true, "V")
      .addToPile(6, true, "I")
      .newRow()
      .addToPile(3, true, "}")
      .addToPile(4, true, "IX")
      .addToPile(6, true, "}")
      .addToPile(6, true, "XI")
      .addToPile(3, true, "{")
      .addToPile(4, true, "}")
      .addToPile(8, true, "VI")
      .addToPile(6, true, "}")
      .newRow()
      .addToPile(2, true, "IV:")
      .addToPile(5, true, "XII")
      .addToPile(6, true, "VI")
      .addToPile(6, true, "V:")
      .addToPile(3, true, "{")
      .addToPile(3, true, "II")
      .addToPile(5, true, "X")
      .addToPile(4, true, "VII")
      .addToPile(6, true, "}")
      .newRow()
      .addToPile(1, true, "Hour")
      .addToPile(5, true, "Hour{")
      .addToPile(7, true, "XI:")
      .addToPile(5, true, "Hour{")
      .addToPile(6, true, "VI:")
      .addToPile(6, true, "X:")
      .addToPile(4, true, "Hour")
      .addToPile(6, true, "I:")
      .newRow()
      .addToPile(0, true, "Hour{")
      .addToPile(6, true, "Hour{")
      .addToPile(6, true, "VIII:")
      .addToPile(6, true, "Hour{")
      .addToPile(6, true, "Hour")
      .addToPile(5, true, "VII:")
      .addToPile(5, true, "Hour{")
      .addToPile(6, true, "IX:")
      .newRow()
      .addToPile(0, false, "//Now a great task: rebuild the rest of the clock.")
      .newRow()
      .addToPile(0, false, "//Stuck? Press ? to see how the original looked.")
      .build();
    }
  }