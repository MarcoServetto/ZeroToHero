package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import resources.File;
public class DirectInstructions {
  private List<Image> images= new ArrayList<>();
  private Image current;
  private void commit(Image i){
    current= i;
    images.add(i);
  }
  public DirectInstructions image(){ commit(new Image(new ArrayList<>())); return this; }
  public DirectInstructions area(double minX, double maxX, double minY, double maxY,String original, String solution){
    current.areas().add(new TArea(original,solution,new Range(minX,maxX,minY,maxY)));
    return this;
  }
  public String build(){    
    String body= IntStream.range(0, images.size())
      .mapToObj(index->images.get(index).body(index))
      .collect(Collectors.joining("\n"));
    return File.DirectInstructions_html
      .text.replace("[###BODY###]", body);
  }
}
record Image(List<TArea> areas){
  String body(int index){ return
     "<div class=\"contentItem\" id=\"content"+index+"\">\n"
    +"<img class=\"img_16_9\" src=\"img"+index+".png\"/>\n"
    + IntStream.range(0, areas.size())
        .mapToObj(i->areas.get(i).body(index,i))
        .collect(Collectors.joining("\n"))
    +"\n</div>";
  }
}
record TArea(String original,String solution,Range r){
  String lift(String s){ return s
    .replace("\r","")
    .replace("\n", "\\n");
  }
  String body(int index1,int index2){ return
     "<textarea class=\"overlayTextarea\"\n"
    +r.asStyle()+"\n"
    +"name=\"Question_"+index1+"_"+index2+"\"\n"
    +"data-solution=\""+solution+"\"\n"
    +"data-original=\""+original+"\"\n"
    +"autocomplete=\"off\"></textarea>";
  }
}