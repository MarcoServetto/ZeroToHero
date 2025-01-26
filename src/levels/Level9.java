package levels;
public class Level9 implements Level{
  @Override public String fileName(){ return "Level9"; }
  @Override public String of(){
    return new htmlMangle.DirectInstructions(fileName(),10)
    // topStart/end   leftStart/end
    .image(7)
    .build(); } }