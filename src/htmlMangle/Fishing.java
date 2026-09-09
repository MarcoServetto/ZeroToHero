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
    return fishes.stream()
      .map(Escape::escapeForHtmlScripts)
      .map(s -> "\"" + s + "\"")
      .collect(Collectors.joining(","));
  }
  public String build(int points){
    return name.htmlNextLevel(File.Fishing_html.text,
        "data-required=\""+points+"\"")
      .replace("[###BODY###]", generate(points));
  }
}