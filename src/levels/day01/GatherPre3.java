package levels.day01;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherPre3 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
//You got it!
//This level is still easy, the next two are going
//to be much harder
Direction:{.turn:Direction,}
""")
        // code with title, group img, img num
    .cardTrash("GoodOrBad 1\nNorth:Direction{turn-East}",BrownMushroom,1)
    .cardTrash("GoodOrBad 2\nNorth Direction:{.turn->North,}",BrownMushroom,13)
    .cardTrash("GoodOrBad 3\nNorth:Direction{turn: North}",BrownMushroom,2)
    .build(); } }