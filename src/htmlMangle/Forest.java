package htmlMangle;

import java.util.Set;
import java.util.stream.Collectors;

import resources.File;

import java.util.HashMap;
import java.util.HashSet;
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
  private final String solution;
  
  public Forest(String solution) { this.solution= solution; }
  
  public Forest addNode(int x, int y, String code) {
	Position p= new Position(x, y);
    nodes.put(p, new ForestNode(p, code));
    return this;
    }
  
  public Forest addDirected(int x1, int y1, int x2, int y2) {
	ForestNode from= nodes.get(new Position(x1, y1));
	if (from == null) throw new IllegalArgumentException("The 'from' node doesn't exist.");
	ForestNode to= nodes.get(new Position(x2, y2));
	if (to == null) throw new IllegalArgumentException("The 'to' node doesn't exist.");
    connections.add(new ForestNodeConnection(from, to));
    return this;
    }
  public Forest addUndirected(int x1, int y1, int x2, int y2) {
	return addDirected(x1, y1, x2, y2).addDirected(x2, y2, x1, y1);
	}
  
  public String build() {
	String nodesHtml = nodes.values().stream()
	  .map(ForestNode::build)
	  .collect(Collectors.joining("\n"));
	String connectionsHtml = connections.stream()
      .map(ForestNodeConnection::build)
	  .collect(Collectors.joining("\n"));
	return File.Forest_html.text
      .replace("[###BODY###]", nodesHtml);
    }
}

record ForestNode(Position position, String code) {
  public String build() {
	return """
      <div class="node"
       style="left:%d%%; top:%d%%;"
       onclick="onNodeClick(this)"
       data-code="%s">
         <div class="tooltip">%s</div>
      </div>
    <script>
      function onNodeClick(el) {
        console.log(el.dataset.code);
      }
    </script>
      
      """.formatted(position.x(), position.y(), code, code);
  }
}
record ForestNodeConnection(ForestNode from, ForestNode to) {
  public String build() {
	return "";
  }
}
record Position(int x, int y) {}