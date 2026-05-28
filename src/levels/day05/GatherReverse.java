package levels.day05;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class GatherReverse implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
Direction: { .turn: Direction }
North: Direction { East  }
East : Direction { South }
South: Direction { West  }
West : Direction { North }

Rotation:{
  .rotate(d: Direction): Direction -> d.turn;
  +(r: Rotation): Rotation-> { d -> this.rotate(r.rotate(d)) };
  .twice: Rotation -> this + this;
  }
""")
        // code with title, group img, img num
    .card("---1---\nR + R + R",  1,Eggplant,1)
    .card("---2---\nR + R + R + R .r North",3,Eggplant,5)
    .card("---3---\nR + (R + R)",1,Eggplant,3)
    .card("---4---\nR .twice .twice",2,BrownMushroom,3)
    .card("---5---\nR + R + R + R",2,BrownMushroom,1)
    .cardTrash("---6---\nR + North",BrownMushroom,4)
    .card("---7---\nNorth",3,Eggplant,4)
    .cardTrash("---8---\nNorh .turn R + R",BrownMushroom,5)
    .card("---9---\n(R + R) + R",1,Eggplant,2)
    .build(); } }