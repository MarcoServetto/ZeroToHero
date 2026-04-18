const resetBtn= document.getElementById('resetBtn');
const submitBtn= document.getElementById('submitBtn');
const undoBtn= document.getElementById('undoBtn');
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
console.log("Starting node: ", currentNode);
var output= document.getElementById("output");
var currentCode= output.getAttribute("data-original");
const solutionCode= output.getAttribute("data-solution");
  
const travelFail= (n1, n2) => { console.log("Cannot travel between ", n1, n2); }
  
const travelPath= (code, x1, y1, x2, y2) => {
  const n1= new Node(x1, y1);
  const n2= new Node(x2, y2);
  if (currentNode.equals(n1)) {
	currentNode = n2;
	output.value = currentCode += code;
  } else if (currentNode.equals(n2)) {
	currentNode = n1;
	output.value = currentCode += code;
  } else {
	travelFail(n1, n2);
  }
}