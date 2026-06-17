package levels.day06;

import java.util.function.Function;

import mainZeroToHero.Days;

public class StartDay6Fire implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.DirectInstructions(name,this.getClass().getSimpleName())
    // topStart/end   leftStart/end
    .image(17)
          .area(15, 95,     50, 99, """
Hour: { .succ: Hour; }//succ = successor
I: Hour{ II }
II: Hour{ II }
III: Hour{ IV }
IV: Hour{ V }
V: Hour{ VI }
VI: Hour{ VII }
VII: Hour{ VIII }
VIII: Hour{ IX }
IX: Hour{ X }
X: Hour{ XI }
XI: Hour{ XII }
XII: Hour{ I }
""","""
Hour: { .succ: Hour; }//succ = successor
I: Hour{ II }
II: Hour{ II }
III: Hour{ IV }
IV: Hour{ V }
V: Hour{ this.succ }
VI: Hour{ VII }
VII: Hour{ VIII }
VIII: Hour{ IX }
IX: Hour{ X }
X: Hour{ XI }
XI: Hour{ XII }
XII: Hour{ I }
""")
    .image(8)
    .build(); } }