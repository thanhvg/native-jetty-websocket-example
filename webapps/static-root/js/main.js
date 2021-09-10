const selfLocation = window.location;


/* WebSocket Update */

const urlWebSocket = window.location.toString()
  .replace('/index.html', '/')
  .replace('http://', 'ws://')
  .replace('https://', 'wss://')
  + "events/";


// let isConnected = false;

let wscon = null;


// enable button and text box
function onConnect() {
  const statusEl = document.getElementById("connectionstatus");
  const btnConnectEl = document.getElementById("btnconnect");
  const btnSendEl = document.getElementById("btnsend");

  statusEl.innerHTML = "on";
  btnConnectEl.setAttribute("disabled", true);
  btnSendEl.removeAttribute("disabled");
}

function onDisconnect() {
  const statusEl = document.getElementById("connectionstatus");
  const btnConnectEl = document.getElementById("btnconnect");
  const btnSendEl = document.getElementById("btnsend");

  statusEl.innerHTML = "off";
  btnConnectEl.removeAttribute("disabled");
  btnSendEl.setAttribute("disabled", true);
}

function connect() {
  wscon = new WebSocket(urlWebSocket);

  wscon.onopen = (a) => {
    console.log('connect', a);
    onConnect();
  }

  wscon.onclose = (a) => {
    console.log('close', a);
    onDisconnect()
  }
  wscon.onerror = (a) => {
    console.log('error', a);
    onConnect();
  }
  wscon.onmessage = (msg) => console.log('message', msg)
}

function sendMessage(msg) {
  wscon.send(document.getElementById("msg").value);
}
