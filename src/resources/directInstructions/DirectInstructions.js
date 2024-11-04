const initSlides = () => {
  let currentIndex = 0;
  const prev = document.getElementById('prevBtn');
  const next = document.getElementById('nextBtn');
  //const reset= document.getElementById('resetBtn');
  const maxIndex = (() => {
	let i = 0; 
    while (true) {
      const ci = document.getElementById('content' + i++);
	  if (ci === null){return i - 2;}
      }
	})();
  const allTextArea = i =>
    Array.from(
      document.getElementById('content' + i)
	    .querySelectorAll('textarea')
      );
  const updateContent = () => {
    for (let i = 0; i <= maxIndex; i++) {
	  document.getElementById('content' + i).classList.remove('active');
      }
	document
	  .getElementById('content' + currentIndex)
	  .classList.add('active');
	prev.disabled = (currentIndex === 0);
	next.disabled = (currentIndex === maxIndex);
	};
  const checkSolution = () => allTextArea(currentIndex).every(t => {
	const userInput = Utils.normalize(t.value);
	const solution = Utils.normalize(Utils.metaData(t, 'solution'));
	return userInput === solution;
	});
  const prevBtn = () => {
	if (currentIndex > 0){ currentIndex--; }
	};
  const nextBtn = () => {
	if (!checkSolution()) {
	  return Utils.showMessageBox('Complete all the text to continue');
	  }
	if (currentIndex < maxIndex){ currentIndex++; }
	};
  const resetBtn = () => {
    Buttons.freezeFor(1000);
	allTextArea(currentIndex).forEach(t0 => {
      t0.disabled = true;
      t0.value = '';
      t0.style.backgroundColor = 'rgba(196, 179, 167, 1)';
	  });
    setTimeout(() => allTextArea(currentIndex).forEach(t1 => 
	  t1.value = Utils.metaData(t1, 'solution')
	  ), 350);
    setTimeout(() => allTextArea(currentIndex).forEach(t2 => {
      t2.value = Utils.metaData(t2, 'original');
      t2.disabled = false;
      t2.style.backgroundColor = '';
      }), 750);
    };
  //init
  for (let i = 0; i <= maxIndex; i++) {
    allTextArea(i).forEach(t => t.value = Utils.metaData(t, 'original'));
    }
  updateContent();
  const Buttons = initButtons(updateContent,{nextBtn,prevBtn,resetBtn});
  };
initSlides();