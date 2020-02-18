var xhr = new XMLHttpRequest();
var scripts
function getAllScripts() {
    console.log("de scripts worden opgeroepen");
    xhr.open("GET", "Controller?action=GetScripts&type=assync", true);
    xhr.onreadystatechange = getScripsLijst;
    xhr.send(null);

}

function getScripsLijst() {
    console.log("wachten op de scripts");
    var table = document.getElementById("availablescriptstable");
    if (xhr.status == 200 && xhr.readyState == 4) {
        console.log("de scripts zijn aangekomen");
        scripts = JSON.parse(xhr.responseText);
        console.log(scripts);
        for(var i in scripts){
            console.log(scripts[i].Id);
            table.innerHTML +=  ' <tr id="' + scripts[i].naam + '"> <td>'+ scripts[i].naam +'</td> <td>'+ scripts[i].extension +'</td> <td></td> <td></td><td><button type="button" onclick="addToQueue( \'' + scripts[i].naam + '\')">Add to queue</button></td><td><button type="button" onclick="deleteScript()"> X </button></td> </tr>'
        }
    }
}

function addToQueue(element){
    document.getElementById(element).style.border = "3px solid green";

    var ol = document.getElementById("list");

    if(document.getElementById(element+"_id") == null){
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(element));
        li.setAttribute("id", element+"_id"); // added line
        ol.appendChild(li);
    }

}

function removeFromQueue() {
    var select = document.getElementById('list');
    select.removeChild(select.lastChild);
}

function runQueue(){

}