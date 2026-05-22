package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestTutorial2 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, "", 
"""
Direction:{
  .turn:Direction;
  .reverse:Direction-> this.turn.turn;
  }
""")
      .addNode(20, 20)
      .addNode(70, 20)
      .addNode(20, 70)
      .addNode(70, 70)
      .addFinishNode(90, 90)
      .connect(0, 1, "Direction:{\n  .turn:Direction;\n", 28,  0, 30, 13)

      .connect(1, 2, "  .reverse:Direction",              62, 24, 36,  7)
      .connect(1, 2, "Direction reverse",                  40, 33, 34,  7)
      .connect(1, 2, "}",                                  18, 45, 10,  7)

      .connect(1, 0, ":{ .turn: ",                         18, 25, 28,  7)

      .connect(2, 3, "-> that",                            30, 43, 18,  7)
      .connect(2, 3, "-> the",                             52, 58, 16,  7)
      .connect(2, 3, "-> this",                            40, 70, 18,  7)

      .connect(3, 4, ";\n  }\n",                             90, 90, 14, 11)
      .connect(3, 2, ".turn",                              48, 90, 18,  7)
      .build();
    }
  }