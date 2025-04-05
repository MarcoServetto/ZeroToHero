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
  public Walking question(String text, WalkingOption option){
    return question(text,option,"");
  }
  public Walking question(String text, WalkingOption option, String motivation){
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
    commit(new WQuestion(Escape.escapeForHtmlAttribute(text), sel,start,end,option,motivation));
    return this;
  }
  public Walking question(String text,int sel, int start, int end, WalkingOption option){
    return question(text,sel,start,end,option,"");
  }
  public Walking question(String text,int sel, int start, int end, WalkingOption option,String motivation){
    commit(new WQuestion(text, sel,start,end,option,motivation));
    return this;
  }
  public String build(){    
    String body= IntStream.range(0, qs.size())
      .mapToObj(index->qs.get(index).body(index))
      .collect(Collectors.joining("\n"))
      +"\n</div>\n<div class=\"topCenter\">"
      +options();
    return name.htmlNextLevel(File.Walking_html.text,
        "data-required=\""+required+"\"")
      .replace("[###BODY###]", body);
  }
  public String options(){
    return//option(label,tooltip,id,explanation,emoji)
        option("parameter","Parameter",1,"a parameter","🏷️")
      + option("method call","Method Call",2,"a method call","🏃")
      + option("object literal","Object Literal",3,"an object literal","📦")
      + option("method declaration","Method Declaration",4,"a method declaration","📝")
      + option("type declaration","Type Declaration",5,"a type declaration","📒")
      + option("type","Type",6,"a type","🧩")
      + option("comment","Comment",7,"a comment","💬")
      + option("error","Error",8,"an error","⚠️");
  }
  public String option(String label,String tooltip, int id, String explanation, String emoji){
    return "<div class=\"buttonRow\">\n<div class=\"roundBtn\" data-tooltip=\""+tooltip
      +"\" data-optionid=\""+id
      +"\" data-optionexplanation=\""+explanation
      +"\" data-optionemoji=\""+emoji+"\">\n"
      +"<button id=\"btn"+id+"\"><span class=\"emoji\">"+emoji
      +"</span></button></div><span>"+label
      +"</span></div>";    
  }
  public interface WalkingOption{}
  public enum Option implements WalkingOption{
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
record WQuestion(String text, int sel, int start, int end, htmlMangle.Walking.WalkingOption option, String motivation){
  String body(int index) {
      var option=(java.lang.Enum<?>)this.option();
      return "<textarea class=\"overlayTextarea\"\n"
          + "    id=\"question" + index + "\"\n"
          + "    name=\"Question_" + index + "\"\n"
          + "    data-original=\"" + text + "\"\n"
          + "    data-red=\"" + sel + "\"\n"
          + "    data-selectionstart=\"" + start + "\"\n"
          + "    data-selectionend=\"" + end + "\"\n"
          + "    data-option=\"" + (option.ordinal()+1) + "\"\n"
          + "    data-motivation=\""+motivation+ "\"\n"
          + "    autocomplete=\"off\" spellcheck=\"false\" autocorrect=\"off\" autocapitalize=\"off\" readonly hidden></textarea>";
  }
}
/*
Bugs:
the tool tip for the next level button appears as a round in the score?
*/