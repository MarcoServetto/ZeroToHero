/*export*/ const InitQuestions = (Buttons)=> _postInit(_InitQuestions(Buttons));

const _InitQuestions = (Buttons)=>{
  const res = [];
  for (let i= 0; true; i++) {
    const textarea = document.getElementById('question' + i);
    if (!textarea) { return res; }
    res.push(QuestionText(textarea,Buttons));
    }
  }
const _postInit = Utils.tagAsync('postInit', (Questions) => {
  setTimeout(()=>_postInit(Questions),100);
  Questions.forEach((q)=>q.keepFocus());
  return Questions;
  });
const QuestionText = (q,Buttons) => {
  //Utils.log(true,`QuestionText initialization for ${q.id}`);
  const redChar= Utils.metaDataInt(q, 'red');
  const requiredOption= Utils.metaDataInt(q, 'option');
  const startOk= Utils.metaDataInt(q, 'selectionstart');
  const endOk= Utils.metaDataInt(q, 'selectionend');
  const originalText= Utils.metaData(q,'original');
  let startFix= redChar;
  let endFix= redChar + 1;
  const toSolution= ()=>{ startFix = startOk; endFix = endOk; };
  const toSingle= ()=>{ startFix = redChar; endFix = redChar + 1; };
  const isCorrectAnswer= (option)=>{
    const isErrorType= requiredOption === 8;
    const selectionOk= q.selectionStart === startOk && q.selectionEnd === endOk;
    return option === requiredOption && (isErrorType || selectionOk);
	};
  const active= Utils.tag('active',(flag)=>{
    if(!flag){ q.classList.remove('active'); return; }
    q.classList.add('active');
    q.value = originalText;
    toSingle();
    highlight();
  });
  const highlightRange= Utils.tag('highlightRange', (a, b)=>{
    Utils.log(true, `Highlighting range ${startFix}-${endFix}`);
    setTimeout(() => { q.focus(); q.setSelectionRange(a, b); }, 0);
    //timeout needed: the mouse event can clear the selection at the end of the event management
    return false;
    });
  const highlight= ()=> highlightRange(startFix, endFix);
  const selectionEvent= ()=>{
    Utils.log(true,'selectionEvent for  '+q.id+' '+startFix+ '-'+endFix);
    if (Buttons.isFrozen()){ return highlight(); }
    const start= Number(q.selectionStart) || 0;
    const end= Number(q.selectionEnd) || 0;
    const included= start <= redChar && end > redChar;
    if (!included){ return highlight(); }
    Utils.log(true,'no highlight for  '+q.id+' '+startFix+ '-'+endFix+' -- '+start+' '+end);
    return highlightRange(start,end);
    };
  const keepFocus= Utils.tag('keepFocus',() => {
    if (!q.classList.contains('active')){ return; }
    if (document.activeElement === q){ return; }
    q.focus();
    selectionEvent();
    });
  const selectionEventMouse= Utils.tag('userEvent:mouseUp',selectionEvent);
  /*const selectionEventSelect= Utils.tag('userEvent:select',(e)=>{
    Utils.log(true, `Selection event isTrusted: ${e.isTrusted}`);
    selectionEvent();
  });*/
  q.addEventListener('mouseup', selectionEventMouse);
  //q.addEventListener('select', selectionEventSelect);
  q.addEventListener('keydown', (e) => e.preventDefault());
  return {
    toSolution,toSingle,isCorrectAnswer,active,
    keepFocus,selectionEvent,
    solved:false,requiredOption};
};