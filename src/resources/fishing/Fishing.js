'use strict';


// TUNING CONSTANTS ----------------------------------------------------------
//
// All position values below are percentages of the full game area, expressed
// as 0..1 numbers. Example: 0.25 means 25% from the left/top.
//
// Fish score examples with the default formula:
//   fishScoreBonus(1, 5)  == 5
//   fishScoreBonus(2, 5)  == 11
//   fishScoreBonus(3, 10) == 33
// Input:
//   comboNumber: 1 for first fish in a streak, 2 for second, etc.
//   fishSize: number of non-whitespace characters in the fish code.
// Output:
//   points gained for catching that fish.
const fishScoreBonus = (comboNumber,fishSize) =>
  Math.ceil(Math.min(fishSize*50,(fishSize) + Math.pow(2,comboNumber-1)));
// Hidden characters in code labels are rendered with this symbol.
const hiddenCharSymbol = '❓';

// Top end of the black fishing line, approximately the rod tip.
const lineStartX = 0.327;
const lineStartY = -0.1;

// Centre ellipse where a fish can be hooked.
const hookAreaX = 0.44;
const hookAreaY = 0.78;
const hookAreaRx = 0.16;
const hookAreaRy = 0.115;

// Free-roaming fish bounds. Raise fishAreaTopMap to keep fish lower.
const fishAreaLeftMap = 0.30;
const fishAreaRightMap = 0.94;
const fishAreaTopMap = 0.58;
const fishAreaBottomMap = 0.95;

// Leashed fish wiggle around the centre.
const leashedFishWiggleX = 0.050;
const leashedFishWiggleY = 0.055;

// Scared fish flee into this wider right-side panic zone, then keep moving
// between random targets in that zone instead of stacking at the right edge.
const scaredFishLeftMap = 0.60;
const scaredFishRightMap = fishAreaRightMap;
const scaredFishSpeed = 0.00034;
const scaredFishAccel = 0.0000011;
const scaredFishTargetDistance = 0.035;
const scaredFishMinTargetX = 0.10;

// Direction display is intentionally sticky to avoid visual left/right flicker.
const facingVelocityDeadZone = 0.000018;
const facingMoveDeadZone = 0.00035;

// Timer tuning.//something seems off here
const baseLeashTimeMs = 15000;
const leashTimePerCharMs = 1500;
const typoPenaltyMinMs = 3000;
const typoPenaltyRatio = 0.05;

// Fish count and speed tuning.
const maxFishes = 3;
const fishVelocityXMin = -0.000035;
const fishVelocityXMax = 0.000035;
const fishVelocityYMin = -0.000025;
const fishVelocityYMax = 0.000025;
const fishAccelX = 0.00000009;
const fishAccelY = 0.00000007;
const fishMaxVx = 0.00009;
const fishMaxVy = 0.00007;
const fishSpawnLeftMap = 1.04;
const fishSpawnRightMap = 1.18;
const fishSpawnVelocityXMin = -0.000095;
const fishSpawnVelocityXMax = -0.000045;

// Animation for catching the fish
const catchAnimationMs = 1500;
const catchTargetY = -0.08;

const FishingText = raw => {
  const chars = [];
  for (let i = 0; i < raw.length; i += 1){
    if (raw[i] !== '@'){ chars.push({ch:raw[i]}); continue; }
    if (raw[i + 1] === '@'){
      i += 2;
      chars.push({ch:raw[i], hideLeash:true});
      continue;
    }
    i += 1;
    chars.push({ch:raw[i], hideFree:true});
  }
  const typeOrder = [];
  let typeLen = 0;
  chars.forEach((c,i) => typeOrder[i] = /\s/.test(c.ch) ? -1 : typeLen++);
  return {raw, chars, plain:chars.map(c=>c.ch).join(''), typeOrder, typeLen};
};

