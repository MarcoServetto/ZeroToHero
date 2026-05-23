package levels.day04;

import java.util.function.Function;

import main.Days;

public class RotationCombine implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name,this.getClass().getSimpleName())
    // topStart/end   leftStart/end
    .image(6)
      .area(20, 95,     0.5, 55, """
D🪰i̷r͟e🧫c͜t𐍈i̸o🪳n: { .t🧨u͠r🕳️n: D̶i🧷r͢e🧊c̴t🦷i͟o🧬n }

N🫠o̸rᚠt͜h: D🪱i͟r🪲e̴c🪦t͠i🧃o̷n { E͢a🪤s̶t }
E̷a🧿s͠t : D🦠i̸r͜e🫧c🪨t͟i̶o🕷️n { S𐌰o̴u🧨t͢h }
S🧫o͟u🦴t̸h: D͠i🪳r🧬e̷c🧷t͜i🪰o̶n { W🧊e͢s🪱t }
W🪦e̸s͟t : D🫥i͜r🪲e͠c🧃t̴i🦷o̷n { N🕳️o͢r🦠t̶h }
""", """
Direction: { .turn: Direction }
North: Direction { East  }
East : Direction { South }
South: Direction { West  }
West : Direction { North }
""")
    .image(1)
      .area(20, 95,     0.5, 55, """
Rotation: {
  .r🧨o͠ta̶te(d:D🪱i͟r🪲e̴c🪦t͠i🧃o̷n):D🦠i̸r͜e🫧cti̶o🕷️n->
    d.t🫥u̸r🪤n;
  +(r:Ro͟ta̸t͜i🪰o̶n):R🪦o̸t͟a🫥t͜i🪲o͠n->
    {d -> ____.ro͢t🪱a̷te(r.r🪰ot͟a🧫t͜e(_))};
  .t🕳️w͢i🦠c̶e: R🪳o🧬t̷at͜i🪰o̶n->
    this + ____;
}
""", """
Rotation: {
  .rotate(d: Direction): Direction ->
    d.turn;
  +(r:Ro͟ta̸t͜i🪰o̶n):R🪦o̸t͟a🫥t͜i🪲o͠n->
    {d -> ____.ro͢t🪱a̷te(r.r🪰ot͟a🧫t͜e(_))};
  .t🕳️w͢i🦠c̶e: R🪳o🧬t̷at͜i🪰o̶n->
    this + ____;
}
""")
    .image(1)
      .area(20, 95,     0.5, 55, """
Rotation: {
  .rotate(d: Direction): Direction ->
    d.turn;
  +(r:Ro͟ta̸t͜i🪰o̶n):R🪦o̸t͟a🫥t͜i🪲o͠n->
    {d -> ____.ro͢t🪱a̷te(r.r🪰ot͟a🧫t͜e(_))};
  .t🕳️w͢i🦠c̶e: R🪳o🧬t̷at͜i🪰o̶n->
    this + ____;
}
""", """
Rotation: {
  .rotate(d: Direction): Direction ->
    d.turn;
  +(r: Rotation): Rotation ->
    { d -> this.rotate( r.rotate(d) ) };
  .t🕳️w͢i🦠c̶e: R🪳o🧬t̷at͜i🪰o̶n->
    this + ____;
}
""")
    .image(1)
      .area(20, 95,     0.5, 55, """
Rotation: {
  .rotate(d: Direction): Direction ->
    d.turn;
  +(r: Rotation): Rotation ->
    { d -> this.rotate( r.rotate(d) ) };
  .t🕳️w͢i🦠c̶e: R🪳o🧬t̷at͜i🪰o̶n->
    this + ____;
}
""", """
Rotation: {
  .rotate(d: Direction): Direction ->
    d.turn;
  +(r: Rotation): Rotation ->
    { d -> this.rotate( r.rotate(d) ) };
  .twice: Rotation->
    this + this;
}
""")
    .image(2)
      .area(20, 95,     0.5, 55, """
DrillDown: {
  #d: Direction: Direction ->
    Rotation
      .twice _______________________
      ______________________________
      ______________________________
      .rotate(_)
  }
}
""", """
DrillDown: {
  #d: Direction: Direction ->
    Rotation
      .twice.twice.twice.twice.twice
      .twice.twice.twice.twice.twice
      .twice.twice
      .rotate(d)
  }
}
""")
    .image(9)
    .build(); } }