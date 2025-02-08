package levels.day01;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherPre2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
//Drag and drop items into baskets
//Bad code in the trash
//Good code in a basket
//Put in the same basket conceptually equal code
//and in different baskets conceptually different code
//This code is visible by all the cards
//The code of a direction also assumes
//the other three directions to be
//declared as shown in the exercises before
Direction:{.turn:Direction,}
""")
        // code with title, group img, img num
    .card("Longer\nNorth:Direction{.turn:Direction->East,}",1,Eggplant,1)
    .card("Long\nNorth:Direction{.turn->East}",1,Eggplant,4)
    .card("Short\nNorth:Direction{East}",1,Eggplant,2)
    .card("East\nEast:Direction{South}",2,YellowFlower,2)
    .cardTrash("Alternative\nNorth:Direction{.turn:Direction,}",Eggplant,5)
    .build(); } }