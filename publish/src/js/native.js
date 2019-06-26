const CORE = {
  cdn: function() {
    return {
      push: function() {

      },
      get: function() {

      }
    }
  }
}

function getPasswordStrength(password) {
  let strength = 0;

  // Valid
  if(!password.match(/[^a-zA-Z0-9 ]/g) && password.length >= 8) {

    // Numberic
    if(password.match(/\d+/g)) {
      strength += 1;
    }

    // Uppercase / Lowercase
    if(password.match(/[a-zA-Z]+/g)) {
      strength += 1;
    }

    return strength;
  }

  return -1;
}

const Tooltip = {
  open: function(node) {
    let tip = document.getElementById('tooltip');
    tip.innerText = node.getAttribute('tooltip-text');
    node.appendChild(tip);
  },
  close: function(node) {
    let tip = document.getElementById('tooltip');
    let hidden = document.getElementById('hidden');
    tip.innerText = "";
    hidden.appendChild(tip);
  }
}
