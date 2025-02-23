package main;

import levels.day01.*;
import levels.day01Leo.*;
import levels.day02.*;
import levels.day03.*;

public class Main {
  public static final boolean debug= false;
  public static void main(){//see comment below
    leoLevels();
    fearlessLevels();
    }
  public static void fearlessLevels(){
    Days d= new Days("assetsDest");
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
    d.add(new WalkingExplainClimb());
    d.add(new ClimbBase());
    d.add(new ClimbBaseArcher());
    d.add(new ClimbStone());
    d.add(new ClimbFood());
    //waking on the way home uses the new kind of named parameters
    }
  public static void leoLevels(){
    Days d= new Days("assetsLeo");
    d.add(new Leo1());
    d.add(new Leo2());
    d.add(new Leo3());
    d.add(new Leo4());
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

background nero fuori da gameArea, e centrare la game area

direct instructions il messaggio per errori personalizzati sopravvive il verde e perfino il next

dragging: safari does not prevent selection of images
matter max-height+max-width alle celle immagini

-book squares in the message, but xxx in the book

dragging: on safari dragging mushrooms make them '3 tall' somehow
and selects all the 3 cells in the basket

-meth --> method

-climb: rimuovere spazion in typename [

Card 7-West out of screen for comment

walking one: select all the line and press the key 'comment'
walking: 
  Consider extending text area in walking?
  alcuni non vedono che --> e' un errore
  
Correct: font size in walking and gather is different
gather, black border on top of trash and other basket names?
The "show solution after 5 attempts never worked..."


*/