package levels.day02;

import java.util.function.Function;

import main.Days;


public class BookArchery2 implements Function<Days.LevelName,String>{
  @Override public String apply(Days.LevelName name){
    return new htmlMangle.Book(name)
    // topStart/end   leftStart/end
      .left("""
## Dynamic Archery
In the previous book, we introduced the Archer type.
```
Archer: {
  <#[0|#|.heading: Direction,|#|.hoarding; Desection,|#|.heading: Direction]#>
  .aiming:  Direction,
  }
<#[2|#|Archer: Archer{|#|ArcherNN: ArcherNN{|#|ArcherNN: Archer{|#|Archer; Archer{|#|ArcherNN: Archer:{]#>
  .heading -> North,
  .aiming  -> North,
  }
```
It is very
<#[0|#|boring|#|exiting|#|fulfilling]#>
to write all of this code to make a new archer.
Instead of writing the verbose code above, we can
declare a type `Archers` that makes `Archer` objects.
This idea of having a type that makes an instance of another type
is quite common and we conventionally call those types with the
name of the created type but ending with 's' to
represent plurality.
We could use other conventions, like `MakeArcher` but we prefer `Archers`
because it is more concise and does not restrict its role to only making
instances.
```
<#[1|#|Direction:{|#|Archers: {|#|Archers {|#|Archer:{]#>
  #(heading: Direction, aiming: Direction): Archer->
    Archer: {
      .heading: Direction->heading,
      .aiming: Direction->aiming,
      }
  }
```
As you can see, we have moved the declaration for `Archer`
inside of a method 
<#[3|#|name.|#|call.|#|name.|#|body.|#|type.]#>


The syntax `heading: Direction` defines `heading` as a **parameter**
of the method called `#`.
This introduces `heading` as a parameter,
only visible inside the method body of `#`. 
The `this` parameter we all know is implicitly
declared by the type declaration.
`heading` is an example of an 
<#[2|#|exited truly defined pai meter.|#|extatic delphine paraglider.|#|explicitly defined parameter.]#>
""")
      .right("""
## Syntax details
We can distinguish between parameters and TypeNames
by remembering that parameters start with a
<#[0|#|lowercase character|#|loner ape character|#|lower secharter]#>
(a-z), while TypeNames start with an Uppercase character (A-Z).

Note also how the parameter `heading` is different from the
method name `.heading`. The key difference is the
<#[1|#|lagging dart wish|#|leading dot which|#|leach pot win ch]#>
identifies `.heading` as a method name.
We have also introduced a method called `#`.

Method names must either:

 1. (1) start with exactly one `.` symbol 
<#[2|#|followed why the gist done|#|for lower yat east one|#|followed by at least one]#>
lowercase letter and any amount of letters and numbers;
or 
 2. (2) <#[1|#|start with a non empty sequence of|#|be only composed of|#|start by composing off]#>
operator symbols.
That is, any symbols in this list `! ~ # & ^ + - * / < > = :`
Indeed, this means that math operators like 
<#[0|#|addition and multiplication|#|attrition and mortification|#|edition on multi licantron]#>
are just method names. 

Finally, type names names always start with an uppercase letter.

Now we can use `Archers` to create archers.

Syntax
`<#[0|#|Archers#(North,South)|#|Archers#[North,South]|#|Archers.#{North,South}|#|Archer#(North,South)|#|Archers.#(North,South)|#|Archer.#(North,South)]#>`
makes an `Archer` that shoots
`<#[2|#|North|#|East|#|South|#|West]#>` 
and faces
`<#[0|#|North|#|East|#|South|#|West]#>`.
""")
    .build(); } }