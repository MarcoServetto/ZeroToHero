package main;

import java.util.ArrayList;
import java.util.List;
import levels.*;

public class Main {
  public static final boolean debug= false;
  static List<Level> levels= new ArrayList<>();
  static void add(Level l){ levels.add(l); }
  static void build(){ levels.stream().forEach(l->l.writeFile()); }
  public static void main(){//see comment below
    add(new Level1());
    add(new Level2());
    add(new Level3());
    build();
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