/*export*/ const InitQuestions = (Buttons)=>
  _postInit(Deck.list('question')
    .map(q=>QuestionText(q,Buttons)));
    const _postInit = Log.tagAsync('postInit', (Questions) => {
  setTimeout(()=>_postInit(Questions),100);
  Questions.forEach((q)=>q.keepFocus());
  return Questions;
  });
const QuestionText = (q,Buttons) => {
  //Log.log(true,`QuestionText initialization for ${q.id}`);
  const redChar= MetaData.int(q, 'red');
  const requiredOption= MetaData.int(q, 'option');
  const startOk= MetaData.int(q, 'selectionstart');
  const endOk= MetaData.int(q, 'selectionend');
  const originalText= MetaData.str(q,'original');
  let startFix= redChar;
  let endFix= redChar + 1;
  const toSolution= ()=>{ startFix = startOk; endFix = endOk; };
  const toSingle= ()=>{ startFix = redChar; endFix = redChar + 1; };
  const isCorrectAnswer= (option)=>{
    const isErrorType= requiredOption === 8;
    let start = q.selectionStart;
    let end = q.selectionEnd;    
    let trimmedStart = start;
    let trimmedEnd = end;
    const trimMe= (i)=>
      i >= start && i< end && (q.value[i] === '\n' || q.value[i] === ' ');
    while (trimMe(trimmedStart)){ trimmedStart += 1; }
    while (trimMe(trimmedEnd - 1)){ trimmedEnd -= 1; }
    const selectionOk= trimmedStart === startOk && trimmedEnd === endOk;
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
    Log.log(true, `Highlighting range ${startFix}-${endFix}`);
    setTimeout(() => { q.focus(); q.setSelectionRange(a, b); }, 0);
    //timeout needed: the mouse event can clear the selection at the end of the event management
    return false;
    });
  const highlight= ()=> highlightRange(startFix, endFix);
  const selectionEvent= ()=>{
    Log.log(true,'selectionEvent for  '+q.id+' '+startFix+ '-'+endFix);
    if (Buttons.isFrozen()){ return highlight(); }
    const start= Number(q.selectionStart) || 0;
    const end= Number(q.selectionEnd) || 0;
    const included= start <= redChar && end > redChar;
    if (!included){ return highlight(); }
    Log.log(true,'no highlight for  '+q.id+' '+startFix+ '-'+endFix+' -- '+start+' '+end);
    return highlightRange(start,end);
    };
  const keepFocus= Log.tag('keepFocus',() => {
    if (q.hidden){ return; }
    if (document.activeElement === q){ return; }
    q.focus();
    selectionEvent();
    });
  const selectionEventMouse= Log.tag('userEvent:mouseUp',selectionEvent);
  q.addEventListener('mouseup', selectionEventMouse);
  q.addEventListener('keydown', (e) => e.preventDefault());
  return {
    toSolution,toSingle,isCorrectAnswer,active,
    keepFocus,selectionEvent,
    solved:false,requiredOption};
};