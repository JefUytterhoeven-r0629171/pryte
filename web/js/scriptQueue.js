var xhr = new XMLHttpRequest();
var scripts;
var quescripts = new Array();

function getAllScripts() {
    xhr.open("GET", "Controller?action=GetScripts&type=assync", true);
    xhr.onreadystatechange = getScriptLijst;
    xhr.send(null);
}

function getScriptLijst() {
    var table = document.getElementById("availablescriptstable");
    if (xhr.status == 200 && xhr.readyState == 4) {
        scripts = JSON.parse(xhr.responseText);
        for(var i in scripts){
            table.innerHTML +=  '<tr id="' + scripts[i].id + '"> <td>' + scripts[i].naam +'</td> <td>'+ scripts[i].extension +'</td> <td></td> <td></td><td><button type="button" onclick="addToQueue( \'' + scripts[i].id + '\')">Add to queue</button></td><td><button type="button" onclick="deleteScript()"> X </button></td> </tr>'
        }
    }
}

function deleteScript(){
    xhr.open("GET", "Controller?action=DeleteScript&type=assync", true);
    xhr.onreadystatechange = getScriptLijst;
    xhr.send(null);
}

function addToQueue(element){
    document.getElementById(element).style.border = "3px solid green";
    slider = document.getElementById("myRange");
    uploaddiv = document.getElementById("uploadscript");
    quediv = document.getElementById("queue");
    var ol = document.getElementById("list");
    for(var i = 0 ; i < scripts.length ; i++){
        if(scripts[i].id == element){
            quescripts.push(scripts[i]);
        }
    }
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(quescripts[quescripts.length-1].naam));
        li.setAttribute("id", quescripts[quescripts.length -1].id+"_id"); // added line
        ol.appendChild(li);
        //change slider to make the que vissible and hide the upload script div
        uploaddiv.style.display = "none";
        quediv.style.display = "block"
        slider.value = 1;
}

function removeFromQueue() {
    var select = document.getElementById('list');
    select.removeChild(select.lastChild);
    quescripts.pop();
}

function runQueue(){
    xhr.open("POST", "Controller?action=RunQueScripts&type=assync", true);
    xhr.onreadystatechange = runQueueResult;
    xhr.send(JSON.stringify(quescripts));
}

function runQueueResult() {
    if (xhr.status == 200 && xhr.readyState == 4) {
        qscripts = JSON.parse(xhr.responseText);
        quescripts = qscripts;
        var list = document.getElementById("list");
        list.innerHTML = "";
        for(var i in quescripts){
            var li = document.createElement("li");
            li.appendChild(document.createTextNode(quescripts[i].naam));
            li.appendChild(document.createTextNode(quescripts[i].output));
            li.setAttribute("id", quescripts[quescripts.length -1].id+"_id"); // added line
            list.appendChild(li);
        }
    }
}

function SplitLeftMode(){
    slider = document.getElementById("myRange");
    uploaddiv = document.getElementById("uploadscript");
    quediv = document.getElementById("queue");
    if (slider.value == 0) {
        uploaddiv.style.display = "block";
        quediv.style.display = "none"
    }else{
        uploaddiv.style.display = "none";
        quediv.style.display = "block"
    }
}