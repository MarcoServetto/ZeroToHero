package levels.day01;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherTrueDecl implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
//This code is visible by all the cards
//The code of a direction also assumes
//the other three directions to be
//declared as shown in the exercises before 
Direction: { .turn: Direction; }
""")

        // code with title, group img, img num
    .card("1-North\nNorth: Direction { .turn-> East; }",1,BrownMushroom,1)
    .card("2-North\nNorth: Direction { East; }",1,BrownMushroom,3)
    .card("3-South\nSouth: Direction { .turn-> West; }",3,Eggplant,11)
    .card("4-West\nWest: Direction { .turn-> North; }",4,YellowFlower,7)    
    .card("5-East\nEast: Direction { South }",2,Tomato,1)
    .card("6-East\nEast: Direction { .turn-> South; }",2,Tomato,10)
    .card("""
      7-West
      West: Direction { .turn[]()-> North; }
      //Remember: [] and () can be omitted, thus
      //code with extra [] and () is still valid
      """,4,YellowFlower,1)    
    .cardTrash("8-North\nNorth: Direction { ->East; }",BrownMushroom,13)
    .card("""
      9-North
      North: Direction { East{}, }
      //Are omittable {} part of object literals?
      //Experiment to discover it!
      """,1,BrownMushroom,12)
    .card("""
     10-East
     East: Direction{ South[]{} }
     //Are omittable [] and {} part
     //of object literals?
     //Experiment to discover it!
     """,2,Tomato,5)
    .cardTrash("""
     11-East
     East: Direction { ()-> South }
     //Are omittable ()-> part of method declarations?
     //Experiment to discover it!
     """,Tomato,23)
    .cardTrash("12-South\nSouth: Direction { .turn-> South }",Eggplant,1)
    .card("13-South\nSouth: Direction { .turn()-> West }",3,Eggplant,3)
    .cardTrash("""
     14-West
     West: Direction { turn-> North, }
     //Something smells strange here.
     //Can you see the issue?
     """,YellowFlower,10)    
    .build(); } }