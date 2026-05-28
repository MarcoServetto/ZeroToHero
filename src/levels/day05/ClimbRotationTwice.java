package levels.day05;

import java.util.List;
import java.util.function.Function;

import mainZeroToHero.Days;

public class ClimbRotationTwice implements Function<Days.LevelName,String>{
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
Now we will learn how .twice works!
""",
"@[R .twice]@ .twice",List.of(
"R.r R",
"R + R",
"{d-> R.r d}"),1)
    .question(
"##@[R + R]@ .twice",List.of(
"{d-> R.r(R.r d)}"),0)
    .question(
"##@[{d-> R.r(R.r d)} .twice]@",List.of(
"{d-> R.r(R.r d)} + {d-> R.r(R.r d)}"),0)
    .question(
"##@[{d-> R.r(R.r d)} + {d-> R.r(R.r d)}]@",List.of(
"{d'-> {d-> R.r(R.r d)}.r({d-> R.r(R.r d)}.r d')}",
"{d-> {d-> R.r(R.r d)}.r({d-> R.r(R.r d)}.r d)}",
"{d-> R.r(R.r d)}"),0)
    .question("{d'-> {d-> R.r(R.r d)}.r({d-> R.r(R.r d)}.r d')}",
"##@[{d'-> {d-> R.r(R.r d)}.r({d-> R.r(R.r d)}.r d')}]@",List.of(
"{d'-> {d-> R.r(d.turn)}.r({d-> R.r(R.r d)}.r d')}",
"{d'-> {d-> R.r(d.turn)}.r({d-> R.r(d.turn)}.r d')}",
"{d'-> {d-> d.turn.turn}.r({d-> d.turn.turn}.r d')}",
"<completed>"),3)
    .build(); }
  }