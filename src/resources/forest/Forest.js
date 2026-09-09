"use strict";

// Forest Minigame Settings
const travelSpeed= 500; // Constant speed along a path
const maxLineLength= 94; // Maximum amount of characters for each line in the output box
const textOutputSpeed= 50; // ms per character to appear

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
class Action {
  constructor(node, code) {
    this.node= node;
    this.code= code;
    }
  }

const currentNodeMarker= Utils.getElementById("currentNodeMarker");
const currentTravelingPath= Utils.getElementById("currentTravelingPath");
const markerWidth= currentNodeMarker.getAttribute("width");
const markerHeight= currentNodeMarker.getAttribute("height");

const output= Utils.getElementById("output");
let currentCode= MetaData.str(output, "original");
const solutionCode= MetaData.str(output, "solution");

const nodesRaw= document.querySelectorAll("circle");
const finishNodesRaw= document.getElementsByClassName("finishNode");
const pathsRaw= document.querySelectorAll("path");

// The code box shown above everything else when an 'edge' is hovered over
const foreignObjectCodeBox= Utils.getElementById("foreignObjectCodeBox");
const codeBoxOverlayTop= Utils.getElementById("codeBoxOverlayTop");
const edges= document.getElementsByClassName("edge");
const codeBoxEdges= document.getElementsByClassName("codeBoxEdge");

// Map each HTML node to Javascript node.
const normalNodes= Array.from(nodesRaw).map(c => new Node(c.cx.baseVal.value, c.cy.baseVal.value));
const finishNodes= Array.from(finishNodesRaw).map(c => new Node(c.cx.baseVal.value, c.cy.baseVal.value));
const nodes= normalNodes.concat(finishNodes);

let interactionEnabled= true;
let currentNode= nodes[0]; // The node the player is currently on
const actionStack= [];

let textToAnimate= ""; // Remaining characters to add to output

const getOutputText= () => {
  return output.textContent;
  };
const setOutputText= text => {
  output.innerHTML = text;
  };

const submit= () => {
  if (!onFinishNode()) { return; }
  textToAnimate = "";
  setOutputText(currentCode);
  const freezeToken= Buttons.freezeToken();
  if (currentCode === solutionCode) { onComplete(); }
  else { onFail(freezeToken); }
  }
const undo= () => {
  if (actionStack.length === 0 || !interactionEnabled) { return; }
  clearInterval(animateTextInterval);
  textToAnimate = "";
  const action= actionStack.pop();
  currentNode = action.node;
  currentCode = action.code;
  hintChar.hidden = true; // Hide Panic
  updateVisuals();
  animateTextInterval = setInterval(animateText, textOutputSpeed);
  }

let panicToHideId= null;
const hintChar= document.getElementById("hintCharacter");
const displayPanicMessage= (msg) => {
  clearTimeout(panicToHideId);
  const speechBubble= hintChar.querySelector(".speechBubble");
  speechBubble.textContent = msg;
  hintChar.hidden = false;
  };

const buttonActions= {
  submitBtn: submit,
  resetBtn: () => location.reload(),
  undoBtn: undo
  };
const Buttons= initButtons(() => {}, buttonActions);

// Pair each edge with its code box via the shared data-edge id, so hovering
// either one highlights both and shows the top overlay code box.
const edgeById= new Map();
Array.from(edges).forEach(edge => {
  edgeById.set(edge.getAttribute("data-edge"), edge);
  });

const showCodeBoxOverlay= (codeBoxEdge) => {
  const box= codeBoxEdge.querySelector(".overlayTextarea");
  foreignObjectCodeBox.setAttribute("x", codeBoxEdge.getAttribute("x"));
  foreignObjectCodeBox.setAttribute("y", codeBoxEdge.getAttribute("y"));
  foreignObjectCodeBox.setAttribute("width", codeBoxEdge.getAttribute("width"));
  foreignObjectCodeBox.setAttribute("height", codeBoxEdge.getAttribute("height"));
  foreignObjectCodeBox.setAttribute("opacity", 1);
  codeBoxOverlayTop.textContent = box.textContent;
  };

const setHovered= (edgeId, hovered) => {
  const edge= edgeById.get(edgeId);
  const codeBoxEdge= Array.from(codeBoxEdges).find(cb => cb.getAttribute("data-edge") === edgeId);
  if (edge) { edge.classList.toggle("hovered", hovered); }
  if (codeBoxEdge) {
    codeBoxEdge.classList.toggle("hovered", hovered);
    if (hovered) { showCodeBoxOverlay(codeBoxEdge); }
    }
  if (!hovered) { foreignObjectCodeBox.setAttribute("opacity", 0); }
};

