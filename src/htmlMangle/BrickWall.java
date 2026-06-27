package htmlMangle;

import java.util.ArrayList;
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
  private int id= 0;
  
  private final List<Brick> brickPile= new ArrayList<>();
  private final List<Row> brickRows= new ArrayList<>();
  
  private final Days.LevelName name;
  
  public BrickWall(Days.LevelName name) {
    this.name= name;
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
      .replace("[###BRICKWALL###]", renderWall())
      .replace("[###PILE###]", renderPile());
    }

  private String renderWall() {
    StringBuilder sb = new StringBuilder();
    for (Row r : brickRows) {
      //sb.append(r.);
      }
    return sb.toString();
    }

  private String renderPile() {
    StringBuilder sb = new StringBuilder();
    for (Brick b : brickPile) { sb.append(b.toHtml()); }
    return sb.toString();
    }

  private static String brickEl(Brick b, int index) {
    String cls = "brick " + (b.movable() ? "movable" : "immovable");
    String idxAttr = index >= 0 ? " data-index=\"" + index + "\"" : "";
    return "<div class=\"" + cls + "\""
      + " data-length=\"" + b.length() + "\""
      + " data-code=\"" + escape(b.code()) + "\""
      + " data-movable=\"" + b.movable() + "\""
      + idxAttr + ">"
      + escape(b.code()) + "</div>";
    }

  private static String escape(String s) {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;");
  }
  
  public static record Row(int length, List<PlacedBrick> placedBricks) {
    public Row(int length) { this(length, new ArrayList<>()); }
    public Row {
      if (length <= 0) { throw new IllegalArgumentException("Row length is required to be positive!"); }
      }
    public Row addBrick(Brick brick, int index) {
      placedBricks.add(new PlacedBrick(brick, index));
      verifyValidity();
      return this;
      }
    private void verifyValidity() {
      List<Integer> emptyIndices= new ArrayList<>(
        IntStream.range(0, length).boxed().collect(Collectors.toList())
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

  public static record PlacedBrick(Brick brick, int index) {}
  
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
      return "<span class=\"brick %s\">%s</span>".formatted(movableStr, code);
      }
  }
}