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
  private String lastContext= "";
  public Climb question(String text, List<String> rocks, int option){
    return question(lastContext,text,rocks,option);
  }
  public Climb question(String context, String text, List<String> rocks, int option){
    lastContext= context;
    text = Escape.cleanUp(text);
    boolean isContinuation= text.startsWith("##");
    if (isContinuation){ text= text.substring(2); }
    context = Escape.cleanUp(context+"\n\n");
    rocks = rocks.stream().map(Escape::cleanUp).toList();
    int start= text.indexOf("@[");
    assert start >= 0:text;
    text = text.replace("@[","");    
    int end= text.indexOf("]@");
    assert end >= 0:text;
    text = text.replace("]@","");
    assert option >= 0 && option < rocks.size();
    if(isContinuation){ checkContinuation(text); }
    commit(new CQuestion(isContinuation,context,text,start,end,rocks,option));
    return this;
  }
  void checkContinuation(String text){
    var last= qs.getLast();
    var sol= last.rocks().get(last.option());
    var prefix= last.text().substring(0,last.start());
    var suffix= last.text().substring(last.end(),last.text().length());
    var next= prefix+sol+suffix;
    assert next.equals(text): 
      next+" -- "+text;
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
record CQuestion(boolean isContinuation, String context, String text, int start, int end, List<String> rocks, int option){
  String body(int index) {
    int[] i={0};
    return 
      "<textarea class=\"overlayTextarea questionText\"\n"
    + "    id=\"question" + index + "\"\n"
    + "    name=\"Question_" + index + "\"\n"
    + "    data-original=\"" + Escape.escapeForHtmlAttribute(text) + "\"\n"
    + "    data-context=\"" + Escape.escapeForHtmlAttribute(context) + "\"\n"
    + "    data-selectionstart=\"" + start + "\"\n"
    + "    data-selectionend=\"" + end + "\"\n"
    + "    data-option=\"" + option + "\"\n"
    + "    data-iscontinuation=\"" + isContinuation + "\"\n"
    + rocks.stream().map(r->
      "    data-rock"+i[0]+"Img=\"Rock"+Climb.nextRock()+".png\""
    + "    data-rock"+(i[0]++)+"Code= \""+Escape.escapeForHtmlAttribute(r)+"\"")
      .collect(Collectors.joining("\n"))          
    + "    autocomplete=\"off\" spellcheck=\"false\" autocorrect=\"off\" autocapitalize=\"off\" readonly hidden draggable=\"false\"></textarea>";
  }
}