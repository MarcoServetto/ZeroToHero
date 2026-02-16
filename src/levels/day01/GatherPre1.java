package levels.day01;
import static htmlMangle.Gather.Kind.*;

import java.util.function.Function;

import main.Days;

public class GatherPre1 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Gather(name,"""
//Follow the instructions in the comments
//to understand how this game works
Direction:{.turn:Direction;}
""")
         // code with title, group img, img num
.card("""
Yes Good
East:Direction{.turn->South;}
//This code is correct: 
//it says turning from East we get South
//Put this code in any basket
""",1,BrownMushroom,3)
.cardTrash("""
Nope, Bad
West:Direction{.turn->West;}
//This code is wrong:
//it says turning from West we get West
//Put this code in the trash basket.
//Then press the next level button.
""",BrownMushroom,5)
.build(); } }