const wall= Utils.getElementById("wall");
const brickRows= document.querySelectorAll(".brickRow");
const movableBricks= document.querySelectorAll(".brick.movable");
const movingBricksDiv= Utils.getElementById("movingBricks");
const pile= Utils.getElementById("pile");

const solution= MetaData.str(wall, "solution");

const SPACE= "\u00A0";

let dragging= false;

const isEmpty= e => { return e && e.classList && e.classList.contains("empty"); }

const setEmpty= e => {
  const len = e.textContent.length;
  const parent = e.parentElement;
  e.removeEventListener("pointerdown", movableBrickEventListeners.get(e));
  movableBrickEventListeners.delete(e);
  for (let i = 0; i < len; i++) {
    const empty = document.createElement("span");
    empty.className = "empty";
    empty.textContent = SPACE;
    parent.insertBefore(empty, e);
  }
  e.remove();
}

const createGhost= e => {
  const ghost= document.createElement("span");
  ghost.textContent = e.textContent;
  ghost.className = "brick movable ghost";
  ghost.style.position = "absolute";
  movingBricksDiv.appendChild(ghost);
  return ghost;
}

var ghostBrick= null;
const movableBrickEventListeners= new Map();

// --- preview state ---
let previewSpans= [];           // the empty spans currently shown as preview

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
  previewSpans = slot;
  return true;
}

const getBrickPosFromMouse= (brick, event) => {
  const rect= brick.getBoundingClientRect();
  return [event.clientX - rect.width/2, event.clientY - rect.height/2];
}

const registerMovable= e => {
  const handler= event => {
    dragging = true;
    ghostBrick = createGhost(e);
	const pos= getBrickPosFromMouse(e, event);
    ghostBrick.style.left = `${pos[0]}px`;
    ghostBrick.style.top = `${pos[1]}px`;
    if (e.parentElement !== pile) {
      setEmpty(e);
    } else { e.remove(); }
  };
  e.addEventListener("pointerdown", handler);
  movableBrickEventListeners.set(e, handler);
}

movableBricks.forEach(b => {
  registerMovable(b);
});

document.addEventListener("pointermove", e => {
  if (ghostBrick === null) { return; }

  // hide ghost momentarily so elementFromPoint reads what's underneath
  ghostBrick.style.visibility = "hidden";
  const target = document.elementFromPoint(e.clientX, e.clientY);
  ghostBrick.style.visibility = "";

  const placed = isEmpty(target) && showPreview(target, ghostBrick.textContent);

  if (placed) {
    // snap ghost to the slot the preview occupies
    const rect = previewSpans[0].getBoundingClientRect();
    ghostBrick.style.left = `${rect.left}px`;
    ghostBrick.style.top = `${rect.top}px`;
  } else {
    // follow the cursor when there's no valid slot
    const pos= getBrickPosFromMouse(ghostBrick, e);
    ghostBrick.style.left = `${pos[0]}px`;
    ghostBrick.style.top = `${pos[1]}px`;
	previewSpans = [];
  }
});

document.addEventListener("pointerup", e => {
  if (ghostBrick === null) { return; }
  ghostBrick.classList.remove("ghost");

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
    registerMovable(placed);
    ghostBrick.remove();
  } else {
    // miss: send back to pile
    ghostBrick.style.position = "";
    ghostBrick.style.left = "";
    ghostBrick.style.top = "";
    pile.appendChild(ghostBrick);
    registerMovable(ghostBrick);
  }

  ghostBrick = null;
  dragging = false;
});

const normaliseWallText= () => {
  let str= "";
  let putSpace= true;
  brickRows.forEach(r => {
    [...r.children].forEach(c => {
      let content= c.textContent;
      if (content === SPACE) {
		if (!putSpace) { return; }
        str += " ";
        putSpace = false;
        return;
        }
      putSpace = true;
      str += content;
      return;
      });
    str = str.trim();
    str += "\n";
    });
  str = str.trim();
  console.log(str);
  return str;
};

const onComplete= () => { console.log("Yay"); };
const onFail= () => { console.log("Nay"); };

const checkSolution= () => {
  const wallText= normaliseWallText();
  if (wallText === solution) { onComplete(); }
  else { onFail(); }
};

const buttonActions= {
  submitBtn: checkSolution,
  hintBtn: () => { console.log("Hint"); },
  };
const Buttons= initButtons(() => {}, buttonActions);