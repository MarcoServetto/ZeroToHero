package levels.day01Leo;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class Leo3 implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,Leo1.bools)
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
}