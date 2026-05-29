package levels.day05;

import java.util.function.Function;

import mainZeroToHero.Days;

public class FishingComb implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Fishing(name)
    // selected, @ for hidden, @@ for hidden on leash
    .fish("North")
    .fish("No@r@@th")
    .fish("East")
    .fish("South@:Direction{@@E@@a@@s@@t}")
    .fish("Rotation:{\n  .rotate(d: Direction)@@:Direction->d.turn;\n  }")
    .fish("+(r:Rotation):Rotation->{d->this.rotate(r.rotate(d))}")
    .fish("Stone.break")
    .fish("""
Archers: {#(h: Direction, a: Direction): Archer ->
  Archer: {
    .heading: Direction -> h;
    .aiming: Direction -> a;
    }
  }
""")
    .build(70);
    }
  }