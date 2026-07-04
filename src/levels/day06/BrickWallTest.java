package levels.day06;

import java.util.function.Function;

import htmlMangle.BrickWall;
import htmlMangle.BrickWall.*;
import mainZeroToHero.Days;

public class BrickWallTest implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name, """
Direction: {
.turn: Direction;
}""")
      .addToPile(Brick.movable("}"))
      .addToPile(Brick.movable("{"))
      .addToPile(Brick.movable(".turn: "))
      .addToPile(Brick.movable("Direction"))
      .addRow(Row.of(
        new PlacedBrick(Brick.immovable(": "), 9),
        new PlacedBrick(Brick.movable("Direction"), 10)))
      .addRow(Row.of(
        new PlacedBrick(Brick.movable(";"), 0),
        new PlacedBrick(Brick.movable("   "), 2)))
      .addRow(Row.of(
        new PlacedBrick(Brick.immovable("          "), 1)))
      .build();
    }
  }