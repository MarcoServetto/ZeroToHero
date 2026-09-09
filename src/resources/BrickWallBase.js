// Shared initialization
let wall= Utils.getElementById("wall");
let pile= Utils.getElementById("pile");
const gameArea= Utils.getElementById("gameArea");
const movingBricksDiv= Utils.getElementById("movingBricks");
const glideTime= 500;

// Initialize BrickWall base game mechanics
// Called by specialized BrickWall implementations (each passes their own wall)
const initBrickWall= (config) => {
  const {
    wall,
    pile,
    gameArea,
    movingBricksDiv,
    glideTime = 500,
    onBrickPickedUp,
    onBrickCommitted,
  } = config;

  const space= "\u00A0";

  let dragging= false;
  let gliding= false;
  let currentEmpty= null;
  let lastSpot= null;
  let ghostBrick= null;
  let fallingCount= 0;
  const movableBrickEventListeners= new Map();

  const isEmpty= e => e && e.classList && e.classList.contains("empty");

  const findSlot= (startSpan, length) => {
    const slot= [];
    let cur= startSpan;
    while (cur && slot.length < length && isEmpty(cur)) {
      slot.push(cur);
      cur = cur.nextElementSibling;
      }
    return slot.length === length ? slot : null;
    };

  // Find the span that starts exactly at column x in a row (null if a brick straddles it)
  const spanAtX= (row, x) => {
    let pos= 0;
    for (const c of row.children) {
      if (pos === x) { return c; }
      if (pos > x) { return null; }
      pos += c.textContent.length;
      }
    return null;
    };

  const getBrickPosFromMouse= (brick, event) => {
    const rect= brick.getBoundingClientRect();
    const gameAreaRect= gameArea.getBoundingClientRect();
    return [event.clientX - gameAreaRect.left - rect.width/2, event.clientY - gameAreaRect.top - rect.height/2];
    };

  const createGhost= e => {
    const ghost= document.createElement("span");
    ghost.textContent = e.textContent;
    ghost.className = "brick movable ghost";
    ghost.style.position = "absolute";
    movingBricksDiv.appendChild(ghost);
    return ghost;
    };

  // Replace a brick with empties, with no side effects on lastSpot / ghostBrick
  const replaceWithEmpties= e => {
    const len= e.textContent.length;
    const parent= e.parentElement;
    e.removeEventListener("pointerdown", movableBrickEventListeners.get(e));
    movableBrickEventListeners.delete(e);
    let firstEmpty= null;
    for (let i= 0; i < len; i++) {
      const empty= document.createElement("span");
      empty.className = "empty";
      empty.textContent = space;
      parent.insertBefore(empty, e);
      if (i === 0) { firstEmpty = empty; }
      }
    e.remove();
    return firstEmpty;
    };

  const setEmpty= e => {
    const firstEmpty= replaceWithEmpties(e);
    lastSpot = findSlot(firstEmpty, ghostBrick.textContent.length);
    };

  const registerMovable= e => {
    const handler= event => {
      if (gliding) { return; }
      dragging = true;
      ghostBrick = createGhost(e);
      const pos= getBrickPosFromMouse(e, event);
      ghostBrick.style.left = `${pos[0]}px`;
      ghostBrick.style.top = `${pos[1]}px`;
      startSparkles(ghostBrick);
      setEmpty(e);
      onBrickPickedUp?.();
      };
    e.addEventListener("pointerdown", handler);
    movableBrickEventListeners.set(e, handler);
    };

  const glideTo= (ghost, slot, done) => {
    const rect= slot[0].getBoundingClientRect();
    const gameAreaRect= gameArea.getBoundingClientRect();
    const toX= rect.left - gameAreaRect.left;
    const toY= rect.top - gameAreaRect.top;
    const fromX= parseFloat(ghost.style.left) || 0;
    const fromY= parseFloat(ghost.style.top) || 0;

    if (Math.abs(toX - fromX) < 0.5 && Math.abs(toY - fromY) < 0.5) {
      done();
      return;
      }

    const start= performance.now();
    const step= t => {
      const progress= Math.min((t - start) / glideTime, 1);
      const eased= 1 - Math.pow(1 - progress, 3);
      ghost.style.left = `${fromX + (toX - fromX) * eased}px`;
      ghost.style.top = `${fromY + (toY - fromY) * eased}px`;
      if (progress < 1) { requestAnimationFrame(step); }
      else { done(); }
      };

    requestAnimationFrame(step);
    };

  const commitBrick= (slot, text) => {
    const parent= slot[0].parentElement;
    const ref= slot[0];
    const placed= document.createElement("span");
    placed.textContent = text;
    placed.className = "brick movable";
    parent.insertBefore(placed, ref);
    slot.forEach(s => s.remove());
    registerMovable(placed);
    onBrickCommitted?.();
    return placed;
    };

  let sparkleInterval= null;

  const spawnSparkle= brick => {
    if (!brick) return;

    const brickRect= brick.getBoundingClientRect();
    const containerRect= movingBricksDiv.getBoundingClientRect();

    const sparkle= document.createElement("div");
    sparkle.className = "sparkle";

    const x= brickRect.left - containerRect.left + Math.random() * brickRect.width;
    const y= brickRect.top - containerRect.top + Math.random() * brickRect.height;

    sparkle.style.left = `${x}px`;
    sparkle.style.top = `${y}px`;

    gameArea.appendChild(sparkle);

    setTimeout(() => sparkle.remove(), 1000);
    };

  const startSparkles= brick => {
    stopSparkles();
    sparkleInterval = setInterval(() => spawnSparkle(brick), 80);
    };

  const stopSparkles= () => {
    if (sparkleInterval !== null) {
      clearInterval(sparkleInterval);
      sparkleInterval = null;
      }
    };

  const normaliseWallText= () => {
    let str= "";
    const brickRows= wall.querySelectorAll(".brickRow");
    brickRows.forEach(r => {
      [...r.children].forEach(c => { str += c.textContent; });
      str += "\n";
      });
    str = Utils.normalize(str);
    return str;
    };

  const getWallText= () => {
    let str= "";
    const brickRows= wall.querySelectorAll(".brickRow");
    brickRows.forEach(r => {
      [...r.children].forEach(c => { str += c.textContent; });
      str += "\n";
      });
    return str;
    };

  // Move a brick to a slot: the DOM is updated immediately (so later gravity
  // checks see the new layout) while a ghost glides down to the new position
  const dropBrick= (brick, slot) => {
    const text= brick.textContent;
    const rect= brick.getBoundingClientRect();
    const gameAreaRect= gameArea.getBoundingClientRect();

    const ghost= createGhost(brick);
    ghost.classList.remove("ghost");
    ghost.style.left = `${rect.left - gameAreaRect.left}px`;
    ghost.style.top = `${rect.top - gameAreaRect.top}px`;

    replaceWithEmpties(brick);
    const placed= commitBrick(slot, text);
    placed.style.visibility = "hidden";

    fallingCount++;
    gliding = true;
    // glideTo only reads slot[0]'s rect, so the placed brick works as the target
    glideTo(ghost, [placed], () => {
      placed.style.visibility = "";
      ghost.remove();
      fallingCount--;
      if (fallingCount === 0) { gliding = false; }
      });
    };

  const runGravity= () => {
    // Bottom-up: by the time we reach a row, everything below it has already settled
    // (bottom row does not need to be checked)
    const brickRows= [...pile.querySelectorAll(".brickRow")];
    for (let i= brickRows.length - 2; i >= 0; i--) {
      const bricks= [...brickRows[i].children]; // snapshot, dropBrick mutates the row
      let x= 0;
      for (const brick of bricks) {
        const len= brick.textContent.length;
        if (brick.classList.contains("movable")) {
          let lowest= null;
          for (let ii= i + 1; ii < brickRows.length; ii++) {
            const found= findSlot(spanAtX(brickRows[ii], x), len);
            if (found === null) { break; } // blocked, can't fall past this row
            lowest = found;
            }
          if (lowest !== null) { dropBrick(brick, lowest); }
          }
        x += len;
        }
      }
    };

  // Set up event listeners
  const brickRows= wall.querySelectorAll(".brickRow");
  const movableBricks= document.querySelectorAll(".brick.movable");
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
      }
    else {
      const pos= getBrickPosFromMouse(ghostBrick, e);
      ghostBrick.style.left = `${pos[0]}px`;
      ghostBrick.style.top = `${pos[1]}px`;
      currentEmpty = null;
      }
    });

  document.addEventListener("pointerup", e => {
    if (ghostBrick === null || gliding) { return; }
    ghostBrick.classList.remove("ghost");

    const text= ghostBrick.textContent;
    let slot= currentEmpty !== null ? findSlot(currentEmpty, text.length) : null;
    const snapBack= slot === null;
    if (snapBack) { slot = lastSpot; }

    const ghost= ghostBrick;
    const finish= () => {
      commitBrick(slot, text);
      ghost.remove();
      currentEmpty = null;
      ghostBrick = null;
      dragging = false;
      gliding = false;
      stopSparkles();
      runGravity();
      };

    if (snapBack) {
      gliding = true;
      ghostBrick = null;
      dragging = false;
      currentEmpty = null;
      glideTo(ghost, slot, finish);
      }
    else {
      finish();
      }
    });

  return {
    registerMovable,
    normaliseWallText,
    getWallText,
    runGravity,
    brickRows,
    };
  };