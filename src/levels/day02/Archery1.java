package levels.day02;

import java.util.function.Function;

import htmlMangle.DirectInstructions.Location;
import main.Days;
public class Archery1 implements Function<Days.LevelName,String>{
  Location l= new Location(2, 97, 0.5, 40.5);
  String intro="""
      //Code to make an archer
      //shooting and then moving
      //to hit the targets
      //Use 'methName -> MethBody,'
      
      Direction:{ .turn: Direction }
      North:Direction{ East  }
      East: Direction{ South }
      South:Direction{ West  }
      West: Direction{ North }
      Archer:{
        .heading: Direction,
        .aiming:  Direction,
        }      
      """;
  String acc(String annotated){
    String question= intro + annotated;
    //intro += DirectInstructions.intoSolution(annotated);
    return question;
  }
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name)
    // topStart/end   leftStart/end
    .image(8)
    .area(l, intro+"""
      Archer1:Archer{
        .heading-> _____,
        .aiming -> _____, 
        }
      """,intro+"""
      Archer1:Archer{
        .heading-> North,
        .aiming -> South, 
        }""")
    .image(2).area(l, intro+"""
      Archer2:Archer{
        .heading-> _____,
        ________________, 
        }
      """,intro+"""
      Archer2:Archer{
        .heading-> North,
        .aiming -> West, 
        }
    """)
    .image(2).area(l, acc("""
      Archer3:Archer{/*[*/
        .heading-> East,
        .aiming -> West, 
      /*]*/}
      """))
    .image(2).area(l, acc("""
      Archer4:Archer{/*[*/
        .heading-> East,
        .aiming -> North, 
      /*]*/}
      """))
    .image(2).area(l, acc("""
      Archer5:Archer{/*[*/
        .heading-> East,
        .aiming -> South, 
      /*]*/}
      """))
    .image(2).area(l, acc("""
      Archer6:Archer{/*[*/
        .heading-> South,
        .aiming -> North, 
      /*]*/}
      """))
    .image(2).area(l, acc("""
      Archer7:Archer{/*[*/
        .heading-> South,
        .aiming -> East, 
      /*]*/}
      """))
    .image(2).area(l, acc("""
      Archer8:Archer{/*[*/
        .heading-> West,
        .aiming -> East, 
      /*]*/}
      """))
    .image(3)
    .build(); } }