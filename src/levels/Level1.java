package levels;

import java.util.List;

public class Level1 implements Level{
  @Override
  public String fileName(){ return "Level1"; }
  @Override
  public String of(){
    String last;
    String noAdd= "You have to complete the provided text, not add new one. In this case, you redeclared See and Hear";
    String noComma= "You forgot the comma! In this case that comma could indeed be omitted, but for now please write it down anyway";
    String hold= "Hold your horses, we are doing one direction at a time!";
    return new htmlMangle.DirectInstructions(fileName(),2)
    // topStart/end   leftStart/end
    .image(12)
      .area(15, 30,     3, 48, "", "Hear:{}")
    .image(2)
      .area(15, 30,     3, 48, "", "See:{}")
    .image()
      .area(33, 80,    45, 98, "\nSee:{}\nHear:{}",last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}",List.of(
                               "See:{}\nHear:{}\nSense:{}\nSee:Sense{}\nHear:Sense{}",noAdd,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nSee:{}\nHear:{}",noAdd,
                               "See:Sense{}Hear:Sense{}","You also need to declare Sense"))
    .image(5)
      .area(15, 85,     1, 46, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nNorth:{}",List.of(
                               "North:{}","Do not delete the existing code, just add and adapt",
                               "North:{}\nSense:{}\nSee:Sense{}\nHear:Sense{}","Add new code in provided empty lines or at the bottom",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nWest:{}\nSouth:{}",hold))
    .image(5)
      .area(15, 85,     1, 46, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nWest:{}\nSouth:{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nWest:{}\nSouth:{}","Forgot East",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nSouth:{}","Forgot West",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nWest:{}","Forgot South"))
    .image(2)
      .area( 2, 85,    55, 98, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}\nDirection:{}","Write Direction before North"))
    .image()
      .area(37, 90,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}",noComma,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{turn:Direction}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}","You forgot the initial dot (.)"
                               ))
    .image(2)
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn,}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}","Add the result of turning!",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}","Add the result of turning!"))
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{}",noComma,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}",hold))
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{}\nSouth:Direction{.turn->West,}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{}",
                               "You just assumed We asked to complete West, right? Read slowly and carefully! :-)",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}",hold))
    .image()
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{.turn->South,}\nWest:Direction{.turn->North,}\nSouth:Direction{.turn->West,}")
    .image(4)
    .build(); } }