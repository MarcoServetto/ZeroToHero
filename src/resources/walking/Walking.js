//import { InitQuestions } from './Question.js';
//import { OptionExplanations } from './GameOptions.js';

document.addEventListener('DOMContentLoaded', () => {
  const nextQuestion= Log.tag('nextQuestion', () => {
    if (streak === 0){streak = 1; }    
    const completed= questions.every(q => q.solved);
    if (completed){ questions.forEach(q => q.solved = false); }
    let i = (currentQuestionIndex + 1) % totalQuestions;
    while (questions[i].solved){ i = (i + 1) % totalQuestions; }
    currentQuestionIndex = i;
    questions.forEach(q => q.active(false));
    questions[currentQuestionIndex].active(true);
    updateContent();
    });
  const updateContent= Log.tag('updateContent',() => {
    requiredPointsElem.textContent = requiredPoints;
    resetAnimationSpeed();
    });
  const speedUp= ()=>{ streak = streak + 1; };
  const resetAnimationSpeed= () => {
    const speed= streak>0?Math.pow(0.3981,streak):0;
    backLayer.style.animationDuration = (1000000 * speed + 's');
    frontLayer.style.animationDuration = (500000 * speed + 's');
    if (speed > 0){
      character.style.animationPlayState = 'running';
      smallCharacter.style.animationPlayState = 'running';
      character.style.animationDuration = ((1.5 + 50 * speed) + 's');
      smallCharacter.style.animationDuration = ((1.5 + 50 * speed) + 's');
      }
    else {
      character.style.animationPlayState = 'paused';
      smallCharacter.style.animationPlayState = 'paused';
      }
    };
  const showScoreAnimation= (increment) => {
    const incrementElem = document.createElement('span');
    incrementElem.classList.add('score-increment');
    incrementElem.textContent = `+${increment}`;
    const scoreCounter = document.querySelector('.score-counter');
    scoreCounter.appendChild(incrementElem);
    setTimeout(() => incrementElem.remove(), 3000);
    setTimeout(() => scoreDisplayElem.textContent = score,1000);
    setTimeout(() => {	  
      scoreDisplayElem.classList.add('animate-glow');
      setTimeout(() => scoreDisplayElem.classList.remove('animate-glow'), 3000);
      },500);
    };
  const showNextLevelButton= () => Utils.showNextLevelButton(
    document.querySelector('.score-text'),
    '<span class="emoji">🎉</span>',
    () => window.location.href = nextLevelUrl
    );
  const handleCorrectAnswer= (increment) => {
    score += increment;
    questions[currentQuestionIndex].solved = true;
    speedUp();
    currentBonusElem.textContent = increment;
    currentPointsElem.textContent = score;
    showScoreAnimation(increment);
    if (score >= requiredPoints){ showNextLevelButton(); }
    };
  const handleIncorrectAnswer= Log.tag('handleIncorrectAnswer', (currentQuestion) => {
    //Log.log(true,'showing answer for incorrect Q' + getQuestionNumber(currentQuestion));
    streak = 0;
    currentBonusElem.textContent = 1;
    currentQuestion.toSolution();
    currentQuestion.selectionEvent();
    Buttons.freezeFor(4050);
    displayExplanationMessage(currentQuestion.requiredOption);
    });
  const displayExplanationMessage = (requiredOption) => {
    const explanation = OptionExplanations[requiredOption] || Utils.error('bad button id '+requiredOption);
    const msg =`
	  <div>
	    <p><strong>You stumble and fall.</strong></p>
		<p>See the text currently selected.</p>
		<p>The right button was "<strong>${explanation}</strong>".</p>
		<hr>
	    <p>Minigame explanation:</p>
	    <ul>
	      <li>👀Look for the character selected in the code.</li>
	      <li>🖱️Select the smallest cohesive amount of code around that character.</li>
	      <li>☑️Click on the button corresponding to the kind of text you selected.</li>
	      <li>🚨There is an error around the selected character? Just press "Error".</li>
		  <li>🎲This puzzle is all about learning patterns via trial and error.</li>
	    </ul>
		<hr>
		<p>☑️ Click here to make this message disappear</p>
		<p>🎉 At the end, you can go to the next level by pressing on the symbol <span class="emoji">🎉</span>.</p>
	  </div>
	  `;
    Utils.showMessageBox(msg, 4000, true, Buttons.freezeToken, nextQuestion);
    };	
  const handleButtonClick = Log.tag('handleButtonClick', (option) => {
    Buttons.freezeFor(500);
    const currentQuestion = questions[currentQuestionIndex];
    const nope= !currentQuestion.isCorrectAnswer(option);
    if (nope){ handleIncorrectAnswer(currentQuestion); return; }
    const increment = Math.pow(2, streak - 1);
    handleCorrectAnswer(increment);
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
  let score= 0;
  let streak= 1;
  const Buttons= initButtons(updateContent, buttonActions);
  Buttons.freezeFor(1500);
  const questions= InitQuestions(Buttons);
  const totalQuestions= questions.length;
  const currentPointsElem= document.getElementById('currentPoints');
  const currentBonusElem= document.getElementById('currentBonus');
  const requiredPoints= MetaData.int(document.body,'required');
  const nextLevelUrl= MetaData.str(document.body,'next');
  const requiredPointsElem= document.getElementById('requiredPoints');
  const scoreDisplayElem= document.querySelector('.score-display');
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
});