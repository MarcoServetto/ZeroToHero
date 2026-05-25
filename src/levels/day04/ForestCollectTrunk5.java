package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestCollectTrunk5 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//Much faster, but still slow. What about this way?

""",
"""
Rotation720:{
  #(d: Direction): Direction-> this.reverse.reverse.reverse.reverse
  }
Rotation3600:{
  #(d: Direction): Direction->
    Rotation720#(Rotation720#(Rotation720#(Rotation720#(Rotation720#(d)))))
  }
""")
      .addNode(3, 95)
      .addNode(3, 60)
      .addNode(13, 30)

      .addNode(54, 65)
      .addNode(70, 65)
      .addNode(84, 30)

      .addFinishNode(98, 5)

      .connect(0, 1, "Rotation720:{\n  #(d: Direction): Direction-> this",
        0, 85, 50, 6)

      .connect(1, 2, ".reverse", 7, 45, 24, 7)

      .connect(1, 3, "\n  }\n", 14, 60, 10, 7)
      .connect(3, 4, "Rotation3600:{\n  #(d: Direction): Direction->\n    ",
        42, 70, 45, 7)

      .connect(4, 5, "Rotation720#(", 65, 45, 20, 7)

      .connect(5, 6, "d)))))\n  }\n", 88, 16, 11, 7)
      .build();
    }
  }