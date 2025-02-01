package levels;
public class Level1 implements Level{
  @Override
  public String fileName(){ return "Level1"; }
  @Override
  public String of(){
    String last;
    return new htmlMangle.DirectInstructions(fileName(),2)
    // topStart/end   leftStart/end
    .image(12)
      .area(15, 30,     3, 48, "", "Hear:{}")
    .image(2)
      .area(15, 30,     3, 48, "", "See:{}")
    .image()
      .area(33, 80,    45, 98, "\nSee:{}\nHear:{}",last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}",
                               "\nSee:{}\nHear:{}\nSense:{}\nSee:Sense{}\nHear:Sense{}|###|"+
                               "You have to complete the provided text, not add new one. In this case, you redeclared See and Hear|###|"+
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nSee:{}\nHear:{}|###|"+
                               "You have to complete the provided text, not add new one. In this case, you redeclared See and Hear")
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
      .area(37, 90,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}|###|You forgot the comma! In this case that comma could indeed be omitted, but for now please write it down anyway")
    .image(2)
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{}",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{}|###|You forgot the comma! In this case that comma could indeed be omitted, but for now please write it down anyway|###|"+
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}|###|hold your horses, we are doing one direction at a time!")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{.turn->West,}",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{}|###|You just assumed I asked to complete West, right? Read slowly and carefully! :-)|###|"+
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}|###|hold your horses, we are doing one direction at a time!")
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}")
    .image(4)
    .build(); } }