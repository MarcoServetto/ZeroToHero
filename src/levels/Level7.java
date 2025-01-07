package levels;
public class Level7 implements Level{
  @Override public String fileName(){ return "Level7"; }
  @Override public String of(){
    return new htmlMangle.DirectInstructions(fileName(),8)
    // topStart/end   leftStart/end
    .image(6)
      .area(30, 95,     3, 48, """
  Direction: {
    .turn: Direction,
    .reverse: Direction-> /*[*/this.turn.turn/*]*/,
    }
  North: Direction { East,  }
  East:  Direction { South, }
  South: Direction { West,  }
  West:  Direction { North, }
  """)
    .image(5)
    .build(); } }