package levels.day06;

import java.util.function.Function;

import htmlMangle.BrickWall;
import htmlMangle.BrickWall.*;
import mainZeroToHero.Days;

public class BrickWallTest implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name)
      .addToPile(Brick.movable("Hii"))
      .addToPile(Brick.movable("Direct"))
      .addToPile(Brick.movable("ion"))
      .addRow(Row.of(20,
        new PlacedBrick(Brick.immovable("World"), 0),
        new PlacedBrick(Brick.movable("Hello"), 5)))
      .addRow(Row.of(25,
        new PlacedBrick(Brick.movable("Row2"), 0),
        new PlacedBrick(Brick.movable("Roow"), 9)))
      .build();
    }
  }