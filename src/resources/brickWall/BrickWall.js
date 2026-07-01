const wall= Utils.getElementById("wall");
const brickRows= document.querySelectorAll(".brickRow");
const movableBricks= document.querySelectorAll(".brick.movable");
const movingBricksDiv= Utils.getElementById("movingBricks");
const pile= Utils.getElementById("pile");

let dragging= false;

const isEmpty= e => { return e && e.classList && e.classList.contains("empty"); }

const combineEmpty= () => {};

const setEmpty= e => {
  const len = e.textContent.length;
  const parent = e.parentElement;
  e.removeEventListener("pointerdown", movableBrickEventListeners.get(e));
  movableBrickEventListeners.delete(e);
  for (let i = 0; i < len; i++) {
    const empty = document.createElement("span");
    empty.className = "empty";
    empty.textContent = "\u00A0";
    parent.insertBefore(empty, e);
  }
  e.remove();
}

const createGhost= e => {
  const ghost= document.createElement("span");
  ghost.textContent = e.textContent;
  ghost.className = "brick movable";
  ghost.style.position = "absolute";
  movingBricksDiv.appendChild(ghost);
  return ghost;
}

var ghostBrick= null;
const movableBrickEventListeners= new Map();

// --- preview state ---
let previewSpans= [];           // the empty spans currently shown as preview
let previewSaved= [];           // saved textContent to restore on miss/move

const clearPreview= () => {
  previewSpans.forEach((span, i) => {
    span.classList.add("empty");
    span.classList.remove("preview");
    span.textContent = previewSaved[i];
  });
  previewSpans = [];
  previewSaved = [];
}

// Given an empty span the mouse is over, find the run of consecutive
// empty siblings starting at it that can fit the brick's length.
const findSlot= (startSpan, length) => {
  const slot= [];
  let cur= startSpan;
  while (cur && slot.length < length && isEmpty(cur)) {
    slot.push(cur);
    cur = cur.nextElementSibling;
  }
  return slot.length === length ? slot : null;
}

const showPreview= (startSpan, text) => {
  const slot= findSlot(startSpan, text.length);
  if (!slot) { return false; }
  slot.forEach((span, i) => {
    previewSaved.push(span.textContent);
    span.classList.remove("empty");
    span.classList.add("preview");
    span.textContent = text[i];
  });
  previewSpans = slot;
  return true;
}

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

  clearPreview();
  // hide ghost momentarily so elementFromPoint reads what's underneath
  ghostBrick.style.visibility = "hidden";
  const target = document.elementFromPoint(e.clientX, e.clientY);
  ghostBrick.style.visibility = "";

  if (isEmpty(target)) {
    showPreview(target, ghostBrick.textContent);
  }
});

document.addEventListener("pointerup", e => {
  if (ghostBrick === null) { return; }

  if (previewSpans.length > 0) {
    // commit: turn the preview run into a single placed brick
    const text = ghostBrick.textContent;
    const parent = previewSpans[0].parentElement;
    const ref = previewSpans[0];
    const placed = document.createElement("span");
    placed.textContent = text;
    placed.className = "brick movable";
    parent.insertBefore(placed, ref);
    previewSpans.forEach(s => s.remove());
    previewSpans = [];
    previewSaved = [];
    registerMovable(placed);
    ghostBrick.remove();
  } else {
    // miss: send back to pile
    clearPreview();
    ghostBrick.style.position = "";
    ghostBrick.style.left = "";
    ghostBrick.style.top = "";
    pile.appendChild(ghostBrick);
    registerMovable(ghostBrick);
  }

  ghostBrick = null;
  dragging = false;
});