const Walking= (score) => {
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
    questions[currentQuestionIndex].solved = !score.justFailed();
    if (score.justFailed()){ score.doMootSuccess(); return; }
    score.doSuccess();
    };
  const currentSpeed=()=>{
    const highSpeed= score.streak() > 0 ? Math.pow(0.3981,score.streak()) : 10000;      
    return Math.max(highSpeed,Math.pow(0.3981,4));//4 is the max visualized speed
    };
  const resetAnimationSpeed=() => {
    const speed= currentSpeed();
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
    score.doFailure()
    currentBonusElem.textContent = 0;
    currentQuestion.toSolution();
    currentQuestion.selectionEvent();
    Buttons.freezeFor(4050);
    displayExplanationMessage(currentQuestion.requiredOption,()=>{
      questions.forEach(q => q.active(false));
      questions[currentQuestionIndex].active(true);
      updateContent();
      });
    };
  const displayExplanationMessage = (requiredOption, onDismiss) => {
    const explanation = OptionExplanations[requiredOption] || Utils.error('bad button id '+requiredOption);
    const msg =`
    <div>
      <p><strong>You stumble and fall.</strong></p>
    <p>The correct highlight/selection is now shown.</p>
    <p>The right button was "<strong>${explanation}</strong>".</p>
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
  const buttonActions = {
    btnParameter: () => handleButtonClick(1),
    btnMethodCall: () => handleButtonClick(2),
    btnObjectLiteral: () => handleButtonClick(3),
    btnMethodDeclaration: () => handleButtonClick(4),
    btnTypeDeclaration: () => handleButtonClick(5),
    btnType: () => handleButtonClick(6),
    btnComment: () => handleButtonClick(7),
    btnError: () => handleButtonClick(8),
  };
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