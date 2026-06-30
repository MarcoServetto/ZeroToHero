const wall= Utils.getElementById("wall");
const brickRows= document.querySelectorAll(".brickRow");
const movableBricks= document.querySelectorAll(".brick.movable");
const movingBricksDiv= Utils.getElementById("movingBricks");
const pile= Utils.getElementById("pile");

let dragging= false;
let offsetX= 0;
let offsetY= 0;

const isEmpty= e => { return e.classList.contains("empty"); }

// Empty spans next to each other are combined into one 
const combineEmpty= () => {
  brickRows.forEach(row => {
    const rowChildren= row.children;
	let lastEmpty= null;
    [...rowChildren].forEach(e => {
      if (!isEmpty(e)) {
		lastEmpty = null;
        return;
        }
      if (lastEmpty === null) {
        lastEmpty = e;
        return;
	    }
      e.textContent += lastEmpty.textContent;
      lastEmpty.remove();
      lastEmpty = e;
      });
    });
  };

const setEmpty= e => {
  e.className = "empty";
  e.textContent = "\u00A0".repeat(e.textContent.length);
  e.removeEventListener("pointerdown", registerMovable);
}
const createGhost= e => {
  const ghost= document.createElement("span");
  ghost.textContent = e.textContent;
  ghost.className = "brick movable";
  ghost.style.position = "absolute";
  movingBricksDiv.appendChild(ghost);
  return ghost;
}

document.addEventListener("pointermove", e => {
  if (!dragging) { return; }
  const element = document.elementFromPoint(e.clientX, e.clientY);
  if (!isEmpty(element)) { return; }
});

var ghostBrick= null;
const movableBrickEventListeners= new Map();

const registerMovable= e => {
  const handler= () => {
    dragging = true;
    ghostBrick = createGhost(e);
    if (e.parentElement !== pile) {
      setEmpty(e);
	  } else { e.remove(); }
    combineEmpty();
    };
  e.addEventListener("pointerdown", handler);
  movableBrickEventListeners.set(e, handler);
  }

movableBricks.forEach(b => {
  registerMovable(b);
  });

document.addEventListener("pointermove", e => {
  if (ghostBrick === null) { return; }
  ghostBrick.style.left = `${e.clientX}px`;
  ghostBrick.style.top = `${e.clientY}px`;
  });

document.addEventListener("pointerup", e => {
  if (ghostBrick === null) { return; }
  ghostBrick.style.position = "";
  ghostBrick.style.left = "";
  ghostBrick.style.top = "";
  pile.appendChild(ghostBrick);
  registerMovable(ghostBrick);
  ghostBrick = null;
  });