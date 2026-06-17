(function () {
  const wallEl = document.getElementById("brickWall");
  const pileEl = document.getElementById("brickPile");
  if (!wallEl || !pileEl) return;

  let dragSource = null; // { brick, from: 'pile'|'row', rowIndex, index }
  let uid = 0;

  // ---- build model from server-rendered DOM ----
  function brickFromEl(el) {
    return {
      id: ++uid,
      length: +el.dataset.length,
      code: el.dataset.code || "",
      movable: el.dataset.movable === "true",
    };
  }

  const rows = [...wallEl.querySelectorAll(".row")].map(rowEl => {
    const length = +rowEl.dataset.length;
    const row = { length, cells: new Array(length).fill(null), bricks: new Map() };
    rowEl.querySelectorAll(".brick").forEach(bEl => {
      placeInModel(row, brickFromEl(bEl), +bEl.dataset.index);
    });
    return row;
  });

  const pile = [...pileEl.querySelectorAll(".brick")].map(brickFromEl);

  // ---- model helpers ----
  function fits(row, brick, index, ignoreId) {
    if (index < 0 || index + brick.length > row.length) return false;
    for (let i = index; i < index + brick.length; i++) {
      if (row.cells[i] !== null && row.cells[i] !== ignoreId) return false;
    }
    return true;
  }

  function placeInModel(row, brick, index) {
    for (let i = index; i < index + brick.length; i++) row.cells[i] = brick.id;
    row.bricks.set(brick.id, { brick, index });
  }

  function removeFromModel(row, brickId) {
    const entry = row.bricks.get(brickId);
    if (!entry) return null;
    for (let i = entry.index; i < entry.index + entry.brick.length; i++) row.cells[i] = null;
    row.bricks.delete(brickId);
    return entry.brick;
  }

  // ---- render ----
  function render() {
    renderWall();
    renderPile();
  }

  function renderWall() {
    wallEl.innerHTML = "";
    rows.forEach((row, rowIndex) => {
      const rowEl = document.createElement("div");
      rowEl.className = "row";
      rowEl.dataset.length = row.length;
      rowEl.style.gridTemplateColumns = `repeat(${row.length}, 1fr)`;

      let i = 0;
      while (i < row.length) {
        const brickId = row.cells[i];
        if (brickId === null) {
          rowEl.appendChild(makeCell(rowIndex, i));
          i++;
        } else {
          const { brick, index } = row.bricks.get(brickId);
          rowEl.appendChild(makeBrickEl(brick, "row", rowIndex, index));
          i = index + brick.length;
        }
      }
      wallEl.appendChild(rowEl);
    });
  }

  function renderPile() {
    pileEl.innerHTML = "";
    pile.forEach(brick => pileEl.appendChild(makeBrickEl(brick, "pile")));
  }

  function makeCell(rowIndex, index) {
    const cell = document.createElement("div");
    cell.className = "cell";
    cell.dataset.row = rowIndex;
    cell.dataset.index = index;
    cell.addEventListener("dragover", onCellDragOver);
    cell.addEventListener("dragleave", onCellDragLeave);
    cell.addEventListener("drop", onCellDrop);
    return cell;
  }

  function makeBrickEl(brick, location, rowIndex, index) {
    const el = document.createElement("div");
    el.className = "brick " + (brick.movable ? "movable" : "immovable");
    el.textContent = brick.code || "\u00A0";
    el.style.gridColumn = `span ${brick.length}`;
	el.style.width = `calc(${brick.length} * 64px + ${brick.length - 1} * 8px)`;
    el.dataset.brickId = brick.id;
    el.dataset.length = brick.length;
    el.dataset.code = brick.code;
    el.dataset.movable = brick.movable;
    if (location === "row") {
      el.dataset.row = rowIndex;
      el.dataset.index = index;
    }

    if (brick.movable) {
      el.draggable = true;
      el.addEventListener("dragstart", e => {
        dragSource = { brick, from: location, rowIndex, index };
        el.classList.add("dragging");
        e.dataTransfer.effectAllowed = "move";
        e.dataTransfer.setData("text/plain", String(brick.id));
      });
      el.addEventListener("dragend", () => {
        el.classList.remove("dragging");
        clearHighlights();
      });
    }

    // a brick already in a row is also a drop target (drop at its start cell)
    el.addEventListener("dragover", e => {
      if (location === "row") onCellDragOverAt(e, rowIndex, index);
    });
    el.addEventListener("drop", e => {
      if (location === "row") onCellDropAt(e, rowIndex, index);
    });
    return el;
  }

  // ---- drag onto cells ----
  function onCellDragOver(e) {
    onCellDragOverAt(e, +e.currentTarget.dataset.row, +e.currentTarget.dataset.index);
  }

  function onCellDragOverAt(e, rowIndex, index) {
    if (!dragSource) return;
    const row = rows[rowIndex];
    const ignore = dragSource.from === "row" && dragSource.rowIndex === rowIndex
      ? dragSource.brick.id : null;
    const ok = fits(row, dragSource.brick, index, ignore);
    e.preventDefault();
    e.dataTransfer.dropEffect = ok ? "move" : "none";
    highlight(rowIndex, index, dragSource.brick.length, ok);
  }

  function onCellDragLeave() {
    clearHighlights();
  }

  function onCellDrop(e) {
    onCellDropAt(e, +e.currentTarget.dataset.row, +e.currentTarget.dataset.index);
  }

  function onCellDropAt(e, rowIndex, index) {
    e.preventDefault();
    clearHighlights();
    if (!dragSource) return;
    const row = rows[rowIndex];
    const ignore = dragSource.from === "row" && dragSource.rowIndex === rowIndex
      ? dragSource.brick.id : null;
    if (!fits(row, dragSource.brick, index, ignore)) return;

    detachSource();
    placeInModel(row, dragSource.brick, index);
    dragSource = null;
    render();
    checkWin();
  }

  // ---- pile as a drop target (drag back out of the wall) ----
  pileEl.addEventListener("dragover", e => {
    if (dragSource && dragSource.from === "row") {
      e.preventDefault();
      e.dataTransfer.dropEffect = "move";
    }
  });
  pileEl.addEventListener("drop", e => {
    e.preventDefault();
    if (!dragSource || dragSource.from !== "row") return;
    detachSource();
    pile.push(dragSource.brick);
    dragSource = null;
    render();
  });

  function detachSource() {
    if (dragSource.from === "row") {
      removeFromModel(rows[dragSource.rowIndex], dragSource.brick.id);
    } else {
      const idx = pile.findIndex(b => b.id === dragSource.brick.id);
      if (idx >= 0) pile.splice(idx, 1);
    }
  }

  // ---- highlight helpers ----
  function highlight(rowIndex, index, length, ok) {
    clearHighlights();
    const rowEl = wallEl.children[rowIndex];
    if (!rowEl) return;
    rowEl.querySelectorAll(".cell").forEach(c => {
      const i = +c.dataset.index;
      if (i >= index && i < index + length) {
        c.classList.add(ok ? "drag-ok" : "drag-bad");
      }
    });
  }

  function clearHighlights() {
    wallEl.querySelectorAll(".drag-ok, .drag-bad")
      .forEach(c => c.classList.remove("drag-ok", "drag-bad"));
  }

  // ---- win condition (placeholder) ----
  function checkWin() {
    // TODO: define what "correct" means and detect it here
  }

  render();
})();