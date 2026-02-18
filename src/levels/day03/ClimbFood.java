package levels.day03;

import java.util.List;
import java.util.function.Function;

import main.Days;

public class ClimbFood implements Function<Days.LevelName,String>{
  public String apply(Days.LevelName name){
    return new htmlMangle.Climb(name,"""
Food: {}
Salt: {}
Mushroom: Food{ .fry: Mushroom -> this }
Eggplant: Food{ .salted(s: Salt): Eggplant-> this }
Tomato: Food  { .cut: CutTomato -> CutTomato }
CutTomato: Tomato{}
Recipe: { 
  .cook(m: Mushroom, e: Eggplant, t: CutTomato): Ratatouille
  -> Ratatouille: {
    .tomato:Tomato -> t;
    .eggplant:Eggplant -> e;
    .mushroom:Mushroom -> m;
    }
  }
""")
    .question("""
      Here is a puzzle about the Ratatouille
      we ate the other day.
      As you can see, you can code up about
      anything you like!
      """,
"Recipe.cook(@[Mushroom.fry]@, Eggplant.salted(Salt), Tomato.cut)",
List.of(
  "Mushroom",
  "Eggplant",
  "Eggplant.salted(Salt))",
  "<completed>"),0)
.question(
"##Recipe.cook(Mushroom, @[Eggplant.salted(Salt)]@, Tomato.cut)",
List.of(
  "Eggplant",
  "Eggplant.salted(Salt)",
  "Mushroom.fry",
  "<completed>"),0)
.question(
"##Recipe.cook(Mushroom, Eggplant, @[Tomato.cut]@)",
List.of(
  "Recipe.cook(Mushroom, Eggplant, Tomato)",
  "CutTomato",
  "Tomato",
  "Recipe",
  "<completed>"),1)
.question(
"##@[Recipe.cook(Mushroom, Eggplant, CutTomato)]@",
List.of(
  "Recipe.cook(Mushroom, Eggplant, Tomato)",
  "Tomato",
  "Ratatouille",
  "Recipe",
  "【🍄, 🍆, 🍅🔪】",
  "<completed>"),4)
.question(
"##@[【🍄, 🍆, 🍅🔪】]@",
List.of(
  "Ratatouille",
  "Recipe",
  "【🍄, 🍆, 🍅🔪】",
  "<completed>"),3)
    .build(); }
  }