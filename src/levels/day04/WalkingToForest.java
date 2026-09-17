package levels.day04;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingToForest implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name, 21)
    // selected, start, end, option
    .question("""
      /* "I know a forest where we can collect wood to
      build the palisade," says Panic. He was already
      waiting for me at the door. */
      Bed:{ .getUp: North; }
      Out:{ #: Direction -> @[Bed.getUp@@.turn]@; }
      """, MethodCall,"top level code")
    .error("""
      @[// Your legs and arms tired from yesterday,
         you ask him if @@it is far.]@
      """,
      "`//` is a SINGLE LINE comment, so the other line is broken!","")
    .error("""
      @[No, no. It would be hard to carry everything
      back if it was too @@far!]@
      """,
      "English must go in comments","")
    .question("""
      @[// Make an East@@.turn here!]@
      """, Comment,"")
    .question("""
      // The two of you keep walking...
      Path:{ @[.traverse@@(): Path;]@ }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, MethodDeclaration,"top level code")
    .question("""
      // And walking...
      Path:{ .traverse(): @[Pat@@h]@; }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, Type,"top level code")
    .question("""
      // Whoever made this path isn't the best...
      @[P@@ath:{ .traverse(): Path; }]@
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, TypeDeclaration,"top level code")
    .question("""
      // Eventually, the path narrows...
      Path:{ .traverse(): Path; }
      NarrowPath:Path{ .traverse -> @[@@VeryNarrowPath]@; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, ObjectLiteral,"top level code")
    .question("""
      // The forest is just ahead.
      Path:{ .traverse(): Path; }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ @[@@.traverse -> this;]@ }
      """, MethodDeclaration,"top level code")
    .question("""
      // Let's go in!
      Path:{ .traverse(): Path; }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> ForestPath; }
      @[ForestPath@@:Path{ ForestPath }]@
      """, TypeDeclaration,"top level code")
    .build();
    }
  }
