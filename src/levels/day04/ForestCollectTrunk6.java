package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestCollectTrunk6 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//That was not faster. What if I use `inheritance`
//to avoid repeating Direction?

""",
"""
Rotation:{ .rotate(d: Direction): Direction }
Rotation720: Rotation{
  d-> d.reverse.reverse.reverse.reverse
  }
Rotation3600: Rotation{ d->
  Rotation720#(Rotation720#(Rotation720#(Rotation720#(Rotation720#(d)))))
  }
""")
      .addNode(6, 92)
      .addNode(18, 75)

      .addNode(3, 60)
      .addNode(13, 30)

      .addNode(54, 65)
      .addNode(70, 65)
      .addNode(84, 30)

      .addFinishNode(98, 5)

      .connect(0, 1, "Rotation:{ .rotate(d: Direction): Direction }\n",
        3, 79, 50, 7)

      .connect(1, 2, "Rotation720: Rotation{\n  d-> d",
        2, 67, 33, 7)

      .connect(2, 3, ".reverse", 7, 45, 24, 7)

      .connect(2, 4, "\n  }\n", 14, 58, 8, 7)

      .connect(4, 5, "Rotation3600: Rotation{ d->\n  ",
        42, 70, 45, 7)

      .connect(5, 6, "Rotation720#(", 65, 45, 20, 7)

      .connect(6, 7, "d)))))\n  }\n", 88, 16, 11, 7)
      .build();
    }
  }