Array.from(edges).forEach(edge => {
  const edgeId= edge.getAttribute("data-edge");
  edge.addEventListener("mouseenter", () => setHovered(edgeId, true));
  edge.addEventListener("mouseleave", () => setHovered(edgeId, false));
  });
Array.from(codeBoxEdges).forEach(codeBoxEdge => {
  const edgeId= codeBoxEdge.getAttribute("data-edge");
  codeBoxEdge.addEventListener("mouseenter", () => setHovered(edgeId, true));
  codeBoxEdge.addEventListener("mouseleave", () => setHovered(edgeId, false));
  });

const checkOverLength= (str) => {
  const lines= str.split("\n");
  return lines.some(line => line.length > maxLineLength);
  }

const onComplete= () => {
  Utils.flashImage("rgba(0, 250, 0, 0.5)","levelEndCharacter","translateY(-5%)");
  const nextLevelUrl= MetaData.str(document.body, "next");
  Utils.checkExists(nextLevelUrl);
  setTimeout(() => window.location.href = nextLevelUrl, 5000);
  }
const onFail= (freezeToken) => {
  showIncorrect();
  output.classList.add("incorrectGlow");
  Utils.flashImage("rgba(250, 0, 0, 0.5)","levelFail","translateY(-5%)");
  setTimeout(() => freezeToken.unfreeze(), 3000);
  }

const updateCurrentNodeMarkerLocation= (x, y) => {
  currentNodeMarker.setAttribute("x", x - markerWidth/2);
  currentNodeMarker.setAttribute("y", y - markerHeight/2);
  }

const onFinishNode= () => { return finishNodes.some(n => n.equals(currentNode)); }
const travelFail= (n1, n2) => { console.log("Cannot travel between ", n1, n2); }
const travelPath= (edgeId, x1, y1, mx, my, x2, y2) => {
  if (!interactionEnabled) { return; }
  if (checkOverLength(getOutputText())) { return; }
  const n1= new Node(x1, y1);
  const n2= new Node(x2, y2);
  if (!(currentNode.equals(n1) || currentNode.equals(n2))) {
    travelFail(n1, n2);
    return;
    }
  actionStack.push(new Action(currentNode, currentCode));
  interactionEnabled = false;
  let otherNode;
  if (currentNode.equals(n1)) {
    currentNode = n2;
    otherNode= n1;
    } else {
    currentNode = n1;
    otherNode= n2;
    }
  submitBtn.disabled = !onFinishNode();
  const code= Utils.getElementById(edgeId).textContent;
  currentCode += code
  textToAnimate += code;
  animateTravelPath(otherNode.x, otherNode.y, mx, my, currentNode.x, currentNode.y); // It's backwards somehow :/
  }

const animateTravelPath= (x1, y1, mx, my, x2, y2) => {
  const d= `M ${x1} ${y1} Q ${mx} ${my} ${x2} ${y2}`;
  currentTravelingPath.setAttribute("d", d);

  const length= currentTravelingPath.getTotalLength();
  const start= performance.now();

  const step= (t) => {
    const progress= Math.min((t - start) / (length*1000/travelSpeed), 1);
    const point= currentTravelingPath.getPointAtLength(length * progress);
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
  setOutputText(currentCode);
  submitBtn.disabled = !onFinishNode();
  if (!checkOverLength(getOutputText())) { output.classList.remove("incorrectGlow"); }
  }

const showIncorrect= () => {
  const currentOutput= getOutputText();
  let incorrectIndex= -1;
  const len = Math.max(currentOutput.length, solutionCode.length);
  for (let i = 0; i < len; i++) {
    if (currentOutput[i] !== solutionCode[i]) {
      incorrectIndex = i;
      break;
      }
    }
  const rightText= currentOutput.slice(0, incorrectIndex);
  const wrongText= currentOutput.slice(incorrectIndex, len);
  setOutputText(rightText + `<span class="redHighlight">${escapeHtml(wrongText)}</span>`);
  }
const escapeHtml= str => {
  return str.replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
  }

let alreadyShowIncorrect= false;
const animateText= () => {
  if (textToAnimate.length === 0) {
	if (!alreadyShowIncorrect && checkOverLength(getOutputText())) {
      displayPanicMessage("We've picked up too much! Try the Undo button.");
      output.classList.add("incorrectGlow");
      showIncorrect();
	  alreadyShowIncorrect = true;
      }
    return;
    }
  alreadyShowIncorrect = false;
  const first= textToAnimate[0];
  textToAnimate = textToAnimate.slice(1);
  const current= getOutputText();
  setOutputText(current + first);
  };

let animateTextInterval= setInterval(animateText, textOutputSpeed);

updateVisuals();