package main;

import levels.day01.*;

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
    d.add(new GatherTrue1());
    d.add(new GatgherTrue2());
    d.add(new ExplainReverse());
    d.add(new WalkingBack());
    d.add(new Day1Outro());
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