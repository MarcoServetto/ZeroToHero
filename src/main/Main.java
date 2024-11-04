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
