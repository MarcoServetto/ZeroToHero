package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestCollectTrunk1 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//Panic: "we need to collect trees to make a palisade.
//We can use Fearless for this!
//To make it fall, we need to `rotate` the tree."
Rotate90:{
""",
"""
  #(d: Direction): Direction-> d.turn;
  }
""")
      .addNode(20, 20)
      .addNode(70, 60)
      .addFinishNode(90, 90)

      .connect(0, 1, "  #",                                 30, 10, 10,  7)
      .connect(0, 1, "  #(d: Direction)-> ",                30, 28, 30,  7)
      .connect(0, 1, "  #(d)-> ",                           38, 43, 20,  7)
      .connect(0, 1, "  #(d: Direction): Direction-> ",      2, 51, 40,  7)

      .connect(1, 2, "d.turn;\n  }\n",                      72, 75, 18, 7)
      .build();
    }
  }