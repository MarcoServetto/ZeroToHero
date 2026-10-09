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
  private String background;
  
  public BrickWall(Days.LevelName name, String solution) {
    this.name= name;
    this.solution= solution;
    wallRows.add(currentWallRowBricks);
    pileRows.add(currentPileRowBricks);
    }
  /**
   * Add a brick to the pile.
   * @param indexSkip - number of spaces to skip along the wall
   * @param movable - whether the brick can be moved around
   * @param s - the text on this brick
   * @return
   */
  public BrickWall addToPile(int indexSkip, boolean movable, String s) {
    currentIndexAlongPileRow += indexSkip;
    currentPileRowBricks.add(new Brick(s, movable, currentIndexAlongPileRow));
    return this;
    }
  /**
   * Create a new row in both the pile and wall.
   * @return
   */
  public BrickWall newRow() {
    currentWallRowBricks = new ArrayList<>();
    currentPileRowBricks = new ArrayList<>();
    wallRows.add(currentWallRowBricks);
    pileRows.add(currentPileRowBricks);
    currentIndexAlongWallRow = 0;
    currentIndexAlongPileRow = 0;
    return this;
    }
  /**
   * Add a brick to the wall. See {@link #addToPile(int, boolean, String)}.
   * @param indexSkip
   * @param movable
   * @param s
   * @return
   */
  public BrickWall addBrick(int indexSkip, boolean movable, String s) {
    currentIndexAlongWallRow += indexSkip;
    currentWallRowBricks.add(new Brick(s, movable, currentIndexAlongWallRow));
    currentIndexAlongWallRow += s.length();
    return this;
    }
  /**
   * Equivalent to {@link #addBrick(indexSkip, false, s)}.
   * @param indexSkip
   * @param s
   * @return
   */
  public BrickWall addImmovable(int indexSkip, String s) { return addBrick(indexSkip, false, s); }
  /**
   * Equivalent to {@link #addBrick(indexSkip, true, s)}.
   * @param indexSkip
   * @param s
   * @return
   */
  public BrickWall addMovable(int indexSkip, String s) { return addBrick(indexSkip, true, s); }
  public BrickWall background(Background kind, int num) {
    if (num <= 0 || num > kind.limit) { throw new Error(kind+" num must be in the 1.."+kind.limit+" range"); }
    this.background= kind.loc+"/"+kind.loc+num+".png";
    return this;
    }
  public String build() {
    return name.htmlNextLevel(File.BrickWall_html.text)
      .replace("[###BACKGROUNDFILE###]", background)
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

  protected String background() { return background; }
  public enum Background {
    Prairie("Prairie",1);
    String loc;
    int limit; // Images are resources/brickWallBackgrounds/LOC/LOC1.png .. LOC<limit>.png
    Background(String loc, int limit) { this.loc= loc; this.limit= limit; }
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