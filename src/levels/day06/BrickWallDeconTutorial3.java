package levels.day06;

import static htmlMangle.BrickWallDeconstruction.Background.*;

import java.util.function.Function;

import htmlMangle.BrickWallDeconstruction;
import mainZeroToHero.Days;

public class BrickWallDeconTutorial3 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWallDeconstruction(name)
      .background(Tutorial, 3)
      .addImmovable(3, "Hour:{.succ:Hour")
      .addReplaceable(0, ";", "")
      .addImmovable(0, "}")
      .newRow()
      .addImmovable(3, "I:Hour{")
      .addMovable(0, ".succ")
      .addMovable(0, ":Hour")
      .addMovable(0, "->")
      .makeReplaceable(3, ".succ->", "")
      .addImmovable(0, "II}").newRow()
      .addImmovable(3, "II:Hour{III}").newRow()
      .addImmovable(3, "III:Hour{IV}").newRow()
      .addImmovable(3, "IV:Hour{V}").newRow()
      .addImmovable(3, "V:Hour{VI}").newRow()
      .addImmovable(3, "VI:Hour{VII}").newRow()
      .addImmovable(3, "VII:Hour{VIII}").newRow()
      .addImmovable(3, "VIII:Hour{")
      .addMovable(0, ".succ")
      .addMovable(0, ":Hour")
      .addMovable(0, "->")
      .makeReplaceable(3, ".succ->", "")
      .addImmovable(0, "IX}")
      .newRow()
      .addImmovable(3, "IX:Hour{X}").newRow()
      .addImmovable(3, "X:Hour{XI}").newRow()
      .addImmovable(3, "XI:Hour{XII}").newRow()
      .addImmovable(3, "XII:Hour{")
      .addReplaceable(0, ".succ->", "")
      .addImmovable(0, "I}")
      .newRow()
      .addImmovable(3, "Wait:{#")
      .addMovable(0, "(h:Hour)")
      .addImmovable(0, ":Hour}")
      .newRow()
      .addImmovable(3, "OneHour:Wait{")
      .addReplaceable(0, "#(h:Hour):Hour->h", "::")
      .addMovable(0, ".succ")
      .addImmovable(0, "}")
      .newRow()
      .addImmovable(3, "TwoHours:Wait{")
      .addReplaceable(0, "h->h", "::")
      .addImmovable(0, ".succ.succ}")
      .addToPile(0, true, "::")
      .addToPile(5, true, "::")
      .build();
    }
  }