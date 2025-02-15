'use strict';
const ensureStrictMode = () => {//To put in utils?
  try { undeclaredVar = 10;}// ReferenceError in strict mode
  catch (e) { return; }
  throw new Error("Strict mode somehow disabled");
  };
ensureStrictMode();

const Climb= (score) => {
  const questionHelp= Utils.getElementById('questionHelp');
  const mountainWall= Utils.getElementById('climbRight');
  const scoreCounter= Utils.getElementById('scoreCounter');
  const climber=      Utils.getElementById("climber");
  const questions   = InitQuestions(()=>false);//never isFrozen
  const nextLevelUrl= MetaData.str(document.body,'next');
  const dragged= Utils.getElementById('draggedCode');
  const codeUnderRock= Utils.getElementById('codeUnderRock');
  let currentQuestionIndex= 0;
  let wrongAttempts       = 0;
  let currentOffset       = 0;
  let draggingIsHidden= true;//sadly dragged.hidden can be set but not read
  const setDraggedHidden= (flag)=>{
    //Log.log(true,"set as "+flag)
    draggingIsHidden= flag;
    dragged.hidden= flag;
    };
  const getDraggedHidden= ()=>draggingIsHidden;
  
  const currentQuestion= ()=>questions[currentQuestionIndex];
  const nextLevel= ()=>{
    Log.log(true,"GoingNext");
    Utils.flashGreen();
    setTimeout(()=>window.location.href = nextLevelUrl, 2500);
    };
  const nextQuestion= ()=>{
    if (currentQuestionIndex === questions.length - 1){ nextLevel(); return; }
    currentQuestionIndex += 1;    
    questions.forEach(q => q.active(false));
    currentQuestion().active(true);
    removeFloaters();
    loadQuestion(currentQuestion());
    };
  const removeFloaters= ()=>{
    setDraggedHidden(true);
    codeUnderRock.hidden = true;
    removeRocks();
    };
  const removeRocks= ()=> Array
    .from(mountainWall.querySelectorAll('.rock'))
    .forEach(r => r.remove());
  const addRock= (img,code,index)=>{
    const rock = document.createElement('img');
    rock.classList.add('rock');
    rock.src = "../../resources/climb/images/rocks/"+img;
    rock.style.left = (index*20)+'%';
    rock.addEventListener('mouseenter', ()=>showRockCode(rock,code));
    rock.addEventListener('mouseleave',  ()=>hideRockCode());
    rock.addEventListener('click', ()=>handleRockSelection(index));
    mountainWall.appendChild(rock);
    };    
  const loadQuestion= (q)=>{
    q.active(true);
    questionHelp.textContent = q.extractStr('context'); 
    let index= 0;
    while (true){
      const img = q.extractStr('rock'+index+'img');
      const code = q.extractStr('rock'+index+'code');
      if (!img || !code) { break; }
      addRock(img, code, index);
      index++;
      }
    };    
  const showRockCode = (rock, code) => {
    codeUnderRock.textContent = code; 
    const width= code.length + 3;
    codeUnderRock.style.width = width + 'ch';
    codeUnderRock.style.height = 'auto';
    codeUnderRock.style.height = ((codeUnderRock.scrollHeight / 16) - 1) + 'em';
    const rockRect = rock.getBoundingClientRect();
    const containerRect = document.querySelector('.gameArea').getBoundingClientRect();
    const relativeTop = ((rockRect.bottom - containerRect.top + 5) / containerRect.height) * 100;
    const relativeLeft = ((rockRect.left - containerRect.left) / containerRect.width) * 100;
    codeUnderRock.style.top = (relativeTop-18) + '%';
    codeUnderRock.style.left = relativeLeft + '%';
    codeUnderRock.hidden = false;
    };
  const hideRockCode = () => codeUnderRock.hidden = true;

  const questionMouseUp= ()=>{
    if (!getDraggedHidden()) { return; }
    const q= currentQuestion();
    const sel= q.currentSelection();
    if(sel === ""){ return; }
    const correctSelection= q.isCorrectSelection();
    if (correctSelection){ startDragPhase(sel); return; }
    q.addClass('incorrectGlow');
    wrongAttempts++;
    setTimeout(()=>q.removeClass('incorrectGlow'), 1000);
    if (wrongAttempts % 5 !== 0) { return; }
    currentQuestion().toSolution();
    };  
  questions.forEach(q=>q.setPostSelect(questionMouseUp));
  const startDragPhase= (selText)=>{
    currentQuestion().addClass('noSelection');    
    dragged.textContent = selText;
    setDraggedHidden(false);
    };
  const dragClone = e => {
    const container= document.querySelector('.gameArea');
    const containerRect= container.getBoundingClientRect();
    const relativeTop= ((e.clientY - containerRect.top + 1) / containerRect.height) * 100;
    const relativeLeft= ((e.clientX - containerRect.left) / containerRect.width) * 100;
    dragged.style.height = 'auto';
    const width= dragged.value.length + 3;
    dragged.style.width = width + 'ch';
    dragged.style.height = ((dragged.scrollHeight / 16)-1)+'em';
    const draggedRect = dragged.getBoundingClientRect();
    const widthPercentage = (draggedRect.width / containerRect.width) * 100;
    dragged.style.top = relativeTop + '%';
    dragged.style.left = (relativeLeft - widthPercentage) + '%';
    };
  //we keep the dragElement moving, but it is often hidden
  document.addEventListener('mousemove', dragClone);
  const handleRockSelection= (rockIndex)=>{
    if (getDraggedHidden()){ return; }
    const q = currentQuestion();
    if (rockIndex !== q.requiredOption){ fallDown(); return; }
    dragged.textContent = "";
    setDraggedHidden(true);
    score.doSuccess();
    moveUp();
    setTimeout(nextQuestion,1700);
    };
  const fadeOutClimber= ()=>{
    climber.style.transition = "opacity 0.5s ease-out";
    climber.style.opacity = 0;
    };
  const fadeInClimber= ()=>{
    const newSrc= swapClimberImage();
    climber.style.transition = "opacity 0.5s ease-out";
    climber.style.opacity = 0;
    setTimeout(() => {
      climber.src = newSrc;
      climber.style.transition = "opacity 0.5s ease-in";
      climber.style.opacity = 1;
      }, 500);
    };
  const swapClimberImage = () => climber.src.includes("climbingA.png") 
    ? "../../resources/climb/images/climbingB.png" 
    : "../../resources/climb/images/climbingA.png";
  const moveUp= ()=>{
    removeFloaters();
    fadeOutClimber();
    const targetOffset= currentOffset + 600;
    const step=5;
    const animate= ()=>{
      if (currentOffset >= targetOffset){ fadeInClimber(); return; }
      currentOffset += step;
      mountainWall.style.backgroundPosition = `center ${-currentOffset}%`;
      requestAnimationFrame(animate);
      };
    animate();
    };
  const fallDown= ()=>{
    removeFloaters();
    fadeOutClimber();
    const step= 50;
    const downFall= -4000 - (score.score()*4000);
    const animate= ()=>{
      if (currentOffset <= downFall) { animateFallEnd(); return; }
      currentOffset = Math.max(downFall, currentOffset - step);
      mountainWall.style.backgroundPosition = `center ${-currentOffset}%`;
      requestAnimationFrame(animate);
      };
    animate();
    };
  const animateFallEnd=()=>{
    flashFall();
    //setTimeout(flashFall,1000);
    setTimeout(flashEnd,3500);
    setTimeout(()=>window.location.reload(),6500);
    };
  const flashFall= ()=>Utils
    .flashImage('rgba(200,20,0,0.9)','fallEndCharacter');
  const flashEnd= ()=>Utils
    .flashImage('rgba(170,60,10,0.7)','fallStarsCharacter');
     

  const updateApiHeight= ()=>{
    const api= Utils.getElementById('api');
    const container= Utils.getElementById('apiContainer');
    const rootFontSize= parseFloat(getComputedStyle(document.documentElement).fontSize);//`1em` in pixels
    const heightPercentage= (api.scrollHeight / container.clientHeight) * 100;
    const heightEm = api.scrollHeight / rootFontSize;
    api.style.minHeight = Math.max(98, heightPercentage) + '%';
    };
  loadQuestion(questions[0]);
  updateApiHeight();
  };
Climb(Score( (streak)=>1 ));