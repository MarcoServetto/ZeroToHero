document.addEventListener("DOMContentLoaded", () => {

    let draggedBrick = null;

    document.querySelectorAll(".movable").forEach(brick => {
        brick.addEventListener("dragstart", () => {
            draggedBrick = brick;
        });
    });

    document.querySelectorAll(".dropSlot").forEach(slot => {

        slot.addEventListener("dragover", event => {
            event.preventDefault();
        });

        slot.addEventListener("drop", event => {
            event.preventDefault();

            if (!draggedBrick) {
                return;
            }

            const row = slot.dataset.row;
            const startIndex = parseInt(slot.dataset.index, 10);
            const brickLength = parseInt(draggedBrick.dataset.length, 10);

            if (!canPlaceBrick(row, startIndex, brickLength)) {
                return;
            }

            placeBrick(draggedBrick, row, startIndex);
            draggedBrick = null;

            checkForVictory();
        });
    });

    function canPlaceBrick(row, startIndex, length) {

        for (let i = startIndex; i < startIndex + length; i++) {

            const slot = document.querySelector(
                `.dropSlot[data-row="${row}"][data-index="${i}"]`
            );

            if (!slot) {
                return false;
            }

            if (slot.classList.contains("occupied")) {
                return false;
            }
        }

        return true;
    }

    function placeBrick(brick, row, startIndex) {

        const firstSlot = document.querySelector(
            `.dropSlot[data-row="${row}"][data-index="${startIndex}"]`
        );

        if (!firstSlot) {
            return;
        }

        const length = parseInt(brick.dataset.length, 10);

        brick.style.position = "static";
        brick.style.top = "";
        brick.style.left = "";
        brick.style.width = `${length * 60}px`;

        brick.draggable = false;
        brick.classList.add("locked");

        firstSlot.parentElement.insertBefore(brick, firstSlot);

        for (let i = startIndex; i < startIndex + length; i++) {

            const slot = document.querySelector(
                `.dropSlot[data-row="${row}"][data-index="${i}"]`
            );

            if (slot) {
                slot.classList.add("occupied");
                slot.style.display = "none";
            }
        }
    }

    function checkForVictory() {

        const targetElement = document.getElementById("targetCount");

        if (!targetElement) {
            return;
        }

        const required = parseInt(targetElement.dataset.value, 10);
        const placed = document.querySelectorAll(".locked").length;

        if (placed >= required) {
            completeLevel();
        }
    }

    function completeLevel() {

        const overlay = document.getElementById("screenOverlay");
        const character = document.getElementById("levelEndCharacter");
        const message = document.getElementById("gameMessage");

        if (overlay) {
            overlay.style.display = "block";
        }

        if (character) {
            character.hidden = false;
        }

        if (message) {
            message.style.display = "block";
            message.textContent = "Puzzle Complete!";
        }
    }
});