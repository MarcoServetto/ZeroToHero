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
  e.className = "empty preview";
  e.textContent = "\u00A0".repeat(e.textContent.length);
}

document.addEventListener("pointermove", e => {
  if (!dragging) { return; }
  const element = document.elementFromPoint(e.clientX, e.clientY);
  if (!isEmpty(element)) { return; }
});

movableBricks.forEach(b => {
  b.addEventListener("pointerdown", e => {
    dragging = true;
    //b.style.position = "absolute";
	offsetX = e.clientX - b.offsetLeft;
    offsetY = e.clientY - b.offsetTop;
	setEmpty(b);
	//movingBricksDiv.appendChild(b);
	//combineEmpty();
	b.setPointerCapture(e.pointerId);
    });
  b.addEventListener("pointermove", e => {
    if (!dragging) { return; }
    //b.style.left = `${e.clientX - offsetX}px`;
    //b.style.top = `${e.clientY - offsetY}px`;
	//movingBricksDiv.appendChild(b);
	//combineEmpty();
    });
  b.addEventListener("pointerup", e => {
	dragging = false;
	//pile.appendChild(b);
    b.style.position = "";
    b.style.zIndex = "";
    b.style.left = "";
    b.style.top = "";
    b.releasePointerCapture(e.pointerId);
    })
  });