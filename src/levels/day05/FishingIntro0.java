package levels.day05;

import java.util.function.Function;

import mainZeroToHero.Days;

public class FishingIntro0 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Fishing(name)
    // selected, @ for hidden, @@ for hidden on leash
    .fish("when near YOU")
    .fish("type this TEXT")
    .fish("type to CATCH!")
    .build(10);
    }
  }