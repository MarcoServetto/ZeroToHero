package main;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import resources.File;
public class Days{
  String assetsDest;
  public Days(String assetsDest){ this.assetsDest= assetsDest; }
  public record LevelName(String currentLevel, String nextLevel,String assetsDest){
    public String htmlNextLevel(String html){ return htmlNextLevel(html,""); }
    public String htmlNextLevel(String html, String more){ return html
      .replace("<body>","<body "+more+" data-next=\"../"
      +nextLevel()+"/"
      +nextLevel()+".html\">");
      }
    public Path directoryName(){
      return File.startPath()
      .getParent()
      .resolve(assetsDest)
      .resolve(currentLevel);
      }
    }
  private int day= 1;
  private int level= 0;
  private boolean nextNow= false; 
  private void next(){
    assert level >= 0 && level <= 98;
    if (!nextNow){ level += 1; return; }
    day += 1; level= 1; nextNow= false; 
    }
  public String currentLevel(){ return "Level"+day+(level <= 9 ? "0" : "") + level; }
  public String nextLevel(){
    if(nextNow){ return "Level"+(day + 1) + "01"; }
    var level= this.level + 1;
    return "Level"+day+(level <= 9 ? "0" : "") + level; 
    }
  private void write(Function<LevelName,String> l){
    var ln= new LevelName(this.currentLevel(),this.nextLevel(),assetsDest);
    new FileWriter().writeFile(ln,l.apply(ln));
  }
  public void add(Function<LevelName,String> l){ next(); write(l); }
  public void addLast(Function<LevelName,String> l){ next(); nextNow= true; write(l); }
}
class FileWriter{
  void writeFile(Days.LevelName name, String s){
    Path p= File.startPath()
      .getParent()
      .resolve(name.assetsDest())
      .resolve(name.currentLevel())
      .resolve(name.currentLevel()+".html");
    try {
      Files.createDirectories(p.getParent());
      Files.writeString(p,s);
    }
    catch (IOException e) { throw new UncheckedIOException(e); }
  }
}