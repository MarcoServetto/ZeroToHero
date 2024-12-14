const Deck= {
  list: (str) => {
    const res = [];
    for (let i= 0; true; i++) {
      const elem = document.getElementById(str + i);
      if (!elem) { return res; }
      res.push(elem);
      }
    },
  hideAll: (str) => Deck.list(str).forEach(c=>c.hidden = true),
  };
const MetaData= {
  str: (t, str) => {
    Utils.check(str === str.toLowerCase(),
      "metadata can not be case sensitive");
    return t.dataset[str].replace(/\\n/g, '\n');
    },
  int: (t, str) => parseInt(MetaData.str(t,str), 10),         
  };
const Log= {
  _currentStack: [],  // Safe for sync code due to JS single-threading
  tag: (name, fn) => (...args) => {
    Log._currentStack.push(name);
    try { return fn(...args); }
    finally { Log._currentStack.pop(); }
    },
  tagAsync: (name, fn) => (...args) => {
    if (Log._currentStack.length > 0){throw new Error(
      `Async handler "${name}" started with non-empty stack:
      [${Log._currentStack.join('->')}].
      This suggests a bug in the tag/tagAsync system.`);}
    Log._currentStack = [name];
    try { return fn(...args); }
    finally { Log._currentStack = []; }
    },
  log: (cond, msg) => {
    if (msg===undefined){ return Log.log(true,cond); }
    if (!cond){ return; }
    console.log(`${msg}\nTagStack: [${Log._currentStack.join('->')}]\n`);
    },
  };
const Utils= {
  error:(text) =>{
    alert(text);
    throw new Error(text);
    },
  checkExists:(value)=>{
    const err= value === null || value === undefined || Number.isNaN(value);
    if(!err){ return value; }
    Utils.error("Value does not exists");
    },
  check:(cond,text) =>{ if(!cond){ Utils.error(text); } },
  normalize: (text) => (' ' + text + ' ')
    .replace(/\s+(?=[^a-zA-Z0-9])/g, '') // Remove spaces before symbols
    .replace(/(?<=[^a-zA-Z0-9])\s+/g, '') // Remove spaces after symbols
    .replace(/\s+/g, ' ') // Collapse remaining spaces
    .trim(),
  
  showMessageBox: (message, timeOut, requireClick, freezeToken, callback) => {
    if (!timeOut){ timeOut= 1000; }
    const messageBox = document.getElementById('gameMessage');
    Utils.check(messageBox,"missing message box");
    messageBox.innerHTML = message;
    messageBox.style.display = 'block';
    const autoMsg= requireClick !== true;
    const t= freezeToken();
    const endMsg = Log.tag('MessageBoxCallBack', () => {
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
//----------- Debugging monkey patching to avoid common mistakes
//(()=>{//Not working. Would need manually wrapping all functions
//  const debugGlobalFunctionProxy = new Proxy(Function.prototype, {
//    get(target, prop, receiver) {
//      if (prop !== 'length'){ return Reflect.get(target, prop, receiver); }
//      throw new Error("Accessing 'length' of a function is discouraged.");
//      }});
//  Object.setPrototypeOf(Function.prototype, debugGlobalFunctionProxy);
//  Log.log("Debug On");
//  })();

