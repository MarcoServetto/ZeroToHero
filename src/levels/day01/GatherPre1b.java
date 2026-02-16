package levels.day01;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherPre1b implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
//Drag and drop items into baskets
//Bad code in the trash
//Good code in a basket
//This code is visible by all the cards
//The code of a direction also assumes
//the other three directions to be
//declared as shown in the exercises before
Direction:{.turn:Direction;}
""")
        // code with title, group img, img num
    .card("GoodOrBad 1\nNorth:Direction{.turn->East;}",1,BrownMushroom,1)
    .cardTrash("GoodOrBad 2\nNorth:Direction{.turn->North;}",BrownMushroom,13)
    .build(); } }