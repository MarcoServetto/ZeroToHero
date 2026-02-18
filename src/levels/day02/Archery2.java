package levels.day02;

import java.util.function.Function;

import htmlMangle.DirectInstructions.Location;
import main.Days;
public class Archery2 implements Function<Days.LevelName,String>{
  Location l= new Location(2, 97, 0.5, 40.5);
  String intro="""
      //Call method Archers# to make
      //an archer shoot and move
      Direction:{ .turn: Direction }
      North:Direction{ East  }
      East: Direction{ South }
      South:Direction{ West  }
      West: Direction{ North }
      Archers:{ #(
          heading: Direction,
          aiming:  Direction): Archer ->
        Archer: {
          .heading: Direction -> heading;
          .aiming:  Direction -> aiming;
          }
        }


      """;
  String acc(String annotated){
    String question= intro + annotated;
    //intro += DirectInstructions.intoSolution(annotated);
    return question;
  }
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name,this.getClass().getSimpleName())
    // topStart/end   leftStart/end
    .image(5)
    .area(l, intro+"""
      Archers#(_____,_____)
      """,intro+"""
      Archers#( North, South ) 
      """)
    .image(2).area(l, intro+"""
      Archers__(_____,_____)
      """,intro+"""
      Archers#(North,West)
      """)
    .image(2).area(l, acc("""
      Archers/*[*/#(East,West)/*]*/
      """))
    .image(2).area(l, acc("""
      /*[*/Archers#(East,North)/*]*/
      """))
    .image(2).area(l, acc("""
      /*[*/Archers#(East,South)/*]*/
      """))
    .image(2).area(l, acc("""
      /*[*/Archers#(South,North)/*]*/
      """))
    .image(2).area(l, acc("""
      /*[*/Archers#(South,East)/*]*/
      """))
    .image(2).area(l, acc("""
      /*[*/Archers#(West,East)/*]*/
      """))
    .image(14)
    .build(); } }