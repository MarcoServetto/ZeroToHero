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
  const unlockedGlowDelay= 35000;
  let unlockedGlowTimerId= null;
  let unlockedGlowReady= false;
  let unlockedGlowArmedFor= null;
  const armUnlockedGlowTimer= () => {
    if (unlockedGlowArmedFor === currentIndex){ return; }
    unlockedGlowArmedFor= currentIndex;
    clearTimeout(unlockedGlowTimerId);
    unlockedGlowReady= false;
    if (!isUnlocked(currentIndex)){ return; }
    unlockedGlowTimerId= setTimeout(() => {
      unlockedGlowReady= true;
      refreshNextButton();
      }, unlockedGlowDelay);
    };
  const exampleButtonDelay= 60000;
  let exampleButtonTimerId= null;
  let exampleButtonReady= false;
  let exampleButtonArmedFor= null;
  const refreshExampleButton= () => {
    example.hidden= !(isExampleSlide(currentIndex) && exampleButtonReady);
    };
  const armExampleButtonTimer= () => {
    if (exampleButtonArmedFor === currentIndex){ return; }
    exampleButtonArmedFor= currentIndex;
    clearTimeout(exampleButtonTimerId);
    exampleButtonReady= false;
    if (!isExampleSlide(currentIndex)){ return; }
    exampleButtonTimerId= setTimeout(() => {
      exampleButtonReady= true;
      refreshExampleButton();
      }, exampleButtonDelay);
    };
  const refreshNextButton = () => {
    const atEnd= (currentIndex === maxIndex);
    const canAdvance= !atEnd && (isUnlocked(currentIndex) || checkSolution().length === 0);
    next.disabled = !canAdvance;
    const shouldGlow= canAdvance && (allTextArea(currentIndex).length > 0
      ? checkSolution().length === 0
      : unlockedGlowReady);
    next.classList.toggle('correctGlow', shouldGlow);
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
    armExampleButtonTimer();
    refreshExampleButton();
    armUnlockedGlowTimer();
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
  const appendProtectedText= (el, text) => {
    text.split('\n').forEach((line, i) => {
      if (i>0){ el.append(document.createTextNode('\n')); }
      if (line === ''){ return; }
      const span= document.createElement('span');
      span.className= 'protectedSpan';
      span.textContent= line;
      el.append(span);
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
      appendProtectedText(el, v.slice(r.start, r.end));
      pos= r.end;
      });
    el.append(document.createTextNode(v.slice(pos)));
    };
  const closeZeroGaps= (t) => {
    for (let i=0;i<t.protectedRanges.length-1;i++){
      const a= t.protectedRanges[i], b= t.protectedRanges[i+1];
      if (a.end !== b.start){ continue; }
      const pos= a.end;
      t.value= t.value.slice(0,pos) + ' ' + t.value.slice(pos);
      const selStart= t.selectionStart, selEnd= t.selectionEnd;
      if (selStart>=pos || selEnd>=pos){
        t.setSelectionRange(selStart>=pos?selStart+1:selStart, selEnd>=pos?selEnd+1:selEnd);
        }
      for (let j=i+1;j<t.protectedRanges.length;j++){
        const r= t.protectedRanges[j];
        t.protectedRanges[j]= {start:r.start+1, end:r.end+1};
        }
      }
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
  const lockTextArea= (t) => { t.locked = true; t.disabled = true; };
  const prevBtn= () => { if (currentIndex > 0){ currentIndex--; } };
  const nextBtn= () => {
    if (checkSolution().length === 0){ allTextArea(currentIndex).forEach(lockTextArea); }
    if (currentIndex < maxIndex){ currentIndex++; }
    hidePanicMessage();
    };
  const resetBtn = () => {
    const textAreas = allTextArea(currentIndex);
    textAreas.forEach(t => {
      if (t.locked){ return; }
      t.value = MetaData.str(t, 'original');
      if (t.protectOverlayEl){
        t.protectedRanges = getProtectedRanges(t);
        closeZeroGaps(t);
        renderProtectOverlay(t);
        }
      });
    };
  const hintBtn = () => {
    const tas= allTextArea(currentIndex);
    if (tas.length === 0) { return; }
    const showDelay= 100, showDuration= 1450 * 1.5, freezeBuffer= 450;
    Buttons.freezeFor(showDelay + showDuration + freezeBuffer);
    tas.forEach(t => {
      t.disabled = true;
      t.dataset.tempValue = t.value;
      t.value = '';
      t.style.backgroundColor = 'rgba(196, 179, 167, 1)';
      if (t.protectOverlayEl){ t.protectOverlayEl.style.visibility = 'hidden'; }
      });
    setTimeout(() => tas.forEach(t =>{
      t.value = MetaData.str(t, 'solution');
      }), showDelay);
    setTimeout(() => tas.forEach(t => {
      t.value = t.dataset.tempValue;
      t.disabled = t.locked;
      t.style.backgroundColor = '';
      if (t.protectOverlayEl){
        t.protectOverlayEl.style.visibility = '';
        renderProtectOverlay(t);
        }
      }), showDelay + showDuration);
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
    const typeIntervalMs= 900;
    const cursorArriveWaitMs= 1800;/*matches the CSS glide duration for exampleCursor*/
    const pressHoldMs= 1000;
    const typeChar= (i) => {
      t.value = solution.slice(0, i);
      t.dispatchEvent(new Event('input'));
      if (i === solution.length){ setTimeout(showCursor, 400); return; }
      setTimeout(() => typeChar(i + 1), typeIntervalMs);
      };
    const showCursor= () => {
      moveCursorTo(t);
      exampleCursor.hidden = false;
      setTimeout(pressCursor, 700);
      };
    const pressCursor= () => {
      moveCursorTo(next);
      setTimeout(mimePress, cursorArriveWaitMs);
      };
    const mimePress= () => {
      exampleCursor.classList.add('pressing');
      setTimeout(finish, pressHoldMs);
      };
    const finish= () => {
      exampleCursor.classList.remove('pressing');
      exampleCursor.hidden = true;
      t.disabled = t.locked;
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
    t.locked = false;
    t.protectedRanges = getProtectedRanges(t);
    closeZeroGaps(t);
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
      closeZeroGaps(t);
      renderProtectOverlay(t);
      const currentInput= {};
      tokenLastInput = currentInput;/*update token*/
      if (t.classList.contains("incorrectGlow")){ displayPanicMessage("",1); }
      t.classList.remove("correctGlow", "incorrectGlow");
      let msg= checkSolutionTA(t);
      customErrorMessage= "";
      refreshNextButton();
      if (msg === defaultMsg){ return; }
      if (msg === "") { t.classList.add("correctGlow"); hidePanicMessage(); return; }
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
  const hidePanicMessage= () => {
    clearTimeout(panicToHideId);
    customErrorMessage= "";
    document.getElementById("hintCharacter").hidden= true;
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
    if (checkSolution().length === 0){ return; }
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