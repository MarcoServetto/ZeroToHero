const Utils = {
  _currentStack: [],  // Safe for sync code due to JS single-threading
  tag: (name, fn) => (...args) => {
    Utils._currentStack.push(name);
    try { return fn(...args); }
    finally { Utils._currentStack.pop(); }
    },
  tagAsync: (name, fn) => (...args) => {
    if (Utils._currentStack.length > 0){throw new Error(
      `Async handler "${name}" started with non-empty stack:
      [${Utils._currentStack.join('->')}].
      This suggests a bug in the tag/tagAsync system.`);}
    Utils._currentStack = [name];
    try { return fn(...args); }
    finally { Utils._currentStack = []; }
    },
    log: (cond, msg) => {
      if (!cond) return;
      console.log(`${msg}\nTagStack: [${Utils._currentStack.join('->')}]\n`);
    },
  error:(text) =>{
    alert(text);
    throw new Error(text);
    },
  check:(cond,text) =>{ if(!cond){ Utils.error(text); } },
  normalize: (text) => (' ' + text + ' ')
	.replace(/\s+(?=[^a-zA-Z0-9])/g, '') // Remove spaces before symbols
	.replace(/(?<=[^a-zA-Z0-9])\s+/g, '') // Remove spaces after symbols
    .replace(/\s+/g, ' ') // Collapse remaining spaces
    .trim(),
  metaData: (t, str) => {
	Utils.check(str === str.toLowerCase(),
	  "metadata can not be case sensitive");
	return t.dataset[str].replace(/\\n/g, '\n');
    },
  metaDataInt: (t, str) => parseInt(Utils.metaData(t,str), 10), 
  
  showMessageBox: (message, timeOut, requireClick, Buttons, callback) => {
	  if (!timeOut){ timeOut= 1000; }
    const messageBox = document.getElementById('gameMessage');
	  Utils.check(messageBox,"missing message box");
    messageBox.innerHTML = message;
    messageBox.style.display = 'block';
    const autoMsg= requireClick !== true;
    const t= Buttons.freezeToken();
    /*const endMsg= () => {
      messageBox.style.display = 'none';
      t.unfreeze();      
      if (callback){ callback(); }
      };*/
    const endMsg = Utils.tag('MessageBoxCallBack', () => {
        messageBox.style.display = 'none';
        t.unfreeze();
        if (callback){ callback(); }
    });
    const timeOutF=autoMsg ? endMsg : ()=>messageBox.addEventListener('click', endMsg, { once: true });
    setTimeout(timeOutF, timeOut);
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
  const freezeToken = () => {
    const token = {};
    freezeButtons.add(token);
    return { unfreeze: () => freezeButtons.delete(token) };
    };
  const freezeFor = time => {
    const token = {};
    freezeButtons.add(token);
    setTimeout(() => freezeButtons.delete(token), time);
    };
  const isFrozen = () => freezeButtons.size !== 0;
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
  return {
    freezeFor:freezeFor,
    freezeToken: freezeToken,
    isFrozen: isFrozen,
    };
  };