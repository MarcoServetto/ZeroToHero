package levels.day02;

import java.util.List;
import java.util.function.Function;

import main.Days;


public class BookArchery implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.Book(name)
    // topStart/end   leftStart/end
      .left("""
## Archery for beginners
In good archery, the archer first shoots and then <#[1|#|rests|#|moves|#|jumps|#|talks]#>.
We can represent an Archer as follows:
```
Archer:{
  .heading: Direction,
  <#[0|#|.aiming:Direction,|#|eating tomatoes|#|.shoot->North|#|.aiming->this]#>
  }
```
""")
      .right("""
We can then write specific kinds of Archers.
For example, this is an `Archer` that will head `North`
after shooting `East`.
```
ArcherNE:{
  .heading -> North, .aiming -> <#[1|#|North|#|East|#|South|#|potato|#|this.aiming|#|this.heading]#>,
  }
```
""")
    .build(); } }