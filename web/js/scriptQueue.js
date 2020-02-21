var xhr = new XMLHttpRequest();
var scripts;
var queueScripts = new Array();

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
    var tbody = document.getElementById('queueTable');
    for(var i = 0 ; i < scripts.length ; i++){
        if(scripts[i].id == element){
            queueScripts.push(scripts[i]);
        }
    }
    var row = tbody.insertRow(-1);
    var cell1 = row.insertCell(0);
    cell1.innerHTML = queueScripts[queueScripts.length-1].naam;
    console.log(queueScripts[queueScripts.length-1]);

    for (var j in queueScripts[queueScripts.length-1].outputlijst){
        /*var cell = row.insertCell(j+1)
        cell.innerHTML = queueScripts[queueScripts.length-1].outputlijst[j];
        cell.setAttribute("id", "output" + j)*/
        row.innerHTML += '<td>' + queueScripts[queueScripts.length-1].outputlijst[j] + '</td>'
    }
    row.setAttribute("id", queueScripts[queueScripts.length -1].id+"_id");
    uploaddiv.style.display = "none";
    quediv.style.display = "block"
    slider.value = 1;
}

function removeFromQueue() {
    var select = document.getElementById('queueTable');
    select.removeChild(select.lastChild);
    queueScripts.pop();
}

function runQueue(){
    xhr.open("POST", "Controller?action=RunQueueScripts&type=assync", true);
    xhr.onreadystatechange = runQueueResult;
    xhr.send(JSON.stringify(queueScripts));
}

function runQueueResult() {
    if (xhr.status == 200 && xhr.readyState == 4) {
        qscripts = JSON.parse(xhr.responseText);
        queueScripts = qscripts;
        var list = document.getElementById("outputList");
        list.innerHTML = "";
        for(var i in queueScripts){
            var li = document.createElement("li");
            li.appendChild(document.createTextNode(queueScripts[i].naam));
            for (var j in queueScripts[i].outputlijst){
                li.appendChild(document.createTextNode(queueScripts[i].outputlijst[j]));
            }
            li.setAttribute("id", queueScripts[queueScripts.length -1].id+"_id"); // added line
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