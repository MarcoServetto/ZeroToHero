const CheckSolution= (freezeToken,allCards)=>{
  const getBasketIds= (id)=>Deck
    .list('basketSlot_'+id+'_')
    .map(q=>MetaData.int(q,'card_id'))
    .filter(cid=>cid !== -1);
  const allBaskets=  [0,1,2,3,4,5,6,7,8];//except trash basket
  const usedBaskets= allBaskets
    .map(bid=>getBasketIds(bid))
    .filter(ids=>ids.length > 0);
  const groups= ids=>ids.map(allCards.groupOf);
  const noTrashIn= ids=>groups(ids).every(n=>n !== 0);
  const onlyTrashIn= ids=>groups(ids).every(n=>n === 0);
  const allSameIn= ids=>new Set(groups(ids)).size === 1;
  const getSameIn= ids=>Utils.checkExists(new Set(groups(ids)).values().next().value);
  const uniqueGroups= new Set(usedBaskets.map(getSameIn));
  const allBasketsAreUnique= uniqueGroups.size == usedBaskets.length; 
  const allCardsUsed= allCards.cards().length === 0;
  const trashCount = usedBaskets.filter(b => !noTrashIn(b)).length;
  const noTrash= trashCount === 0;
  const onlyTrash= onlyTrashIn(getBasketIds(9));
  const allSame= usedBaskets.every(allSameIn);

  const explanation=(() => {
    if(!allCardsUsed){ return "You have not yet collected all the items."; }
    if (!noTrash){   return "Not all poisonous items are in the trash.<BR>"+trashCount+" toxic code outside the trash."; }
    if (!onlyTrash){ return "Some good items ended up in the trash."; }
    if (!allSame){   return "Some baskets contain items of different kinds."; }
    if (!allBasketsAreUnique){ return "Some items of the same kind are split across baskets.<BR>There are "+uniqueGroups.size+" unique groups but you used "+usedBaskets.length+" baskets"; }
    return "";
    })();
  const msg= `
    <div>
    <p><strong>Items not well sorted. Try again!</strong></p>
    <p><strong style="color: rgb(254, 80, 80);">${explanation}</strong></p>
    <hr>
    <p>Minigame explanation:</p>
    <ul>
      <li>📦 The top-left area contains code shared by all snippets.</li>       
      <li>🖱️🔄 Drag and drop code snippets to organize them.</li>
      <li>🧺 Group code that does the same thing in the same basket.</li>
      <li>🔀 Code that does different things goes into different baskets. 🧺</li>
      <li>🚨 Broken or non-compiling code? Put it in the trash basket.🗑️</li>
      <li>🤔 Think logically to succeed! </li>
      <li>🌱You can try as many times as you like.</li>
      </ul>
    <hr>
    <p>☑️ Click here to make this message disappear</p>
    </div>
    `;
  const nextLevelUrl = MetaData.str(document.body, 'next');
  Utils.checkExists(nextLevelUrl);
  if (explanation.length == 0){ window.location.href = nextLevelUrl; return; }
  Utils.showMessageBox(msg, 1000, true, freezeToken,()=>{});
  };
  
  
const Gather= Log.tagAsync('Gather', () => {
  const codeBaskets = document.getElementById('codeBaskets');
  const mouseOverCodeBlock = document.getElementById('mouseOverCodeBlock');
  const allSlots = Array.from(codeBaskets.querySelectorAll('.basketSlot'));

  const isOverSlot= (x, y) => {
    const elements= document.elementsFromPoint(x, y);
    const slot= elements
      .find(el => el.classList && el.classList.contains('basketSlot'));
    return slot || null;
    };
  const isOverDeck = (x, y) => {
    const deckRect = cardDeck.getBoundingClientRect();
    const cond = (x >= deckRect.left && x <= deckRect.right && y >= deckRect.top && y <= deckRect.bottom);
    return cond;
    };
  const emptySlotItem= {cardId:-1, imgUrl:''};
  const getSlotItem = slot => {
    const cardId = MetaData.int(slot,'card_id');

    const imgUrl = slot.src; 
    return { cardId, imgUrl };
    };
  const setSlotItem = (slot, item) => {
    slot.dataset.card_id = '' + item.cardId;
    const okUrl= item.imgUrl && item.imgUrl.includes('/images/');
    if (okUrl) { slot.src = item.imgUrl;}
    else { slot.removeAttribute('src'); }
    };
 
  const slotDropItem = (x, y, draggedItem, srcSlot) => {
    const destSlot = isOverSlot(x, y);
    const dropOnDeck = isOverDeck(x, y);
    const dropOnSlot = !!destSlot;
    if (!dropOnSlot && !dropOnDeck){ setSlotItem(srcSlot, draggedItem); return; }
    if(dropOnDeck){ allCards.push(draggedItem.cardId); return; }
    const existingItem = getSlotItem(destSlot);
    setSlotItem(srcSlot, existingItem);
    setSlotItem(destSlot, draggedItem);
    };
  const dropEvent= (e,card)=>{
    const slot = isOverSlot(e.clientX,e.clientY);
    if (!slot){ return -2; }
    slot.src = card.imgUrl();
    const oldId= MetaData.int(slot,'card_id');
    slot.dataset.card_id = `${card.cardId()}`;
    console.log(`Placed card ${slot.dataset.card_id} in slot ${slot.id}`);
    return oldId;
    };
  const isFrozen= ()=>Buttons.isFrozen();//to solve circular dep Buttons<->Dragging
  const Dragging= initDragging(isFrozen);
  const allCards= CodeCards(Dragging,dropEvent);
  const slotMouseDown= srcSlot => Dragging.makeMouseDown(ee => {
    const draggedItem = getSlotItem(srcSlot);
    setSlotItem(srcSlot, emptySlotItem);
    Dragging.setMouseUp(e => slotDropItem(e.clientX, e.clientY, draggedItem, srcSlot));
    return draggedItem.imgUrl;
    });
  allSlots.forEach(s => s.addEventListener('mousedown', slotMouseDown(s)));

  const slotMouseEnter= (e) => {
    if(Buttons.isFrozen()){ return; }
    const slot = e.currentTarget;
    const cardId = MetaData.int(slot,'card_id');
    const noCard = cardId === -1;
    mouseOverCodeBlock.hidden = noCard;
    if(noCard){ return; }
    const code= allCards.codeOf(cardId);
    mouseOverCodeBlock.querySelector('textarea').value = code;
    };
  const slotMouseLeave= (e)=>{ mouseOverCodeBlock.hidden = true; };
  allSlots.forEach(s => {
    s.addEventListener('mouseenter', slotMouseEnter);
    s.addEventListener('mouseleave', slotMouseLeave);
    });
  Utils.check(allCards.cards().length!==0,"No cards in init")
  const submitBtn= e=>CheckSolution(Buttons.freezeToken,allCards);
  const buttonActions = {
    submitBtn,
    resetBtn: () => location.reload()
    };
  const Buttons = initButtons(() => {}, buttonActions);
  });

Gather();
/*
TODO:
bug for finish 
block drag/drop (mouse down) if button frozen
just remove all selection all over everywhere all the time

*/