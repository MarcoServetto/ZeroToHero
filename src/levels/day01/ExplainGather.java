package levels.day01;

import java.util.function.Function;

import main.Days;

public class ExplainGather implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name,this.getClass().getSimpleName())
    .image(6)
    .build(); } }