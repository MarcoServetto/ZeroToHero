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
  }