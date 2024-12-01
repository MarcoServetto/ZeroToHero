//import { InitQuestions } from './Question.js';
//import { OptionExplanations } from './GameOptions.js';

document.addEventListener('DOMContentLoaded', () => {
  const nextQuestion = Utils.tag('nextQuestion', () => {    
    const completed= questions.every(q => q.solved);
    if (completed){ questions.forEach(q => q.solved = false); }
    let i = (currentQuestionIndex + 1) % totalQuestions;
    while (questions[i].solved){ i = (i + 1) % totalQuestions; }
    currentQuestionIndex = i;
    questions.forEach(q => q.active(false));
    questions[currentQuestionIndex].active(true);
    updateContent();
    });
  const updateContent= Utils.tag('updateContent',() => {
    //questions.forEach(q => q.active(false));
    //questions[currentQuestionIndex].active(true);
    requiredPointsElem.textContent = requiredPoints;
    resetAnimationSpeed();
    });
  const speedUp= ()=>{ streak = streak + 1; };
  const resetAnimationSpeed= () => {
    backLayer.style.animationDuration = (1000000*Math.pow(0.3981,streak)+'s');
    frontLayer.style.animationDuration = (500000*Math.pow(0.3981,streak)+'s');
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
  const handleIncorrectAnswer= Utils.tag('handleIncorrectAnswer', (currentQuestion) => {
    //Utils.log(true,'showing answer for incorrect Q' + getQuestionNumber(currentQuestion));
    streak = 0;
    resetAnimationSpeed();
    streak = 1;
    currentBonusElem.textContent = streak;
    currentQuestion.toSolution();
    currentQuestion.selectionEvent();
    Buttons.freezeFor(4050);
    displayExplanationMessage(currentQuestion.requiredOption);
    });
  const displayExplanationMessage = (requiredOption) => {
    const explanation = OptionExplanations[requiredOption] || Utils.error('bad button id '+requiredOption);
    const msg = `You stumble and fall.
    It was ${explanation}, see the text currently selected.
    Minigame explanation:
      - Look for the character selected in the code.
      - Select the smallest cohesive amout of code around that character.
      - Click on the button corresponding to the kind of text you selected.
      - There is an error around the selected character? just press error.
    `;
    Utils.showMessageBox(msg, 4000, true, Buttons, nextQuestion);
    };	
  const handleButtonClick = Utils.tag('handleButtonClick', (option) => {
    Buttons.freezeFor(500);
    const currentQuestion = questions[currentQuestionIndex];
    const nope= !currentQuestion.isCorrectAnswer(option);
    if (nope){ handleIncorrectAnswer(currentQuestion); return; }
    const increment = Math.pow(2, streak - 1);
    handleCorrectAnswer(increment);
    nextQuestion();
    });
  const buttonActions = {
    btnBinding: () => handleButtonClick(1),
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
  const questions= InitQuestions(Buttons);
  const totalQuestions= questions.length;
  const currentPointsElem= document.getElementById('currentPoints');
  const currentBonusElem= document.getElementById('currentBonus');
  const requiredPoints= Utils.metaDataInt(document.body,'required');
  const nextLevelUrl= Utils.metaData(document.body,'next');
  const requiredPointsElem= document.getElementById('requiredPoints');
  const scoreDisplayElem= document.querySelector('.score-display');
  const backLayer= document.querySelector('.back-layer');
  const frontLayer= document.querySelector('.front-layer');
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