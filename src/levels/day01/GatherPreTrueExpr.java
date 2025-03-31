package levels.day01;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherPreTrueExpr implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
//Now some expressions. You have to identify equivalent
//expression: expressions denoting the same values

//This code is visible by all the cards
//here '.turn->' can be omitted, thus we can write
//North: Direction { East } instead of the longer
//North: Direction { .turn->East, }

Direction: { .turn: Direction }
North: Direction { East  }
East:  Direction { South }
South: Direction { West  }
West:  Direction { North }
""")
          // code with title, group img, img num
.card("""
South
South
//This is a simple expression just represening South
""",1,BrownMushroom,1)
.card("""
South again?
South.turn.turn.turn.turn
//This is a more complex expression,
//by turing 4 times we go back to South
//Put this in the same basket of the former card.
""",1,BrownMushroom,2)
.card("""
Not South
North.turn
//This does not represent South, but East.
//Put it in another basket.
""",2,Eggplant,1)
.cardTrash("""
Bad syntax
East.tarn
//The code above is mispelled
//(method .tarn does not exists)
//Put it in the trash
""",BrownMushroom,7)
.card("""
East?
East[]{}
//This also goes East
//object literals have optional [..] and {..}
//after them, we will see their use later.
""",1,Eggplant,4)
.build(); } }