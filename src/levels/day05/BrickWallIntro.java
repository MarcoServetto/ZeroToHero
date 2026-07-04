package levels.day05;

import java.util.function.Function;

import mainZeroToHero.Days;
import htmlMangle.BrickWall;

public class BrickWallIntro implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new BrickWall(name, "")
      
      .build();
    }
  }