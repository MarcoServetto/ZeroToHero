package levels.day01Leo;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class Leo2 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,Leo1.bools)
    .question("""
      Now we use "not"
      """,
      "@[not T]@",
      List.of("T","F","not not F","0"),
      1)
    .question("""
      Now we use "not" twice
      """,
      "not @[not F]@",
      List.of("T","F","5","1"),
      0)
    .question("""
      Here we ask two steps in one
      """,
      "@[not not F]@",
      List.of("F","T","2"),
      0)
    .question("""
      "and" and "not" together
      """,
      "T and @[not T]@",
      List.of("0","T","F"),
      2)
    .question("""
        Final result for "and" and "not" together
        """,
        "@[T and not T]@",
        List.of("0","T","F"),
        2)
    .question("""
      Here we make a chain of reductions
      What do you think the final result is going to be?
      """,
      "@[12 > 4]@ and 5 = 3",
      List.of("F","T","3", "T or F"),
      1)
    .question(
      "##T and @[5 = 3]@",
      List.of("F","T","3", "T or F"),
      0)
    .question(
      "##@[T and F]@",
      List.of("F","T","3", "T or F"),
      0)
    .question(
      "##@[F]@",
      List.of("<completed>","T","3", "T or F"),
      0)
    .build(); }
}