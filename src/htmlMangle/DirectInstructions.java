package htmlMangle;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.Days;
import main.Main;
import resources.File;
public class DirectInstructions {
  private Days.LevelName name;
  private Map<Integer, String> map;
  private final String imageFolder;
  public DirectInstructions(Days.LevelName name, String imageFolder){
    this.name = name;
    this.imageFolder = imageFolder;
    this.map = loadImageMap();    
    }
  private List<Image> images= new ArrayList<>();
  private Image current;
  private void commit(Image i){
    current= i;
    images.add(i);
    }
  public DirectInstructions image(){ commit(new Image(name, new ArrayList<>(), map)); return this; }
  public DirectInstructions image(int size){
    assert size>0;
    if(size==1){ return image(); } 
    return image().image(size-1);
    }
  public DirectInstructions area(double minX, double maxX, double minY, double maxY,String original, String solution){
    return area(minX,maxX,minY,maxY,original,solution,List.of());
    }
  public DirectInstructions area(double minX, double maxX, double minY, double maxY,String original, String solution,List<String> alternatives){
    if (Main.debug){ original= solution; }
    var alts=alternatives.stream().collect(Collectors.joining("|###|"));
    current.areas().add(new TArea(original,solution,alts,new Range(minX,maxX,minY,maxY)));
    return this;
    }
  public static String intoSolution(String s){
    return s.replace("/*[*/", "").replace("/*]*/", ""); 
    }
  public record Location(double minX, double maxX, double minY, double maxY){}
  public DirectInstructions area(Location l, String annotatedOriginal){
    return area(l.minX,l.maxX,l.minY,l.maxY,annotatedOriginal);
  }
  public DirectInstructions area(Location l, String original, String solution){
    return area(l.minX,l.maxX,l.minY,l.maxY,original,solution);
  }
  public DirectInstructions area(double minX, double maxX, double minY, double maxY, String annotatedOriginal){
    String solution= intoSolution(annotatedOriginal);
    String start= Pattern.quote("/*[*/");
    String end= Pattern.quote("/*]*/");
    Pattern pattern= Pattern.compile(start + ".*?" + end, Pattern.DOTALL);
    String original= pattern.matcher(annotatedOriginal).replaceAll("_____");
    return area(minX,maxX,minY,maxY,original,solution);
    }
  public String build(){    
    String body= IntStream.range(0, images.size())
      .mapToObj(index->images.get(index).body(index))
      .collect(Collectors.joining("\n"));
    return name
      .htmlNextLevel(File.DirectInstructions_html.text)
      .replace("[###BODY###]", body);
    }
  private static final Pattern ImagePattern= Pattern.compile("(.+?)-0?(\\d+)\\.jpg");
  private Map<Integer, String> loadImageMap(){
    Map<Integer, String> map= new HashMap<>();   
    Path imagesDir= name.directoryName().getParent().resolve(imageFolder);
    assert imagesDir!= null;
    List<Path> paths; try(var s= Files.walk(imagesDir)) { paths= s.toList(); }
    catch(IOException ioe) {throw new UncheckedIOException(ioe); }
    for (var p : paths){
      String filename= p.getFileName().toString();
      var matcher= ImagePattern.matcher(filename);
      if (!matcher.matches()){ continue; }
      int number= Integer.parseInt(matcher.group(2));
      assert !map.containsKey(number)
        : "Multiple images found for number " + number 
        + ": " + map.get(number) + " and " + filename;        
      map.put(number, "../" + imageFolder + "/" + filename);
    }
    return map;
  }
}
record Image(Days.LevelName name, List<TArea> areas, Map<Integer, String> map){
  String indexToName(int index){
    if(!map.containsKey(index)){ System.err.println("Image "+index+" is missing in "+name.directoryName()); }
    return map.getOrDefault(index,"ImageNotFound.jpg");
    }
  String body(int index){ return
     "<div class=\"contentItem\" id=\"content"+index+"\" hidden>\n"
    +"<img class=\"img_16_9\" src=\""+indexToName(index+1)+"\" draggable=\"false\"/>\n"
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