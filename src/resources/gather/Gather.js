const cardTransformScale = 0.4;

let allBaskets = null;
let cardsInDeck = null;

const cardDeck = document.getElementById("card-deck");
const gameBoard = document.getElementById("game-board");

var hoverCard = document.getElementById("hover-card");

let draggingData = {
  startX: 0,
  startY: 0,
  newX: 0,
  newY: 0,
  offsetX: 0,
  offsetY: 0,
  minX: 0,
  minY: 0,
  maxX: 0,
  maxY: 0,
  type: "",
};

let beingDragged = null;

const flowerCards = [
  {
    cardId: "0",
    cardName: "Card 0",
    flowerUrl: "images/Mushroom/BrownM1.png",
    value: "4",
    code: "1 + 3",
  },
  {
    cardId: "1",
    cardName: "Card 1",
    flowerUrl: "images/EggPlant/EggP1.png",
    value: "7",
    code: "4 + 3",
  },
  {
    cardId: "2",
    cardName: "Card 2",
    flowerUrl: "images/Mushroom/BrownM1.png",
    value: "4",
    code: "2 + 2",
  },
  {
    cardId: "3",
    cardName: "Card 3",
    flowerUrl: "images/RedFlower/RedF1.png",
    value: "error",
    code: "2 + 3 - ",
  },
  {
    cardId: "4",
    cardName: "Card 4",
    flowerUrl: "images/YellowFlower/YellowF1.png",
    value: "7",
    code: "8 - 1",
  },
  {
    cardId: "5",
    cardName: "Card 5",
    flowerUrl: "images/EggPlant/EggP1.png",
    value: "4",
    code: "3 + 1",
  },
  {
    cardId: "6",
    cardName: "Card 6",
    flowerUrl: "images/Mushroom/BrownM1.png",
    value: "4",
    code: "10 / 2 - 1",
  },
  {
    cardId: "7",
    cardName: "Card 7",
    flowerUrl: "images/RedFlower/RedF1.png",
    value: "error",
    code: "5 + apple",
  },
  {
    cardId: "8",
    cardName: "Card 8",
    flowerUrl: "images/YellowFlower/YellowF1.png",
    value: "error",
    code: "5 / 0",
  },
  {
    cardId: "9",
    cardName: "Card 9",
    flowerUrl: "images/EggPlant/EggP1.png",
    value: "7",
    code: "14 / 2",
  },
];

/* create beingDragged deck */

function createHoverCard(id) {
  const title = document.createElement("div");
  title.classList.add("flower-card-title");
  title.textContent = `Card ${id}`;

  const content = document.createElement("div");
  content.classList.add("flower-card-content");

  const flowerImageContainer = document.createElement("div");
  flowerImageContainer.classList.add("flower-card-image");
  const flowerImage = document.createElement("img");
  flowerImage.classList.add("flower-card-image");
  flowerImage.src = "./images/RedF1.png";
  flowerImageContainer.appendChild(flowerImage);

  const flowerCode = document.createElement("div");
  flowerCode.classList.add("flower-card-code");
  flowerCode.textContent = "code";

  content.appendChild(flowerImageContainer);
  content.appendChild(flowerCode);

  hoverCard.appendChild(title);
  hoverCard.appendChild(content);

  hoverCard.addEventListener("mousedown", mouseDown);
}

/* create beingDragged deck */

function createCard(id) {
  const data = flowerCards[id];
  const element = document.createElement("div");
  element.classList.add("flower-card");
  element.id = data.cardId;

  const title = document.createElement("div");
  title.classList.add("flower-card-title");
  title.textContent = data.cardName;

  const content = document.createElement("div");
  content.classList.add("flower-card-content");

  const flowerImageContainer = document.createElement("div");
  flowerImageContainer.classList.add("flower-card-image");
  const flowerImage = document.createElement("img");
  flowerImage.classList.add("flower-card-image");
  flowerImage.src = data.flowerUrl;
  flowerImageContainer.appendChild(flowerImage);

  const flowerCode = document.createElement("div");
  flowerCode.classList.add("flower-card-code");
  flowerCode.textContent = data.code;

  content.appendChild(flowerImageContainer);
  content.appendChild(flowerCode);

  element.appendChild(title);
  element.appendChild(content);

  element.addEventListener("mousedown", mouseDown);

  element.setAttribute("value", data.value);

  return element;
}

function setupCardDeck() {
  const cardDeck = document.getElementById("card-deck");
  let cardList = [];
  for (let i = 0; i < flowerCards.length; i++) {
    // TODO - should be flowerCards.length
    const card = createCard(i);
    cardDeck.appendChild(card);
    cardList.push(card.id);
  }
  return cardList;
}

function mouseOver(e) {
  const id = e.target.getAttribute("flower-card-id");
  hoverCard.appendChild(createCard(id));
  hoverCard.style.display = "flex";
}

