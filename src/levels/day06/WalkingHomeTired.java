package levels.day06;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingHomeTired implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name, 20)
    // selected, start, end, option
    .questionTopLevel("""
      /* The villagers cheer as we leave Baligard.
      "Our savior!" Even the one who shouted
      about the broken clock waves goodbye. */
      @[// Panic: "They will tell this story for y@@ears!"]@
      """, Comment)
    .questionTopLevel("""
      // The clock tower strikes VI.
      // Panic: "An hour on the wall, and we are home."
      Wait:{ #(h: Hour): Hour }
      OneHour:Wait{ ::.succ }
      Home:{ #: Hour -> @[OneHour@@#(VI)]@ }
      """, MethodCall)
    .questionTopLevel("""
      // Panic: "Home at VII. Just in time for bed."
      Wait:{ #(h: Hour): Hour }
      OneHour:Wait{ ::.succ }
      Home:{ #: Hour -> OneHour#(@[V@@I]@) }
      """, ObjectLiteral)
    .questionTopLevel("""
      // The wall is quiet. No monsters up here.
      Wait:{ #(h: @[Ho@@ur]@): Hour }
      """, Type)
    .questionTopLevel("""
      // My legs feel like they are made of stone.
      Wait:{ @[#(h: Hour)@@: Hour]@ }
      """, MethodDeclaration)
    .questionTopLevel("""
      // Panic: "OneHour! We found it in the old houses!"
      @[One@@Hour:Wait{ ::.succ }]@
      """, TypeDeclaration)
    .errorTopLevel("""
      // Panic: "Can we stay up until XIII?"
      @[XII:Hour{ XI@@II }]@
      """,
      "There is no `XIII` on a clock: after `XII` comes `I`")
    .questionTopLevel("""
      // No: after XII the clock starts again.
      XII:Hour{ @[@@I]@ }
      """, ObjectLiteral)
    .questionTopLevel("""
      // Panic yawns: "Two more hours awake and
      // I will fall asleep while walking."
      Late:{ #: Hour -> @[VII.succ@@.succ]@ }
      """, MethodCall)
    .questionTopLevel("""
      // Panic yawns: "Two more hours awake and
      // I will fall asleep while walking."
      Late:{ #: Hour -> @[VII@@.succ]@.succ }
      """, MethodCall)
    .errorTopLevel("""
      // I am so tired I write the clock wrong.
      @[VII:Hour{ .succ V@@III }]@
      """,
      "`.succ VIII` is missing the `->`: write `.succ -> VIII`, or just `VIII`")
    .questionTopLevel("""
      /* The clock strikes VII as we reach home.
      I fall asleep before my head touches the pillow. */
      @[// Panic: "Good night, h@@ero."]@
      """, Comment)
    .build();
    }
  }