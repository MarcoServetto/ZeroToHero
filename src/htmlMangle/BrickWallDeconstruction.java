package htmlMangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mainZeroToHero.Days;
import resources.File;

public class BrickWallDeconstruction extends BrickWall {
  private List<String> checkpoints= new ArrayList<>();
  private List<String[]> wallPermutations= new ArrayList<>();
  
  public BrickWallDeconstruction(Days.LevelName name) {
    super(name, "");
    }
  public BrickWallDeconstruction checkpoint(String checkpoint) {
    checkpoints.add(checkpoint);
    return this;
    }
  public BrickWallDeconstruction newRow() {
    super.newRow();
    return this;
    }
  public BrickWallDeconstruction addToPile(int indexSkip, boolean movable, String s) {
    super.addToPile(indexSkip, movable, s);
    return this;
    }
  public BrickWallDeconstruction addBrick(int indexSkip, boolean movable, String s) {
    super.addBrick(indexSkip, movable, s);
    if (indexSkip > 0) { wallPermutations.add(new String[]{" ".repeat(indexSkip)}); }
    wallPermutations.add(new String[]{s});
    return this;
    }
  public BrickWallDeconstruction addMovable(int indexSkip, String s) {
    super.addMovable(indexSkip, s);
    return this;
    }
  public BrickWallDeconstruction addImmovable(int indexSkip, String s) {
    super.addImmovable(indexSkip, s);
    return this;
    }
  public BrickWallDeconstruction addReplaceable(int indexSkip, String from, String... to) {
    super.addBrick(indexSkip, true, from);
    String[] permutations= new String[to.length + 1];
    permutations[0] = from;
    System.arraycopy(to, 0, permutations, 1, to.length);
    wallPermutations.add(permutations);
    return this;
    }
  public BrickWallDeconstruction makeReplaceable(int lastBricks, String... to) {
    String[] newPermutation = new String[to.length + 1];
    newPermutation[0] = "";
    List<String[]> subPermutations = wallPermutations.subList(wallPermutations.size() - lastBricks, wallPermutations.size());
    for (String[] subPermutation : subPermutations) {
      newPermutation[0] += subPermutation[0];
      }
    subPermutations.clear();
    System.arraycopy(to, 0, newPermutation, 1, to.length);
    subPermutations.add(newPermutation);
    return this;
    }
  public String build() {
    checkpoints.addAll(Arrays.asList(wallPermutations()));
    String wallString= renderWall(wallRows, wallLength, true);
    String pileString= renderWall(pileRows, pileLength, false);
    String checkpointsString= Arrays.stream(wallPermutations())
      .map(Escape::escapeForHtmlScripts)
      .collect(Collectors.joining("\", \"", "<script>globalThis.checkpoints = [\"", "\"];</script>"));
    return name.htmlNextLevel(File.BrickWallDeconstruction_html.text)
      .replace("[###WALL###]", wallString)
      .replace("[###PILE###]", pileString)
      .replace("[###CHECKPOINTS###]", checkpointsString);
    }
  
  private String[] wallPermutations() {
    List<String> results= new ArrayList<>();
    results.add("");
    for (String[] options : wallPermutations) {
      List<String> next= new ArrayList<>();
      for (String result : results) {
        for (String option : options) {
          next.add(result + option);
          }
        }
        results = next;
      }
    return results.toArray(new String[0]);
    }
}
