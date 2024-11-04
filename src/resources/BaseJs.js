const Utils = {
  normalize: (text) => text.replace(/\s+/g, ' ').trim(),
  metaData: (t, str) => t.dataset[str].replace(/\\n/g, '\n'),
  showMessageBox: (message) => {
    const messageBox = document.getElementById('gameMessage');
    messageBox.textContent = message;
    messageBox.style.display = 'block';
    setTimeout(() => (messageBox.style.display = 'none'), 1000);
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
  }