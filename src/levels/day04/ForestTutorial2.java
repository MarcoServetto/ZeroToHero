package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestTutorial2 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, "", 
"""
Direction:{
  .turn:Direction;
  .reverse:Direction-> this.turn.turn;
  }
""")
      .addNode(20, 10)
      .addNode(70, 20)
      .addNode(20, 70)
      .addNode(90, 70)
      .addFinishNode(90, 90)
      .connect(0, 1, "Direction:{\n  .turn:Direction;\n", 28,  2, 23, 7)

      .connect(1, 2, "  .reverse:Direction",              67, 24, 23,  7)
      .connect(1, 2, "Direction reverse",                  43, 32, 20,  7)
      .connect(1, 2, "}",                                  50, 24, 8,  7)

      .connect(1, 0, ":{ .turn: ",                         22, 17, 15,  7)

      .connect(2, 3, "-> that",                            68, 48, 18,  7)
      .connect(2, 3, "-> the",                             63, 58, 16,  7)
      .connect(2, 3, "-> this",                            60, 70, 18,  7)

      .connect(3, 4, ";\n  }\n",                             90, 80, 14, 7)
      .connect(3, 2, ".turn",                              55, 80, 18,  7)
      .build();
    }
  }