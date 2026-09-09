package levels.day06;

import java.util.function.Function;

import htmlMangle.BrickWallDeconstruction;
import mainZeroToHero.Days;

public class BrickWallDeconTest implements Function<Days.LevelName, String>{
  public String apply(Days.LevelName name) {
    //Wood[]:Material{ imm .mass(length: Nat): imm Nat -> length+length+length; }
    BrickWallDeconstruction bw= new BrickWallDeconstruction(name)
      .addMovable(0, "Wood")
      .addReplaceable(0, "[]", "")
      .addMovable(0, ":")
      .addMovable(0, "Material")
      .addMovable(0, "{ ")
      .newRow()
      .addReplaceable(2, "imm", "")
      .addMovable(0, ".mass")
      .addMovable(0, "(")
      .addMovable(0, "length")
      .addReplaceable(0, ": Nat", "")
      .addMovable(0, ")")
      .addMovable(0, ":")
      .addReplaceable(0, " imm ", "")
      .addReplaceable(0, "Nat", "")
      .addMovable(1, "->")
      .addMovable(1, "length")
      .addMovable(0, "+")
      .addMovable(0, "length")
      .addMovable(0, "+")
      .addMovable(0, "length")
      .makeReplaceable(5, "3*length", "length*3")
      //.addReplaceable(1, "length+length+length", "3*length", "length*3")
      .addMovable(0, ";")
      .newRow()
      .addMovable(0, "}")
      .addToPile(0, true, "3")
      .addToPile(0, true, "*")
      .addToPile(5, true, "length");
    return bw.build();
    }
  }