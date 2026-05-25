package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class DuskDanger implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name,this.getClass().getSimpleName())
    // topStart/end   leftStart/end
    .image(1)
      .area(20, 95,     67, 99.5, """
Rotate3600: {
  #(d: Direction): Direction >>
    d
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn;
  }
  """, """
Rotate3600: {
  #(d: Direction): Direction ->
    d
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn
      .turn.turn.turn.turn;
  }
  """)
    .image(10)
    .build(); } }