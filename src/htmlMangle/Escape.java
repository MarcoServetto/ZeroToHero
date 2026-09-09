package htmlMangle;

import java.util.stream.Collectors;

public class Escape {
  public static String norm(String s){
    //s= s.replaceAll("[ \t\r]+\n", "\n");
    //s = s.replaceAll("\\s+\n", "\n");
    //if (s.endsWith("\n")){ s = s.substring(0, s.length() - 1); }
    return Escape.escapeForHtmlAttribute(cleanUp(s));
    }
  public static String cleanUp(String s){
    s = s.replaceAll("\r","").replaceAll(" +\n", "\n");
    if (s.endsWith("\n")){ s = s.substring(0, s.length() - 1); }
    return s;
    }
  public static String escapeForHtmlAttribute(String input) {
    return input.chars()
      .mapToObj(c -> switch (c) {
        case '&' -> "&amp;";
        case '<' -> "&lt;";
        case '>' -> "&gt;";
        case '"' -> "&quot;";
        case '\n' -> "&#10;";
        case '\r' -> "";//filter out windows
        case '\t' -> "&#9;";
        default -> String.valueOf((char) c);
        })
      .collect(Collectors.joining());
    }
  public static String escapeForHtmlText(String input) {
    return escapeForHtmlAttribute(input).replace(" ", "&nbsp;");
    }
  public static String escapeForHtmlScripts(String s){
    var res= new StringBuilder();
    for (var i= 0; i < s.length(); i++){
      var c= s.charAt(i);
      switch(c){
        case '"'  -> res.append("\\\"");
        case '\\' -> res.append("\\\\");
        case '\b' -> res.append("\\b");
        case '\f' -> res.append("\\f");
        case '\n' -> res.append("\\n");
        case '\r' -> res.append("\\r");
        case '\t' -> res.append("\\t");
        case '<'  -> res.append("\\u003C");
        default -> {
          if (c < 32) res.append("\\u%04X".formatted((int)c));
          else res.append(c);
         }
       }
     }
    return res.toString();
   }
  }