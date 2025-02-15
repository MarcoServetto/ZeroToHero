package main;

import levels.day01.*;
import levels.day02.*;
import levels.day03.*;

public class Main {
  public static final boolean debug= false;
  public static void main(){//see comment below
    Days d= new Days();
    d.add(new IntroLevel());
    d.add(new Walking1());
    d.add(new Walking2());
    d.add(new ExplainGather());
    d.add(new GatherPre1());
    d.add(new GatherPre2());
    d.add(new GatherPre3());
    d.add(new GatgherTrueExpr());
    d.add(new GatherTrueDecl());
    d.add(new ExplainReverse());
    d.add(new WalkingBack());
    d.addLast(new Day1Outro());
    d.add(new BookIntro());
    d.add(new BookArchery1());
    d.add(new Archery1());
    d.add(new BookArchery2());
    d.addLast(new Archery2());
    d.add(new ClimbBase());
  }
}
/*
NOTE: a clean way to use this project is to 'link source' to the 
ZeroToHero/src location from a project anywhere/anywhere else on your HD
NOTE: to make this run you may have to set user.dir to the right location, for example
adding a file RealMain.java (already in the git ignore) as follows
package main;
public class RealMain {
  public static void main(String[] arg){
    System.setProperty("user.dir", "C:\\..\\ZeroToHero");
    Main.main();
  }
}
*/
/*---feedback notes
'can you stand' but he is shown standing before...
 
 walking error message barely out of screen
 
 Try to use Escape.norm instead of replaceAll in walking generator
 
 Check that screenOverlay can be last instead of first in the gameArea and if so
 make a constant string for all the stuff to close the game areas. 
 
   climbing game:
   grab a selection and drag it to a rock representing a reduction for it
   need to be the smallest selection and the smallest reduction possible with the
   given rocks
   graphically: man left/right arm up, wall in position,
   on click man disappear, wall slides down (we go up)
   man appear with flipped image
   on error, display as for 'we did it' but with falling man image
   then display stars around head image, then restart
*/