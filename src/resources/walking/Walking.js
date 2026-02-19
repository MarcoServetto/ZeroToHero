'use strict';
const Walking= (score) => {
  const optBtns= [1,2,3,4,5,6,7,8].map(i=>Utils.getElementById('btn'+i));
  const hintClear= ()=>optBtns.forEach(b=>b.classList.remove('hintDim','hintCorrect'));
  const hintStart= (q,opt)=>{
    const b= optBtns[opt-1] || Utils.error('bad opt '+opt);
    optBtns.forEach(x=>x.classList.add('hintDim'));
    q.setHintBlink(on=>b.classList.toggle('hintCorrect',on));
    b.classList.add('hintCorrect');
    };
  const hintStop= (q)=>{ q.setHintBlink(on=>{}); hintClear(); };
  const nextQuestion= () => {
    const completed= questions.every(q => q.solved);
    if (completed){ questions.forEach(q => q.solved = false); }
    let i = (currentQuestionIndex + 1) % totalQuestions;
    while (questions[i].solved){ i = (i + 1) % totalQuestions; }
    currentQuestionIndex = i;
    questions.forEach(q => q.active(false));
    questions[currentQuestionIndex].active(true);
    updateContent();
    };
  const updateContent= () => {
    requiredPointsElem.textContent = requiredPoints;
    resetAnimationSpeed();
    };
  const speedUp= ()=>{
    var x=score.justFailed()? 1 : score.streak()+1;
    if (x>8){x=8;}
    Utils.flashImage('rgba(250,250,250,0.01)','speedUp'+x,'translateY(40%) scale(0.55)');
    questions[currentQuestionIndex].solved = !score.justFailed();
    if (score.justFailed()){ score.doMootSuccess(); return; }
    score.doSuccess();
    };
  const currentSpeed=()=>{
    const highSpeed= score.justFailed() ? 10000: Math.pow(0.3981,score.streak());      
    return Math.max(highSpeed,Math.pow(0.3981,4));//4 is the max visualized speed
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
    '<span class="emoji">🎉</span>',
    ()=>window.location.href = nextLevelUrl
    );
  let rightAfterPass= 0; 
  const handleCorrectAnswer= ()=>{
    speedUp();
    currentBonusElem.textContent = score.nextIncrement();
    if (score.score() >= requiredPoints){ showNextLevelButton(); rightAfterPass += 1; }
    if (rightAfterPass > 5){ rightAfterPass = 3; setTimeout(Utils.flashGreen, 900); }
    };
  const handleIncorrectAnswer= (currentQuestion) => {
    const longW= score.streak() > 1;
    score.doFailure()
    currentBonusElem.textContent = 0;
    const opt= currentQuestion.requiredOption;
    const motivation= currentQuestion.extractStr('motivation');
    currentQuestion.toSolution();
    currentQuestion.selectionEvent();
    hintStart(currentQuestion,opt);
    playFallAnimation(longW,opt,motivation,()=>{ //displayExplanationMessage
      hintStop(currentQuestion);
      questions.forEach(q => q.active(false));
      questions[currentQuestionIndex].active(true);
      updateContent();
      });
    };
  const playFallAnimation= (longW,requiredOption,motivation,onDone)=>{//motivation is now dead code
    const waitT= longW ? 10000 : 5000;
    Buttons.freezeFor(waitT+100);
    if (longW){
      Utils.flashImage('rgba(200,20,0,0.9)','fallEndCharacter','translateY(30%) scale(0.65)');
      setTimeout(()=>Utils.flashImage('rgba(170,60,10,0.7)','fallStarsCharacter','translateY(30%) scale(0.65)'),3500);
    }
    setTimeout(onDone,waitT);
  };
  const displayExplanationMessage = (requiredOption,motivation,onDismiss) => {//this method is now dead code
    const explanation = OptionExplanations[requiredOption] || Utils.error('bad button id '+requiredOption);
    const msg =`
    <div>
      <p><strong>You stumble and fall.</strong></p>
    <p>The correct highlight/selection is now shown.</p>
    <p>The right button was "<strong>${explanation}</strong>".</p>
    <p> ${motivation}</p>
    <hr>
      <p>Minigame explanation:</p>
      <ul>
        <li>👀Look for the character highlighted in the code.</li>
        <li>🖱️Select the smallest cohesive amount of code around that character.</li>
        <li>☑️Click on the button corresponding to the kind of text you selected.</li>
        <li>🚨There is an error around the highlighted character? Just press "Error".</li>
      <li>🎲This puzzle is all about learning patterns via trial and error.</li>
      </ul>
    <hr>
    <p>☑️ Click here to make this message disappear</p>
    </div>
    `;
    Utils.showMessageBox(msg, 4000, true, Buttons.freezeToken, onDismiss);
    };  
  const handleButtonClick = Log.tag('handleButtonClick', (option) => {
    Buttons.freezeFor(500);
    const currentQuestion = questions[currentQuestionIndex];
    const nope= !currentQuestion.isCorrectAnswer(option);
    if (nope){ handleIncorrectAnswer(currentQuestion); return; }
    handleCorrectAnswer();
    nextQuestion();
    });
  const buttonActions = {};
  for (const index in OptionExplanations){
    buttonActions['btn' + index]= ()=>handleButtonClick(Number(index));
  }
  //init code
  let currentQuestionIndex= 0;
  const Buttons= initButtons(updateContent, buttonActions);
  //Buttons.freezeFor(1500);
  const questions= InitQuestions(Buttons.isFrozen);
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
  (streak)=>streak > 0 ? Math.pow(2, streak - 1) : 0
  ));