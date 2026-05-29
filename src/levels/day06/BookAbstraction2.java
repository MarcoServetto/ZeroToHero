package levels.day06;

import java.util.function.Function;

import mainZeroToHero.Days;


public class BookAbstraction2 implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.Book(name)
    // topStart/end   leftStart/end
      .left("""
### “Naming Parametric Abstractions”
Programming is just *“Naming Parametric Abstractions”*.
We have now seen two kinds of (named and parametric) abstractions:

#### __Methods__
Methods allow us to abstract away the specific 
<#[1|#|type signature|#|implementation|#|uniform|#|author]#>
of a method body: we can simply call the method again
instead of typing again the full body.
The method has both a name and named parameters.

#### __Subtyping__
Subtyping allows us to abstract types into categories:
 when mentioning `Direction` as
a <#[2|#|value|#|name|#|type|#|method|#|parameter]#>
we mean any of the values 
<#[0|#|implementing|#|denoting|#|improving|#|modifying|#|cursing]#>
`Direction`.
The type has both a name and named subtypes.
For example, for the type `Direction`, the subtypes are
`North`, `East`, `South` and `West`.
`Nort` declares to be a subtype of `Direction` by __naming__ direction:
`North:Direction{East}` 

We will see other forms of abstraction later on.
""")
      .right("""
## Syntactic sugar
Any feature aiming to skip or simplify code is called 
<#[0|#|syntactic sugar|#|syntetic cougar|#|ontactic sonar|#|selecting sugar]#>.
When we write
`North:Direction{.turn->East}` instead of the more verbose 
`<#[0|#|.turn:Direction->East;|#|.turn: Rotation->East;|#|.turn:East->East;|#|.turn:North->East;]#>`
we are relying on syntactic sugar.
The same as when we write `North:{East}`. Here the sugar also 
<#[1|#|prefers|#|infers|#|defers|#|refers]#> `.turn->` and the ending `;`.

We have seen how we wrote:
```
Rotation:{ #(d: Direction): Direction; }
Rotate90: Rotation{dir-> dir.turn }
```
It is very common to have to <#[0|#|implement|#|outlive|#|declare|#|define]#>
a single method with a single parameter by directly calling methods on that parameter.
There is a convenient sugar for this case! `::`
We can write just:
```
Rotate90: Rotation{::turn }
```
This means that we can write:
```
Rotate180: Rotation{::turn::turn<#[0|#|.turn|#|.turn.turn|#| 180|#|.reverse]#>}
```
to rotate twice and even 
```
Rotate0: Rotation{::}
```
to not rotate.
That is, `Rotate0: Rotation{::}` is equivalent to `Rotate0: Rotation{#(dir)->dir}`.

Using `{::}` is very common in Fearless.
Another useful form of sugar allows to omit
<#[0|#|round|#|metal|#|plastic|#|tax]#>
brackets when calling methods with a single parameter, or when defining methods with inferred types.
For example we can write 
```
Rotate90 + Rotate90  //shorter 
Rotate90 +(Rotate90) //with explicit brackets

Rotate90: Rotation{# dir -> dir.turn } //shorter
Rotate90: Rotation{<#[0|#|#(dir)|#|(::)|#|#(::)|#|#::]#>-> dir.turn } //with explicit brackets
```
""")
    .build(); } }