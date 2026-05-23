package htmlMangle;

import java.util.Set;
import java.util.stream.Collectors;

import main.Days;
import resources.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Forest mini-game has nodes connected by a path.
 * Travel between nodes is only permitted if there exists a path connecting them.
 * Each path contains a block of code that is collected on traversal.
 * The puzzle can be completed if the code blocks are collected in the right order,
 *  so that the resulting code is valid and correctly answers the question.
 */
public class Forest {
  private static final float SIZE_MULTIPLIER= 10f;
  private static final float DEFAULT_CODE_BOX_WIDTH= 25;
  private static final float DEFAULT_CODE_BOX_HEIGHT= 4;
  private final Map<Position, Node> nodes= new LinkedHashMap<>();
  private final Set<ForestNodeConnection> connections= new LinkedHashSet<>();
  private final String initialCode;
  private final String solution;
  private final Days.LevelName name;
  private Background background= Background.DAWN;
  
  public Forest(Days.LevelName name, String initialCode, String solution) {
    this.name= name;
    this.initialCode= initialCode;
    this.solution= solution; 
    }
  /**
   * Add a node to the Forest. The first node added to the Forest becomes the starting node.
   * @param x position of the node.
   * @param y position of the node.
   * @return this
   */
  public Forest addNode(float x, float y) {
    return addNode(x, y, "", "red");
    }
  public Forest addFinishNode(float x, float y) {
    return addNode(x, y, "finishNode", "lightgreen");
    }
  private Forest addNode(float x, float y, String className, String colour) {
    x *= SIZE_MULTIPLIER;
    y *= SIZE_MULTIPLIER;
    Position p= new Position((int)x, (int)y);
    nodes.put(p, new Node(p, className, colour));
    return this;
    }
  
  /**
   * Add a connection between two nodes and the position of the box to display the code.
   * The order of the position of the two nodes matters and you should not add a two-way connection.
   * @param n1 Index of the first node.
   * @param n2 Index of the second node.
   * @param code Code associated with this edge
   * @param bx Box's top-left corner x-position
   * @param by Box's top-left corner y-position
   * @param bw Box's width
   * @param bh Box's height
   * @return this
   */
  public Forest connect(int n1, int n2, String code, int bx, int by, int bw, int bh) {
    int nodesSize= nodes.size();
    if (nodesSize < n1) { throw new IllegalArgumentException("The index of first node is invalid."); }
    if (nodesSize < n2) { throw new IllegalArgumentException("The index of second node is invalid."); }
    if (n2 < n1) { // Swap them around
      int temp= n1;
      n1 = n2;
      n2 = temp;
      }
    connections.add(new ForestNodeConnection(n1, n2, code, bx, by, bw, bh));
    return this;
    }
  public Forest connect(int n1, int n2, String code, int bx, int by, int bw) {
    return connect(n1, n2, code, bx, by, bw, (int)DEFAULT_CODE_BOX_HEIGHT);
    }
  public Forest connect(int n1, int n2, String code, int bx, int by) {
    return connect(n1, n2, code, bx, by, (int)DEFAULT_CODE_BOX_WIDTH);
    }
  public Forest background(Background background) {
    this.background = background;
    return this;
    }
  
