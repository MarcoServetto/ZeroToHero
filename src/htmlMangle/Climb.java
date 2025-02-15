package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.Days;
import resources.File;
public class Climb {
  private static Random rand=new Random(123);
  public static int nextRock(){ return rand.nextInt(8) + 1; }
  private Days.LevelName name;
  private String api;
  public Climb(Days.LevelName name,String api){ this.name= name; this.api= api;}
  private List<CQuestion> qs= new ArrayList<>();
  private void commit(CQuestion q){ qs.add(q); }
  public Climb question(String context, String text, List<String> rocks, int option){
    text = Escape.norm(text);
    context = Escape.norm(context);
    rocks = rocks.stream().map(Escape::norm).toList();
    int start= text.indexOf("@[");
    assert start >= 0:text;
    text = text.replace("@[","");    
    int end= text.indexOf("]@");
    assert end >= 0:text;
    text = text.replace("]@","");
    assert option >= 0 && option < rocks.size();
    commit(new CQuestion(context,text,start,end,rocks,option));
    return this;
  }
  String pre(){
    return """
      <div id="apiContainer">
      <textarea id="api" class="overlayTextarea noSelection" readonly>
      """ + this.api + """
      </textarea></div>
      <textarea id="questionHelp" class="overlayTextarea noSelection" readonly></textarea>
      """;
    }
  public String build(){    
    String body= IntStream.range(0, qs.size())
      .mapToObj(index->qs.get(index).body(index))
      .collect(Collectors.joining("\n"));
    return name.htmlNextLevel(File.Climb_html.text)
      .replace("[###BODY###]", pre()+body);
  }
}
record CQuestion(String context, String text, int start, int end, List<String> rocks, int option){
  String body(int index) {
    int[] i={0};
    return 
      "<textarea class=\"overlayTextarea questionText\"\n"
    + "    id=\"question" + index + "\"\n"
    + "    name=\"Question_" + index + "\"\n"
    + "    data-original=\"" + text + "\"\n"
    + "    data-context=\"" + context + "\"\n"
    + "    data-selectionstart=\"" + start + "\"\n"
    + "    data-selectionend=\"" + end + "\"\n"
    + "    data-option=\"" + option + "\"\n"
    + rocks.stream().map(r->
      "    data-rock"+i[0]+"Img=\"Rock"+Climb.nextRock()+".png\""
    + "    data-rock"+(i[0]++)+"Code= \""+r+"\"")
      .collect(Collectors.joining("\n"))          
    + "    autocomplete=\"off\" spellcheck=\"false\" autocorrect=\"off\" autocapitalize=\"off\" readonly hidden draggable=\"false\"></textarea>";
  }
}