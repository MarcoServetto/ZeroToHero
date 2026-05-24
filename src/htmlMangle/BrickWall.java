package htmlMangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.Days;

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
  private final List<Brick> brickPile= new ArrayList<>();
  private final List<Row> brickRows= new ArrayList<>();
  
  public BrickWall(Days.LevelName name) {
    
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
    return "";
    }
  
  public record Row(int length, List<PlacedBrick> placedBricks) {
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

  record PlacedBrick(Brick brick, int index) {}
  
  public record Brick(int length, String code, boolean moveable) {
    static public Brick movable(int length, String code) { return new Brick(length, code, true); }
    static public Brick movable(int length) { return new Brick(length, "", true); }
    static public Brick movable(String code) { return new Brick(code.length(), code, true); }
    
    static public Brick immovable(int length, String code) { return new Brick(length, code, false); }
    static public Brick immovable(int length) { return new Brick(length, "", false); }
    static public Brick immovable(String code) { return new Brick(code.length(), code, false); }
    
    public Brick {
      if (length < code.length()) { // The brick has to be longer than the code to fit it
        throw new IllegalArgumentException("Brick with code `" + code + "` requires at least length " + code.length());
        }
      }
    }
  }