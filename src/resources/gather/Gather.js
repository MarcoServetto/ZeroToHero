//<script type="text/javascript" src="../../resources/BaseJs.js" defer></script>
//<script type="text/javascript" src="../../resources/gather/Gather.js" defer></script>

const Gather= Utils.tagAsync('Gather', () => {
  let beingDragged = null;
  let draggingData = {};
  const mouseDown = Utils.tag('mouseDown', e => {
    beingDragged= e.target.closest('.card, .codeBasket');//let outside
    if (!beingDragged){ return; }
    const rect= beingDragged.getBoundingClientRect();
    draggingData= {//let outside
      offsetX: e.clientX - rect.left,
      offsetY: e.clientY - rect.top,
      startX: rect.left,
      startY: rect.top,
      type: beingDragged.classList.contains('card') ? 'card' : 'codeBasket',
      };
    beingDragged.style.zIndex= 1000;
    });
  const mouseMove = Utils.tag('mouseMove', e => {
    if (!beingDragged){ return; }
    const newX= e.clientX - draggingData.offsetX;
    const newY= e.clientY - draggingData.offsetY;
    beingDragged.style.left = newX + 'px';//needed since we get data in px?
    beingDragged.style.top = newY + 'px';
  });
  // Additional game logic functions, rewritten in your style
  // ...
  document.addEventListener('mousemove', mouseMove);
  //document.addEventListener('mouseup', mouseUp);//no need
  // Initialize buttons if needed
  const buttonActions = {
    // Define button actions here
  };
  const gameArea=    document.getElementById('gameArea');
  const codeBaskets= document.getElementById('codeBaskets');
  const cardDeck=    document.getElementById('cardDeck');
  const hoverCard=   document.getElementById('mouseOverCodeBlock');
  const cardsInDeck= Array.from(cardDeck.querySelectorAll('.card'));
  const allBaskets= Array.from(codeBaskets.querySelectorAll('.codeBasket'));
  cardsInDeck.forEach(card => card.addEventListener('mousedown', mouseDown));

  const Buttons = initButtons(() => {}, buttonActions);
});
Gather();
