const createElem=(obj) => {
  const element = document.createElement(obj.tag);
  obj.classList.forEach(e => {
    element.classList.add(e);
    });
  if (Object.hasOwn(obj, 'id')) {
    element.id = obj.id;
  }
  return element;  
}

const createFlowerBasket = (text, id) => {
  const element = createElem({
    tag: "div",
    classList: ["code-basket"],
    id: `code-basket-${id}`  // Using template literal
  });
  
  const title = document.createElement("div");
  title.classList.add("code-basket-title");
  title.textContent = text; // Add a title for each basket

  const content = document.createElement("div");
  content.classList.add("code-basket-content");

  // Create 9 grid items
  for (let i = 0; i < 9; i++) {
    const gridItem = document.createElement("div");
    gridItem.classList.add("basket-flower");
    gridItem.id = `basket-flower-${id}-${i}`;  // Using template literal
    content.appendChild(gridItem);
  }

  element.appendChild(title);
  element.appendChild(content);

  return element;
}


function createTrashFlowerBasket() {
  const element = document.createElement("div");
  element.classList.add("code-basket"); // Add CSS class instead of inline styles
  element.style.backgroundColor = "darkgrey";
  element.id = "code-basket-trash";

  const title = document.createElement("div");
  title.classList.add("code-basket-title");
  title.style.backgroundColor = "darkgrey";
  title.textContent = "Trash"; // Add a title for each basket

  const content = document.createElement("div");
  content.classList.add("code-basket-content");

  for (let i = 0; i < 9; i++) {
    const gridItem = document.createElement("div");
    gridItem.classList.add("basket-flower");
    gridItem.style.backgroundColor = "grey";
    gridItem.id = "basket-flower-trash" + "-" + i;
    content.appendChild(gridItem);
  }

  element.appendChild(title);
  element.appendChild(content);

  return element;
}

function setupBaskets() {
  // Add 10 components (5 in each row) to the HTML
  const container = document.getElementById("code-baskets");
  let basketList = [];
  for (let i = 0; i < 9; i++) {
    const component = createFlowerBasket(`Basket ${i}`, i);
    container.appendChild(component);
    basketList.push(component.id);
  }

  const trashComponent = createTrashFlowerBasket();
  container.appendChild(trashComponent);
  basketList.push(trashComponent.id);

  return basketList;
}