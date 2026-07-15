const wall= Utils.getElementById("wall");
const brickRows= document.querySelectorAll(".brickRow");
const movableBricks= document.querySelectorAll(".brick.movable");
const movingBricksDiv= Utils.getElementById("movingBricks");
const pile= Utils.getElementById("pile");
const gameArea= Utils.getElementById("gameArea");

const solution= MetaData.str(wall, "solution");

const space= "\u00A0";

let dragging= false;
let currentEmpty= null; // The current empty span the mouse is on while dragging

const isEmpty= e => e && e.classList && e.classList.contains("empty");

const setEmpty= e => {
  const len = e.textContent.length;
  const parent = e.parentElement;
  e.removeEventListener("pointerdown", movableBrickEventListeners.get(e));
  movableBrickEventListeners.delete(e);
  for (let i = 0; i < len; i++) {
    const empty = document.createElement("span");
    empty.className = "empty";
    empty.textContent = space;
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

let ghostBrick= null;
const movableBrickEventListeners= new Map();

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

const getBrickPosFromMouse= (brick, event) => {
  const rect= brick.getBoundingClientRect();
  const gameAreaRect= gameArea.getBoundingClientRect();
  return [event.clientX - gameAreaRect.left - rect.width/2, event.clientY - gameAreaRect.top - rect.height/2];
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

movableBricks.forEach(b => registerMovable(b));

document.addEventListener("pointermove", e => {
  if (ghostBrick === null) { return; }

  ghostBrick.style.visibility = "hidden";
  const target= document.elementFromPoint(e.clientX, e.clientY);
  ghostBrick.style.visibility = "";

  const slot= isEmpty(target) ? findSlot(target, ghostBrick.textContent.length) : null;

  if (slot !== null) {
    const rect= slot[0].getBoundingClientRect();
    const gameAreaRect= gameArea.getBoundingClientRect();
    ghostBrick.style.left = `${rect.left - gameAreaRect.left}px`;
    ghostBrick.style.top = `${rect.top - gameAreaRect.top}px`;
    currentEmpty = slot[0];
    } else {
    const pos= getBrickPosFromMouse(ghostBrick, e);
    ghostBrick.style.left = `${pos[0]}px`;
    ghostBrick.style.top = `${pos[1]}px`;
    currentEmpty = null;
    }
  });

document.addEventListener("pointerup", e => {
  if (ghostBrick === null) { return; }
  ghostBrick.classList.remove("ghost");

  const text= ghostBrick.textContent;
  const slot= currentEmpty !== null ? findSlot(currentEmpty, text.length) : null;

  if (slot !== null) {
    // commit: turn the run of empties into a single placed brick
    const parent= slot[0].parentElement;
    const ref= slot[0];
    const placed= document.createElement("span");
    placed.textContent = text;
    placed.className = "brick movable";
    parent.insertBefore(placed, ref);
    slot.forEach(s => s.remove());
    registerMovable(placed);
    ghostBrick.remove();
    } else {
    // miss, or not enough consecutive empties: send back to pile
    ghostBrick.style.position = "";
    ghostBrick.style.left = "";
    ghostBrick.style.top = "";
    pile.appendChild(ghostBrick);
    registerMovable(ghostBrick);
    }
  currentEmpty = null;
  ghostBrick = null;
  dragging = false;
  });

const normaliseWallText= () => {
  let str= "";
  brickRows.forEach(r => {
    [...r.children].forEach(c => { str += c.textContent; });
    str += "\n";
    });
  str = Utils.normalize(str);
  console.log(str);
  return str;
  };

const onComplete= () => {
  Utils.flashImage("rgba(0, 250, 0, 0.5)","levelEndCharacter","translateY(-5%)");
  const nextLevelUrl= MetaData.str(document.body, "next");
  Utils.checkExists(nextLevelUrl);
  setTimeout(() => window.location.href = nextLevelUrl, 5000);
  };
const onFail= () => { console.log("Nay"); };

const checkSolution= () => {
  const wallText= normaliseWallText();
  if (wallText === Utils.normalize(solution)) { onComplete(); }
  else { onFail(); }
  };

const hint= () => {
  console.log("Hint");
  };

const buttonActions= {
  submitBtn: checkSolution,
  hintBtn: hint,
  };
const Buttons= initButtons(() => {}, buttonActions);