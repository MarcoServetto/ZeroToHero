package main;

import java.util.ArrayList;
import java.util.List;

import levels.*;

public class Main {
  static List<Level> levels= new ArrayList<>();
  static void add(Level l){ levels.add(l); }
  static void build(){ levels.stream().forEach(l->l.writeFile()); }
  public static void main(String[] arg){
    add(new Level1());
    build();
  }
}
/*
NOTE: to make this run you may have to set user.dir to the right location, for example
adding a file RealMain.java (already in the git ignore) as follows
package main;
public class RealMain {
  public static void main(String[] arg){
    System.setProperty("user.dir", "C:\\..\\ZeroToHero");
    Main.main(arg);
  }
}
*/