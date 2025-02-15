package levels.day03;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class ClimbBase2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,ClimbBase.directions)
    .question("""
      Now for Archer#
      Reduction produces values that are better represented
      in a more abstract and compact way (not valid syntax)
      We will use [[...]] or typename[[...]] for those values
      We can not write those notations directly in the code,
      but they are useful to visualize the reduction.
      """,
      "@[Archers#(North,South)]@",
      List.of("Archer{..}",
          "[[🚶⬆️, 🎯⬇️]]",
          "Anon:Archer{.heading->North, .aiming->South}",
          "Archer{.heading->North, .aiming->South}"),
      1)
    .question("""
      Now with a few more steps!
      Remember, select the smallest available reduction step!
      """,
      "Archers#(@[North.reverse]@,South)",
      List.of("North.turn.turn","this.turn.turn","South", "East.turn","East"),
      0)
    .question("""
      Now with a few more steps!
      Remember, select the smallest available reduction step!
      """,
      "Archers#(@[North.turn]@.turn,South)",
      List.of("North.turn.turn","this.turn.turn","South", "East.turn","East"),
      4)
    .question("""
      Now with a few more steps!
      Remember, select the smallest available reduction step!
      """,
      "Archers#(@[East.turn]@,South)",
      List.of("North.turn.turn","this.turn.turn","South", "East.turn","East"),
      2)
    .question("""
      Now with a few more steps!
      Remember, select the smallest available reduction step!
      """,
      "@[Archers#(South,South)]@",
      List.of("[[🚶⬆️, 🎯⬇️]]","[[🚶⬇️, 🎯⬇️]]","[[🚶⬇️, 🎯⬆️]]","[[🚶⬆️, 🎯⬆️]]"),
      1)
    .question("""
        Now with a few more steps!
        Remember, select the smallest available reduction step!
        """,
        "@[[[🚶⬆️, 🎯⬇️]]]@",
        List.of("<completed>"),
        0)
    .build(); }
  }