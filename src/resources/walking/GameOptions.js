'use strict';
/*export*/ const OptionExplanations = (() => {
  const result = {};
  document.querySelectorAll('.roundBtn[data-optionid]').forEach((elem) => {
    const id= MetaData.int(elem,'optionid');
    const explanation= MetaData.str(elem,'optionexplanation');
    const emoji= MetaData.str(elem,'optionemoji');
    Utils.check(!Number.isNaN(id) && explanation && emoji,
      "Bad metadata in option button: missing id, explanation, or emoji");
    result[id] = `<span class="emoji">${emoji}</span> ${explanation}`;
    });
  return result;
})();