let tellerInput = 2;
let tellerOutput = 2;
function addInputVariable() {
    var inputDiv = document.getElementById('inputDiv');
    inputDiv.innerHTML += '<div id="'+ tellerInput +'"><label for="input' + tellerInput +'">Type input variable '+ tellerInput +'</label>: <input type="text" name="input" id="input' + tellerInput + '"/><br></div>';
    tellerInput++;
}
function addOutputVariable() {
    var teller = tellerOutput -1;
    var outputDiv = document.getElementById('outputDiv');
    outputDiv.innerHTML += '<div id="'+ tellerOutput +'"><label for="output' + tellerOutput + '">Type output variable ' + tellerOutput + '</label>: <input type="text" name="output" id="' + tellerOutput + '"/><br><button type="button" onclick="deleteOutputVar(teller)">- output var</button></div>';
    tellerOutput++;
}

function deleteOutputVar(teller){
    console.log(teller)
    document.getElementById("outputDiv").removeChild(document.getElementById(teller))
}

function addToQueue(){

}

function deleteScript(){

}