let tellerInput = 2;
let tellerOutput = 2;
function addInputVariable() {
    var inputDiv = document.getElementById('inputDiv');
    var variableDiv = document.createElement('div');
    var currentTeller = tellerInput;
    variableDiv.setAttribute('id', 'input'+tellerInput)
    variableDiv.innerHTML +=
        '<label for="input' + tellerInput +'">Type input variable '+ tellerInput +'</label>: ' +
        '<input type="text" name="input" id="input' + tellerInput + '"/><br>' +
        '<button type="button" onclick="deleteInputVar(currentTeller)">- input var</button><br>';
    inputDiv.appendChild(variableDiv)
    tellerInput++;
}
function addOutputVariable() {
    var outputDiv = document.getElementById('outputDiv');
    var variableDiv = document.createElement('div')
    variableDiv.setAttribute('id', 'input'+tellerOutput)
    outputDiv.innerHTML += '<label for="output' + tellerOutput + '">Type output variable ' + tellerOutput + '</label>: ' +
        '<input type="text" name="output" id="' + tellerOutput + '"/><br>' +
        '<button type="button" onclick="deleteOutputVar(tellerOutput)">- output var</button>';
    outputDiv.appendChild(variableDiv)
    tellerOutput++;
}
function deleteInputVar(teller){
    console.log(teller)
    document.getElementById("inputDiv").removeChild(document.getElementById('input'+teller))
}
function deleteOutputVar(teller){
    console.log(teller)
    document.getElementById("outputDiv").rem
}

function addToQueue(){

}

function deleteScript(){

}