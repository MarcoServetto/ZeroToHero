//Replaces multiple whitespace with a single space and trims
const normalize = (text) => text.replace(/\s+/g, ' ').trim();
const metaData = (t,str) => 
  t.dataset[str].replace(/\\n/g, '\n');
const forAllTextArea= (i,consumer) =>
  Array.from(
    document.getElementById('content' + i)
	.querySelectorAll('textarea')
  ).every(consumer);
const checkSolution = () => forAllTextArea(currentIndex,t => {
  const userInput = normalize(t.value);
  const solution = normalize(metaData(t,'solution'));
  return userInput === solution;
  });
const initContent = () => {
  for (let i = 0; i <= maxIndex; i++) {
	forAllTextArea(i,t=> t.value = metaData(t,'original') );
  }
  updateContent();
}
const updateContent = () => {
  for (let i = 0; i <= maxIndex; i++) {
    document.getElementById('content' + i).classList.remove('active');
    }
  document.getElementById('content' + currentIndex).classList.add('active');
  prevBtn.disabled = (currentIndex === 0);
  nextBtn.disabled = (currentIndex === maxIndex);
  };
const showMessageBox = (message) => {
  const messageBox = document.getElementById('gameMessage');
  messageBox.textContent = message;
  messageBox.style.display = 'block';
  setTimeout(() => messageBox.style.display = 'none', 1000);
  };
const freezeButtons = new Set();
const freezeButtonsFor = (time) => {
  const token= {};
  freezeButtons.add(token);
  setTimeout(()=>freezeButtons.delete(token),time);
  };
const initButtons = (buttonActions) => {
  initContent();
  document.querySelectorAll('button').forEach(button => {
    const bid = button.id;
    if (!bid){ return; }
    const action = buttonActions[bid];
    if (!action) {
      const txt= `No action defined for button with id: ${bid}`;
      alert(txt);throw new Error(txt);
    }
    button.addEventListener('click', ()=>{
	  if (freezeButtons.size !== 0){ return; }
	  action();
	  updateContent();
      });
  });
};
//---------------
let currentIndex = 0;
const maxIndex = 3;

initButtons({
  prevBtn: () => {
    if (currentIndex > 0){ currentIndex--; }
    },
  nextBtn: () => {
	if (!checkSolution()){ 
      return showMessageBox("Complete all the text to continue");
	  }
	if (currentIndex < maxIndex){ currentIndex++; }
    },
  resetBtn: () => {
	freezeButtonsFor(1000);
	forAllTextArea(currentIndex,t =>{
	  t.disabled = true;
      t.value = '';
      t.style.backgroundColor = 'rgba(196, 179, 167, 1)';
      setTimeout(() => {
        t.value = metaData(t, 'solution');
        setTimeout(() => {
          t.value = metaData(t, 'original');
          t.disabled = false;
          t.style.backgroundColor = '';
	      }, 400);
	    }, 350);
	  });
    },
  });