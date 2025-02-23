package levels.day01Leo;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class Leo4 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,lambdas)
    .question("New rules here, look at the bottom of the rules set",
      "@[{x -> 10 + x}#4]@",
      List.of("T","F","10 + 4","4 + 10","14"),
      2)
    .question(
      "##@[10 + 4]@",
      List.of("T","F","10 + 4","4 + 10","14"),
      4)
    .question("Here with two arguments",
      "@[{x,y -> y + x}#(10,20)]@",
      List.of("T","F","10 + 20","20 + 10","30"),
      3)
    .question(
      "##@[20 + 10]@",
      List.of("T","F","10 + 20","20 + 10","30"),
      4)
    .question("Higher order functions",
      "@[{x,y -> x#y}#({z->5},20)]@",
      List.of("{z->5}#20","5","25","0","->"),
      0)
    .question(
      "##@[{z->5}#20]@",
      List.of("{z->5}#20","5","25","0","->"),
      1)
   .build(); }
  public static String lambdas=Leo1.bools+"\n"+"""
    {x -> x + 1}# 5  -> 5 + 1
    {x, y -> x * y }#(1,4)  -> 1 * 4
    {y, x -> (y + 1) * x }#(2,3)  -> (3 + 1) * 2    
    """;
}