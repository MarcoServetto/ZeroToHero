'use strict';
const Book = () => {
  const gameArea= Utils.getElementById('gameArea');
  const clockEl=  Utils.getElementById('countDownClock');
  clockEl.addEventListener('click', () => {
    if (!clockEl.classList.contains('ready')) { return; }
    Utils.showMessageBox(
      `
      <div>
        <h2>How to Play</h2>
        <p>
          This is the Book Minigame! Your goal
          is to click on the missing words (the XXXXXXXX)
          and select the correct option.
          When you select a word, if it's right, the word is
          filled in. A wrong guess will make you wait a bit
          before trying again. The more mistakes you make in a row,
          the longer you will have to wait.
        </p>
        <hr>
        <p>Click anywhere to close this message.</p>
      </div>
      `,
      0,
      true,
      () => ({ unfreeze: () => {} }),
      () => {}
      );
    });
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
  
  
  const updateTimerDisplay= () => {
    const remaining = freeze.getRemainingTime();
    if (remaining > 0) {
      const seconds = Math.ceil(remaining / 1000);
      clockEl.textContent = `0:${seconds < 10 ? '0' + seconds : seconds}`;
      clockEl.classList.remove('ready');
      clockEl.classList.add('waiting');
    } else {
      clockEl.textContent = 'GO!!';
      clockEl.classList.remove('waiting');
      clockEl.classList.add('ready');
    }
  };
  setInterval(updateTimerDisplay, 1000);
  
  holes.forEach(hole =>
    hole.addEventListener('click', () => {
      if (freeze.isLocked()){ return showTimeoutMessage(); }
      if (currentPopup){ 
        gameArea.removeChild(currentPopup);
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
        gameArea.removeChild(popup);
        if (currentPopup === popup){ currentPopup = null; }
      }
      if (!isCorrect){ freeze.onMistake(); updateTimerDisplay(); return; }
      hole.replaceWith(document.createTextNode(opt));
      updateMissingWordsCount();
      freeze.onCorrect();      
      });
    return btn;
    };
  const showHoleOptions= (hole) => {
    const correct= MetaData.str(hole, 'correct');
    const list= MetaData.str(hole, 'options').split('|#|');
    const popup = document.createElement('div');
    popup.classList.add('bookPopup');
    popup.style.position = 'absolute';
    popup.style.zIndex = '10';
    const containerRect = gameArea.getBoundingClientRect();
    const holeRect = hole.getBoundingClientRect();
    const relativeTop = ((holeRect.top - containerRect.top + holeRect.height + 1) / containerRect.height) * 100;
    const relativeLeft = ((holeRect.left - containerRect.left) / containerRect.width) * 100;
    popup.style.top = relativeTop + '%';
    popup.style.left = relativeLeft + '%';
    list.forEach(opt => {
      const btn = makeOptionEntry(hole, opt, opt === correct, popup);
      popup.appendChild(btn);
      });
    gameArea.appendChild(popup);
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