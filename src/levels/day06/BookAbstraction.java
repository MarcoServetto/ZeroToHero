package levels.day06;

import java.util.function.Function;

import mainZeroToHero.Days;


public class BookAbstraction implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.Book(name)
    // topStart/end   leftStart/end
      .left("""
## Type names, method names, parameter names.
Our code is like a building, making a big building requires a lot of work,
and if it gets too big, it will 
<#[0|#|collapse.|#|be amazing.|#|stink.|#|sink.]#>

We need to find ways to build small, well organized, strong code structures.

A big structure is not just hard to create, it is hard to keep clean and
in working order. Surprisingly, this is the same for code.
Your needs will change over time, and your code will need to adapt to follow your
ever changing needs.
Consider the following archer code:
```
Rotation: {#(r:Direction):Direction->...}
Archers: {...}
Archer: {
  .heading: Direction;
  .aiming: Direction;
  .turn(r: Rotation): Archer-> {
    Archers#(<#[0|#|r#(this.heading)|#|r.rotate(heading)|#|this#(r,heading)|#|this.heading]#>, this.aiming)
  }
}
```
""")
      .right("""
This code example shows the core ideas of programming:

#### __We define names to denote concepts.__
We have a few kinds of names. `Direction` and `North` are
<#[0|#|type names|#|type declarations|#|method names|#|method declarations|#|parameter names|#|parameter declarations]#>
`.turn` and `.heading` are
<#[2|#|type names|#|type declarations|#|method names|#|method declarations|#|parameter names|#|parameter declarations]#>
and `this` and `r` are 
<#[4|#|type names|#|type declarations|#|method names|#|method declarations|#|parameter names|#|parameter declarations]#>.

#### __Those names forge a world where our code can run.__

We encode behavior by
<#[0|#|passing|#|copying|#|repeating|#|twisting]#>
values around from method to method.
Parameter names are used to hold those 
<#[1|#|types|#|values|#|declarations|#|implementations]#>
while we wire them from one place to another.
We can see a value as a person and a parameter name as a work uniform that many people can use.
But, the person needs to have the right size to fit those clothes.
The person size is the type of the value, while the uniform size is the type
of the <#[2|#|meter|#|parcel|#|parameter|#|person]#>.
This makes reasoning about coding 
<#[3|#|intense|#|redundant|#|boring|#|easier]#>:
only certain values can be passed into certain methods.
""")
    .build(); } }