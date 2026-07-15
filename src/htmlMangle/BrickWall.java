package htmlMangle;

import java.util.ArrayList;
import java.util.List;

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
  private static final int maxLength = 80;
  
  private final List<Brick> brickPile= new ArrayList<>();
  private final List<List<Brick>> brickRows= new ArrayList<>();
  private List<Brick> currentRowBricks= new ArrayList<>();
  
  private final Days.LevelName name;
  private final String solution;
  int currentIndexAlongRow= 0;
  
  public BrickWall(Days.LevelName name, String solution) {
    this.name= name;
    this.solution= solution;
    }
  public BrickWall addToPile(int indexSkip, boolean movable, String s) {
    brickPile.add(new Brick(s, movable, indexSkip));
    return this;
    }
  public BrickWall newRow() {
    brickRows.add(currentRowBricks);
    currentIndexAlongRow = 0;
    currentRowBricks = new ArrayList<>();
    return this;
    }
  public BrickWall addBrick(int indexSkip, boolean movable, String s) {
    currentIndexAlongRow += indexSkip;
    currentRowBricks.add(new Brick(s, movable, currentIndexAlongRow));
    return this;
    }
  public BrickWall addImmovable(int indexSkip, String s) { return addBrick(indexSkip, false, s); }
  public BrickWall addMovable(int indexSkip, String s) { return addBrick(indexSkip, true, s); }
  public String build() {
    return name.htmlNextLevel(File.BrickWall_html.text)
      .replace("[###WALL###]", renderWall())
      .replace("[###PILE###]", renderPile());
    }

  private String renderWall() {
    brickRows.add(currentRowBricks);
    StringBuilder sb = new StringBuilder();
    for (List<Brick> sortedBricks : brickRows) {
      sb.append("<span class=\"brickRow\">");
      int currentIndex = 0;
      for (Brick brick : sortedBricks) {
        int brickIndex= brick.index();
        int len= brick.length();
        for (; currentIndex < brickIndex; currentIndex++) {
          sb.append("<span class=\"empty\">&nbsp;</span>");
          }
        sb.append(brick.toHtml());
        currentIndex += len;
        }
      for (; currentIndex < maxLength; currentIndex++) {
        sb.append("<span class=\"empty\">&nbsp;</span>");
        }
      sb.append("</span>");
      }
    return "<div id=\"wall\" class=\"wall\" data-solution=\"" + Escape.escapeForHtmlText(solution) + "\">" + sb.toString() + "</div>";
    }

  private String renderPile() {
    StringBuilder sb= new StringBuilder();
    for (Brick b : brickPile) { sb.append(b.toHtml()); }
    return sb.toString();
    }

  private record Brick(String code, boolean movable, int index) {
    public int length() { return code.length(); }
    public Brick {
      if (code.length() <= 0) { throw new IllegalArgumentException("Brick cannot be empty!"); }
      }
    public String toHtml() {
      String movableStr = movable ? "movable" : "";
      return "<span class=\"brick %s\">%s</span>".formatted(movableStr, Escape.escapeForHtmlText(code));
      }
    }
}