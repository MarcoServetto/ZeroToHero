const initSlides = () => {
  let currentIndex = 0;
  const prev = document.getElementById('prevBtn');
  const next = document.getElementById('nextBtn');
  const nextLevelUrl = MetaData.str(document.body,'next');
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
    Deck.hideAll('content');
    document
      .getElementById('content' + currentIndex)
      .hidden = false;
    prev.disabled = (currentIndex === 0);
    next.disabled = (currentIndex === maxIndex);
    if (next.disabled){ Utils.showNextLevelButton(
      document.getElementById('endButtonPlaceholder'),
      '<span class="emoji">🎉</span>',
      () => window.location.href = nextLevelUrl
      );}
    };
  const checkSolution = () => allTextArea(currentIndex).every(t => {
	const userInput = Utils.normalize(t.value);
	const solution = Utils.normalize(MetaData.str(t, 'solution'));
	return userInput === solution;
	});
  const prevBtn = () => { if (currentIndex > 0){ currentIndex--; } };
  const nextBtn = () => {
	if (!checkSolution()) { return Utils.showMessageBox(`
	  <div>
	    <p style="font-size: 2.5ex; text-align: center;"><strong>Complete all the text to continue!</strong></p>
	    <hr>
	    <p>Game explanation:</p>
	    <ul>
	      <li>🖊️ Complete the text area with the needed content.</li>
	      <li>⟳ You can reset the text area to the original content by pressing the blue ⟳ button.</li>
	      <li>✨ This also shows the solution for a moment!</li>
	      <li>🎉 At the end, you can go to the next level by pressing on the symbol <span class="emoji">🎉</span>.</li>
	    </ul>
		<hr>
		<p>☑️ Click here to make this message disappear</p>
	  </div>
      `,0,true,Buttons.freezeToken,()=>{}); }
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
	  t1.value = MetaData.str(t1, 'solution')
	  ), 350);
    setTimeout(() => allTextArea(currentIndex).forEach(t2 => {
      t2.value = MetaData.str(t2, 'original');
      t2.disabled = false;
      t2.style.backgroundColor = '';
      }), 750);
    };
  //init
  for (let i = 0; i <= maxIndex; i++) {
    allTextArea(i).forEach(t => t.value = MetaData.str(t, 'original'));
    }
  updateContent();
  const Buttons = initButtons(updateContent,{nextBtn,prevBtn,resetBtn});
  document.querySelectorAll('img')//force img preloading
    .forEach(img =>img.offsetHeight);
  };
initSlides();