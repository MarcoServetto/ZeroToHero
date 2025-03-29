package levels.day01;

import java.util.List;
import java.util.function.Function;

import main.Days;


public class IntroLevel implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    String last;
    String noAdd= "You have to complete the provided text, not add new one. In this case, you redeclared See and Hear";
    String noComma= "You forgot the comma! In this case that comma could indeed be omitted, but for now please write it down anyway";
    String hold= "Hold your horses, we are doing one direction at a time!";
    String methInCurly="Declare the method BETWEEN the curly {..}";
    return new htmlMangle.DirectInstructions(name)
    // topStart/end   leftStart/end
    .image(12)
      .area(15, 30,     3, 48, "", "Hear:{}",List.of(
        "Hear{}", "You forgot the colon (:)",
        "Hear:()","Curly parenthesis, not round ones",
        "Hear:[]","Curly parenthesis, not square ones",
        "'Hear:{}'", "Do not insert the (')",
        "`Hear:{}`", "Do not insert the (')",
        "hear:{}", "You need Hear with uppercase H"
        ))
    .image(2)
      .area(15, 30,     3, 48, "", "See:{}",List.of(
        "See{}", "You forgot the colon (:)",
        "See:()","Curly parenthesis, not round ones",
        "See:[]","Curly parenthesis, not square ones",
        "'See:{}'", "Do not insert the (')",
        "`See:{}`", "Do not insert the (')",
        "see:{}", "You need See with uppercase S"
        ))
    .image()
      .area(33, 80,    45, 98, "\nSee:{}\nHear:{}",last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}",List.of(
                               "See:{}\nHear:{}\nSense:{}\nSee:Sense{}\nHear:Sense{}",noAdd,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nSee:{}\nHear:{}",noAdd,
                               "See:Sense{}Hear:Sense{}","You also need to declare Sense",
                               "See:Sense{}Hear:Sense{}Sense:{}","It looks better with Sense on top"
                               ))
    .image(5)
      .area(15, 85,     1, 46, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nNorth:{}",List.of(
                               "","Do not delete the existing code, just add and adapt. Use reset to get the provided code back",
                               "North:{}","Do not delete the existing code, just add and adapt",
                               "North:{}\nSense:{}\nSee:Sense{}\nHear:Sense{}","Add new code in provided empty lines or at the bottom",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nWest:{}\nSouth:{}",hold))
    .image(5)
      .area(15, 85,     1, 46, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}\nWest:{}\nSouth:{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}\nEast:{}","Add also West and South!",
                               "East:{}\nSense:{}\nSee:Sense{}\nHear:Sense{}\n\nNorth:{}","Add at the bottom!",
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
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{turn:Direction}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}","You forgot the initial dot (.)",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}\n.turn:Direction,",methInCurly,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}\n.turn:Direction",methInCurly,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}\n.turn",methInCurly,
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{}\nNorth:Direction{}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}\nturn",methInCurly
                               ))
    .image(2)
      .area(27, 85,     1, 41, last,last=
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East,}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}",List.of(
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn,}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}","Add the result of turning!",
                               "Sense:{}\nSee:Sense{}\nHear:Sense{}\nDirection:{.turn:Direction,}\nNorth:Direction{.turn->East}\nEast:Direction{}\nWest:Direction{}\nSouth:Direction{}","You forgot the comma!",
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