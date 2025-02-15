package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.Days;
import resources.File;
public class Walking {
  private Days.LevelName name;
  private int required;
  public Walking(Days.LevelName name,int required){
    this.required= required;
    this.name= name;
    }
  private List<WQuestion> qs= new ArrayList<>();
  private void commit(WQuestion q){
    qs.add(q);
  }
  public Walking question(String text, Option option){
    text = Escape.cleanUp(text);
    int start= text.indexOf("@[");
    assert start >= 0:text;
    text = text.replace("@[","");    
    int sel=text.indexOf("@@");
    assert sel >= 0:text;
    text = text.replace("@@","");    
    int end= text.indexOf("]@");
    assert end >= 0:text;
    text = text.replace("]@","");
    commit(new WQuestion(Escape.escapeForHtmlAttribute(text), sel,start,end,option));
    return this;
  }
  public Walking question(String text,int sel, int start, int end, Option option){
    commit(new WQuestion(text, sel,start,end,option));
    return this;
  }
  public String build(){    
    String body= IntStream.range(0, qs.size())
      .mapToObj(index->qs.get(index).body(index))
      .collect(Collectors.joining("\n"));
    return name.htmlNextLevel(File.Walking_html.text,
        "data-required=\""+required+"\"")
      .replace("[###BODY###]", body);
  }
  public enum Option{
    Parameter,
    MethodCall,
    ObjectLiteral,
    MethodDeclaration,
    TypeDeclaration,
    Type,
    Comment,
    Error;
  }
}
record WQuestion(String text, int sel, int start, int end, htmlMangle.Walking.Option option) {
  String body(int index) {
      return "<textarea class=\"overlayTextarea\"\n"
          + "    id=\"question" + index + "\"\n"
          + "    name=\"Question_" + index + "\"\n"
          + "    data-original=\"" + text + "\"\n"
          + "    data-red=\"" + sel + "\"\n"
          + "    data-selectionstart=\"" + start + "\"\n"
          + "    data-selectionend=\"" + end + "\"\n"
          + "    data-option=\"" + (option.ordinal()+1) + "\"\n"
          + "    autocomplete=\"off\" spellcheck=\"false\" autocorrect=\"off\" autocapitalize=\"off\" readonly hidden></textarea>";
  }
}
/*
Bugs:
the tool tip for the next level button appears as a round in the score?
*/