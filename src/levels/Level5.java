package levels;
import static htmlMangle.Gather.Kind.*;
//Panic: ok, this is a good area for some tasty
//mushrooms and wild eggplants
//Panic: ah, you can write .turn-> or not, since it is the only abstract method in Direction.
//Panic: also, empty parenthesis can be omitted                        

public class Level5 implements Level{
  public String fileName(){ return "Level5"; }
  public String of(){
    return new htmlMangle.Gather(fileName(),"""
//This code is visible by all the cards to the left
Direction:{.turn:Direction,}
North:Direction {.turn->East,}
East:Direction{South}
South:Direction{West}
West:Direction{North}
""",1)
        // code with title, group img, img num
    .card("North\nNorth",1,BrownMushroom,1)
    .card("North again?\nNorth.turn.turn.turn.turn",1,BrownMushroom,2)
    .card("Not north\nNorth.turn",2,Eggplant,1)
    .cardTrash("Starts with dot?\n.turn East",BrownMushroom,5)
    .card("Start East and turn\nEast.turn.turn.turn",1,BrownMushroom,3)
    .cardTrash("Will this work?\nEast.turn{}",BrownMushroom,7)
    .card("Empty () make no difference\nEast.turn().turn.turn",1,BrownMushroom,4)
    .card("Clearly East\nEast",2,Eggplant,2)
    .cardTrash("Turning from where?\nDirection.turn",BrownMushroom,6)

    .build(); } }