package main;

import levels.day01.*;
import levels.day02.*;

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
*/