function mouseOut(e) {
  hoverCard.style.display = "none";
}

function handleMouseDownFlowerCard(e) {
  beingDragged = document.getElementById(cardsInDeck[cardsInDeck.length - 1]); // ensure card is the top one on the deck

  beingDragged.style.transform = `scale(${cardTransformScale})`; // make the card smaller during drag
  beingDragged.style.transition = "transform 0.2s ease";
  beingDragged.style.zIndex = 1000;

  const cardDeckRect = cardDeck.getBoundingClientRect();
  const beingDraggedRect = beingDragged.getBoundingClientRect();

  draggingData.offsetX = e.clientX - beingDraggedRect.left;
  draggingData.offsetY = e.clientY - beingDraggedRect.top;

  draggingData.startX = e.clientX - cardDeckRect.left - draggingData.offsetX;
  draggingData.startY = e.clientY - cardDeckRect.top - draggingData.offsetY;
}

function handleMouseDownBasketFlower(e) {
  beingDragged.removeEventListener("mouseover", mouseOver);
  beingDragged.removeEventListener("mouseout", mouseOut);

  const beingDraggedRect = beingDragged.getBoundingClientRect();

  beingDragged.style.zIndex = 1000;

  draggingData.offsetX = e.clientX - beingDraggedRect.left;
  draggingData.offsetY = e.clientY - beingDraggedRect.top;

  draggingData.startX = beingDraggedRect.left;
  draggingData.startY = beingDraggedRect.top;
}

function mouseDown(e) {
  beingDragged = document.elementFromPoint(e.clientX, e.clientY);
  gameBoard.classList.add("disable-selection");

  if (beingDragged.className.includes("flower-card")) {
    draggingData.type = "flower-card";
    handleMouseDownFlowerCard(e);
  } else if (beingDragged.className.includes("basket-flower")) {
    draggingData.type = "basket-flower";
    handleMouseDownBasketFlower(e);
  }

  document.addEventListener("mousemove", mouseMove);
  document.addEventListener("mouseup", mouseUp);
}

function handleMouseMoveFlowerCard(e) {
  const cardDeckRect = cardDeck.getBoundingClientRect();
  const gameBoardRect = gameBoard.getBoundingClientRect();

  draggingData.newX = e.clientX - cardDeckRect.left - draggingData.offsetX;
  draggingData.newY = e.clientY - cardDeckRect.top - draggingData.offsetY;

  draggingData.minX =
    cardDeckRect.width * (0.5 + cardTransformScale / 2) - gameBoardRect.width;
  draggingData.maxX = cardDeckRect.width * (0.5 - cardTransformScale / 2);

  draggingData.maxY =
    gameBoardRect.height - cardDeckRect.height * (0.5 + cardTransformScale / 2);
  draggingData.minY = -cardDeckRect.height * (0.5 - cardTransformScale / 2);
}

function handleMouseMoveBasketFlower(e) {
  const basketFlowerRect = beingDragged.getBoundingClientRect();
  const gameBoardRect = gameBoard.getBoundingClientRect();

  draggingData.newX = e.clientX - draggingData.startX - draggingData.offsetX;
  draggingData.newY = e.clientY - draggingData.startY - draggingData.offsetY;

  draggingData.minX = gameBoardRect.left - draggingData.startX;
  draggingData.maxX =
    gameBoardRect.right - basketFlowerRect.width - draggingData.startX;

  draggingData.minY = gameBoardRect.top - draggingData.startY;
  draggingData.maxY =
    gameBoardRect.bottom - basketFlowerRect.height - draggingData.startY;
}

function mouseMove(e) {
  if (draggingData.type === "flower-card") {
    handleMouseMoveFlowerCard(e);
  } else if (draggingData.type === "basket-flower") {
    handleMouseMoveBasketFlower(e);
  }

  if (draggingData.newX > draggingData.maxX) {
    draggingData.newX = draggingData.maxX;
  } else if (draggingData.newX < draggingData.minX) {
    draggingData.newX = draggingData.minX;
  }

  if (draggingData.newY < draggingData.minY) {
    draggingData.newY = draggingData.minY;
  } else if (draggingData.newY > draggingData.maxY) {
    draggingData.newY = draggingData.maxY;
  }

  beingDragged.style.top = draggingData.newY + "px";
  beingDragged.style.left = draggingData.newX + "px";
}

function allBadCodeInTrash() {
  return noBadCodeInOtherBaskets();
}

function noBadCodeInFlowerList(flowerList) {
  for (let i = 0; i < flowerList.length; i++) {
    const id = flowerList[i];
    if (flowerCards[id].value === "error") {
      return false;
    }
  }
  return true;
}

