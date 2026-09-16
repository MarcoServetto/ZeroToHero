package levels.day05;
import static htmlMangle.Walking.Option.*;

import java.util.function.Function;

import mainZeroToHero.Days;

public class WalkingHomeAlive implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Walking(name, 18)
    // selected, start, end, option
    .question("""
@[// ++ Ok, lets go home @@finally]@
""", Comment)
    .question("""
HungerLevel:{++:HungerLevel}
Full:HungerLevel{Comfortable}
Comfortable:HungerLevel{Snacky}
Snacky:HungerLevel{Hungry}
Hungry:HungerLevel{Famished}
Famished:HungerLevel{Starving}
Starving:HungerLevel{Starving}

//You said that at our home village we can fish.
@[Hungry ++ @@++]@
""", MethodCall)
    .error("""
@[HungerLevel:{++:HungerLevel}
Full:HungerLevel{Comfortable}
Comfortable:HungerLevel{Snacky}
Snacky:HungerLevel{Hungry}
Hungry:HungerLevel{Famished}
Famished:HungerLevel{Starving}
Starving:HungerLevel{Starving}

//Should we fish?
Famished ++@@++]@
""",
      "'Famished ++++' has no space between the two '++' calls, so the four "
      +"plus signs read as one unrecognized '++++' token instead of two "
      +"chained '++' method calls")
    .question("""
HungerLevel:{++:HungerLevel}
Full:HungerLevel{Comfortable}
Comfortable:HungerLevel{Snacky}
Snacky:HungerLevel{Hungry}
Hungry:HungerLevel{Famished}
Famished:HungerLevel{Starving}
Starving:HungerLevel{Starving}

//Panic: You can fish if you want.
@[Famished@@++]@ ++
""", MethodCall)

    .error("""
HungerLevel:{++:HungerLevel}
Full:HungerLevel{Comfortable}
Comfortable:HungerLevel{Snacky}
Snacky:HungerLevel{Hungry}
Hungry:HungerLevel{Famished}
Famished:HungerLevel{Starving}
Starving:HungerLevel{Starving}

//But I'm a rabbit. Vegetarian,
//like all Rabbits.
@[Starving++@@++]@
""",
      "'Starving++++' has no space between the two '++' calls, so the four "
      +"plus signs read as one unrecognized '++++' token instead of two "
      +"chained '++' method calls")

    .build();
    }
  }