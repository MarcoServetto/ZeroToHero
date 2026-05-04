'use strict';

// Forest Minigame Settings
const TRAVEL_SPEED= 0.75; // Seconds


const resetBtn= document.getElementById('resetBtn');
const submitBtn= document.getElementById('submitBtn');
const undoBtn= document.getElementById('undoBtn');
const currentNodeMarker= document.getElementById('currentNodeMarker');
const currentTravelingPath= document.getElementById('currentTravelingPath');
var interactionEnabled= true;

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
  const nextLevelUrl = MetaData.str(document.body, 'next');
  Utils.checkExists(nextLevelUrl);
  setTimeout(() => window.location.href = nextLevelUrl, 1000);
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
  if (!interactionEnabled) { return; }
  const n1= new Node(x1, y1);
  const n2= new Node(x2, y2);
  if (!(currentNode.equals(n1) || currentNode.equals(n2))) {
	travelFail(n1, n2);
	return;
    }
  interactionEnabled = false;
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
}

const animateTravelPath= (x1, y1, mx, my, x2, y2) => {
  const d = `M ${x1} ${y1} Q ${mx} ${my} ${x2} ${y2}`;
  currentTravelingPath.setAttribute("d", d);

  const length = currentTravelingPath.getTotalLength();
  const start= performance.now();

  const step= (t) => {
    const progress = Math.min((t - start) / (TRAVEL_SPEED*1000), 1);
    const point = currentTravelingPath.getPointAtLength(length * progress);
    updateCurrentNodeMarkerLocation(point.x, point.y);
    if (progress < 1) {
      requestAnimationFrame(step);
      } else {
		interactionEnabled = true;
		currentTravelingPath.setAttribute("d", "");
	  }
    };

  requestAnimationFrame(step);
  }