package htmlMangle;

import java.util.stream.Collectors;

public class Escape {
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