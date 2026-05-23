package levels.day04;

import java.util.function.Function;

import main.Days;

public class ForestCollectTrunk4 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//I need to rotate faster... Faster!
//What if I reuse another rotation?

""",
"""
Rotation360:{
  #(d: Direction): Direction-> this.reverse.reverse
  }
Rotation720:{
  #(d: Direction): Direction-> Rotation360#(Rotation360#(d))
  }
Rotation3600:{
  #(d: Direction): Direction->
    Rotation720#(Rotation720#(Rotation720#(Rotation720#(Rotation720#(d)))))
  }
""")
      .addNode(3, 95)
      .addNode(3, 60)
      .addNode(13, 30)

      .addNode(34, 65)
      .addNode(50, 65)
      .addNode(50, 30)

      .addNode(80, 65)
      .addNode(90, 65)
      .addNode(98, 30)

      .addFinishNode(98, 05)

      .connect(0, 1, "Rotation360:{\n  #(d: Direction): Direction-> this",
        0, 85, 50, 6)

      .connect(1, 2, ".reverse", 7, 45, 24, 7)

      .connect(1, 3, "\n  }\n", 14, 60, 10, 7)
      .connect(3, 4, "Rotation720:{\n  #(d: Direction): Direction-> ",
        8, 70, 52, 7)

      .connect(4, 5, "Rotation360#(", 39, 45, 32, 7)

      .connect(4, 6, "d))\n  }\n", 56, 60, 8, 7)
      .connect(6, 7, "Rotation3600:{\n  #(d: Direction): Direction->\n    ",
        70, 70, 45, 7)

      .connect(7, 8, "Rotation720#(", 81, 45, 20, 7)

      .connect(8, 9, "d)))))\n  }\n", 88, 16, 11, 7)
      .build();
    }
  }