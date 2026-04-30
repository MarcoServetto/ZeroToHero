package htmlMangle;

import java.util.Set;
import java.util.stream.Collectors;

import resources.File;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The Forest mini-game has nodes connected by a path.
 * Travel between nodes is only permitted if there exists a path connecting them.
 * Each node contains a block of code that is collected on arrival.
 * The puzzle can be completed if the code blocks are collected in the right order,
 *  so that the resulting code is valid and correctly answers the question.
 */
public class Forest {
  private final Map<Position, ForestNode> nodes= new HashMap<>();
  private final Set<ForestNodeConnection> connections= new HashSet<>();
  private final String initialCode;
  private final String solution;
  private static final int SIZE_MULTIPLIER= 1;
  
  public Forest(String initialCode, String solution) { 
    this.initialCode= initialCode; this.solution= solution; 
    }
  /**
   * Add a node to the Forest. The first node added to the Forest becomes the starting node.
   * @param x position of the node.
   * @param y position of the node.
   * @return this
   */
  public Forest addNode(int x, int y) {
    x *= SIZE_MULTIPLIER;
    y *= SIZE_MULTIPLIER;
	  Position p= new Position(x, y);
    nodes.put(p, new ForestNode(p));
    return this;
    }
  
  public Forest addDirected(int x1, int y1, int x2, int y2, String code) {
    x1 *= SIZE_MULTIPLIER;
    y1 *= SIZE_MULTIPLIER;
    x2 *= SIZE_MULTIPLIER;
    y2 *= SIZE_MULTIPLIER;
  	ForestNode from= nodes.get(new Position(x1, y1));
  	if (from == null) throw new IllegalArgumentException("The 'from' node doesn't exist.");
  	ForestNode to= nodes.get(new Position(x2, y2));
  	if (to == null) throw new IllegalArgumentException("The 'to' node doesn't exist.");
    connections.add(new ForestNodeConnection(from, to, code));
    return this;
    }
  
  public Forest addUndirected(int x1, int y1, int x2, int y2, String code) {
  	return addDirected(x1, y1, x2, y2, code).addDirected(x2, y2, x1, y1, code);
    }
  
  public String build() {
    String nodesHtml = nodes.values().stream()
      .map(ForestNode::build)
      .collect(Collectors.joining("\n"));
    Map<String, List<ForestNodeConnection>> connectionGroups= connections.stream()
      .collect(Collectors.groupingBy(
        conn -> {
          Position a = conn.from().position();
	        Position b = conn.to().position();
	        // normalize order so A-B == B-A
	        if (a.x() < b.x() || (a.x() == b.x() && a.y() <= b.y())) {
	          return a.x() + "," + a.y() + "-" + b.x() + "," + b.y();
	        } else {
	          return b.x() + "," + b.y() + "-" + a.x() + "," + a.y();
	        }
	      }
      ));
    StringBuilder pathsHtml= new StringBuilder();
    Collection<List<ForestNodeConnection>> connections= connectionGroups.values();
    //System.out.println(connections);
    for (List<ForestNodeConnection> conns : connections) {
      for (int i = 0; i < conns.size(); i++) {
        ForestNodeConnection c = conns.get(i);
        pathsHtml.append(drawPath(c.code(), c.from(), c.to(), conns.size(), i));
        }
      }
    String outputBoxHtml = """
      <textarea class="overlayTextarea" id="output"
			style="top:0%%;left:70.00%%;width:30%%;height:70.00%%;"
			name="ForestOutputBox"
			data-solution="%s"
			data-original="%s"
			data-alternative=""
			wrap="soft"
			autocomplete="off" spellcheck="false" autocorrect="off" autocapitalize="off" readonly>%s</textarea>
      """.formatted(solution, initialCode, initialCode);
    return File.Forest_html.text
      .replace("[###PATHS###]", pathsHtml.toString())
      .replace("[###BODY###]", nodesHtml)
      .replace("[###OUTPUT###]", outputBoxHtml);
    }
  
  private String drawPath(String code, ForestNode from, ForestNode to, int totalConnections, int index) {
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
      <g class="edge">
        <path class="hitPath" d='m %1$d %2$d Q %3$.2f %4$.2f %5$d %6$d' onclick='travelPath("%7$s", %1$d, %2$d, %3$.2f, %4$.2f, %5$d, %6$d)' />
        <path class='path' d='m %1$d %2$d Q %3$.2f %4$.2f %5$d %6$d' stroke-dasharray="4 4"/>
      </g>
      """, x1, y1, mx, my, x2, y2, code
      );
    }
  
  }

record ForestNode(Position position) {
  public String build() {
    return """
        <circle 
          cx="%d" cy="%d" 
          r="2" 
          fill="red"
        />
      """.formatted(position.x(), position.y());
    }
  }
record ForestNodeConnection(ForestNode from, ForestNode to, String code) {}
record Position(int x, int y) {}