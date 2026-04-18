const resetBtn= document.getElementById('resetBtn');
const submitBtn= document.getElementById('submitBtn');
const undoBtn= document.getElementById('undoBtn');
resetBtn.addEventListener('click', () => {
  location.reload();
  });

submitBtn.addEventListener('click', () => {
  location.reload();
  })

undoBtn.addEventListener('click', () => {
  location.reload();
  })

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
  constructor(x1, y1, x2, y2) {
	this.n1= new Node(x1, y1);
	this.n2= new Node(x2, y2);
    }
}

const nodes = Array.from(nodesRaw).map(c => new Node(c.cx.baseVal.value, c.cy.baseVal.value));
var currentNode = nodes[0];
  
const travelFail= (n1, n2) => { console.log("Cannot travel between ", n1, n2); }
  
const travelPath= (x1, y1, x2, y2) => {
  const n1= new Node(x1, y1);
  const n2= new Node(x2, y2);
  if (currentNode.equals(n1)) {
	currentNode = n2;
  } else if (currentNode.equals(n2)) {
	currentNode = n1;
  } else {
	travelFail(n1, n2);
  }
}