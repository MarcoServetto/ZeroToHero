'use strict';

const InitColorQuestions= (isFrozen)=>
 _postColorInit(Deck.list('question').map(q=>ColorQuestion(q,isFrozen)));

let colorCount= 0;
const _postColorInit= Log.tagAsync('postColorInit',(questions)=>{
 setTimeout(()=>_postColorInit(questions),100);
 colorCount = (colorCount + 1) % 7;
 questions.forEach(q=>q.keepFocus());
 questions
  .filter(q=>q.isBlinking())
  .forEach(q=>{
   colorCount!==0?q.toSolution():q.toSingle();
   q.selectionEvent();
  });
 return questions;
});

const ColorQuestion= (q,isFrozen)=>{
 const redChar= MetaData.int(q,'red');
 const requiredOption= MetaData.int(q,'option');
 const startOk= MetaData.int(q,'selectionstart');
 const endOk= MetaData.int(q,'selectionend');
 const originalText= MetaData.str(q,'original');
 const frameIcon= MetaData.str(q,'frameicon');
 const frameTooltip= MetaData.str(q,'frametooltip');
 const pane= document.createElement('div');
 q.value = originalText;
 q.after(pane);
 pane.className = 'colorQuestion';
 pane.hidden = true;
 pane.style.backgroundColor = getComputedStyle(q).backgroundColor;
 q.classList.add('colorSourceTextarea');
 let selected= new Set();
 let focusStart= redChar;
 let focusEnd= redChar + 1;
 let blinking= false;
 let hintBlink= on=>{};
 let postSelect= ()=>{};
 let onTextPress= ()=>{};
 let mousePressed= false;
 const cells= [];
 const noRedChar= ()=>Number.isNaN(redChar);
 const trimMe= i=>i >= 0 && i < originalText.length
  && (originalText[i] === '\n' || originalText[i] === ' ');
 const explicit= i=>originalText[i] !== '\n';
 const setHintBlink= cb=>hintBlink = cb;
 const setPostSelect= cb=>postSelect = cb;
 const setOnTextPress= cb=>onTextPress = cb;
 const addClass= str=>pane.classList.add(str);
 const removeClass= str=>pane.classList.remove(str);
 const extractStr= str=>MetaData.str(q,str);
 const extractInt= str=>MetaData.int(q,str);
 const selectedBounds= ()=>{
  if (selected.size === 0){ return {start:0,end:0}; }
  return {start:Math.min(...selected),end:Math.max(...selected)+1};
 };
 const currentSelectionRange= ()=>{
  const r= selectedBounds();
  let start= r.start;
  let end= r.end;
  while (start < end && trimMe(start)){ start += 1; }
  while (start < end && trimMe(end - 1)){ end -= 1; }
  return {start,end};
 };
 const syncNativeSelection= ()=>{
  const r= currentSelectionRange();
  q.selectionStart = r.start;
  q.selectionEnd = r.end;
 };
 const refresh= ()=>{
  syncNativeSelection();
  cells.forEach((c,i)=>{
   if (!c){ return; }
   c.classList.toggle('selected',selected.has(i));
   c.classList.toggle('focusChar',i >= focusStart && i < focusEnd);
  });
 };
 const addRawRange= (a,b)=>{
  const start= Math.max(0,Math.min(a,b));
  const end= Math.min(originalText.length - 1,Math.max(a,b));
  for (let i=start;i<=end;i += 1){ selected.add(i); }
 };
 const selectIndex= i=>{
  if (selected.has(i)){ return; }
  if (selected.size === 0){
   selected.add(i);
   refresh();
   return;
  }
  const oldStart= Math.min(...selected);
  const oldEnd= Math.max(...selected);
  selected.add(i);
  addRawRange(Math.min(oldStart,i),Math.max(oldEnd,i));
  refresh();
 };
 const resetToRed= ()=>{
  selected = new Set();
  if (!noRedChar()){ selected.add(redChar); }
  refresh();
 };
 const unselectIndex= i=>{
  if (!selected.has(i)){ return; }
  if (noRedChar()){
   selected.delete(i);
   refresh();
   return;
  }
  if (i === redChar){
   resetToRed();
   return;
  }
  if (i < redChar){
   for (let j=0;j<=i;j += 1){ selected.delete(j); }
   selected.add(redChar);
   refresh();
   return;
  }
  for (let j=i;j<originalText.length;j += 1){ selected.delete(j); }
  selected.add(redChar);
  refresh();
 };
 const touchIndex= i=>{
  selected.has(i)?unselectIndex(i):selectIndex(i);
 };
 const cellIndex= e=>{
  const c= e.target.closest('.colorChar');
  return c && pane.contains(c)?Number(c.dataset.index):NaN;
 };
 const pointerDown= e=>{
  const i= cellIndex(e);
  if (Number.isNaN(i)){ return; }
  e.preventDefault();
  onTextPress();
  if (isFrozen()){ return; }
  mousePressed = true;
  touchIndex(i);
 };
 const pointerOver= e=>{
  if (isFrozen() || (e.buttons & 1) === 0){ return; }
  const i= cellIndex(e);
  if (Number.isNaN(i)){ return; }
  mousePressed = true;
  selectIndex(i);
 };
 const pointerUp= ()=>{
  if (!mousePressed){ return; }
  mousePressed = false;
  selectionEvent();
  postSelect();
 };
 const build= ()=>{
  pane.textContent = '';
  cells.length = 0;
  if (frameIcon){
   const icon= document.createElement('div');
   icon.className = 'frameIcon';
   icon.textContent = frameIcon;
   icon.dataset.tooltip = frameTooltip;
   pane.append(icon);
  }
  for (let i=0;i<originalText.length;i += 1){
   const ch= originalText[i];
   if (ch === '\n'){
    cells[i] = null;
    pane.append(document.createElement('br'));
    continue;
   }
   const span= document.createElement('span');
   span.className = 'colorChar';
   span.dataset.index = i;
   span.textContent = ch === ' '?String.fromCharCode(160):ch;
   cells[i] = span;
   pane.append(span);
  }
 };
 const toSolution= ()=>{
  focusStart = startOk;
  focusEnd = endOk;
  blinking = true;
  selected = new Set();
  addRawRange(startOk,endOk - 1);
  hintBlink(true);
  refresh();
 };
 const toSingle= ()=>{
  focusStart = redChar;
  focusEnd = redChar + 1;
  resetToRed();
  hintBlink(false);
 };
 const currentSelection= ()=>{
  const r= currentSelectionRange();
  return originalText.slice(r.start,r.end);
 };
 const clearSelection= ()=>{ selected = new Set(); refresh(); };
 const cellAt= i=>cells[i] || null;
 const includesRed= ()=>{
  const r= selectedBounds();
  return noRedChar() || (r.start <= redChar && r.end > redChar && selected.has(redChar));
 };
 const allExplicitSelected= (a,b)=>{
  for (let i=a;i<b;i += 1){
   if (explicit(i) && !selected.has(i)){ return false; }
  }
  return true;
 };
 const isCorrectSelection= ()=>{
  const r= currentSelectionRange();
  return r.start === startOk && r.end === endOk && allExplicitSelected(startOk,endOk);
 };
 const isOverSelection= ()=>{
  const r= currentSelectionRange();
  return r.start <= startOk && r.end >= endOk && (r.start < startOk || r.end > endOk);
 };
 const isCorrectAnswer= option=>{
  const isErrorType= requiredOption === 8;
  return option === requiredOption && (isErrorType || isCorrectSelection());
 };
 const isDefaultSelection= ()=>{
  const r= currentSelectionRange();
  return !noRedChar() && r.start === redChar && r.end === redChar + 1;
 };
 const needsSelectionPrompt= ()=>
  requiredOption !== 8 && isDefaultSelection() && (endOk - startOk) !== 1;
 const active= Log.tag('colorActive',flag=>{
  q.hidden = true;
  pane.hidden = !flag;
  if (!flag){ hintBlink(false); return; }
  blinking = false;
  toSingle();
 });
 const selectionEvent= ()=>{
  if (isFrozen()){ refresh(); return false; }
  if (!includesRed()){ toSingle(); }
  return false;
 };
 const keepFocus= Log.tag('colorKeepFocus',()=>{
  if (pane.hidden){ return; }
  selectionEvent();
 });
 build();
 pane.addEventListener('pointerdown',pointerDown);
 pane.addEventListener('pointerover',pointerOver);
 window.addEventListener('pointerup',pointerUp);
 window.addEventListener('pointercancel',pointerUp);
 q.addEventListener('keydown',e=>e.preventDefault());
 return {
  toSolution,toSingle,currentSelection,
  isCorrectAnswer,isCorrectSelection,isOverSelection,needsSelectionPrompt,
  active,keepFocus,selectionEvent,
  extractStr,extractInt,
  addClass,removeClass,setPostSelect,setHintBlink,setOnTextPress,
  isBlinking:()=>blinking,
  solved:false,requiredOption,inner:()=>q,visible:()=>pane,
  isExample:extractStr('example') === 'true',failCount:0,demoShown:false,
  startOk,endOk,cellAt,selectAt:selectIndex,clearSelection
 };
};

