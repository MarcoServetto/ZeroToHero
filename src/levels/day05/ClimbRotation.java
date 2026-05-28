package levels.day05;

import java.util.List;
import java.util.function.Function;

import mainZeroToHero.Days;

public class ClimbRotation implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,"""
Direction: { .turn: Direction }
North: Direction { East  }
East : Direction { South }
South: Direction { West  }
West : Direction { North }

Rotation:{
  .rotate(d: Direction): Direction -> d.turn;
  +(r: Rotation): Rotation-> { d -> this.rotate(r.rotate(d)) }
  }
""")
    .question("""
At dawn, the monsters left,
and thin blades of sunlight slipped between the poles.
Then the cave warmed, and the dead wolf began to rot.
The smell drove us out. We pull the barricade open and start climing
the stone wall, desperate for a view, to know where we were.
""",
"@[Rotation + Rotation]@",List.of(
"{d-> this.rotate(this.rotate(d))}",
"{d-> Rotation.rotate(d.rotate(this))}",
"{d-> Rotation.rotate(Rotation.rotate(this))}",
"{d-> Rotation.rotate(Rotation.rotate(d))}"),3)
    .question(
"@[Rotation + Rotation]@.rotate North",List.of(
"{d-> Rotation.rotate(Rotation.rotate(d))}",
"{d-> Rotation.rotate(Rotation.rotate(d))}.rotate North",
"{d-> Rotation.rotate(North)}",
"{d-> Rotation.rotate(Rotation.rotate(North))}"),0)
    .question(
"##@[{d-> Rotation.rotate(Rotation.rotate(d))}.rotate North]@",List.of(
"Rotation.rotate(Rotation.rotate(North))",
"d.rotate(Rotation.rotate(d))",
"Rotation.rotate(Rotation.rotate(d))",
"<completed>"),0)
    .question(
"##Rotation.rotate(@[Rotation.rotate(North)]@)",List.of(
"East",
"Left",
"North.turn",
"Rotation"),2)
    .question(
"##Rotation.rotate(@[North.turn]@)",List.of(
"East",
"Left",
"North.turn",
"Rotation"),0)
    .question(
"##@[Rotation.rotate(East)]@",List.of(
"East",
"East.turn",
"North.turn",
"Rotation"),1)
    .question(
"##@[East.turn]@",List.of(
"East",
"South",
"North",
"West"),1)
    .build(); }
  }