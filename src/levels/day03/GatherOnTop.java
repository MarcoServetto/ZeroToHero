package levels.day03;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherOnTop implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,ClimbBase.directions)
        // code with title, group, img, img num

.card("Under a bush\nArchers#(North, North)",1,Eggplant,1) // north,north = 1

.card("Between the rocks\nArchers#(North, South)",2,BrownMushroom,3) // north, south = 2
.card("In the grass\nArchers#(North, North).aimTo(South)",2,BrownMushroom,2)
.card("Near a tree\nArchers#(North, West.turn).aimTo(South)",2,BrownMushroom,4)
.card("Near a rock\nArchers#(North, South.reverse).aimTo(South)",2,BrownMushroom,5)

.card("Near a fallen tree\nArchers#(South, South)",3,BrownMushroom,7) // south, south =3
.card("Near a pond\nArchers#(South, North).aimTo(South)",3,BrownMushroom,6)
.card("Under a spidernet\nArchers#(South, West.turn).aimTo(North.turn.turn)",3,BrownMushroom,8)
.card("Near a dead bush\nArchers#(South, South).aimTo(North.reverse)",3,BrownMushroom,9)

.card("Near the cliff\nArchers#(South, North)",4,BrownMushroom,11) // south, north =4
.card("Inside a tree hole\nArchers#(South, North).headTo(South)",4,BrownMushroom,10)
.card("""
  Under the grass
  Archers#(South, West.turn).headTo(North.turn.turn)
  """,4,BrownMushroom,12)
.card("On the side of the cliff\nArchers#(South, South).aimTo(North)",4,BrownMushroom,13)

.cardTrash("Inside a pond\nArchers#(South, North).move(South)",BrownMushroom,14) // error
.cardTrash("Between two branches\nArchers#(archer.heading)",BrownMushroom,15)
.cardTrash("""
  In the mud
  Archers#(South:Rotation, West.turn).headTo(North.rotate)
  """,Tomato,2)
.cardTrash("On the side of a dead tree\nArchers:#(South, South).aimTo(North)",BrownMushroom,16)
.shuffle()
.build(); } }