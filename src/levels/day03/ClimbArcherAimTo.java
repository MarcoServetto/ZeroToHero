package levels.day03;

import java.util.List;
import java.util.function.Function;

import mainZeroToHero.Days;

public class ClimbArcherAimTo implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,ClimbBase.directionsAimTo)
    .question("""
      Now we use methods Archer.headTo and Archer.aimTo
      Look to the code above to see the implementation.
      Remember to select the result that can be obtained
      in the least number of steps.
      That is, occasionally only rocks that skips some steps will be available!
      """,
      "Archers#(South, @[West.turn]@).headTo(North.turn.turn)",
      List.of("Archer{..}",
        "【🚶⬆️, 🎯⬇️】",
        "West", "East", "North"
        ),
      4)
    .question(
      "##@[Archers#(South, North)]@.headTo(North.turn.turn)",
      List.of("【🚶⬇️, 🎯⬆️】", "【🚶⬆️, 🎯⬇️】"),
      0)
    .question(
      "##【🚶⬇️, 🎯⬆️】.headTo(@[North.turn.turn]@)",
      List.of("North.turn.turn","this.turn.turn","South"),
      2)
    .question(
      "##@[【🚶⬇️, 🎯⬆️】.headTo(South)]@",
      List.of("North.turn.turn","【🚶⬇️, 🎯⬇️】","【🚶⬇️, 🎯⬆️】"),
      2)
    .question(
      "##@[【🚶⬇️, 🎯⬆️】]@",
      List.of("<completed>"),
      0)
    .build(); }
  }