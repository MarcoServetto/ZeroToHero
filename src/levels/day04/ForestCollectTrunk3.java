package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestCollectTrunk3 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//The last tree trunk snapped like a charm!
//"Cool, let me do it even more!"
//(format 5 lines of 8 .turn each)

""",
"""
Rotate3600:{
  #(d: Direction): Direction-> d
    .turn.turn.turn.turn.turn.turn.turn.turn
    .turn.turn.turn.turn.turn.turn.turn.turn
    .turn.turn.turn.turn.turn.turn.turn.turn
    .turn.turn.turn.turn.turn.turn.turn.turn
    .turn.turn.turn.turn.turn.turn.turn.turn;
  }
""")
      .addNode(10, 10)
      .addNode(28, 56)
      .addNode(72, 56)
      .addFinishNode(90, 88)

      .connect(0, 1, "Rotate3600:{\n  #(d: Direction): Direction-> d\n", 5,  20, 42, 7)
      .connect(2, 1, ".turn.turn.turn",                  42, 40, 20,  7)
      .connect(1, 2, "    .turn",              42, 52, 20,  7)
      .connect(1, 2, ".turn\n",            42, 68, 20, 7)
      .connect(2, 3, ".turn;\n  }\n",               72, 76, 20, 7)
      .build();
    }
  }