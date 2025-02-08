package htmlMangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.Days;
import resources.File;
public class Gather {
  private Days.LevelName name;
  private String common;
  public Gather(Days.LevelName name, String common){
    this.name= name;
    this.common= common;
    }
  private List<Card> cs= new ArrayList<>();
  public Gather cardTrash(String text, Kind kind, int num){ return cardPr(text,0,kind,num); }
  public Gather card(String text, int group, Kind kind, int num){
    if(group<=0 || group>9){ throw new Error("group must be in the 1..9 range"); }
    return cardPr(text,group,kind,num);
  }

  private Gather cardPr(String text, int group, Kind kind, int num){
    if(num<=0 || num>kind.limit){ throw new Error(kind+" num must be in the 1.."+kind.limit+" range"); }
    String loc= kind.loc+"/"+kind.loc+num+".png";
    int split= text.indexOf("\n");
    assert split!=-1;
    var code= text.substring(split+1,text.length());
    var title= text.substring(0,split);
    cs.add(new Card(name.currentLevel(),code,group,loc,cs.size(),title));
    return this;
  }
  public String build(){
    var diffCode= cs.stream().map(Card::code).map(String::trim).distinct().count() == cs.size();
    var diffTitle= cs.stream().map(Card::title).map(String::trim).distinct().count() == cs.size();
    var diffImg= cs.stream().map(Card::loc).map(String::trim).distinct().count() == cs.size();
    if (!diffCode){ throw new Error("Repeated card code"); }
    if (!diffImg){ throw new Error("Repeated card img"); }
    if (!diffTitle){ throw new Error("Repeated card title"); }

    String pre="""
    <div class="commonCode">
      <div class="cardCode">
        <textarea readonly class="overlayTextarea"
        autocomplete="off" spellcheck="false"
        autocorrect="off" autocapitalize="off">
    """
+common
+   """
        </textarea>
      </div>
    </div>
    <div id="cardDeck">
    """;
    String body= cs.stream()
      .map(Card::body)
      .collect(Collectors.joining("\n"));
    return name.htmlNextLevel(File.Gather_html.text)
      .replace("[###BODY###]", pre+body
        +"<p>End of items — drag one back to compare their code, but leave it empty at the finish. Press the button at the botton to check your solution.</p></div>\n");
  }
  public enum Kind{
    BrownMushroom("BrownM",21),
    Eggplant("EggP",25),
    YellowFlower("YellowF",29),
    RedFlower("RedF",21),
    Tomato("Tomato",25);
    String loc;
    int limit;
    Kind(String loc, int limit){
      this.loc= loc; this.limit= limit;
    }
  }
}
record Card(
    String fileName, String code,
    int group, String loc,
    int id, String title){
  String body(){ return 
    "<div class=\"card\" id=\"card_"+id+"\"\n"
   +"data-card_id=\""+id+"\"\n"
   +"data-url=\""+loc+"\"\n"
   +"data-group=\""+group+"\"\n"
   +">\n"
   +"<div class=\"cardTitle\">"+title+"</div>\n"
   +"<img class=\"cardImage\" src=\"../../resources/gather/images/"+loc+"\" alt=\"Image\" />\n"
   +"""
    <div class="cardCode">
    <textarea readonly class="overlayTextarea"
    autocomplete="off" spellcheck="false"
    autocorrect="off" autocapitalize="off">
    """
   +code
   +"""
    </textarea>
    </div>
    </div>
    """;
  }
}