function getFlowersInBasket(id) {
  let result = [];

  // Get the element by its ID
  const basket = document.getElementById(id);

  // Check if the element exists
  if (basket) {
    // Loop through all its children
    for (let i = 0; i < basket.children.length; i++) {
      const child = basket.children[i];

      if (child.className === "code-basket-content") {
        for (let j = 0; j < child.children.length; j++) {
          // Check if the child has the attribute "flower-card-id"
          const grandChild = child.children[j];
          if (grandChild.hasAttribute("flower-card-id")) {
            // Add the value of the attribute to the result list
            result.push(grandChild.getAttribute("flower-card-id"));
          }
        }
      }
    }
  }

  return result;
}

function noBadCodeInOtherBaskets() {
  for (let i = 0; i < allBaskets.length; i++) {
    const id = allBaskets[i];
    const flowers = getFlowersInBasket(id);
    if (id !== "code-basket-trash" && !noBadCodeInFlowerList(flowers)) {
      return false;
    }
  }
  return true;
}

function allTrashIsBadCode() {
  const flowers = getFlowersInBasket("code-basket-trash");
  for (let i = 0; i < flowers.length; i++) {
    const tile = flowerCards[flowers[i]];
    if (tile.value !== null && tile.value !== "error") {
      return false;
    }
  }
  return true;
}

function eachCodeInEachBasketIsTheSame() {
  for (let i = 0; i < allBaskets.length; i++) {
    const basket = allBaskets[i];
    const flowers = getFlowersInBasket(basket);
    // for each basket
    if (flowers.length === 0) {
      // if array is empty...
      continue; // no need to check. go to next basket
    }

    const allNull = flowers.every((element) => element === null);
    if (allNull) {
      // if every flower in a basket is null...
      continue; // no need to check. go to next basket
    }

    var firstValue = ""; // the first flower group found in the basket

    for (let j = 0; j < flowers.length; j++) {
      const flower = flowerCards[flowers[j]];
      if (flower.value !== null) {
        firstValue = flower.value; // we will compare every other flower to this one
        break;
      }
    }

    for (let j = 0; j < flowers.length; j++) {
      const flower = flowerCards[flowers[j]];
      if (flower.value !== null && flower.value !== firstValue) {
        // if this flower has a different flower group...
        return false; // then at least two flowers in a single basket are not the same value
      }
    }
  }
  return true; // no problem found
}

function getValueOfUniformBasket(basketId) {
  const flowerIds = getFlowersInBasket(basketId);
  const flower = flowerCards[flowerIds[0]];
  return flower.value;
}

function allBasketsAreUnique() {
  if (!eachCodeInEachBasketIsTheSame()) {
    // if a basket contains more than one type of code...
    return false; // Baskets can't be unique if they are multi-valued
  }

  var values = []; // to hold the value of each basket
  // for each basket
  for (let i = 0; i < allBaskets.length; i++) {
    const basketId = allBaskets[i];
    const flowers = getFlowersInBasket(basketId);
    if (flowers.length === 0) {
      // if list of flowers is empty
      continue; // skip this basket (multiple baskets can be empty)
    }
    const basketValue = getValueOfUniformBasket(basketId);
    values.push(basketValue);
  }

  if (values.length === 0) {
    return true; // if no baskets have values, then all baskets are unique
  }

  for (var i = 0; i < values.length - 1; i++) {
    // for each unique pair of basket values
    for (var j = i + 1; j < values.length; j++) {
      if (values[i] === values[j] && values[i] !== "null") {
        // if two baskets have the same non-null value
        return false; // then not all baskets are unique
      }
    }
  }

  return true;
}

function checkSubmit() {
  const message = document.getElementById("card-deck-text");

  if (!allBadCodeInTrash()) {
    message.innerText = "wrong answer! not all bad code is in trash!";
  } else if (!allTrashIsBadCode()) {
    message.innerText = "wrong answer! some good code is in the bin!";
  } else if (!eachCodeInEachBasketIsTheSame()) {
    message.innerText =
      "wrong answer! not all code in baskets grouped correctly!";
  } else if (!allBasketsAreUnique()) {
    message.innerText = "wrong answer! not all baskets are unique!";
  } else {
    message.innerText = "you win!";
  }
}

function makeFlowerTile(id) {
  const flower = flowerCards[id];

  const flowerImage = document.createElement("img");
  flowerImage.classList.add("flower-tile");
  flowerImage.src = flower.flowerUrl;

  return flowerImage;
}

