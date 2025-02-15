package levels.day01Leo;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class Leo1 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,bools)
    .question("""
      This is the climbing minigame
      Highlight/Select all the code below and click on the first rock
      """,
      "@[3 + 4]@",
      List.of("7","12","3"),
      0)
    .question("""
        Here Highlight/Select only the first operation.
        Select the rock with the correct result
        """,
        "(@[1 + 2]@)*4",
        List.of("0","3","5","1"),
        1)
    .question("""
        Here Highlight/Select the first operation that
        need to be executed
        """,
        "5*(@[6 - 4]@)",
        List.of("0","1","2"),
        2)
    .question("""
        Here we use some of the boolean.
        Follow the behaviour shown above
        """,
        "T and (@[T or F]@)",
        List.of("0","T","F"),
        1)
    .question("""
        Here we continue reducing from before
        You see, the code is as before but one step further
        """,
        "@[T and T]@",
        List.of("F","T","3", "T or F"),
        1)
    .build(); }
  String bools="""
    T and T -> T
    T and F -> F
    F and T -> F
    F and F -> F

    T or T -> T
    T or F -> T
    F or T -> T
    F or F -> F

    not T -> F
    not F -> T
    
    N1 + N2 -> N
    N1 * N2 -> N
    N1 - N2 -> N
    N1 / N2 -> N
    
    N1 = N2 -> B
    N1 > N2 -> B
    N1 < N2 -> B
    N1 <= N2 -> B
    N1 => N2 -> B
    """;  
}