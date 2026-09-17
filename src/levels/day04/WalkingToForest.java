package levels.day04;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingToForest implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name, 21)
    // selected, start, end, option
    .questionTopLevel("""
      /* "I know a forest where we can collect wood to
      build the palisade," says Panic. He was already
      waiting for me at the door. */
      Bed:{ .getUp: North; }
      Out:{ #: Direction -> @[Bed.getUp@@.turn]@; }
      """, MethodCall)
    .errorTopLevel("""
      @[// Your legs and arms tired from yesterday,
         you ask him if @@it is far.]@
      """,
      "`//` is a SINGLE LINE comment, so the other line is broken!")
    .errorTopLevel("""
      @[No, no. It would be hard to carry everything
      back if it was too @@far!]@
      """,
      "English must go in comments")
    .questionTopLevel("""
      @[// Make an East@@.turn here!]@
      """, Comment)
    .questionTopLevel("""
      // The two of you keep walking...
      Path:{ @[.traverse@@(): Path;]@ }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, MethodDeclaration)
    .questionTopLevel("""
      // And walking...
      Path:{ .traverse(): @[Pat@@h]@; }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, Type)
    .questionTopLevel("""
      // Whoever made this path isn't the best...
      @[P@@ath:{ .traverse(): Path; }]@
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, TypeDeclaration)
    .questionTopLevel("""
      // Eventually, the path narrows...
      Path:{ .traverse(): Path; }
      NarrowPath:Path{ .traverse -> @[@@VeryNarrowPath]@; }
      VeryNarrowPath:Path{ .traverse -> this; }
      """, ObjectLiteral)
    .questionTopLevel("""
      // The forest is just ahead.
      Path:{ .traverse(): Path; }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ @[@@.traverse -> this;]@ }
      """, MethodDeclaration)
    .questionTopLevel("""
      // Let's go in!
      Path:{ .traverse(): Path; }
      NarrowPath:Path{ .traverse -> VeryNarrowPath; }
      VeryNarrowPath:Path{ .traverse -> ForestPath; }
      @[ForestPath@@:Path{ ForestPath }]@
      """, TypeDeclaration)
    .build();
    }
  }