function handleMouseUpFlowerCard(e) {
  let droppedOnBasket = false;

  const elementsUnderMouse = document.elementsFromPoint(e.clientX, e.clientY);
  elementsUnderMouse.forEach((element) => {
    if (
      element.id &&
      element.id.startsWith("basket-flower-") &&
      element.style.backgroundColor !== "red"
    ) {
      // if drop over basket flower grid
      element.setAttribute("original-color", element.style.backgroundColor);
      // element.style.backgroundColor = "red"; // Set the color of the div to red

      element.appendChild(makeFlowerTile(beingDragged.id));

      element.setAttribute("flower-card-id", beingDragged.id);
      element.addEventListener("mousedown", mouseDown);

      element.addEventListener("mouseover", mouseOver);
      element.addEventListener("mouseout", mouseOut);

      cardsInDeck.pop();
      beingDragged.remove();
      droppedOnBasket = true;
    }
  });

  if (!droppedOnBasket) {
    beingDragged.style.transform = "scale(1)"; // Return to the original size
    beingDragged.style.transition = "transform 0.2s ease";

    beingDragged.style.top = draggingData.startY + "px";
    beingDragged.style.left = draggingData.startX + "px";
  }

  if (cardsInDeck.length !== 0) {
    // if deck not empty
    beingDragged = document.getElementById(cardsInDeck[cardsInDeck.length - 1]); // the top card in the deck is now being dragged
    beingDragged.addEventListener("mousedown", mouseDown);
  } else {
    // if deck is empty
    const message = document.createElement("div");
    message.className = "top-element";
    message.id = "card-deck-text";
    message.innerText = "Card deck is empty!";

    const submitButton = document.createElement("button");
    submitButton.className = "bottom-button";
    submitButton.innerText = "Submit";

    submitButton.onclick = function () {
      checkSubmit();
    };

    cardDeck.appendChild(message);
    cardDeck.appendChild(submitButton);
  }
}

function handleMouseUpBasketFlower(e) {
  const elementsUnderMouse = document.elementsFromPoint(e.clientX, e.clientY);

  let addToDeck = true;

  elementsUnderMouse.forEach((element) => {
    if (
      // if drop on another flower tile, do nothing
      element.id &&
      element.id !== beingDragged.id &&
      element.id.startsWith("basket-flower-") &&
      element.style.backgroundColor === "red"
    ) {
      beingDragged.style.left = 0;
      beingDragged.style.top = 0;
      addToDeck = false;
    } else if (
      // if drop on empty tile, swap
      element.id &&
      element.id !== beingDragged.id &&
      element.id.startsWith("basket-flower-")
    ) {
      element.addEventListener("mousedown", mouseDown); // element is now a flower tile
      element.addEventListener("mouseover", mouseOver);
      element.addEventListener("mouseout", mouseOut);

      const id = beingDragged.getAttribute("flower-card-id");

      element.setAttribute("flower-card-id", id);
      beingDragged.removeAttribute("flower-card-id");

      beingDragged.style.left = 0;
      beingDragged.style.top = 0;

      beingDragged.style.backgroundColor =
        beingDragged.getAttribute("original-color"); // being dragged is now an empty tile

      element.setAttribute("original-color", element.style.backgroundColor);
      // element.style.backgroundColor = "red";

      element.appendChild(makeFlowerTile(id));

      beingDragged.removeEventListener("mousedown", mouseDown); // being dragged is now just an empty tile
      beingDragged.removeEventListener("mouseover", mouseOver);
      beingDragged.removeEventListener("mouseout", mouseOut);

      // TODO - experiment with removing event listener by string name only, not including method name

      // TODO when dragging element, remove mouseover or somehow disable it

      addToDeck = false;
    } else if (element.className.startsWith("code-basket")) {
      // if drop in same spot
      beingDragged.style.left = 0;
      beingDragged.style.top = 0;
      addToDeck = false;
    }
  });

  if (addToDeck) {
    beingDragged.style.left = 0;
    beingDragged.style.top = 0;
    beingDragged.style.backgroundColor =
      beingDragged.getAttribute("original-color"); // being dragged is now just a tile
    beingDragged.removeEventListener("mousedown", mouseDown);
    beingDragged.removeEventListener("mouseover", mouseOver);
    beingDragged.removeEventListener("mouseout", mouseOut);

    hoverCard.style.display = "none"; // hide hover card

    // make a new card with the flower card id and add to deck
    const attribute = beingDragged.getAttribute("flower-card-id").split("-");
    const id = attribute[2];
    const card = createCard(id);
    cardDeck.appendChild(card);
    cardsInDeck.push(card.id);
  }
}

function mouseUp(e) {
  gameBoard.classList.remove("disable-selection");
  beingDragged.style.zIndex = "";

  if (draggingData.type === "flower-card") {
    handleMouseUpFlowerCard(e);
  } else if (draggingData.type === "basket-flower") {
    handleMouseUpBasketFlower(e);
  }

  document.removeEventListener("mousemove", mouseMove);
  document.removeEventListener("mouseup", mouseUp);
}

allBaskets = setupBaskets();
cardsInDeck = setupCardDeck();
