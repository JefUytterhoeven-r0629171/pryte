var xhr = new XMLHttpRequest();
var scripts;
var queueScripts = new Array();
var variableCounter = 1;
var outputVariableCounter = 1;
var queueOutputVariables = new Array();
var queueScriptCounter = 1;
var totalOutputslastQueuescript = 0;

function getAllScripts() {
    xhr.open("GET", "Controller?action=GetScripts&type=assync", true);
    xhr.onreadystatechange = getScriptLijst;
    xhr.send(null);
}

function getScriptLijst() {
    var tbody = document.getElementById("availablescriptstable");
    console.log("getScriptLijst");
    if (xhr.status == 200 && xhr.readyState == 4) {
        scripts = JSON.parse(xhr.responseText);
        for(var i in scripts){
            var row = tbody.insertRow(-1);
            row.setAttribute("id", scripts[i].id )
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);
            var cell5 = row.insertCell(4);
            var cell6 = row.insertCell(5);
            cell1.innerHTML = scripts[i].naam;
            cell2.innerHTML = scripts[i].extension;
            for(var j in scripts[i].inputtypes){
                if(scripts[i].inputtypes[j] != ""){
                    cell3.innerHTML += scripts[i].inputtypes[j] + ', ';
                }
            }
            for(var k in scripts[i].outputtypes){
                cell4.innerHTML += scripts[i].outputtypes[k] + ', ';
            }
            cell5.innerHTML += '<button type="button" onclick="addToQueue( \'' + scripts[i].id + '\')">Add to queue</button>'
            cell6.innerHTML += '<button type="button" onclick="deleteScript(\'' + i +'\')"> X </button>'

        }
    }
}



function deleteScript(i){
    xhr.open("GET", "Controller?action=DeleteScript&type=assync&naam="+scripts[i].naam, true);
    xhr.onreadystatechange = function(){
        console.log(scripts[i].id);
        console.log(i);
        document.getElementById(scripts[i].id).remove();
    };
    xhr.send(null);
}


function addToQueue(element){
    //document.getElementById(element).style.border = "3px solid green";
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
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);

    cell1.innerHTML = queueScripts[queueScripts.length-1].naam;
    for (var j in queueScripts[queueScripts.length-1].inputtypes){
        var label = document.createElement("label");
        var select = document.createElement("select");
        var idtekst = queueScripts.length +"_" +j;
        label.setAttribute("for", idtekst);
        select.setAttribute("class", "form-control form-control-sm");
        label.innerHTML = queueScripts[queueScripts.length-1].inputtypes[j];
        j++;
        cell2.setAttribute("id", "InputTdScript"+queueScripts.length);

        j--;
        select.setAttribute("id", idtekst  );


            for (var outputVar in queueOutputVariables){
                var option = document.createElement('option');
                option.setAttribute("value",  queueOutputVariables[outputVar]);
                option.innerHTML = queueOutputVariables[outputVar];
                select.appendChild(option);
            }
        if(select.options.length != 0) {
            cell2.appendChild(label);
            cell2.appendChild(select);
        }


    }
    totalOutputslastQueuescript = queueScripts[queueScripts.length-1].outputtypes.length;
    for (var k in queueScripts[queueScripts.length-1].outputtypes){
        queueOutputVariables.push(queueScripts[queueScripts.length-1].outputtypes[k] + '_' + queueScriptCounter  + '_' + outputVariableCounter);
        cell3.innerHTML += '<td>' + queueScripts[queueScripts.length-1].outputtypes[k] + '_' + queueScriptCounter + '_' + outputVariableCounter + '</td><br>';
        outputVariableCounter++;
    }
    row.setAttribute("id", queueScripts[queueScripts.length -1].id+"_id");
    uploaddiv.style.display = "none";
    quediv.style.display = "block";
    slider.value = 1;
    queueScriptCounter++;
    outputVariableCounter = 1;
}

function removeFromQueue() {
    var queueTable = document.getElementById('queueTable');
    if (queueTable.rows.length != 0) {
        queueScriptCounter--;
    }
    queueTable.removeChild(queueTable.lastChild);
    queueScripts.pop();
    for(var i = 0; i < totalOutputslastQueuescript; i++) {
        queueOutputVariables.pop();
    }
}

function runQueue(){
    for (var script in queueScripts){
        script++;
        var tekstid = "InputTdScript" + script;
        script--;
        var children = document.getElementById(tekstid).getElementsByTagName('select');
        queueScripts[script].inputIndex  = [];
        for(var m in children){
            if(!isNaN(m)){
                var select = children[m];
                console.log(m);
                queueScripts[script].inputIndex.push(select.value);
                console.log( queueScripts[script]);
            }
        }
    }
    xhr.open("POST", "Controller?action=RunQueueScripts&type=assync", true);
    xhr.onreadystatechange = runQueueResult;
    xhr.send(JSON.stringify(queueScripts));
}

function runQueueResult() {
    if (xhr.status == 200 && xhr.readyState == 4) {
        qscripts = JSON.parse(xhr.responseText);
        queueScripts = qscripts;
        var title = document.getElementById("outputTitle");
        title.style.display = "block";
        var list = document.getElementById("outputList");
        list.innerHTML = "";
        for(var i in queueScripts){
            var li = document.createElement("li");
            var h5 = document.createElement("h5");
            var ulist = document.createElement('ul');
            ulist.setAttribute("class","list-group list-group-flush");
            h5.appendChild(document.createTextNode(queueScripts[i].naam));
            li.appendChild(h5);

            for (var j in queueScripts[i].outputlijst){
                var ulist_li = document.createElement('li');
                if(queueScripts[i].outputtypes[j] == "png"){
                    var img = document.createElement("img");
                    img.setAttribute("src","data:image/png;base64,"+queueScripts[i].outputlijst[j])
                    ulist_li.appendChild(img);
                } else {
                    ulist_li.appendChild(document.createTextNode(queueScripts[i].outputlijst[j]));
                }
                ulist_li.setAttribute("class","list-group-item");
                ulist.appendChild(ulist_li);
            }
            li.setAttribute("id", queueScripts[queueScripts.length -1].id+"_id");// added line
            li.appendChild(ulist);
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