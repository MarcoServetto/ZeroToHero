package levels.day03;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class ClimbStone implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,"""
    Stone:  { .break: Rock   -> Rock;   }
    Rock:   { .break: Pebble -> Pebble; }
    Pebble: { .break: Sand   -> Sand;   }
    Sand:   { .break: Sand   -> Sand;   }                          
    """)
    .question("""
      Those stones are breaking.
      Follow the behaviour expressed in the above
      code to avoid folling down!
      """,
      "@[Stone.break]@.break.break.break.break",
      List.of("East.turn",
              "West",
              "Rock",
              "Rock.break",
              "<completed>"),
      2)
    .question(
      "##@[Rock.break]@.break.break.break",
      List.of("Pebble",
              "Sand",
              "Sand.break",
              "Archers#(North.reverse,South)",
              "<completed>"),
      0)
    .question(
      "##@[Pebble.break]@.break.break",
      List.of("Sand.break.break.break",
              "Sand.break.break",
              "Sand.break",
              "Sand",
              "<completed>"),
      3)
    .question(
      "##@[Sand.break]@.break",
      List.of("Sand",
              "<completed>"),
      0)
    .question(
      "##@[Sand.break]@",
      List.of("Sand.break","Sand","Rock","<completed>"),
      1)
    .question(
      "##@[Sand]@",
      List.of("Sand.break","Sand","Rock","<completed>"),
      3)
    .build(); }
  }