  public String build() {
    return name.htmlNextLevel(File.Forest_html.text)
      .replace("[###BACKGROUNDFILE###]", background.filename())
      .replace("[###PATHS###]", pathsHtml())
      .replace("[###BODY###]", nodesHtml())
      .replace("[###OUTPUT###]", outputBoxHtml());
    }
  private String nodesHtml() {
    return nodes.values().stream()
      .map(Node::build)
      .collect(Collectors.joining("\n"));
    }
  private String pathsHtml() {
    List<Node> forestNodesOrdered= new ArrayList<>(nodes.values());
    Map<String, List<ForestNodeConnection>> connectionGroups= connections.stream()
      .collect(Collectors.groupingBy(conn -> conn.fromIndex() + "," + conn.toIndex()));
    StringBuilder pathsHtml= new StringBuilder();
    Collection<List<ForestNodeConnection>> connections= connectionGroups.values();
    int id= 0;
    for (List<ForestNodeConnection> conns : connections) {
      for (int i= 0; i < conns.size(); i++) {
        ForestNodeConnection c= conns.get(i);
        Node from= forestNodesOrdered.get(c.fromIndex());
        Node to= forestNodesOrdered.get(c.toIndex());
        pathsHtml.append(drawPath(c.code(), c.x(), c.y(), from, to, conns.size(), i, id++, c.w(), c.h()));
        }
      }
    return pathsHtml.toString();
    }
  private String outputBoxHtml() {
    return """
      <textarea class="overlayTextarea" id="output"
      style="top:0%%;left:60.00%%;width:40%%;height:60.00%%;overflow-x:auto;pointer-events:auto"
      name="ForestOutputBox"
      data-solution="%s"
      data-original="%s"
      autocomplete="off" spellcheck="false" autocorrect="off" autocapitalize="off" readonly>%s</textarea>
      """.formatted(Escape.escapeForHtmlAttribute(initialCode + solution), Escape.escapeForHtmlAttribute(initialCode), Escape.escapeForHtmlAttribute(initialCode));
    }
  
  private String drawPath(String code, int x, int y, Node from, Node to, int totalConnections, int index, int id, int boxWidth, int boxHeight) {
    int x1= from.position().x();
    int y1= from.position().y();
    int x2= to.position().x();
    int y2= to.position().y();

    int dx= x2 - x1;
    int dy= y2 - y1;
    double length= Math.sqrt(dx * dx + dy * dy);

    // perpendicular unit vector
    double nx= -dy / length;
    double ny= dx / length;

    // center index around 0 (e.g. -1, 0, 1)
    double offsetIndex= index - (totalConnections - 1) / 2.0;

    // scale curve strength with number of connections
    double baseCurve= 10.0 * SIZE_MULTIPLIER; // tweak this
    double curveAmount= baseCurve * (1 + totalConnections * 0.5);

    double offset= offsetIndex * curveAmount;

    // control point (midpoint + perpendicular offset)
    double mx= (x1 + x2) / 2 + nx * offset;
    double my= (y1 + y2) / 2 + ny * offset;

    return String.format(
      """
      <g class="edge" onclick='travelPath("edge_%10$d", %1$d, %2$d, %3$.2f, %4$.2f, %5$d, %6$d)'>
        <path class="hitPath" d='m %1$d %2$d Q %3$.2f %4$.2f %5$d %6$d'/>
        <path class='path' d='m %1$d %2$d Q %3$.2f %4$.2f %5$d %6$d' stroke-dasharray="16 16"/>
        <foreignObject x="%8$d" y="%9$d" width="%11$dpx" height="%12$dpx">
          <textarea
            id="edge_%10$d"
            class="overlayTextarea"
            style="top:0%%;left:0%%;width:100%%;height:100%%;font-size:20px;overflow-x:auto;"
            name="ForestCodeBox"
            wrap="soft"
            autocomplete="off"
            spellcheck="false"
            readonly
            >%7$s</textarea>
          </foreignObject>
      </g>
      """, x1, y1, mx, my, x2, y2, Escape.escapeForHtmlAttribute(code), (int)(x * SIZE_MULTIPLIER), (int)(y * SIZE_MULTIPLIER), id, (int)(boxWidth * SIZE_MULTIPLIER), (int)(boxHeight * SIZE_MULTIPLIER)
      );
    }
  
  record Node(Position position, String elementClass, String fill) {
    public String build() {
      return """
        <circle class="%s"
          cx="%d" cy="%d"
          r="15"
          fill="%s"
        />""".formatted(elementClass, position.x(), position.y(), fill);
      }
    }
  public enum Background {
    DAWN("forestDawn.png");
    String filename; // Located in resources/forest/FILENAME
    Background(String filename) { this.filename= filename; }
    String filename() { return filename; }
  }
  }
record ForestNodeConnection(int fromIndex, int toIndex, String code, int x, int y, int w, int h) {}
record Position(int x, int y) {}