const Fishing = (() => {
  const centre = {x:hookAreaX,y:hookAreaY,rx:hookAreaRx,ry:hookAreaRy};
  const rodAnchor = {x:lineStartX,y:lineStartY};
  const fishTexts = globalThis.level.map(FishingText);
  const fishes = [];
  let activeFish = null;
  let fishSeq = 0;
  let lastTime = performance.now();
  let lastFishSize = 1;
  let levelDone = false;
  let caughtFish = null;

  const score = Score(comboNumber => fishScoreBonus(comboNumber,lastFishSize));
  const gameArea = Utils.getElementById('gameArea');
  const pond = Utils.getElementById('pond');
  const fishLayer = Utils.getElementById('fishes');
  const leashBar = Utils.getElementById('leashBar');
  const rodLine = Utils.getElementById('rodLine');
  const bonus = Utils.getElementById('currentBonus');
  const required = MetaData.int(document.body,'required');
  const nextLevelUrl = MetaData.str(document.body,'next');
  const requiredPoints = Utils.getElementById('requiredPoints');
  requiredPoints.textContent = required;

  const esc = s => s.replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
  const marker = (hidden,ch) => hidden ? `<span class="hiddenChar">${hiddenCharSymbol}</span>` : esc(ch);
  const typed = (fish,i) => fish.text.typeOrder[i] >= 0 && fish.text.typeOrder[i] < fish.progress;
  const hidden = (c,forLeash) => c.hideFree || (forLeash && c.hideLeash);
  const shown = (fish,forLeash) => fish.text.chars.map((c,i)=>{
    const text = c.ch === '\n' ? '\n' : marker(hidden(c,forLeash),c.ch);
    return typed(fish,i) ? `<span class="typed">${text}</span>` : text;
  }).join('');
  const rand = (a,b) => a + Math.random() * (b - a);
  const clamp = (n,a,b) => Math.max(a,Math.min(b,n));
  const setFacing = (fish,dx,deadZone) => {
    if (dx > deadZone){ fish.facingRight = true; }
    if (dx < -deadZone){ fish.facingRight = false; }
  };
  const keepFishInBounds = fish => {
    if (fish.x < fishAreaLeftMap){
      fish.x = fishAreaLeftMap;
      fish.vx = Math.abs(fish.vx);
    }
    if (fish.x > fishAreaRightMap && fish.vx >= 0){
      fish.vx = fishSpawnVelocityXMax;
    }
    if (fish.x <= fishAreaRightMap){
      fish.x = clamp(fish.x,fishAreaLeftMap,fishAreaRightMap);
    }
    if (fish.y < fishAreaTopMap || fish.y > fishAreaBottomMap){ fish.vy *= -1; }
    fish.y = clamp(fish.y,fishAreaTopMap,fishAreaBottomMap);
  };
  const distCentre = f => Math.hypot((f.x - centre.x) / centre.rx,(f.y - centre.y) / centre.ry);
  const isNear = f => distCentre(f) <= 1;
  const updateBonus = () => bonus.textContent = score.streak();
  const setLeashBar = ratio => {
    leashBar.style.width = `${Math.max(0,ratio) * 100}%`;
    leashBar.style.backgroundColor = `hsl(${Math.max(0,ratio) * 120},80%,45%)`;
  };
  const catchRatio = fish =>
    clamp((performance.now() - fish.catchStartTime) / catchAnimationMs,0,1);
  const clearScaredTargets = () => fishes.forEach(fish => {
    fish.scareX = null;
    fish.scareY = null;
  });
  const setPanelIdle = () => {
    setLeashBar(0);
    rodLine.classList.remove('active');
    gameArea.classList.remove('fishingAction');
    clearScaredTargets();
  };
  const updateRodLine = fish => {
    const dx = (fish.x - rodAnchor.x) * pond.clientWidth;
    const dy = (fish.y - rodAnchor.y) * pond.clientHeight;
    rodLine.style.left = `${rodAnchor.x * 100}%`;
    rodLine.style.top = `${rodAnchor.y * 100}%`;
    rodLine.style.height = `${Math.hypot(dx,dy)}px`;
    rodLine.style.transform = `rotate(${Math.atan2(dy,dx) - Math.PI / 2}rad)`;
  };
  const refreshFish = fish => {
    fish.el.style.left = `${fish.x * 100}%`;
    fish.el.style.top = `${fish.y * 100}%`;
    fish.el.classList.toggle('near',isNear(fish));
    fish.el.classList.toggle('leashed',fish === activeFish);
    fish.el.classList.toggle('caught',fish === caughtFish);
    fish.el.classList.toggle('right',fish.facingRight);
    if (fish === caughtFish){
      const r = catchRatio(fish);
      fish.el.style.opacity = 1 - r;
      fish.el.style.filter = `drop-shadow(0 0 ${1 + r * 4}ex rgb(80,255,80))`;
      fish.code.innerHTML = '';
      return;
    }
    fish.el.style.opacity = 1;
    fish.el.style.filter = '';
    fish.code.innerHTML = shown(fish,fish === activeFish);
  };
  let nextFishText = 0;
  const misspelledFishTexts = [];
  const pickText = () => {
    if (nextFishText < fishTexts.length){ return fishTexts[nextFishText++]; }
    if (misspelledFishTexts.length > 0){ return misspelledFishTexts.shift(); }
    return fishTexts[Math.floor(Math.random() * fishTexts.length)];
  };
  const assignScaredTarget = fish => {
    if (fish.x < scaredFishLeftMap){
      fish.scareX = rand(Math.min(scaredFishRightMap,fish.x + scaredFishMinTargetX),scaredFishRightMap);
      fish.scareY = rand(fishAreaTopMap,fishAreaBottomMap);
      return;
    }
    if (fish.x > scaredFishRightMap - scaredFishMinTargetX){
      fish.scareX = rand(scaredFishLeftMap,Math.max(scaredFishLeftMap,fish.x - scaredFishMinTargetX));
      fish.scareY = rand(fishAreaTopMap,fishAreaBottomMap);
      return;
    }
    if (Math.random() < 0.65){
      fish.scareX = rand(fish.x + scaredFishMinTargetX,scaredFishRightMap);
      fish.scareY = rand(fishAreaTopMap,fishAreaBottomMap);
      return;
    }
    fish.scareX = rand(scaredFishLeftMap,fish.x - scaredFishMinTargetX);
    fish.scareY = rand(fishAreaTopMap,fishAreaBottomMap);
  };
  const spawnFish = () => {
    const el = document.createElement('div');
    el.className = 'fish';
    el.innerHTML = '<div class="fishBody"><div class="fishTail"></div><div class="fishFin"></div><div class="fishEye"></div></div><pre class="fishCode"></pre>';
    const fish = {
      id:fishSeq++, el, code:el.querySelector('.fishCode'), text:pickText(), progress:0,
      x:rand(fishSpawnLeftMap,fishSpawnRightMap), y:rand(fishAreaTopMap,fishAreaBottomMap),
      vx:rand(fishSpawnVelocityXMin,fishSpawnVelocityXMax), vy:rand(fishVelocityYMin,fishVelocityYMax),
      facingRight:false, scareX:null, scareY:null, misspelled:false,
      maxTime:0, timeLeft:0, wiggle:rand(0,10)
    };
    fish.facingRight = false;
    fishLayer.appendChild(el);
    fishes.push(fish);
    refreshFish(fish);
  };
  const removeFish = fish => {
    fish.el.remove();
    fishes.splice(fishes.indexOf(fish),1);
  };
  
  const showLevelEndCharacter = () => {
    const end = Utils.getElementById('levelEndCharacter');
    end.hidden = false;
  };
  const showNextLevel = () => {
    if (levelDone){ return; }
    levelDone = true;
    Utils.showNextLevelButton(
      document.querySelector('.scoreText'),
      '<span class="emoji">🎉</span>',
      ()=>window.location.href = nextLevelUrl
      );
  };
  const failCombo = () => { score.doFailure(); updateBonus(); };
  const escapeFish = () => {
    const fish = activeFish;
    fish.progress = 0;
    fish.timeLeft = 0;
    if (fish.misspelled){ misspelledFishTexts.push(fish.text); }
    removeFish(fish);
    activeFish = null;
    failCombo();
    setPanelIdle();
    setTimeout(spawnFish,900);
  };
  const catchFish = () => {
    const fish = activeFish;
    lastFishSize = fish.text.typeLen;
    score.doSuccess();
    updateBonus();
    if (score.score() >= required){ showNextLevel(); }
    activeFish = null;
    caughtFish = fish;
    fish.progress = fish.text.typeLen;
    fish.catchStartTime = performance.now();
    fish.catchStartX = fish.x;
    fish.catchStartY = fish.y;
    fish.catchTargetX = rodAnchor.x;
    fish.catchTargetY = catchTargetY;
    fish.vx = 0;
    fish.vy = 0;
    fish.el.classList.remove('flashMiss');
    gameArea.classList.add('fishingAction');
    rodLine.classList.add('active');
    updateRodLine(fish);
    refreshFish(fish);
  };
  const moveCaught = fish => {
    const r = catchRatio(fish);
    const eased = 1 - Math.pow(1 - r,3);
    fish.x = fish.catchStartX + (fish.catchTargetX - fish.catchStartX) * eased;
    fish.y = fish.catchStartY + (fish.catchTargetY - fish.catchStartY) * eased;
    updateRodLine(fish);
    if (r < 1){ return; }
    removeFish(fish);
    caughtFish = null;
    setPanelIdle();
    setTimeout(spawnFish,900);
  };
  const startLeash = fish => {
    activeFish = fish;
    fishes.filter(f => f !== fish).forEach(assignScaredTarget);
    fish.progress = 0;
    fish.misspelled = false;
    fish.maxTime = baseLeashTimeMs + fish.text.typeLen * leashTimePerCharMs;
    fish.timeLeft = fish.maxTime;
    gameArea.classList.add('fishingAction');
    rodLine.classList.add('active');
    updateRodLine(fish);
    refreshFish(fish);
  };
  const typo = () => {
    if (!activeFish){ return; }
    const fish = activeFish;
    fish.misspelled = true;
    fish.timeLeft -= Math.max(typoPenaltyMinMs,fish.maxTime * typoPenaltyRatio);
    fish.el.classList.add('flashMiss');
    setTimeout(()=>fish.el.classList.remove('flashMiss'),600);
    if (fish.timeLeft <= 0){ escapeFish(); }
  };
  const expectedChar = fish => fish.text.chars.find((_,i)=>fish.text.typeOrder[i] === fish.progress).ch;
  const typeLeashed = ch => {
    if (/\s/.test(ch)){ return; }
    if (expectedChar(activeFish) !== ch){ typo(); return; }
    activeFish.progress += 1;
    refreshFish(activeFish);
    if (activeFish.progress === activeFish.text.typeLen){ catchFish(); }
  };
  const startIfPossible = ch => {
    if (/\s/.test(ch)){ return; }
    const candidates = fishes
      .filter(f => isNear(f) && expectedChar(f) === ch)
      .sort((a,b)=>distCentre(a) - distCentre(b));
    if (candidates.length === 0){ return; }
    startLeash(candidates[0]);
    typeLeashed(ch);
  };
  const keyChar = e => {
    if (e.ctrlKey || e.altKey || e.metaKey){ return null; }
    if (e.key === 'Enter'){ return '\n'; }
    if (e.key === 'Tab'){ return '\t'; }
    return e.key.length === 1 ? e.key : null;
  };
  const keyDown = e => {
    const ch = keyChar(e);
    if (ch === null){ return; }
    e.preventDefault();
    if (caughtFish){ return; }
    if (activeFish){ typeLeashed(ch); return; }
    startIfPossible(ch);
  };
  const moveFree = (fish,dt) => {
    fish.vx += rand(-fishAccelX,fishAccelX) * dt;
    fish.vy += rand(-fishAccelY,fishAccelY) * dt;
    fish.vx = clamp(fish.vx,-fishMaxVx,fishMaxVx);
    fish.vy = clamp(fish.vy,-fishMaxVy,fishMaxVy);
    fish.x += fish.vx * dt;
    fish.y += fish.vy * dt;
    keepFishInBounds(fish);    
    setFacing(fish,fish.vx,facingVelocityDeadZone);
  };
  const moveScared = (fish,dt) => {
    if (fish.scareX === null){ assignScaredTarget(fish); }
    if (Math.hypot(fish.scareX - fish.x,fish.scareY - fish.y) < scaredFishTargetDistance){
      assignScaredTarget(fish);
    }
    const dx = fish.scareX - fish.x;
    const dy = fish.scareY - fish.y;
    const len = Math.max(0.001,Math.hypot(dx,dy));
    fish.vx += (dx / len) * scaredFishAccel * dt;
    fish.vy += (dy / len) * scaredFishAccel * dt;
    fish.vx = clamp(fish.vx,-scaredFishSpeed,scaredFishSpeed);
    fish.vy = clamp(fish.vy,-scaredFishSpeed,scaredFishSpeed);
    fish.x += fish.vx * dt;
    fish.y += fish.vy * dt;
    keepFishInBounds(fish);
    setFacing(fish,fish.vx,facingVelocityDeadZone);
  };
  const moveLeashed = (fish,dt) => {
    fish.wiggle += dt * 0.004;
    const oldX = fish.x;
    fish.x += (centre.x + Math.sin(fish.wiggle) * leashedFishWiggleX - fish.x) * 0.055;
    fish.y += (centre.y + Math.cos(fish.wiggle * 1.7) * leashedFishWiggleY - fish.y) * 0.055;
    setFacing(fish,fish.x - oldX,facingMoveDeadZone);
    fish.timeLeft -= dt;
    setLeashBar(fish.timeLeft / fish.maxTime);
    updateRodLine(fish);
    if (fish.timeLeft <= 0){ escapeFish(); }
  };
  const frame = now => {
    const dt = Math.min(80,now - lastTime);
    lastTime = now;
    [...fishes].forEach(fish => {
      if (fish === caughtFish){ moveCaught(fish); }
      else if (fish === activeFish){ moveLeashed(fish,dt); }
      else if (activeFish){ moveScared(fish,dt); }
      else { moveFree(fish,dt); }
      refreshFish(fish);
    });
    requestAnimationFrame(frame);
  };
  const init = () => {
    for (let i = 0; i < maxFishes; i += 1){ spawnFish(); }
    document.addEventListener('keydown',keyDown,true);
    pond.focus();
    updateBonus();
    setPanelIdle();
    requestAnimationFrame(frame);
  };
  return {init};
})();

window.addEventListener('DOMContentLoaded',Fishing.init);