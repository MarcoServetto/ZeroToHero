/*export*/ const initDragging = (isFrozen)=> {
  const dragDropImg= document.getElementById('dragDropImg');
  const gameArea = document.getElementById('gameArea');
  let dragging= false;
  const mouseMove = Log.tag('mouseMove', e => {
    if (!dragging){ return; }
    const half= dragDropImg.getBoundingClientRect().width/2;
    const mouseX= e.clientX - (gameArea.getBoundingClientRect().left+half);
    const mouseY= e.clientY - (gameArea.getBoundingClientRect().top+half);
    dragDropImg.style.left = `${mouseX}px`;
    dragDropImg.style.top =  `${mouseY}px`;
    });
    const makeMouseDown= f => e =>{
      if (isFrozen()){ return; }
      if (dragging){ return; }
      dragging = true;
      window.getSelection().removeAllRanges();
      document.body.classList.add('noSelect');      
      const newImg = document.createElement('img');
      newImg.src = f(e);
      newImg.style.width = '100%';
      newImg.style.height = 'auto';
      dragDropImg.appendChild(newImg);
      dragDropImg.hidden = false;
      mouseMove(e);
      };
    let currentMouseUpCont=e=>{};
    const setMouseUp= f=>{ currentMouseUpCont = f; };
    const mouseUp= e => {
      if (!dragging){ return; }
      dragging = false;
      document.body.classList.remove('noSelect');
      dragDropImg.hidden = true;
      dragDropImg.innerHTML = '';
      currentMouseUpCont(e);
      };
  document.addEventListener('mousemove', mouseMove);
  document.addEventListener('mouseup', mouseUp);
  document.querySelectorAll('img')//avoid browser specific img drag stuff
    .forEach(img => img.setAttribute('draggable', 'false'));
  return {
    isDragging:()=>dragging,
    makeMouseDown,setMouseUp,
    }
  };
/*export*/ const CodeCards = (Dragging, dropEvent)=> {
  const update= ()=> cards.forEach((c,index)=>c.active(index === 0));
  const pop= () => {
    if (cards.length === 0){ return; }
    cards[0].active(false);
    cards.shift();
    update();
    };
  const push= (id) => {
    const elem= document.getElementById('card_'+id);
    cards.unshift(CodeCard(elem,mouseDownF)); 
    update();
    };
  const mouseDownF= Dragging.makeMouseDown((e)=>{
    cards[0].drag();
    Dragging.setMouseUp(mouseUpF);
    return cards[0].imgUrl();
    });
  const mouseUpF = e => {
    const oldId= dropEvent(e,cards[0]);
    cards[0].undrag();
    if (oldId==-2){ return; }//no slot
    pop();
    if (oldId==-1){ return; }//new slot
    push(oldId);
    Dragging.setMouseUp(ee => {});
    };
  const cards= Deck.list('card_').map(q=>CodeCard(q,mouseDownF));
  Utils.check(cards.length!==0,"No cards in init")
  const codeOf= (i)=>{
    const textArea = document
      .getElementById('card_'+i).querySelector('textarea');
    Utils.checkExists(textArea);
    return textArea.value;
    };
  const groupOf= (i)=>{
    Utils.checkExists(i);
    const q= document.getElementById('card_'+i);
    Utils.checkExists(q);
    return MetaData.int(q, 'group');
    };
  update();
  return {pop,push,
    cards:()=>cards,
    get:(i)=> cards[i],
    codeOf,
    groupOf,
    };
  };
const CodeCard = (q,mouseDown) => {
  const cardId= MetaData.int(q,'card_id');
  const imgUrl= "../../resources/gather/images/"
    + MetaData.str(q, 'url');
  const group = MetaData.int(q, 'group');//0 is trash
  const drag= ()=> q.classList.add('draggingCard');
  const undrag= ()=> q.classList.remove('draggingCard');
  const active= (flag)=>{ q.hidden = !flag };
  q.addEventListener('mousedown', mouseDown);
  return {
    active, cardId:()=>cardId,
    group:()=>group, imgUrl:()=>imgUrl,
    drag, undrag
    };
  };