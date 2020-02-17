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

function runQueue(){

}