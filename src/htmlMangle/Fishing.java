package htmlMangle;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import mainZeroToHero.Days;
import resources.File;
public class Fishing {
  Days.LevelName name;
  public Fishing(Days.LevelName name){ this.name= name; }
  public Fishing fish(String f){ this.fishes.add(f); return this; }
  private List<String> fishes= new ArrayList<String>();
  private String generate(int points){
    return "<script>globalThis.level = [\n"
      +fishes()+"];\n"
      +"</script>";
  }
  private String fishes(){
    return fishes.stream().map(Fishing::jsonString).collect(Collectors.joining(","));
  }
  public String build(int points){
    return name.htmlNextLevel(File.Fishing_html.text,
        "data-required=\""+points+"\"")
      .replace("[###BODY###]", generate(points));
  }
  private static String jsonString(String s){
  var res= new StringBuilder("\"");
  for (var i= 0; i < s.length(); i++){
    var c= s.charAt(i);
    switch(c){
      case '"'  -> res.append("\\\"");
      case '\\' -> res.append("\\\\");
      case '\b' -> res.append("\\b");
      case '\f' -> res.append("\\f");
      case '\n' -> res.append("\\n");
      case '\r' -> res.append("\\r");
      case '\t' -> res.append("\\t");
      case '<'  -> res.append("\\u003C");
      default -> {
        if (c < 32) res.append("\\u%04X".formatted((int)c));
        else res.append(c);
      }
    }
  }
  return res.append("\"").toString();
}
}