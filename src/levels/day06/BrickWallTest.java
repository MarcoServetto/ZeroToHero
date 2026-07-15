package levels.day06;

import java.util.function.Function;

import htmlMangle.BrickWall;
import mainZeroToHero.Days;

public class BrickWallTest implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name, """
Direction: {
.turn: Direction;
}""")
      .addImmovable(9, ": ")
      .addMovable(0, "Direction")
      .newRow()
      .addMovable(0, ";")
      .addMovable(1, "   ")
      .newRow()
      .addImmovable(1, "          ")
      .addToPile(0, true, "}")
      .addToPile(0, true, "{")
      .addToPile(0, true, ".turn: ")
      .addToPile(0, true, "Direction")
      .addToPile(0, true, "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww")
      .build();
    }
  }