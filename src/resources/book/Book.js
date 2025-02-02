const Book = () => {
  let currentPopup = null;
  const initTimeoutManager = () => {
    let lockedUntil = 0;
    const base = 10000; // 10 seconds in ms
    let current = base;
    const isLocked = () => Date.now() < lockedUntil;
    const onMistake = () => {
      const now = Date.now();
      current = Math.min(current * 1.5, 60000);
      lockedUntil = now + current;
      };
    const onCorrect = () => {
      current = Math.max(base, current / 2);
      };
    const getRemainingTime = () => Math.max(lockedUntil - Date.now(), 0);
    return {
      isLocked,
      onMistake,
      onCorrect,
      getRemainingTime
      };
    };
  const updateMissingWordsCount = () => {
    const missingWordsElement = document.getElementById('missingWordsText');
    const count = document.querySelectorAll('.hole').length;
    missingWordsElement.textContent = `Missing words: ${count}`;
    if (count !== 0) { return; }
    Utils.showNextLevelButton(
      document.getElementById('endButtonPlaceholder'),
      '<span class="emoji">🎉</span>',
      () => window.location.href = nextLevelUrl
      );
    };
  const freeze = initTimeoutManager();
  const nextLevelUrl = MetaData.str(document.body, 'next');
  const holes = Array.from(document.querySelectorAll('.hole'));
  
  setInterval(() => {
    const remaining= freeze.getRemainingTime();
    const clockEl= document.getElementById('countDownClock');
    if (remaining > 0){
      const seconds = Math.ceil(remaining / 1000);
      clockEl.textContent = `0:${seconds < 10 ? '0' + seconds : seconds}`;
      clockEl.classList.remove('ready');
      clockEl.classList.add('waiting');
      }
    else{
      clockEl.textContent = 'GO!!'; // a 4-character friendly message
      clockEl.classList.remove('waiting');
      clockEl.classList.add('ready');
      }
    }, 1000);
  holes.forEach(hole =>
    hole.addEventListener('click', () => {
      if (freeze.isLocked()){ return showTimeoutMessage(); }
      if (currentPopup){ 
        document.body.removeChild(currentPopup);
        currentPopup = null;
        }
      showHoleOptions(hole);
      })
    );
  const makeOptionEntry= (hole, opt, isCorrect, popup) => {
    const btn= document.createElement('button');
    btn.textContent = opt;
    btn.style.display = 'block';
    btn.addEventListener('click', () => {
      if (popup && popup.parentNode){
        document.body.removeChild(popup);
        if (currentPopup === popup){ currentPopup = null; }
      }
      if (!isCorrect){ freeze.onMistake(); return; }
      hole.textContent = opt;
      hole.classList.remove('hole');
      updateMissingWordsCount();
      freeze.onCorrect();      
      });
    return btn;
    };
  const showHoleOptions= (hole) => {
    const correct= MetaData.str(hole, 'correct');
    const list= MetaData.str(hole, 'options').split(',');
    const popup = document.createElement('div');
    popup.classList.add('bookPopup');
    popup.style.position = 'absolute';
    popup.style.zIndex = '10';
    const rect = hole.getBoundingClientRect();
    popup.style.top = (rect.top + rect.height + 1) + 'px';
    popup.style.left = rect.left + 'px';
    list.forEach(opt => {
      const btn = makeOptionEntry(hole, opt, opt === correct, popup);
      popup.appendChild(btn);
      });
    document.body.appendChild(popup);
    currentPopup = popup;
    };
  const showTimeoutMessage = () => {
    Utils.showMessageBox(
      `
      <div>
        <p>You need to wait before trying again!</p>
        <p>Please read the context more carefully.</p>
        <hr>
        <p>Click anywhere to close this message.</p>
      </div>
      `,
      0,
      true,
      () => ({ unfreeze: () => {} }),
      () => {}
      );
    };
  //init
  updateMissingWordsCount();
  };
Book();
/*
const Book= ()=> {
  const freeze = initTimeoutManager();
  const nextLevelUrl= MetaData.str(document.body,'next');
  const holes= Array.from(document.querySelectorAll('.hole'));
  holes.forEach(hole => hole.addEventListener('click', ()=>{
    const locked= freeze.isLocked();
    if (locked){ return showTimeoutMessage(); }
    showHoleOptions(hole, freeze);
    }));
  const makeOptionEntry= (hole, opt, isCorrect)=>{
    const btn= document.createElement('button');
    btn.textContent= opt;
    btn.style.display= 'block';
    btn.addEventListener('click', ()=>{
      document.body.removeChild(popup);//this needs to be more global
      if (isCorrect){
        hole.textContent= opt;
        //hole.style.color= 'rgb(20,20,20)';
        //hole.style.borderBottom= 'none';
        hole.classList.remove('hole');
        freeze.onCorrect();
        }
      else { freeze.onMistake(); }
      });          
      return bnt;
    };
  const showHoleOptions= (hole, freeze) => {
    const correct= MetaData.str(hole,'correct');
    const list= MetaData.str(hole,'options').split(',');
    
    const popup= document.createElement('div');
    popup.classList.add('bookPopup');
    popup.style.position= 'absolute';
    popup.style.zIndex= '10';
    
    const rect= hole.getBoundingClientRect();
    popup.style.top= (rect.top + rect.height + 1)+'px';//nope px nope nope
    popup.style.left= (rect.left)+'px';

    list.forEach(opt => {
      const btn= makeOptionEntry(hole, opt, opt === correct);
      popup.appendChild(btn);
      });
    document.body.appendChild(popup);
    };
  //HERE: note, we need to remove the px and make it disappear also on clicks on other
  //questions
  const showTimeoutMessage= () => {
    Utils.showMessageBox(`
      <div>
        <p>You need to wait before trying again!</p>
        <p>Please read the context more carefully.</p>
        <hr>
        <p>Click anywhere to close this message.</p>
      </div>
      `,0,true,()=>({unfreeze:()=>{}}),()=>{});
  };
  const initTimeoutManager= () => {
    let lockedUntil= 0;
    let base= 10000; // 10s in ms
    let current= base;
    const isLocked= ()=>{
      const now= Date.now();
      return now < lockedUntil;
    };

  const onMistake= ()=>{
    const now= Date.now();
    current= Math.min(current * 1.5, 60000); // up to 60s
    lockedUntil= now + current;
  };

  const onCorrect= ()=>{
    // Halve the current penalty down to 10s min
    current= Math.max(base, current / 2);
  };

  return {
    isLocked,
    onMistake,
    onCorrect
  };
};
*/