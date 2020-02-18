var xhr = new XMLHttpRequest();
var scripts;
var quescripts = new Array();
function getAllScripts() {
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
            console.log(scripts[i].id);
            table.innerHTML +=  ' <tr id="' + scripts[i].id + '"> <td>'+ scripts[i].naam +'</td> <td>'+ scripts[i].extension +'</td> <td></td> <td></td><td><button type="button" onclick="addToQueue( \'' + scripts[i].id + '\')">Add to queue</button></td><td><button type="button" onclick="deleteScript()"> X </button></td> </tr>'
        }
    }
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
    console.log(quescripts[quescripts.length-1]);


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

    quescripts.remove(quescripts.length-1);
}



function runQueue(){
    xhr.open("POST", "Controller?action=RunQueScripts&type=assync", true);
    xhr.onreadystatechange = runQueResult;
    xhr.send(JSON.stringify(quescripts));
}

function runQueResult() {
    console.log("wachten op de quescripts");

    if (xhr.status == 200 && xhr.readyState == 4) {
        console.log("de scripts zijn aangekomen");
        qscripts = JSON.parse(xhr.responseText);
        console.log(qscripts);
        quescripts = qscripts;
        var list = document.getElementById("list");
        list.innerHTML = "";
        for(var i in quescripts){
            console.log(qscripts[i].id);

            var li = document.createElement("li");
            li.appendChild(document.createTextNode(quescripts[i].naam));
            li.appendChild(document.createTextNode(quescripts[i].outputtypes));
            li.setAttribute("id", quescripts[quescripts.length -1].id+"_id"); // added line
            list.appendChild(li);
        }


    }
}

function SplitLefdMode(){
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