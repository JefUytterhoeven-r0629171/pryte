let tellerInput = 1;
let tellerOutput = 1;

function addInputVariable() {
    tellerInput++;
    var inputDiv = document.getElementById('inputDiv');
    var variableDiv = document.createElement('div');
    variableDiv.setAttribute('class', 'form-group');
    variableDiv.setAttribute('id', 'input'+tellerInput);
    variableDiv.innerHTML +=
        '<label for="input' + tellerInput +'">Input variable '+ tellerInput +'</label>: ' +
        '<input class="form-control" type="text" name="input" id="input' + tellerInput + '" placeholder="Type"/>';
    inputDiv.appendChild(variableDiv)
}

function addOutputVariable() {
    tellerOutput++;
    var outputDiv = document.getElementById('outputDiv');
    var variableDiv = document.createElement('div');
    variableDiv.setAttribute('id', 'input'+tellerOutput);
    variableDiv.setAttribute('class', 'form-group');
    variableDiv.innerHTML += '' +
        '<label for="output' + tellerOutput + '">Output variable ' + tellerOutput + '</label>' +
        '<input class="form-control" type="text" name="output" id="input' + tellerOutput + '" placeholder="Type"/>';
    outputDiv.appendChild(variableDiv)
}

function deleteInputVar() {
    if(tellerInput != 1) {
        var select = document.getElementById('inputDiv');
        select.removeChild(select.lastChild);
        tellerInput--
    }
}

function deleteOutputVar() {
    if(tellerOutput != 1) {
        var select = document.getElementById('outputDiv');
        select.removeChild(select.lastChild);
        tellerOutput--;
    }
}


function deleteScript(){

}