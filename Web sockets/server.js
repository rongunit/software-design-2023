const WebSocket = require('ws')

const wss = new WebSocket.Server({ port: 8080 });

wss.on("connection", (ws) => {
    console.log("New client connected!");

    ws.on("message", (message) => {
        console.log(`Recieved message: ${message}`);
        //ws.send(`Your mesasge: ${message}`)
        //ws.send(message.toString().charAt(0))
        sendMessageBack(ws, message);
    });
});

function sendMessageBack(ws, message) {
    let index = 0;
    //const msgSplit = [...message];
    const interval = setInterval(() => {
        if (index < message.length) {
            ws.send(message.toString().charAt(index));
            index++;
        } else {
            clearInterval(interval);
        }
    }, 1000);
}