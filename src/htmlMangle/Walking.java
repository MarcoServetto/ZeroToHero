package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import resources.File;
public class Walking {
  private String fileName;
  private int required;
  private int next;
  public Walking(String fileName, int required,int next){
    this.fileName= fileName;
    this.required= required;
    this.next=next;
    }
  private List<Question> qs= new ArrayList<>();
  private void commit(Question q){
    qs.add(q);
  }
  public Walking question(String text, Option option){
    int start= text.indexOf("@[");
    assert start >= 0:text;
    text = text.replace("@[","");    
    int sel=text.indexOf("@@");
    assert sel >= 0:text;
    text = text.replace("@@","");    
    int end= text.indexOf("]@");
    assert end >= 0:text;
    text = text.replace("]@","");
    commit(new Question(fileName,text, sel,start,end,option));
    return this;
  }
  public Walking question(String text,int sel, int start, int end, Option option){
    commit(new Question(fileName,text, sel,start,end,option));
    return this;
  }
  public String build(){    
    String body= IntStream.range(0, qs.size())
      .mapToObj(index->qs.get(index).body(index))
      .collect(Collectors.joining("\n"));
    return File.Walking_html
      .text
      .replace("<body>","<body data-required=\""
        +required+"\" data-next=\"../level"
        +next+"/Level"
        +next+".html\">")
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
record Question(String fileName, String text, int sel, int start, int end, htmlMangle.Walking.Option option) {
  String body(int index) {
      return "<textarea class=\"overlayTextarea\"\n"
          + "    id=\"question" + index + "\"\n"
          + "    name=\"Question_" + index + "\"\n"
          + "    data-original=\"" + text + "\"\n"
          + "    data-red=\"" + sel + "\"\n"
          + "    data-selectionstart=\"" + start + "\"\n"
          + "    data-selectionend=\"" + end + "\"\n"
          + "    data-option=\"" + (option.ordinal()+1) + "\"\n"
          + "    autocomplete=\"off\" spellcheck=\"false\" autocorrect=\"off\" autocapitalize=\"off\" readonly></textarea>";
  }
}
/*
Bugs:
the tool tip for the next level button appears as a round in the score?


*/