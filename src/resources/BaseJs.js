const Utils = {
  error:(text) =>{
	alert(text);
	throw new Error(text);
    },
  check:(cond,text) =>{ if(!cond){ Utils.error(text); } },
  normalize: (text) => text.replace(/\s+/g, ' ').trim(),
  metaData: (t, str) => {
	Utils.check(str === str.toLowerCase(),
	  "metadata can not be case sensitive");
	return t.dataset[str].replace(/\\n/g, '\n');
    },
  metaDataInt: (t, str) => parseInt(Utils.metaData(t,str), 10), 
  showMessageBox: (message,timeOut) => {
	if (!timeOut){ timeOut= 1000; }
    const messageBox = document.getElementById('gameMessage');
	Utils.check(messageBox,
	  "missing message box");
    messageBox.textContent = message;
    messageBox.style.display = 'block';
    setTimeout(() => (messageBox.style.display = 'none'), timeOut);
  },  
  showNextLevelButton: (target, innerHTML, onClick ) => {
    if (!target){ return; }//Most times we want to show it and keep it shown
    const buttonContainer = document.createElement('div');
    buttonContainer.classList.add('roundBtn', 'next-level-button');
    const button = document.createElement('button');
    button.innerHTML = innerHTML;
    button.setAttribute('data-tooltip', 'Next Level');
    button.addEventListener('click', onClick);
    buttonContainer.appendChild(button);
    target.replaceWith(buttonContainer);
    return buttonContainer;
    },
  };

const initButtons = (updateContent,buttonActions) => {
  const freezeButtons = new Set();
  const freezeFor = time => {
    const token = {};
    freezeButtons.add(token);
    setTimeout(() => freezeButtons.delete(token), time);
    };
  //init
  document.querySelectorAll('button').forEach(button => {
	const bid = button.id;
	if (!bid){ return; }
	const action = buttonActions[bid];
	if (!action) {
	  const txt= `No action defined for button with id: ${bid}`;
	  alert(txt); throw new Error(txt);
	  }
	button.addEventListener('click', ()=>{
      if (freezeButtons.size !== 0){ return; }
      action();
      updateContent();
      });
    });
  return {freezeFor:freezeFor};
  };