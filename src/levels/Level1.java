package levels;
public class Level1 implements Level{
  public String fileName(){ return "level1"; }
  public String of(){ return new htmlMangle.DirectInstructions()
    .image()
    .image()
      .area(10, 20, 30, 50, "aa", "aaFoo")
      .area(30, 40, 30, 50, "aa", "aaFoo")
      .area(50, 60, 30, 50, "aa", "aaFoo")
    .build(); } }