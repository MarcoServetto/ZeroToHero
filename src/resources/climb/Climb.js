'use strict';
const Climb= (score) => {
  const gameArea= Utils.getElementById('gameArea');
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
    draggingIsHidden= flag;
    dragged.hidden= flag;
    updateDraggedHeight();
    };
  const getDraggedHidden= ()=>draggingIsHidden;
  
  const currentQuestion= ()=>questions[currentQuestionIndex];
  const nextLevel= ()=>{
    Utils.flashGreen();
    setTimeout(()=>window.location.href = nextLevelUrl, 2500);
    };
  const nextQuestion= ()=>{
    const oldQ= currentQuestion();
    if (currentQuestionIndex === questions.length - 1){ nextLevel(); return; }
    currentQuestionIndex += 1;
    questions.forEach(q => q.active(false));
    currentQuestion().active(true);
    removeFloaters();
    loadQuestion(currentQuestion(),oldQ);
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
  const loadQuestion= (q,oldQ)=>{
    const isContinuation= q.extractStr('iscontinuation')==="true";
    q.active(true);
    if(!isContinuation){ animateBaseTransition(q.inner()); }
    else{ animateTextTransition(oldQ, q, q.inner()); }    
    questionHelp.value = q.extractStr('context'); 
    let index= 0;
    while (true){
      const img = q.extractStr('rock'+index+'img');
      const code = q.extractStr('rock'+index+'code');
      if (!img || !code) { break; }
      addRock(img, code, index);
      index++;
      }
    };
  const codeWidth= (code)=>{
    const width= (code.length+2.5) *1.1;
    return width + 'ch'
    };
  const showRockCode = (rock, code) => {
    codeUnderRock.hidden = false;
    codeUnderRock.value = " "+code;
    codeUnderRock.style.width = codeWidth(" "+code);
    codeUnderRock.offsetWidth;
    updateCodeUnderRockHeight();
    const rockRect = rock.getBoundingClientRect();
    const containerRect = gameArea.getBoundingClientRect();
    const relativeTop = ((rockRect.bottom - containerRect.top + 5) / containerRect.height) * 100;
    const codeWidthPx = codeUnderRock.getBoundingClientRect().width;
    const codeWidthPercent = (codeWidthPx / containerRect.width) * 100;
    const relativeLeft = ((rockRect.left - containerRect.left) / containerRect.width) * 100 - codeWidthPercent;
    codeUnderRock.style.top = (relativeTop-18) + '%';
    codeUnderRock.style.left = relativeLeft + '%';
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
    //if (wrongAttempts % 5 !== 0) { return; }//TODO: remove wrong attempts or make this work
    //currentQuestion().toSolution();
    //currentQuestion().selectionEvent();
    //setTimeout(()=>q.someCallToRemoveSelection(), 1000);
    };  
  questions.forEach(q=>q.setPostSelect(questionMouseUp));
  const startDragPhase= (selText)=>{
    currentQuestion().addClass('noSelection');    
    dragged.value = " "+selText;
    dragged.style.width = codeWidth(" "+selText);
    setDraggedHidden(false);
    };
  const dragClone = e => {
    const containerRect= gameArea.getBoundingClientRect();
    const relativeTop= ((e.clientY - containerRect.top + 1) / containerRect.height) * 100;
    const relativeLeft= ((e.clientX - containerRect.left) / containerRect.width) * 100;
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
    dragged.value = "";
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
  const updateDraggedHeight= ()=> TextAreaSize.textAreaContainerHeight(dragged,gameArea);
  const updateCodeUnderRockHeight= ()=> TextAreaSize.textAreaContainerHeight(codeUnderRock,gameArea);
  //--------------- next step animation
  const revealTime= 500;
  const stepTime= 100;
  const animateBaseTransition= (container)=>{
    const txt= container.value;
    const oldBg= container.style.backgroundColor;
    container.style.backgroundColor = 'rgba(241, 231, 211,0.6)';
    container.value = '';
    setTimeout(() => {
      container.value = txt; container.style.backgroundColor = oldBg
      }, revealTime);
    };
  const animateTextTransition= (oldQ, newQ, container)=>{
    const oldText= oldQ.extractStr('original');
    const start= oldQ.extractInt('selectionstart');
    const end=   oldQ.extractInt('selectionend');
    const newText= newQ.extractStr('original');
    const prefix= oldText.slice(0, start);
    const oldMiddle= oldText.slice(start, end);
    const suffix= oldText.slice(end);
    const newMiddle= newText.slice(prefix.length, newText.length - suffix.length);     
    Utils.assertEqual(prefix + oldMiddle + suffix, oldText);
    Utils.assertEqual(prefix + newMiddle + suffix, newText);
    const padCount= Math.abs(newMiddle.length - oldMiddle.length);
    const leftPad= Math.floor(padCount / 2);
    const rightPad= padCount - leftPad;
    const paddedNewMiddle = " ".repeat(leftPad) + newMiddle + " ".repeat(rightPad);
    container.value = oldText;
    const done= (unused)=> container.value = newText;
    const doInsertSubs= ()=> animateInsertion(oldMiddle, leftPad, rightPad);
    
    const doSubsInsert= ()=> animateSubstitution(oldMiddle, paddedNewMiddle,
      (intermediateM)=> animateRemoval(intermediateM, leftPad, rightPad));

    const doSubs=()=> animateSubstitution(oldMiddle, newMiddle, done);

    const animateSubstitution= (currentStr, targetStr, doneCallback)=>{
      const current = currentStr.split('');
      const target = targetStr.split('');
      Utils.assertEqual(current.length, target.length);
      let i = 0;
      const step= ()=>{ //current.join('') is a padded text in SubsInsert
        if (i >= current.length){ doneCallback(current.join('')); return; }
        if (current[i] !== target[i]){ current[i] = target[i]; }
        container.value = prefix + current.join('') + suffix;
        i++;
        setTimeout(step, stepTime);
        };
      setTimeout(step, stepTime);
      };      
    const animateInsertion= (current, left, right)=>{
      let currentMiddle = current;
      const step= ()=>{
        if (left > 0){ left--; currentMiddle = " " + currentMiddle; }
        if (right > 0){ right--; currentMiddle = currentMiddle + " "; }
        container.value = prefix + currentMiddle + suffix;
        if (left > 0 || right > 0){ setTimeout(step, stepTime); return; }
        animateSubstitution(currentMiddle, newMiddle, done);
        };
        setTimeout(step, stepTime);
      };
    const animateRemoval= (current, left, right)=>{
      let currentMiddle = current;
      const step= ()=>{
        const remLeft= left > 0 && currentMiddle[0] === ' ';
        const remRight= right > 0 && currentMiddle[currentMiddle.length - 1] === ' ';
        if (remLeft){ currentMiddle = currentMiddle.slice(1); left--; }
        if (remRight){ currentMiddle = currentMiddle.slice(0, -1); right--; }
        container.value = prefix + currentMiddle + suffix;
        if (left > 0 || right > 0){ setTimeout(step, stepTime); return; }
        done("");
        };
      container.value = prefix + currentMiddle + suffix;
      setTimeout(step, stepTime);
      };
    if(newMiddle.length > oldMiddle.length){ doInsertSubs(); return; }
    if(newMiddle.length < oldMiddle.length){ doSubsInsert(); return; }
    doSubs();
    };
  //------------Init
  loadQuestion(questions[0],questions[0]);
  TextAreaSize.updateApiHeight();
  };
Climb(Score( (streak)=>1 ));