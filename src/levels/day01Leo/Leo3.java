package levels.day01Leo;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class Leo3 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,bools)
    .question("Hard mode",
       "not (@[not T]@ and not T)",
       List.of("T","F","G","0","0/0"),
       1)
    .question("Hard mode",
       "not (F and @[not T]@)",
       List.of("T","F","G","0","0/0"),
       1)
    .question("Hard mode",
       "not (@[F and F]@)",
       List.of("T","F","G","0","0/0"),
       1)
    .question("Hard mode",
       "not @[(F)]@",
       List.of("T","F","G","0","0/0"),
       1)
    .question("Hard mode",
       "@[not F]@",
       List.of("T","F","G","0","0/0"),
       0)    
    
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