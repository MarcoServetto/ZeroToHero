package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import main.Main;
import resources.File;
public class Book {
  private String fileName;
  private int next;
  public Book(String fileName, int next){
    this.fileName= fileName; 
    this.next= next;
  }
  private String left;
  private String right;
  private Image current;
  public String generate(String text){
    List<Parser.ParserExtension> extensions = List.of(TablesExtension.create());
    Parser parser = Parser.builder().extensions(extensions).build();
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    return renderer.render(parser.parse(text));    
  }

  public String build(){    
    String body= IntStream.range(0, images.size())
      .mapToObj(index->images.get(index).body(index))
      .collect(Collectors.joining("\n"));
    return File.DirectInstructions_html.text
      .replace("<body>","<body data-next=\"../Level"
        +next+"/Level"
        +next+".html\">")
      .replace("[###BODY###]", body);
  }
}