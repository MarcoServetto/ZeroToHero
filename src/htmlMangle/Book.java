package htmlMangle;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import main.Days;
import resources.File;
public class Book {
  Days.LevelName name;
  public Book(Days.LevelName name){ this.name= name; }
  public Book left(String left){ this.left = left; return this; }
  public Book right(String right){ this.right = right; return this; }
  private String left;
  private String right;
  //static private final String openTag= "<#[";//integrated in the regex
  //static private final String closeTag= "]#>";
  static private final String middleTag= "|#|";
  static private final String holeStart= "<span class=\"hole\" data-correct=\"";
  static private final String holeMiddle= "\" data-options=\"";
  static private final String holeChar= "X";
  static private final String holeEnd1= "\">";
  static private final String holeEnd2= "</span>";
  static private final Pattern pattern = Pattern.compile("<#\\[(.*?)\\]#>");
  private final Map<String,String> holeReplacements= new HashMap<>();
  private int holeCounter= 0;
  
  private String htmlHole(int solution, List<String> elems){
    String first= Escape.escapeForHtmlAttribute(elems.get(solution));
    int maxSize= elems.stream().mapToInt(String::length).max().getAsInt();
    String options= elems.stream()
      .map(Escape::escapeForHtmlAttribute)
      .collect(Collectors.joining(middleTag));
    return holeStart + first + holeMiddle + options
      + holeEnd1 + holeChar.repeat((maxSize/2)+1) + holeEnd2;
    }  
  private String allHtmlHoles(String text){
    //find all openTag text closeTag
    //replace it with htmlHole(first,elems), for example <#[1|#|abc|#|dfg]#>
    //would be replaced with the result of htmlHole(1,List.of("abc","dfg")) 
    Matcher matcher = pattern.matcher(text);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      String content = matcher.group(1); // The part inside <#[ ... ]#> 
      String[] parts = content.split("\\Q"+middleTag+"\\E");
      int solutionIndex = Integer.parseInt(parts[0]);
      List<String> elems = List.of(parts);
      String replacement = htmlHole(solutionIndex, elems.subList(1,elems.size()));
      String placeholder = "@@@HOLE_" + (holeCounter++) + "@@@";
      holeReplacements.put(placeholder, replacement);
      matcher.appendReplacement(sb, Matcher.quoteReplacement(placeholder));
      }
    matcher.appendTail(sb);
    return sb.toString();
    }
  private String restorePlaceholders(String text){
    for (var entry : holeReplacements.entrySet()){
      text = text.replace(entry.getKey(), entry.getValue());
      }
    return text;
    }
  private String generate(String text){
    String htmlText=allHtmlHoles(text);
    List<Parser.ParserExtension> extensions = List.of(TablesExtension.create());
    Parser parser = Parser.builder().extensions(extensions).build();
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    String renderedHtml = renderer.render(parser.parse(htmlText));
    return restorePlaceholders(renderedHtml);    
    }
  public String build(){
    return name.htmlNextLevel(File.Book_html.text)
      .replace("[###BODY_LEFT###]", generate(left))
      .replace("[###BODY_RIGHT###]", generate(right));
  }
}