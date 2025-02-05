package levels.day01;

import java.util.function.Function;

import main.Days;
public class Day1Outro implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name)
    // topStart/end   leftStart/end
    .image(7)
    .build(); } }