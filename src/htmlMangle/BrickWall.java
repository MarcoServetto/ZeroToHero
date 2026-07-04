package htmlMangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import mainZeroToHero.Days;
import resources.File;

/**
 * The Brick Wall mini-game has a wall of bricks, which are arranged in rows.
 * There is a pile of movable bricks that can be dragged to fill gaps
 *   in the wall.
 * Some bricks already in the wall can be moved around or removed.
 * The puzzle is completed when the bricks are arranged correctly along
 *   the wall.
 * Not all gaps have to be filled, and not all bricks have to be used.
 * This mini-game can also double as a mini-game of code simplification,
 *   where the player has to remove all the unnecessary bricks.
 */
public class BrickWall {
  private static int MAX_LENGTH = 80;
  
  private final List<Brick> brickPile= new ArrayList<>();
  private final List<Row> brickRows= new ArrayList<>();
  
  private final Days.LevelName name;
  private final String solution;
  
  public BrickWall(Days.LevelName name, String solution) {
    this.name= name;
    this.solution= solution;
    }
  public BrickWall addToPile(Brick brick) {
    brickPile.add(brick);
    return this;
    }
  public BrickWall addRow(Row row) {
    brickRows.add(row);
    return this;

    }
  public String build() {
    return name.htmlNextLevel(File.BrickWall_html.text)
      .replace("[###WALL###]", renderWall())
      .replace("[###PILE###]", renderPile());
    }

  private String renderWall() {
    StringBuilder sb = new StringBuilder();
    for (Row r : brickRows) {
      sb.append("<span class=\"brickRow\">");
      List<PlacedBrick> sortedBricks = new ArrayList<>(r.placedBricks());
      Collections.sort(sortedBricks);
      int currentIndex = 0;
      for (PlacedBrick placedBrick : sortedBricks) {
        Brick brick = placedBrick.brick();
        int brickIndex = placedBrick.index();
        int len = placedBrick.brick().length();
        for (; currentIndex < brickIndex; currentIndex++) {
          sb.append("<span class=\"empty\">&nbsp;</span>");
          }
        sb.append(brick.toHtml());
        currentIndex += len;
        }
      for (; currentIndex < MAX_LENGTH; currentIndex++) {
        sb.append("<span class=\"empty\">&nbsp;</span>");
        }
      sb.append("</span>");
      }
    return "<div id=\"wall\" class=\"wall\" data-solution=\"" + escape(solution) + "\">" + sb.toString() + "</div>";
    }

  private String renderPile() {
    StringBuilder sb = new StringBuilder();
    for (Brick b : brickPile) { sb.append(b.toHtml()); }
    return sb.toString();
    }

  public static String escape(String s) {
    return s.replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;")
      .replace("\"", "&quot;")
      .replace(" ", "&nbsp;");
  }
  
  public static record Row(List<PlacedBrick> placedBricks) {
    public static Row of(PlacedBrick... bricks) {
      return new Row(List.of(bricks));
      }
    public Row() { this(new ArrayList<>()); }

    public Row addBrick(Brick brick, int index) {
      placedBricks.add(new PlacedBrick(brick, index));
      verifyValidity();
      return this;
      }
    private void verifyValidity() {
      List<Integer> emptyIndices= new ArrayList<>(
        IntStream.range(0, MAX_LENGTH).boxed().collect(Collectors.toList())
        );
      placedBricks.stream()
        .forEach(b -> {
          int startindex= b.index();
          int endIndexExclusive= startindex + b.brick().length();
          IntStream.range(startindex, endIndexExclusive)
            .forEach(i -> {
              if (!emptyIndices.remove(Integer.valueOf(i))) {
                throw new IllegalArgumentException("Brick with code `" + b.brick().code() + "` does not fit in its row!");
                }
            });
        });
      }
    }

  public static record PlacedBrick(Brick brick, int index) implements Comparable<PlacedBrick> {
    public int compareTo(PlacedBrick other) { return index() - other.index(); }
    }
  
  public static record Brick(String code, boolean movable) {
    static public Brick movable(String code) { return new Brick(code, true); }
    static public Brick movable(int length) { return new Brick(" ".repeat(length), true); }
    
    static public Brick immovable(String code) { return new Brick(code, false); }
    static public Brick immovable(int length) { return new Brick(" ".repeat(length), false); }
    
    public int length() { return code.length(); }
    
    public Brick {
      if (code.length() <= 0) { throw new IllegalArgumentException("Brick cannot be empty!"); }
      }
    public String toHtml() {
      String movableStr = movable ? "movable" : "";
      return "<span class=\"brick %s\">%s</span>".formatted(movableStr, BrickWall.escape(code));
      }
  }
}