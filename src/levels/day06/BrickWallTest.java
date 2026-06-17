package levels.day06;

import java.util.List;
import java.util.function.Function;

import htmlMangle.BrickWall;
import htmlMangle.BrickWall.*;
import mainZeroToHero.Days;

public class BrickWallTest implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name)
      .addToPile(Brick.movable(5, "Hii"))
      .addToPile(Brick.movable(6, "Direct"))
      .addToPile(Brick.movable(1, ":"))
      .addRow(new Row(20, List.of(
        new PlacedBrick(Brick.movable(5, "World"), 0),
        new PlacedBrick(Brick.movable(6, "Hello"), 5)
        )))
      .build();
    }
  }