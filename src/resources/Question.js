/*export*/ const InitQuestions = (isFrozen)=>
  _postInit(Deck.list('question')
    .map(q=>QuestionText(q,isFrozen)));
    const _postInit = Log.tagAsync('postInit', (Questions) => {
  setTimeout(()=>_postInit(Questions),100);
  Questions.forEach((q)=>q.keepFocus());
  return Questions;
  });
const QuestionText = (q,isFrozen) => {
  const redChar= MetaData.int(q, 'red');
  const requiredOption= MetaData.int(q, 'option');
  const startOk= MetaData.int(q, 'selectionstart');
  const endOk= MetaData.int(q, 'selectionend');
  const originalText= MetaData.str(q,'original');
  let startFix= redChar;
  let endFix= redChar + 1;
  const noRedChar= ()=>Number.isNaN(redChar);
  const extractStr= (str)=>MetaData.str(q, str);
  const extractInt= (str)=>MetaData.int(q, str);
  const toSolution= ()=>{ startFix = startOk; endFix = endOk; };
  const toSingle= ()=>{ startFix = redChar; endFix = redChar + 1; };
  const currentSelectionRange= ()=>{
    let start = q.selectionStart;
    let end = q.selectionEnd;    
    let trimmedStart = start;
    let trimmedEnd = end;
    const trimMe= (i)=>
      i >= start && i< end && (q.value[i] === '\n' || q.value[i] === ' ');
    while (trimMe(trimmedStart)){ trimmedStart += 1; }
    while (trimMe(trimmedEnd - 1)){ trimmedEnd -= 1; }
    return {start:trimmedStart,end:trimmedEnd};  
  };
  const currentSelection= ()=>{
    const range= currentSelectionRange();
    return q.value.slice(range.start, range.end);
    };
  const isCorrectSelection= ()=>{
    const range= currentSelectionRange();
    return range.start === startOk && range.end === endOk;          
    };
  const isCorrectAnswer= (option)=>{
    const isErrorType= requiredOption === 8;
    const selectionOk=isCorrectSelection();
    return option === requiredOption && (isErrorType || selectionOk);
  };
  const active= Log.tag('active',(flag)=>{
    if(!flag){ q.hidden = true; return; }
    q.hidden = false;
    q.value = originalText;
    toSingle();
    highlight();
  });
  const highlightRange= Log.tag('highlightRange', (a, b)=>{
    setTimeout(() => { q.focus(); q.setSelectionRange(a, b); }, 0);
    //timeout needed: the mouse event can clear the selection at the end of the event management
    return false;
    });
  const highlight= ()=> highlightRange(startFix, endFix);
  const selectionEvent= ()=>{
    if (isFrozen()){ return highlight(); }
    const start= Number(q.selectionStart) || 0;
    const end= Number(q.selectionEnd) || 0;
    const included= noRedChar() || (start <= redChar && end > redChar);
    if (!included){ return highlight(); }
    };
  const keepFocus= Log.tag('keepFocus',() => {
    if (q.hidden){ return; }
    if (document.activeElement === q){ return; }
    q.focus();
    selectionEvent();
    });
  const addClass= (str)=> q.classList.add(str);
  const removeClass= (str)=> q.classList.remove(str);
  let postSelect= ()=>{};
  const setPostSelect= (callBack)=>postSelect=callBack;
  const selectionEventMouse = () => {
    setTimeout(selectionEvent, 10);
    setTimeout(postSelect, 11);
    };
  q.addEventListener('mouseup', selectionEventMouse);
  q.addEventListener('mouseleave', selectionEventMouse);
  q.addEventListener('keydown', (e) => e.preventDefault());
  q.addEventListener('mousedown', (e)=>{ q.selectionStart = q.selectionEnd = 0; });

  return {
    toSolution, toSingle, currentSelection,
    isCorrectAnswer, isCorrectSelection,
    active, keepFocus, selectionEvent,
    extractStr, extractInt,
    addClass, removeClass, setPostSelect,
    solved:false, requiredOption};
};