"use strict";

// Forest Minigame Settings
const TRAVEL_SPEED= 0.75; // Seconds between two nodes


class Node {
  constructor(x, y) {
    this.x= x;
    this.y= y;
    }
  equals(other) {
    return this.x === other.x && this.y === other.y;
    }
  }
class FinishNode extends Node {}
class Path {
  constructor(code, x1, y1, x2, y2) {
	this.code= code;
	this.n1= new Node(x1, y1);
	this.n2= new Node(x2, y2);
    }
  }
class Action {
  constructor(node, code) {
    this.node= node;
    this.code= code;
    }
  }

const resetBtn= Utils.getElementById("resetBtn");
const submitBtn= Utils.getElementById("submitBtn");
const undoBtn= Utils.getElementById("undoBtn");
const currentNodeMarker= Utils.getElementById("currentNodeMarker");
const currentTravelingPath= Utils.getElementById("currentTravelingPath");

var output= Utils.getElementById("output");
var currentCode= MetaData.str(output, "original");
const solutionCode= MetaData.str(output, "solution");

const nodesRaw= document.querySelectorAll("circle");
const finishNodesRaw= document.getElementsByClassName("finishNode");
const pathsRaw= document.querySelectorAll("path");

// The code box shown above everything else when an 'edge' is hovered over
const foreignObjectCodeBox= Utils.getElementById("foreignObjectCodeBox");
const codeBoxOverlayTop= Utils.getElementById("codeBoxOverlayTop");
const edges= document.getElementsByClassName("edge");

// Map each HTML node to Javascript node.
const normalNodes= Array.from(nodesRaw).map(c => new Node(c.cx.baseVal.value, c.cy.baseVal.value));
const finishNodes= Array.from(finishNodesRaw).map(c => new FinishNode(c.cx.baseVal.value, c.cy.baseVal.value));
const nodes= normalNodes.concat(finishNodes);

Array.from(edges).forEach(edge => {
  edge.addEventListener("mouseenter", () => {
	const foreignObject= edge.querySelector("foreignObject");
	const codeBox= edge.querySelector(".overlayTextarea");
	foreignObjectCodeBox.setAttribute("opacity", 1);
	foreignObjectCodeBox.setAttribute("x", foreignObject.getAttribute("x"));
	foreignObjectCodeBox.setAttribute("y", foreignObject.getAttribute("y"));
	foreignObjectCodeBox.setAttribute("width", foreignObject.getAttribute("width"));
	foreignObjectCodeBox.setAttribute("height", foreignObject.getAttribute("height"));
	codeBoxOverlayTop.value = codeBox.value;
  });
  edge.addEventListener("mouseleave", () => {
	foreignObjectCodeBox.setAttribute("opacity", 0);
  })
});

resetBtn.addEventListener("click", () => {
  location.reload();
  });

submitBtn.addEventListener("click", () => {
  if (!interactionEnabled) { return; }
  if (!onFinishNode()) { return; }
  if (currentCode === solutionCode) onComplete();
  else onFail();
  });

undoBtn.addEventListener("click", () => {
  if (actionStack.length === 0 || !interactionEnabled) { return; }
  const action= actionStack.pop();
  currentNode = action.node;
  currentCode = action.code;
  updateVisuals();
  });

const onComplete= () => {
  interactionEnabled = false;
  Utils.flashImage("rgba(0, 250, 0, 0.5)","levelEndCharacter","translateY(-5%)");
  const nextLevelUrl= MetaData.str(document.body, "next");
  Utils.checkExists(nextLevelUrl);
  setTimeout(() => window.location.href = nextLevelUrl, 5000);
  }
const onFail= () => {
  console.log("Incorrect")
  }

const updateCurrentNodeMarkerLocation= (x, y) => {
  currentNodeMarker.setAttribute("x", x - 5);
  currentNodeMarker.setAttribute("y", y - 5);
  }

var interactionEnabled= true;
var currentNode= nodes[0]; // The node the player is currently on
const actionStack= [];

updateCurrentNodeMarkerLocation(currentNode.x, currentNode.y);

const onFinishNode= () => { return finishNodes.some(n => n.equals(currentNode)); }
const travelFail= (n1, n2) => { console.log("Cannot travel between ", n1, n2); }
const travelPath= (edgeId, x1, y1, mx, my, x2, y2) => {
  const code= Utils.getElementById(edgeId).value;
  if (!interactionEnabled) { return; }
  const n1= new Node(x1, y1);
  const n2= new Node(x2, y2);
  if (!(currentNode.equals(n1) || currentNode.equals(n2))) {
	travelFail(n1, n2);
	return;
    }
  actionStack.push(new Action(currentNode, currentCode));
  interactionEnabled = false;
  var otherNode;
  if (currentNode.equals(n1)) {
	currentNode = n2;
	otherNode= n1;
    } else {
	currentNode = n1;
	otherNode= n2;
    }
  submitBtn.disabled = !onFinishNode();
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

const updateVisuals= () => {
  updateCurrentNodeMarkerLocation(currentNode.x, currentNode.y);
  output.value = currentCode;
  }

submitBtn.disabled = !onFinishNode();