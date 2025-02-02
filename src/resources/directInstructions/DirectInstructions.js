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
  const getAlternativePairs= (t)=>{
    const altStr= MetaData.str(t, 'alternative');
    if (!altStr){ return []; }
    const parts= altStr.split('|###|').map(part => part.trim());
    const pairs= [];
    for (let i= 0; i < parts.length; i += 2) {
      const altSolution= Utils.normalize(Utils.checkExists(parts[i]));
      const altMessage=  Utils.checkExists(parts[i + 1]);
      pairs.push({ altSolution, altMessage });
      }
    return pairs;
    }    
  const checkSolution = () => allTextArea(currentIndex).map(t => {
    const userInput = Utils.normalize(t.value);
    const solution = Utils.normalize(MetaData.str(t, 'solution'));
    const alts= getAlternativePairs(t);
    if (userInput === solution){ return ""; }
    for (const { altSolution, altMessage } of alts){
       if (userInput === altSolution){ return altMessage; }
     }
    return "Complete all the text to continue!";
    }).filter(s=>s !== "");
  const prevBtn = () => { if (currentIndex > 0){ currentIndex--; } };
  const nextBtn = () => {
    const errs= checkSolution();
    if (errs.length === 0){ 
      if (currentIndex < maxIndex){ currentIndex++; }
      return;
      }
    let msg= errs.join("<BR>");
    return Utils.showMessageBox(`
      <div>
        <p style="font-size: 2.5ex; text-align: center;">
          <strong>${msg}</strong>
          </p>
        <hr>
        <p>Game explanation:</p>
        <ul>
          <li>🖊️ Complete the text area with the needed content.</li>
          <li>⟳ You can reset the text area to the original content by pressing the blue ⟳ button.</li>
          <li>❓ You can see a solution hint via the ❓ button.</li>
          <li>🎉 At the end, you can go to the next level by pressing on the symbol <span class="emoji">🎉</span>.</li>
          </ul>
        <hr>
        <p>☑️ Click here to make this message disappear</p>
        </div>
      `,0,true,Buttons.freezeToken,()=>{});
    };
  const resetBtn = () => {
    const textAreas = allTextArea(currentIndex);
    textAreas.forEach(t => t.value = MetaData.str(t, 'original'));
    };
  const hintBtn = () => {
    const tas= allTextArea(currentIndex);
    if (tas.length === 0) { return; }
    Buttons.freezeFor(2000);
    tas.forEach(t => {
      t.disabled = true;
      t.dataset.tempValue = t.value;
      t.value = '';
      t.style.backgroundColor = 'rgba(196, 179, 167, 1)';
      });
    setTimeout(() => tas.forEach(t =>{
      t.value = MetaData.str(t, 'solution');
      }), 100);
    setTimeout(() => tas.forEach(t => {
      t.value = t.dataset.tempValue;
      t.disabled = false;
      t.style.backgroundColor = '';
      }), 1550);
    };
  //init
  for (let i = 0; i <= maxIndex; i++) {
    allTextArea(i).forEach(t => t.value = MetaData.str(t, 'original'));
    }
  updateContent();
  const Buttons = initButtons(updateContent,{nextBtn,prevBtn,resetBtn,hintBtn});
  document.querySelectorAll('img')//force img preloading
    .forEach(img =>img.offsetHeight);
  };
initSlides();