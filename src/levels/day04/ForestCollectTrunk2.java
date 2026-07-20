package levels.day04;

import java.util.function.Function;

import mainZeroToHero.Days;

public class ForestCollectTrunk2 implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    return new htmlMangle.Forest(name, """
//The last tree broke, but not a clean break, 
//the trunk is ruined and not good for the palisade.
//Panic: "Rotating the tree just 90 degrees sometimes does not work."

//"I think I know how to do this better!"

""",
"""
Rotate360:{
  #(d: Direction): Direction->
    d.turn.turn.turn.turn;
  }
""")
      .addNode(20, 20)
      .addNode(65, 35)
      .addNode(35, 65)
      .addFinishNode(90, 90)

      .connect(0, 1, "Rotate360:{\n  #(d: Direction): Direction->\n    d", 35,  18, 40, 7)

      .connect(1, 2, ".turn",                                      56, 42, 10,  7)
      .connect(2, 1, ".turn",                                      36, 42, 10,  7)

      .connect(1, 3, ";\n  }\n",                                    78, 68, 14, 7)
      .build();
    }
  }