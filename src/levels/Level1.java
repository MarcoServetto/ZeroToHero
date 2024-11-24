package levels;
public class Level1 implements Level{
  @Override
  public String fileName(){ return "Level1"; }
  @Override
  public String of(){
    String last;
    return new htmlMangle.DirectInstructions(fileName(),2)
    // topStart/end   leftStart/end
    .image(8)
      .area(15, 30,     3, 48, "", "Hear:{}")
    .image(2)
      .area(15, 30,     3, 48, "", "See:{}")
    .image()
      .area(33, 80,    45, 98, "\nSee:{}\nHear:{}",last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}")
    .image(5)
      .area(15, 85,     1, 46, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nNorth:{}")
    .image(5)
      .area(15, 85,     1, 46, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nWest:{}\nSouth:{}")
    .image(2)
      .area( 2, 85,    55, 98, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}")
    .image(2)
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{}")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{.turn->West,}")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}")
    .image(2)
    .build(); } }