const Walking= (score) => {
 const optBtns= [1,2,3,4,5,6,7,8].map(i=>Utils.getElementById('btn'+i));
 const hintClear= ()=>optBtns.forEach(b=>b.classList.remove('hintDim','hintCorrect'));
 const hintStart= (q,opt)=>{
  const b= optBtns[opt-1] || Utils.error('bad opt '+opt);
  optBtns.forEach(x=>x.classList.add('hintDim'));
  q.setHintBlink(on=>b.classList.toggle('hintCorrect',on));
  b.classList.add('hintCorrect');
 };
 const hintStop= q=>{ q.setHintBlink(on=>{}); hintClear(); };
 const hintChar= Utils.getElementById('hintCharacter');
 let panicToHideId= null;
 const displayPanicMessage= (msg,duration) => {
  clearTimeout(panicToHideId);
  hintChar.querySelector('.speechBubble').textContent = msg;
  hintChar.hidden = false;
  panicToHideId= setTimeout(()=>{ hintChar.hidden = true; },duration);
 };
 const hidePanicMessage= () => {
  clearTimeout(panicToHideId);
  hintChar.hidden = true;
 };
 const gameArea= Utils.getElementById('gameArea');
 const exampleCursor= Utils.getElementById('exampleCursor');
 const moveCursorTo= el=>{
  const g= gameArea.getBoundingClientRect();
  const r= el.getBoundingClientRect();
  exampleCursor.style.left= (r.left + r.width / 2 - g.left) + 'px';
  exampleCursor.style.top= (r.top + r.height / 2 - g.top) + 'px';
 };
 const stepMs= 380;
 const moveWaitMs= 500;
 const pressHoldMs= 600;
 const playExample= (q)=>{
  const btn= optBtns[q.requiredOption-1];
  const token= Buttons.freezeToken();
  const finish= ()=>{
   exampleCursor.classList.remove('pressing');
   exampleCursor.hidden= true;
   q.active(true);
   token.unfreeze();
  };
  const mimePress= ()=>{
   exampleCursor.classList.add('pressing');
   setTimeout(finish,pressHoldMs);
  };
  const moveToButton= ()=>{
   moveCursorTo(btn);
   setTimeout(mimePress,moveWaitMs);
  };
  const selectStep= i=>{
   if (i >= q.endOk){ setTimeout(moveToButton,moveWaitMs); return; }
   const cell= q.cellAt(i);
   if (cell){ moveCursorTo(cell); }
   q.selectAt(i);
   setTimeout(()=>selectStep(i + 1),stepMs);
  };
  q.toSingle();
  q.clearSelection();
  const firstCell= q.cellAt(q.startOk);
  if (firstCell){ moveCursorTo(firstCell); }
  exampleCursor.hidden= false;
  setTimeout(()=>selectStep(q.startOk),moveWaitMs);
 };
 const maybeShowExample= ()=>{
  const q= questions[currentQuestionIndex];
  if (!q.isExample || q.failCount <= 3 || q.demoShown){ return; }
  q.demoShown = true;
  playExample(q);
 };
 const nextQuestion= () => {
  const completed= questions.every(q => q.solved);
  if (completed){ questions.forEach(q => q.solved = false); }
  let i = (currentQuestionIndex + 1) % totalQuestions;
  while (questions[i].solved){ i = (i + 1) % totalQuestions; }
  currentQuestionIndex = i;
  questions.forEach(q => q.active(false));
  questions[currentQuestionIndex].active(true);
  hidePanicMessage();
  updateContent();
 };
 const updateContent= () => {
  requiredPointsElem.textContent = requiredPoints;
  resetAnimationSpeed();
  maybeShowExample();
 };
 const speedUp= ()=>{
  var x=score.justFailed()? 1 : score.streak()+1;
  if (x>8){ x=8; }
  Utils.flashImage('rgba(250,250,250,0.01)','speedUp'+x,'translateY(40%) scale(0.55)');
  questions[currentQuestionIndex].solved = !score.justFailed();
  if (score.justFailed()){ score.doMootSuccess(); return; }
  score.doSuccess();
 };
 const currentSpeed=()=>{
  const highSpeed= score.justFailed() ? 10000: Math.pow(0.3981,score.streak());
  return Math.max(highSpeed,Math.pow(0.3981,4));
 };
 const resetAnimationSpeed=() => {
  const speed= currentSpeed();
  Log.log(true,speed);
  backLayer.style.animationDuration = (1000000 * speed + 's');
  frontLayer.style.animationDuration = (500000 * speed + 's');
  character.style.animationDuration = ((1.5 + 50 * speed) + 's');
  smallCharacter.style.animationDuration = ((1.5 + 50 * speed) + 's');
 };
 const showNextLevelButton= () => Utils.showNextLevelButton(
  document.querySelector('.scoreText'),
  '<span class="emoji">&#x1f389;</span>',
  ()=>window.location.href = nextLevelUrl
 );
 let rightAfterPass= 0;
 const handleCorrectAnswer= ()=>{
  speedUp();
  currentBonusElem.textContent = score.nextIncrement();
  if (score.score() >= requiredPoints){ showNextLevelButton(); rightAfterPass += 1; }
  if (rightAfterPass > 5){ rightAfterPass = 3; setTimeout(Utils.flashGreen, 900); }
 };
 const failWaitMs= longW=>longW ? 10000 : 5000;
 const overSelectionHint= 'remember, select the smallest syntactically self-contained unit';
 const handleIncorrectAnswer= (currentQuestion,option) => {
  const longW= score.streak() > 1;
  const overSelected= option === currentQuestion.requiredOption && currentQuestion.isOverSelection();
  score.doFailure();
  currentBonusElem.textContent = 0;
  currentQuestion.failCount += 1;
  const opt= currentQuestion.requiredOption;
  const motivation= currentQuestion.extractStr('motivation');

  currentQuestion.toSolution();
  currentQuestion.selectionEvent();
  hintStart(currentQuestion,opt);
  if (opt === 8){
   displayPanicMessage(currentQuestion.extractStr('errorexplanation'),failWaitMs(longW));
  } else if (overSelected && Math.random() < 1 / 3){
   displayPanicMessage(overSelectionHint,failWaitMs(longW));
  }

  let fall= null;
  currentQuestion.setOnTextPress(()=>{ if (fall){ fall.stop(); } });

  fall = playFallAnimation(longW,opt,motivation,()=>{
   currentQuestion.setOnTextPress(()=>{});
   hintStop(currentQuestion);
   questions.forEach(q => q.active(false));
   questions[currentQuestionIndex].active(true);
   updateContent();
  });
 };
 const playFallAnimation= (longW,requiredOption,motivation,onDone)=>{//we should find an elegant way to still display the motivation
  const waitT= failWaitMs(longW);
  const freeze= Buttons.freezeFor(waitT+100);
  const timers= [];
  const flashers= [];
  let done= false;
  const later= (f,t)=>timers.push(setTimeout(f,t));
  const finish= ()=>{
   if (done){ return; }
   done = true;
   timers.forEach(clearTimeout);
   flashers.forEach(f=>f.stop());
   freeze.unfreeze();
   onDone();
  };
  if (longW){
   flashers.push(Utils.flashImage(
    'rgba(200,20,0,0.9)',
    'fallEndCharacter',
    'translateY(30%) scale(0.65)'
   ));
   later(()=>flashers.push(Utils.flashImage(
    'rgba(170,60,10,0.7)',
    'fallStarsCharacter',
    'translateY(30%) scale(0.65)'
   )),3500);
  }
  later(finish,waitT);
  return { stop:finish };
 };
 const handleButtonClick = Log.tag('handleButtonClick', (option) => {
  const currentQuestion = questions[currentQuestionIndex];
  if (currentQuestion.needsSelectionPrompt()){
   displayPanicMessage('select text first\nthen press button',4000);
   return;
  }
  const nope= !currentQuestion.isCorrectAnswer(option);
  if (nope){ handleIncorrectAnswer(currentQuestion,option); return; }
  currentQuestion.failCount = 0;
  currentQuestion.demoShown = false;
  Buttons.freezeFor(500);
  handleCorrectAnswer();
  nextQuestion();
 });
 const buttonActions = {};
 for (const index in OptionExplanations){
  buttonActions['btn' + index]= ()=>handleButtonClick(Number(index));
 }
 let currentQuestionIndex= 0;
 const Buttons= initButtons(updateContent, buttonActions);
 const questions= InitColorQuestions(Buttons.isFrozen);
 const totalQuestions= questions.length;
 const currentBonusElem= Utils.getElementById('currentBonus');
 const requiredPoints= MetaData.int(document.body,'required');
 const nextLevelUrl= MetaData.str(document.body,'next');
 const requiredPointsElem= Utils.getElementById('requiredPoints');
 const backLayer= document.querySelector('.back-layer');
 const frontLayer= document.querySelector('.front-layer');
 const character = document.querySelector('.character');
 const smallCharacter = document.querySelector('.small-character');
 questions.forEach(q => q.active(false));
 questions[currentQuestionIndex].active(true);
 updateContent();
 const animationTxt= 'scrollFrontBackground 1s linear infinite';
 window.addEventListener('resize', () => {
  backLayer.style.animation = 'none';
  frontLayer.style.animation = 'none';
  void backLayer.offsetWidth;
  void frontLayer.offsetWidth;
  backLayer.style.animation = animationTxt;
  frontLayer.style.animation = animationTxt;
  resetAnimationSpeed();
 });
};

Walking(Score(
 streak=>streak > 0 ? Math.pow(2, streak - 1) : 0
));