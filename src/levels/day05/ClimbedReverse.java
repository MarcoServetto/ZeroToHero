package levels.day05;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ClimbedReverse implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name,this.getClass().getSimpleName())
    // topStart/end   leftStart/end
    .image(6)
    .build(); } }