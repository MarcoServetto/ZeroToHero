document.addEventListener('DOMContentLoaded', () => {
  const optionExplanations = {
    1: 'a binding',
    2: 'a method call',
    3: 'an object literal',
    4: 'a method declaration',
    5: 'a type declaration',
    6: 'a type',
    7: 'a comment',
    8: 'an error',
	};
  let currentQuestionIndex = 0;
  let score = 0;
  let streak = 1;
  const questions = document.querySelectorAll('.overlayTextarea');
  const totalQuestions = questions.length;
  const currentPointsElem = document.getElementById('currentPoints');
  const currentBonusElem = document.getElementById('currentBonus');
  const requiredPoints = Utils.metaDataInt(document.body,'required');
  const nextLevelUrl = Utils.metaData(document.body,'next');
  const requiredPointsElem = document.getElementById('requiredPoints');
  const scoreDisplayElem = document.querySelector('.score-display');
  const backLayer = document.querySelector('.back-layer');
  const frontLayer = document.querySelector('.front-layer');
  const solved = new Array(totalQuestions).fill(false);
  const nextQuestion = () => {
	//alert("current solved? "+solved[currentQuestionIndex]);
    if (solved.every(q => q)) {
		//alert("all solved");
		solved.fill(false); }
    let i = (currentQuestionIndex + 1) % totalQuestions;
    while (solved[i]){i = (i + 1) % totalQuestions;}
    currentQuestionIndex = i;
	//alert("next solved? "+solved[currentQuestionIndex]);
    updateContent();
    };
  const updateContent = () => {
    questions.forEach(q => q.classList.remove('active'));
	questions.forEach(q => setUpTextArea(q, Utils.metaDataInt(q,'red')));
    const currentQuestion = questions[currentQuestionIndex];
    currentQuestion.classList.add('active');
	currentQuestion.value = currentQuestion.dataset.original;
    currentQuestion.selectionStart = 0;
    currentQuestion.selectionEnd = 0;
	requiredPointsElem.textContent = requiredPoints;
	resetAnimationSpeed();
    };
  const speedUp = () => {
	streak = streak + 1;
    };
  const resetAnimationSpeed = () => {
    backLayer.style.animationDuration = (1000000*Math.pow(0.3981,streak)+'s');
    frontLayer.style.animationDuration = (500000*Math.pow(0.3981,streak)+'s');
  };
  const showScoreAnimation = (increment) => {
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
  const highlightCharacter= (textarea,charNum) => highlight(textarea,charNum,charNum+1);
  const highlight= (textarea,a,b) => {
	textarea.focus();
	textarea.setSelectionRange(a,b);
	};
  const checkSelection= (textarea,charNum) => {
    const selectionStart = textarea.selectionStart;
    const selectionEnd = textarea.selectionEnd;
	const included= selectionStart <= charNum && selectionEnd > charNum;
	if (!included){ highlightCharacter(textarea,charNum); return; }
    };
  const setUpTextArea= (textarea,charNum) => {
	const cs=()=>setTimeout(checkSelection(textarea,charNum),10);
    textarea.addEventListener('mouseup', cs);
    textarea.addEventListener('select', cs);
    textarea.addEventListener('keydown', (event) => event.preventDefault());
	highlightCharacter(textarea,charNum);
    };
  const keepFocus = () => {
	setTimeout(keepFocus,100);
	const currentQuestion = questions[currentQuestionIndex];
	if(document.activeElement === currentQuestion){ return; }
	currentQuestion.focus();
	checkSelection(currentQuestion, Utils.metaDataInt(currentQuestion, 'red'));
	}
  const isCorrectAnswer = (option, currentQuestion) => {
    const requiredOption = Utils.metaDataInt(currentQuestion, 'option');
    const isErrorType = requiredOption === 8;
    const selectionStart = currentQuestion.selectionStart;
	const selectionEnd = currentQuestion.selectionEnd;
	const dataSelectionStart = Utils.metaDataInt(currentQuestion, 'selectionstart');
	const dataSelectionEnd = Utils.metaDataInt(currentQuestion, 'selectionend');
    return (option === requiredOption && (isErrorType ||
	  (selectionStart === dataSelectionStart && selectionEnd === dataSelectionEnd)));
	};
  const showNextLevelButton= () => Utils.showNextLevelButton(
	document.querySelector('.score-text'),
	'<span class="emoji">🎉</span>',
	() => window.location.href = nextLevelUrl
    );
  const handleCorrectAnswer = (increment) => {
	score += increment;
	solved[currentQuestionIndex] = true;
	speedUp();
	currentBonusElem.textContent = increment;
	currentPointsElem.textContent = score;
	showScoreAnimation(increment);
	if (score >= requiredPoints){ showNextLevelButton(); }
	};
  const handleIncorrectAnswer = (currentQuestion) => {
	streak = 1;
	currentBonusElem.textContent = streak;
    const requiredOption = Utils.metaDataInt(currentQuestion, 'option');
	const isErrorType = requiredOption === 8;
    const start = isErrorType ? 0 : Utils.metaDataInt(currentQuestion, 'selectionstart');
	const end = isErrorType ? currentQuestion.value.length : Utils.metaDataInt(currentQuestion, 'selectionend');
    setTimeout(() => highlight(currentQuestion, start, end), 0);
    displayExplanationMessage(requiredOption);
	setTimeout(nextQuestion, 4000);
	Buttons.freezeFor(4050);
	};

  const displayExplanationMessage = (requiredOption) => {
	const explanation = optionExplanations[requiredOption] || Utils.error('bad button id');
	const msg = `You stumble and fall. It was ${explanation}, see shown selection.`;
	Utils.showMessageBox(msg, 4000);
	};
	
  const handleButtonClick = (option) => {
	const currentQuestion = questions[currentQuestionIndex];
    if (!isCorrectAnswer(option, currentQuestion)){ handleIncorrectAnswer(currentQuestion); return; }
    const increment = Math.pow(2, streak - 1);
	handleCorrectAnswer(increment);
    nextQuestion();
	Buttons.freezeFor(500);
	};
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
  const Buttons = initButtons(updateContent, buttonActions);
  updateContent();
  setTimeout(keepFocus,100);
  const animationTxt = 'scrollFrontBackground 1s linear infinite';
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