const Book= ()=> {
  const freeze = initTimeoutManager();
  const nextLevelUrl= MetaData.str(document.body,'next');
  const holes= Array.from(document.querySelectorAll('.hole'));
  holes.forEach(hole => hole.addEventListener('click', ()=>{
    const locked= freeze.isLocked();
    if (locked){ return showTimeoutMessage(); }
    showHoleOptions(hole, freeze);
    }));
  const makeOptionEntry= (hole, opt, isCorrect)=>{
    const btn= document.createElement('button');
    btn.textContent= opt;
    btn.style.display= 'block';
    btn.addEventListener('click', ()=>{
      document.body.removeChild(popup);//this needs to be more global
      if (isCorrect){
        hole.textContent= opt;
        //hole.style.color= 'rgb(20,20,20)';
        //hole.style.borderBottom= 'none';
        hole.classList.remove('hole');
        freeze.onCorrect();
        }
      else { freeze.onMistake(); }
      });          
      return bnt;
    };
  const showHoleOptions= (hole, freeze) => {
    const correct= MetaData.str(hole,'correct');
    const list= MetaData.str(hole,'options').split(',');
    
    const popup= document.createElement('div');
    popup.classList.add('bookPopup');
    popup.style.position= 'absolute';
    popup.style.zIndex= '10';
    
    const rect= hole.getBoundingClientRect();
    popup.style.top= (rect.top + rect.height + 1)+'px';//nope px nope nope
    popup.style.left= (rect.left)+'px';

    list.forEach(opt => {
      const btn= makeOptionEntry(hole, opt, opt === correct);
      popup.appendChild(btn);
      });
    document.body.appendChild(popup);
    };
  //HERE: note, we need to remove the px and make it disappear also on clicks on other
  //questions
  const showTimeoutMessage= () => {
    Utils.showMessageBox(`
      <div>
        <p>You need to wait before trying again!</p>
        <p>Please read the context more carefully.</p>
        <hr>
        <p>Click anywhere to close this message.</p>
      </div>
      `,0,true,()=>({unfreeze:()=>{}}),()=>{});
  };
  const initTimeoutManager= () => {
    let lockedUntil= 0;
    let base= 10000; // 10s in ms
    let current= base;
    const isLocked= ()=>{
      const now= Date.now();
      return now < lockedUntil;
    };

  const onMistake= ()=>{
    const now= Date.now();
    current= Math.min(current * 1.5, 60000); // up to 60s
    lockedUntil= now + current;
  };

  const onCorrect= ()=>{
    // Halve the current penalty down to 10s min
    current= Math.max(base, current / 2);
  };

  return {
    isLocked,
    onMistake,
    onCorrect
  };
};