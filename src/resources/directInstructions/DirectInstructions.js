const initSlides= () => {
  let currentIndex= 0;
  let customErrorMessage= "";
  const prev = document.getElementById('prevBtn');
  const next = document.getElementById('nextBtn');
  const example = document.getElementById('exampleBtn');
  const exampleCursor = document.getElementById('exampleCursor');
  const gameArea = document.getElementById('gameArea');
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
  const isUnlocked= (i) => {
    const slide= document.getElementById('content' + i);
    return slide !== null && slide.dataset.unlocked === 'true';
    };
  const isExampleSlide= (i) => {
    const slide= document.getElementById('content' + i);
    return slide !== null && slide.dataset.example === 'true';
    };
  const refreshNextButton = () => {
    const atEnd= (currentIndex === maxIndex);
    const canAdvance= !atEnd && (isUnlocked(currentIndex) || checkSolution().length === 0);
    next.disabled = !canAdvance;
    next.classList.toggle('correctGlow', canAdvance);
    if (atEnd){ Utils.showNextLevelButton(
      document.getElementById('endButtonPlaceholder'),
      '<span class="emoji">🎉</span>',
      () => window.location.href = nextLevelUrl
      );}
    };
  const updateContent = () => {
    ensureSlide(currentIndex);
    document.querySelectorAll('.contentItem').forEach(c => c.hidden = true);
    const slide= document.getElementById('content' + currentIndex);
    if (slide !== null){ slide.hidden = false; }
    prev.disabled = (currentIndex === 0);
    example.hidden = !isExampleSlide(currentIndex);
    refreshNextButton();
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
  const getOrSolutions= (t)=>{
    const orStr= MetaData.str(t, 'orsolution');
    if (!orStr){ return []; }
    return orStr.split('|###|').map(s => Utils.normalize(s));
    }
  const checkSolutionTA= (t)=>{
    const userInput = Utils.normalize(t.value);
    const solution = Utils.normalize(MetaData.str(t, 'solution'));
    const alts= getAlternativePairs(t);
    if (userInput === solution){ return ""; }
    if (getOrSolutions(t).includes(userInput)){ return ""; }
    for (const { altSolution, altMessage } of alts){
      if (userInput === altSolution){ return altMessage; }
      }
    return defaultMsg;
    }
  const checkSolution= () => allTextArea(currentIndex)
    .map(checkSolutionTA).filter(s=>s !== "");
  const getProtectedRanges= (t)=>{
    const str= MetaData.str(t, 'protected');
    if (!str){ return []; }
    return str.split(',').map(part => {
      const [start,end]= part.split('-').map(Number);
      return {start,end};
      });
    };
  const renderProtectOverlay= (t)=>{
    const el= t.protectOverlayEl;
    if (!el){ return; }
    el.textContent= '';
    const v= t.value;
    let pos= 0;
    t.protectedRanges.forEach(r => {
      el.append(document.createTextNode(v.slice(pos, r.start)));
      const span= document.createElement('span');
      span.className= 'protectedSpan';
      span.textContent= v.slice(r.start, r.end);
      el.append(span);
      pos= r.end;
      });
    el.append(document.createTextNode(v.slice(pos)));
    };
  const editRange= (t, e)=>{
    let start= t.selectionStart, end= t.selectionEnd;
    if (start === end && e.inputType === 'deleteContentBackward'){ start= Math.max(0, start - 1); }
    if (start === end && e.inputType === 'deleteContentForward'){ end= Math.min(t.value.length, end + 1); }
    return {start, end};
    };
  const guardProtected= (t, e)=>{
    const {start, end}= editRange(t, e);
    const hit= t.protectedRanges.some(r => start < r.end && end > r.start);
    if (hit){ e.preventDefault(); return; }
    const inserted= e.data ? e.data.length : 0;
    const delta= inserted - (end - start);
    if (delta === 0){ return; }
    t.protectedRanges= t.protectedRanges.map(r =>
      start >= r.end ? r : { start: r.start + delta, end: r.end + delta });
    };
  const prevBtn= () => { if (currentIndex > 0){ currentIndex--; } };
  const nextBtn= () => { if (currentIndex < maxIndex){ currentIndex++; } };
  const resetBtn = () => {
    const textAreas = allTextArea(currentIndex);
    textAreas.forEach(t => {
      t.value = MetaData.str(t, 'original');
      if (t.protectOverlayEl){
        t.protectedRanges = getProtectedRanges(t);
        renderProtectOverlay(t);
        }
      });
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
      if (t.protectOverlayEl){ t.protectOverlayEl.style.visibility = 'hidden'; }
      });
    setTimeout(() => tas.forEach(t =>{
      t.value = MetaData.str(t, 'solution');
      }), 100);
    setTimeout(() => tas.forEach(t => {
      t.value = t.dataset.tempValue;
      t.disabled = false;
      t.style.backgroundColor = '';
      if (t.protectOverlayEl){
        t.protectOverlayEl.style.visibility = '';
        renderProtectOverlay(t);
        }
      }), 1550);
    };
  const moveCursorTo= (el) => {
    const g= gameArea.getBoundingClientRect();
    const r= el.getBoundingClientRect();
    exampleCursor.style.left= (r.left + r.width / 2 - g.left) + 'px';
    exampleCursor.style.top= (r.top + r.height / 2 - g.top) + 'px';
    };
  const exampleBtn= () => {
    const tas= allTextArea(currentIndex);
    if (tas.length === 0){ return; }
    const t= tas[0];
    const before= t.value;
    const solution= MetaData.str(t, 'solution');
    const token= Buttons.freezeToken();
    t.disabled = true;
    const typeChar= (i) => {
      t.value = solution.slice(0, i);
      t.dispatchEvent(new Event('input'));
      if (i === solution.length){ setTimeout(showCursor, 400); return; }
      setTimeout(() => typeChar(i + 1), 90);
      };
    const showCursor= () => {
      moveCursorTo(t);
      exampleCursor.hidden = false;
      setTimeout(pressCursor, 700);
      };
    const pressCursor= () => {
      moveCursorTo(next);
      setTimeout(mimePress, 700);
      };
    const mimePress= () => {
      exampleCursor.classList.add('pressing');
      setTimeout(finish, 500);
      };
    const finish= () => {
      exampleCursor.classList.remove('pressing');
      exampleCursor.hidden = true;
      t.disabled = false;
      t.value = before;
      t.dispatchEvent(new Event('input'));
      token.unfreeze();
      };
    t.value = '';
    t.dispatchEvent(new Event('input'));
    setTimeout(() => typeChar(1), 300);
    };
  const textInit= t =>{
    t.value = MetaData.str(t, 'original');
    t.protectedRanges = getProtectedRanges(t);
    if (t.protectedRanges.length > 0){
      const overlay= document.createElement('div');
      overlay.className= 'overlayTextarea protectOverlay';
      overlay.setAttribute('style', t.getAttribute('style'));
      t.parentNode.insertBefore(overlay, t);
      t.classList.add('hasProtected');
      t.protectOverlayEl= overlay;
      renderProtectOverlay(t);
      t.addEventListener('beforeinput', e => guardProtected(t, e));
      }
    let tokenLastInput= {};
    t.addEventListener('input', () => {
      renderProtectOverlay(t);
      const currentInput= {};
      tokenLastInput = currentInput;/*update token*/
      if (t.classList.contains("incorrectGlow")){ displayPanicMessage("",1); }
      t.classList.remove("correctGlow", "incorrectGlow");
      let msg= checkSolutionTA(t);
      customErrorMessage= "";
      refreshNextButton();
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
  const Buttons = initButtons(updateContent,{nextBtn,prevBtn,resetBtn,hintBtn,exampleBtn});
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