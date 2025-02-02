package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.Main;
import resources.File;
public class DirectInstructions {
  private String fileName;
  private int next;
  public DirectInstructions(String fileName, int next){
    this.fileName= fileName; 
    this.next= next;
  }
  private List<Image> images= new ArrayList<>();
  private Image current;
  private void commit(Image i){
    current= i;
    images.add(i);
  }
  public DirectInstructions image(){ commit(new Image(fileName,new ArrayList<>())); return this; }
  public DirectInstructions image(int size){
    assert size>0;
    if(size==1){ return image(); } 
    return image().image(size-1);
    }
  public DirectInstructions area(double minX, double maxX, double minY, double maxY,String original, String solution){
    return area(minX,maxX,minY,maxY,original,solution,List.of());
  }

  public DirectInstructions area(double minX, double maxX, double minY, double maxY,String original, String solution,List<String> alternatives){
    if(Main.debug) {original= solution;}
    var alts=alternatives.stream().collect(Collectors.joining("|###|"));
    current.areas().add(new TArea(original,solution,alts,new Range(minX,maxX,minY,maxY)));
    return this;
  }
  public DirectInstructions area(double minX, double maxX, double minY, double maxY,String annotatedOriginal){
    String solution= annotatedOriginal
      .replace("/*[*/", "")
      .replace("/*]*/", "");
    String start= Pattern.quote("/*[*/");
    String end= Pattern.quote("/*]*/");
    String original= annotatedOriginal.replaceAll(start+".*?"+end,"[???]");
    return area(minX,maxX,minY,maxY,original,solution);
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
record Image(String fileName, List<TArea> areas){
  String indexToName(int index){
    return fileName+(index>9?"-":"-0")+index;
  }
  String body(int index){ return
     "<div class=\"contentItem\" id=\"content"+index+"\" hidden>\n"
    +"<img class=\"img_16_9\" src=\""+indexToName(index+1)+".jpg\" draggable=\"false\"/>\n"
    + IntStream.range(0, areas.size())
        .mapToObj(i->areas.get(i).body(index,i))
        .collect(Collectors.joining("\n"))
    +"\n</div>";
  }
}
record TArea(String original,String solution,String alternatives,Range r){
  String lift(String s){ return s
    .replace("\r","")
    .replace("\n", "\\n");
  }
  String body(int index1,int index2){ return
     "<textarea class=\"overlayTextarea\"\n"
    +r.asStyle()+"\n"
    +"name=\"Question_"+index1+"_"+index2+"\"\n"
    +"data-solution=\""+Escape.escapeForHtmlAttribute(solution)+"\"\n"
    +"data-original=\""+Escape.escapeForHtmlAttribute(original)+"\"\n"
    +"data-alternative=\""+Escape.escapeForHtmlAttribute(alternatives)+"\"\n"
    +"autocomplete=\"off\" spellcheck=\"false\" autocorrect=\"off\" autocapitalize=\"off\"></textarea>";
  }
}