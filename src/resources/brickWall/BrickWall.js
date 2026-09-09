const answerWall= Utils.getElementById("answerWall");
const scale= Utils.getElementById("scale");

const solution= MetaData.str(wall, "solution");
const maxTilt= 80;

let vibratingBrick= null;

const hintChar= document.getElementById("hintCharacter");
const displayPanicMessage= (msg, image) => {
  hintChar.querySelector(".panicImg").src = "../../assets/panic/" + image;
  const speechBubble= hintChar.querySelector(".speechBubble");
  speechBubble.textContent = msg;
  hintChar.hidden = false;
  };

const brickWall= initBrickWall({
  wall,
  pile,
  gameArea,
  movingBricksDiv,
  glideTime: 500,
  onBrickPickedUp: () => {
    answerWall.classList.add("hidden");
    if (vibratingBrick !== null) { vibratingBrick.classList.remove("vibrate"); }
    vibratingBrick = null;
    hintChar.hidden = true;
    },
  onBrickCommitted: () => {
    updateVisuals();
    },
  });

const weigh= str => str.replace(/[\s\u00A0]/g, "").length;

const updateVisuals= () => {
  const wallCount= weigh(brickWall.normaliseWallText());
  const solCount= weigh(Utils.normalize(solution));
  const balanced= wallCount === solCount;

  const ratio= solCount === 0 ? 0 : (wallCount - solCount) / solCount;
  const clamped= Math.max(-1, Math.min(1, ratio));

  scale.style.setProperty("--tilt", `${balanced ? 0 : clamped * maxTilt}deg`);
  scale.classList.toggle("balanced", balanced);
  };

const _findFirstWrongBrick= (brickRows, solution) => {
  const sol= Utils.normalize(solution);
  let raw= "";
  for (const r of brickRows) {
    for (const c of r.children) {
      const text= c.textContent;
      for (let i= 0; i < text.length; i++) {
        raw += text[i];
        if (!sol.startsWith(Utils.normalize(raw))) { return c.original ? c.original : c; }
        }
      }
    raw += "\n";
    if (!sol.startsWith(Utils.normalize(raw))) { return null; }
    }
  return null;
  };

const findFirstWrongBrick= () => {
  return _findFirstWrongBrick(brickWall.brickRows, solution);
  };

const findFirstWrongBrickReversed= () => {
  const reversedRows= [...brickWall.brickRows].reverse().map(row => ({
    children: [...row.children].reverse().map(child => ({
      original: child,
      textContent: child.textContent.split("").reverse().join("")
      }))
    }));
  return _findFirstWrongBrick(reversedRows, solution.split('').reverse().join(''));
  };

const onComplete= () => {
  Utils.flashImage("rgba(0, 250, 0, 0.5)","levelEndCharacter","translateY(-5%)");
  const nextLevelUrl= MetaData.str(document.body, "next");
  Utils.checkExists(nextLevelUrl);
  setTimeout(() => window.location.href = nextLevelUrl, 5000);
  };

const onFail= () => {
  let wrongBrick= findFirstWrongBrick();
  if (wrongBrick === null) {
    displayPanicMessage("Everything looks right so far, but there's still some bricks missing.", "panicThumbs1.png");
    return;
    }
  if (wrongBrick.classList.contains("movable")) {
    wrongBrick.classList.add("vibrate");
    vibratingBrick = wrongBrick;
    displayPanicMessage("Something doesn't seem right. That brick is shaking!", "panic20.png");
    return;
    }
  wrongBrick = findFirstWrongBrickReversed();
  if (wrongBrick !== null && wrongBrick.classList.contains("movable")) {
    wrongBrick.classList.add("vibrate");
    vibratingBrick = wrongBrick;
    displayPanicMessage("Something doesn't seem right. That brick is shaking!", "panic20.png");
    return;
    }
  displayPanicMessage("I'm so confused, something doesn't seem right.", "panic27.png");
  };

const checkSolution= () => {
  const wallText= brickWall.normaliseWallText();
  if (wallText === Utils.normalize(solution)) { onComplete(); }
  else { onFail(); }
  };

const hint= () => {
  answerWall.classList.remove("hidden");
  };

scale.addEventListener("mouseenter", () => {
  const tilt= getComputedStyle(scale).getPropertyValue("--tilt");
  if (tilt === "0deg") { displayPanicMessage("You've got the right amount of bricks. But are they correct?", "panicThumbs1.png"); }
  else if (tilt.startsWith("-")) { displayPanicMessage("Keep going, we're not done yet.", "panic25.png"); }
  else { displayPanicMessage("There's too many bricks on the wall! Take some away.", "panic01.png"); }
  });

scale.addEventListener("mouseleave", () => {
  hintChar.hidden = true;
  });

const buttonActions= {
  submitBtn: checkSolution,
  hintBtn: hint,
  };
const Buttons= initButtons(() => {}, buttonActions);

updateVisuals();
