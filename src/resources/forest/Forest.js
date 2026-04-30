const resetBtn= document.getElementById('resetBtn');
const submitBtn= document.getElementById('submitBtn');
const undoBtn= document.getElementById('undoBtn');
const currentNodeMarker= document.getElementById('currentNodeMarker');
const currentTravelingPath= document.getElementById('currentTravelingPath');

resetBtn.addEventListener('click', () => {
  location.reload();
  });

submitBtn.addEventListener('click', () => {
  if (currentCode === solutionCode) onComplete();
  else onFail();
  });

undoBtn.addEventListener('click', () => {
  location.reload();
  });

const onComplete= () => {
  console.log("Done");
}
const onFail= () => {
  console.log("Incorrect")
}

const updateCurrentNodeMarkerLocation= (x, y) => {
  currentNodeMarker.setAttribute("x", x - 5);
  currentNodeMarker.setAttribute("y", y - 5);
}

const nodesRaw= document.querySelectorAll("circle");
const pathsRaw= document.querySelectorAll("path");

class Node {
  constructor(x, y) {
    this.x= x;
    this.y= y;
    }
  equals(other) {
    return this.x === other.x && this.y === other.y;
    }
  }
class Path {
  constructor(code, x1, y1, x2, y2) {
	this.code= code;
	this.n1= new Node(x1, y1);
	this.n2= new Node(x2, y2);
    }
}

const nodes= Array.from(nodesRaw).map(c => new Node(c.cx.baseVal.value, c.cy.baseVal.value));
var currentNode= nodes[0];
updateCurrentNodeMarkerLocation(currentNode.x, currentNode.y);

var output= document.getElementById("output");
var currentCode= output.getAttribute("data-original");
const solutionCode= output.getAttribute("data-solution");
  
const travelFail= (n1, n2) => { console.log("Cannot travel between ", n1, n2); }
  
const travelPath= (code, x1, y1, mx, my, x2, y2) => {
  const n1= new Node(x1, y1);
  const n2= new Node(x2, y2);
  if (!(currentNode.equals(n1) || currentNode.equals(n2))) {
	travelFail(n1, n2);
	return;
    }
  var otherNode;
  if (currentNode.equals(n1)) {
	currentNode = n2;
	otherNode= n1;
  } else {
	currentNode = n1;
	otherNode= n2;
  }
  output.value = currentCode += code;
  animateTravelPath(otherNode.x, otherNode.y, mx, my, currentNode.x, currentNode.y); // It's backwards somehow :/
  updateCurrentNodeMarkerLocation(currentNode.x, currentNode.y);
}

const animateTravelPath= (x1, y1, mx, my, x2, y2) => {
	const d = `M ${x1} ${y1} Q ${mx} ${my} ${x2} ${y2}`;

	  currentTravelingPath.setAttribute("d", d);

	  const length = currentTravelingPath.getTotalLength();

	  currentTravelingPath.style.transition = "none";
	  currentTravelingPath.style.strokeDasharray = length;
	  currentTravelingPath.style.strokeDashoffset = length;

	  // Force reflow
	  currentTravelingPath.getBoundingClientRect();

	  currentTravelingPath.style.transition = "stroke-dashoffset 0.6s ease";
	  currentTravelingPath.style.strokeDashoffset = "0";
}
currentTravelingPath.addEventListener("transitionend", (e) => {
  if (e.propertyName === "stroke-dashoffset") {
    currentTravelingPath.setAttribute("d", "");
    }
});