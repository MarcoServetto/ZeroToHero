package levels;
public class Level4 implements Level{
  @Override public String fileName(){ return "Level4"; }
  @Override public String of(){
    return new htmlMangle.DirectInstructions(fileName(),5)
    .image(5)
    .build(); } }