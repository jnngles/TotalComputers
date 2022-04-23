var name = window.location
document.session.name.value = name

var text = document.session.name.value

var ws = new WebSocket("ws://" + window.location.hostname + ":7255/");

const IDbyName = new Map();
const soundByName = new Map();

ws.onopen = function () {

    if (name != null) {
        document.write("Connected to websocket server! <br>");
        ws.send("name:" + delineate(text));
        document.write("Sent data: name:" + delineate(text) + "<br>");
    }

};

ws.onmessage = function (evt) {
    if(IDbyName.has(evt.data)) {
        var sound = soundByName.get(evt.data);
        var id = IDbyName.get(evt.data);
        if(sound.playing(id)) sound.pause(id);
        else sound.play(id);
    } else {
        var sound = new Howl({
            src: ['sounds/'+evt.data]
        });
        soundByName.set(evt.data, sound)
        IDbyName.set(evt.data, sound.play());
    }
};

ws.onclose = function () {
    alert("Closed!");
};

ws.onerror = function (err) {
    alert("Error: " + err);
};

function delineate(str) {
    theleft = str.indexOf("=") + 1;
    theright = str.lastIndexOf("&");
    return (str.substring(theleft, theright));
}