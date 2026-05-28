package levels.day05;

import java.util.List;
import java.util.function.Function;

import mainZeroToHero.Days;

public class ClimbRotation2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,"""
Direction: { .turn: Direction }
North: Direction { East  }
East : Direction { South }
South: Direction { West  }
West : Direction { North }

R:{
  .r(d: Direction): Direction -> d.turn;
  +(r: R): R-> { d -> this.r(r.r d) };
  .twice: R -> this + this;
  }
""")
    .question("""
We now use R for Rotation and .r for .rotate.
We also omit unnecessary parentheses.

The mountain is getting steeper.
The good news: the code is shorter.
The bad news: the code is shorter.
""",
"@[R + R]@ + R .r North",List.of(
"{d-> R + R.r d}",
"{d-> R.r(d.r R)}",
"{d-> R.r(R.r d)}"),2)
    .question(
"##@[{d-> R.r(R.r d)} + R]@ .r North",List.of(
"{d-> {d-> R.r(R.r d)}.r(R.r d)}",
"{d'-> {d-> R.r(R.r d)}.r(R.r d')}",
"{d-> R.r(R.r d)}",
"{d-> West}"),1)
    .question(
"##@[{d'-> {d-> R.r(R.r d)}.r(R.r d')} .r North]@",List.of(
"{d-> R.r(R.r d)}.r(R.r North)"),0)
    .question(
"##{d-> R.r(R.r d)}.r(@[R.r North]@)",List.of(
"R.r East",
"North.r R",
"North.turn"),2)
    .question(
"##{d-> R.r(R.r d)}.r(@[North.turn]@)",List.of(
"North",
"East",
"West",
"South"),1)
    .questionInstead(
"##{d-> R.r(R.r d)}.r(East)",
"##@[{d-> R.r(R.r d)}.r East]@",List.of(
"{d-> R.r(R.r East)}",
"R.r(R.r East)"),1)
    .question(
"##R.r(@[R.r East]@)",List.of(
"R.r South",
"East.turn"),1)
    .question(
"##R.r(@[East.turn]@)",List.of(
"South"),0)
    .questionInstead(
"##R.r(South)",
"##@[R.r South]@",List.of(
"West",
"R.turn",
"South.turn"),2)
    .question(
"##@[South.turn]@",List.of(
"West"),0)
    .build(); }
  }