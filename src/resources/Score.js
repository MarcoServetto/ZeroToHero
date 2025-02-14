const Score= (incrementFun) => {
  const scoreDisplayElem= Utils.getElementById('scoreDisplay');
  let score= 0;
  let streak= 1;
  let justFailed= false;
  const adjustScoreFontSize= () => {
    const baseFontSize= 5;
    const minFontSize= 1;
    const scoreLength= score.toString().length;
    const newFontSize= Math.max(minFontSize, baseFontSize - (scoreLength * 0.3));
    scoreDisplayElem.style.fontSize = `${newFontSize}ex`;
    };
  const showScoreAnimation= (increment)=>{
    const incrementElem = document.createElement('span');
    incrementElem.classList.add('scoreIncrement');
    incrementElem.textContent = `+${increment}`;
    const scoreCounter = document.getElementById('scoreCounter');
    scoreCounter.appendChild(incrementElem);
    adjustScoreFontSize();
    setTimeout(()=>incrementElem.remove(), 3000);
    setTimeout(()=>scoreDisplayElem.textContent = score, 1000);
    setTimeout(()=>{    
      scoreDisplayElem.classList.add('animate-glow');
      setTimeout(()=>scoreDisplayElem.classList.remove('animate-glow'), 3000);
      },500);
    };
  const nextIncrement= ()=>incrementFun(streak)
  const doSuccess= ()=>{
    justFailed = false;
    const increment= nextIncrement();
    score += increment;
    streak += 1;
    showScoreAnimation(increment);
    };
  const doMootSuccess= ()=>{
    justFailed = false;
    showScoreAnimation(0);
    };
  const doFailure= ()=>{ streak = 1; justFailed = true; };
  return {
    doSuccess, doFailure, doMootSuccess, nextIncrement, 
    score:()=>score, streak:()=>streak, justFailed:()=>justFailed,
    };
  };