const initSlides= () => {
  let currentIndex= 0;
  let customErrorMessage= "";
  const prev = document.getElementById('prevBtn');
  const next = document.getElementById('nextBtn');
  const nextLevelUrl = MetaData.str(document.body,'next');
  const slideTemplate= i => document.getElementById('slide' + i);
  const maxIndex = (() => {
    if (document.getElementById('content0') === null){ return -1; }
    let i = 1;
    while (slideTemplate(i) !== null){ i++; }
    return i - 1;
    })();
  const allTextArea = i => {
    const slide= document.getElementById('content' + i);
    if (slide === null){ return []; }/*out of range: no slide, no text areas*/
    return Array.from(slide.querySelectorAll('textarea'));
    };
  const updateContent = () => {
    ensureSlide(currentIndex);
    document.querySelectorAll('.contentItem').forEach(c => c.hidden = true);
    const slide= document.getElementById('content' + currentIndex);
    if (slide !== null){ slide.hidden = false; }
    prev.disabled = (currentIndex === 0);
    next.disabled = (currentIndex === maxIndex);
    if (next.disabled){ Utils.showNextLevelButton(
      document.getElementById('endButtonPlaceholder'),
      '<span class="emoji">🎉</span>',
      () => window.location.href = nextLevelUrl
      );}
    refreshOverlay();
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
  const defaultMsg= "Complete all the text to continue!";
  const checkSolutionTA= (t)=>{
    const userInput = Utils.normalize(t.value);
    const solution = Utils.normalize(MetaData.str(t, 'solution'));
    const alts= getAlternativePairs(t);
    if (userInput === solution){ return ""; }
    for (const { altSolution, altMessage } of alts){
      if (userInput === altSolution){ return altMessage; }
      }
    return defaultMsg;
    }
  const checkSolution= () => allTextArea(currentIndex)
    .map(checkSolutionTA).filter(s=>s !== "");
  const prevBtn= () => { if (currentIndex > 0){ currentIndex--; } };
  const showMessageBox= (msg)=> Utils.showMessageBox(`
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
  const msgClass=(e)=> e === defaultMsg ? "" : 'class="customMessage"'; 
  const nextBtn= () => {
    const errs= checkSolution();
    if (errs.length === 0){ 
      if (currentIndex < maxIndex){ currentIndex++; }
      return;
      }
    let msg = errs
      .map(e => `<span ${msgClass(e)}>${e}</span>`)
      .join("<br>");
    return showMessageBox(msg);
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
  const textInit= t =>{
    t.value = MetaData.str(t, 'original');
    let tokenLastInput= {};
    t.addEventListener('input', () => {
      const currentInput= {};
      tokenLastInput = currentInput;/*update token*/
      if (t.classList.contains("incorrectGlow")){ displayPanicMessage("",1); }      
      t.classList.remove("correctGlow", "incorrectGlow");
      let msg= checkSolutionTA(t);
      customErrorMessage= "";
      if (msg === defaultMsg){ return; }
      if (msg === "") { t.classList.add("correctGlow"); return; }
      customErrorMessage= msg;      
      setTimeout(() => {
        if (tokenLastInput !== currentInput){ return; }
        t.classList.add("incorrectGlow");
        displayPanicMessage(msg,15000);  
        }, 1300);      
      });
    };
  let panicToHideId= null;
  const displayPanicMessage= (msg,duration) => {
    clearTimeout(panicToHideId);
    const hintChar= document.getElementById("hintCharacter");
    const speechBubble= hintChar.querySelector(".speechBubble");
    speechBubble.textContent = msg;
    hintChar.hidden = false;
    panicToHideId = setTimeout(()=>{
      customErrorMessage = "";
      hintChar.hidden = true;
      }, duration);          
    };  
  //slides after the first one wait in inert <template>s, so that their images
  //start loading only once the slide before them is fully loaded
  const overlay= Utils.getElementById('screenOverlay');
  const loadingCircle= Utils.getElementById('loadingCircle');
  const created= [];
  const loaded= [];
  let pageLoaded= false;
  let overlayShown= true;/*the page starts on the initial black screen*/
  const setOverlay= (show)=>{
    if (overlayShown === show){ return; }
    overlayShown = show;
    loadingCircle.hidden = !show;
    overlay.style.transition = show ? 'none' : 'opacity 0.5s ease-out';
    overlay.style.opacity = show ? '1' : '0';
    };
  const refreshOverlay= ()=>{
    if (!pageLoaded){ return; }/*BaseJs owns the initial black screen*/
    if (currentIndex > maxIndex){ return; }/*no slide to wait for*/
    setOverlay(!loaded[currentIndex]);
    };
  const loadNextSlide= ()=>{
    if (!pageLoaded){ return; }
    let i= 0;
    while (i <= maxIndex && created[i]){ i++; }
    if (i > maxIndex){ return; }
    ensureSlide(i);
    };
  const slideLoaded= (i)=>{
    if (loaded[i]){ return; }
    loaded[i] = true;
    if (i === currentIndex){ refreshOverlay(); }
    loadNextSlide();
    };
  const ensureSlide= (i)=>{
    if (i < 0 || i > maxIndex || created[i]){ return; }
    created[i] = true;
    if (i > 0){
      const template= slideTemplate(i);
      const slide= document.importNode(template.content.firstElementChild, true);
      template.parentNode.insertBefore(slide, template);
      }
    allTextArea(i).forEach(textInit);
    const img= document.getElementById('content' + i).querySelector('img');
    if (img === null || (img.complete && img.naturalWidth > 0)){ return slideLoaded(i); }
    const done= () => slideLoaded(i);
    const failLoadImg= () => {
      console.error(`Failed to load image for slide ${i}: ${img.src}`);
      done();
      };
    img.addEventListener('load', done, { once: true });
    img.addEventListener('error', failLoadImg, { once: true });
    };
  //init
  updateContent();
  const Buttons = initButtons(updateContent,{nextBtn,prevBtn,resetBtn,hintBtn});
  const InactiveNudge= inactiveNudge(Buttons.isFrozen,30000,()=>{
    const tas= allTextArea(currentIndex);
    if (tas.length === 0) { return; }
    if (customErrorMessage !== ""){ return; }
    displayPanicMessage(nextHint(),8000);
    });
  let messageIndex = 0;
  const hintMessages = [
    "Psst! Try the hint button",
    "It isn’t cheating. Try hints",
    "No shame in using hints",
    "Use hint! It won’t bite",
    "Do learning -- not struggling",
    ];
  const nextHint= ()=>{
    const res= hintMessages[messageIndex];
    messageIndex = (messageIndex + 1) % hintMessages.length;
    return res;
    };
  window.addEventListener("load", () => {//BaseJs is fading the black screen out
    pageLoaded = true;
    overlayShown = false;
    loadingCircle.hidden = true;
    loadNextSlide();
    refreshOverlay();
    });
  };
initSlides();