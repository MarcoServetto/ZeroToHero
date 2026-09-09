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
  protected static final int wallLength= 80;
  protected static final int pileLength= 53;
  
  protected final List<List<Brick>> pileRows= new ArrayList<>();
  protected final List<List<Brick>> wallRows= new ArrayList<>();
  private List<Brick> currentWallRowBricks= new ArrayList<>();
  private List<Brick> currentPileRowBricks= new ArrayList<>();
  
  protected final Days.LevelName name;
  private final String solution;
  private int currentIndexAlongWallRow= 0;
  private int currentIndexAlongPileRow= 0;
  
  public BrickWall(Days.LevelName name, String solution) {
    this.name= name;
    this.solution= solution;
    wallRows.add(currentWallRowBricks);
    pileRows.add(currentPileRowBricks);
    }
  public BrickWall addToPile(int indexSkip, boolean movable, String s) {
    currentIndexAlongPileRow += indexSkip;
    currentPileRowBricks.add(new Brick(s, movable, currentIndexAlongPileRow));
    return this;
    }
  public BrickWall newRow() {
    currentWallRowBricks = new ArrayList<>();
    currentPileRowBricks = new ArrayList<>();
    wallRows.add(currentWallRowBricks);
    pileRows.add(currentPileRowBricks);
    currentIndexAlongWallRow = 0;
    currentIndexAlongPileRow = 0;
    return this;
    }
  public BrickWall addBrick(int indexSkip, boolean movable, String s) {
    currentIndexAlongWallRow += indexSkip;
    currentWallRowBricks.add(new Brick(s, movable, currentIndexAlongWallRow));
    currentIndexAlongWallRow += s.length();
    return this;
    }
  public BrickWall addImmovable(int indexSkip, String s) { return addBrick(indexSkip, false, s); }
  public BrickWall addMovable(int indexSkip, String s) { return addBrick(indexSkip, true, s); }
  public String build() {
    return name.htmlNextLevel(File.BrickWall_html.text)
      .replace("[###WALL###]", renderWall(wallRows, wallLength, true))
      .replace("[###PILE###]", renderWall(pileRows, pileLength, false))
      .replace("[###ANSWERWALL###]", renderAnswerWall());
    }

  protected String renderWall(List<List<Brick>> brickRows, int length, boolean isWall) {
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
      for (; currentIndex < length; currentIndex++) {
        sb.append("<span class=\"empty\">&nbsp;</span>");
        }
      sb.append("</span>");
      }
    return !isWall ? "<div id=\"pile\" class=\"brickPile\">" + sb.toString() + "</div>" : 
      "<div id=\"wall\" class=\"wall\" data-solution=\"" + Escape.escapeForHtmlText(solution) + "\">" + sb.toString() + "</div>";
    }
  private String renderAnswerWall() {
    return "<pre id=\"answerWall\" class=\"wall answer hidden\">" + Escape.escapeForHtmlText(solution) + "</pre>";
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