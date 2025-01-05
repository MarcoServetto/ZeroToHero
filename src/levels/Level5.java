package levels;
import static htmlMangle.Gather.Kind.*;

public class Level5 implements Level{
  public String fileName(){ return "Level5"; }
  public String of(){
    return new htmlMangle.Gather(fileName(),"""
//This code is visible by all the cards to the left
Direction:{.turn:Direction,}
""",6)

        // code with title, group img, img num
    .card("1-North\nNorth:Direction{.turn->East,}",1,BrownMushroom,1)
    .card("2-North\nNorth:Direction{East,}",1,BrownMushroom,3)
    .card("3-South\nSouth:Direction{.turn->West,}",3,Eggplant,11)
    .card("4-West\nWest:Direction{.turn->North,}",4,YellowFlower,7)    
    .card("5-East\nEast:Direction{South}",2,Tomato,1)
    .card("6-East\nEast:Direction{.turn->South,}",2,Tomato,10)
    .card("7-West\nWest:Direction{.turn[]()->North,}",4,YellowFlower,1)    
    .cardTrash("8-North\nNorth:Direction{->East,}",BrownMushroom,13)
    .card("9-North\nNorth:Direction{East{},}",1,BrownMushroom,12)
    .card("10-East\nEast:Direction{South[]{}}",2,Tomato,5)
    .cardTrash("11-East\nEast:Direction{()->South}",Tomato,23)    
    .cardTrash("12-South\nSouth:Direction{.turn->South}",Eggplant,1)
    .card("13-South\nSouth:Direction{.turn()->West}",3,Eggplant,3)
    .cardTrash("14-West\nWest:Direction{turn->North,}",YellowFlower,10)    
    .build(); } }