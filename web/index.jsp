<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: jefuy
  Date: 13-Feb-20
  Time: 2:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css">
    <link rel="stylesheet" href="css/style.css">
    <script type="text/javascript" src="js/scriptVariables.js"></script>
    <script type="text/javascript" src="js/scriptQueue.js"></script>
  </head>
  <body>
    <div class="split left">
      <h3>Upload script</h3>
      <form id="addScriptForm" enctype="multipart/form-data" method="POST" action="Controller?action=UploadFile">
        <div class="form-group">
          <input class="form-control-file" type="file" id="file" name="file"/>
        </div>
        <div class="form-group">
          <label for="fileNaam">File naam</label>
          <input class="form-control" type="text" id="fileNaam"/>
        </div>
        <hr/>
        <div id="inputDiv">
          <div class="form-group">
            <label for="input1">Input variable 1</label>
            <input class="form-control" type="text" name="input" id="input1" placeholder="Type">
          </div>
        </div>
        <button type="button" onclick="addInputVariable()">+ input var</button>
        <button type="button" onclick="deleteInputVar()">- input var</button>
        <hr/>
        <div id="outputDiv">
          <div class="form-group">
            <label for="input1">Output variable 1</label>
            <input class="form-control" type="text" name="output" id="output1" placeholder="Type">
          </div>
        </div>
        <button type="button" onclick="addOutputVariable()">+ output var</button>
        <button type="button" onclick="deleteOutputVar()">- output var</button>
        <hr/>
        <br><br>
        <input type="submit" name="Submit" value="voeg toe" />
      </form>
    </div>
    <div class="split right">
      <div id="queue">
        <h3>Queue</h3>
        <ol id="list" type="1">

        </ol>
        <button type="button" onclick="runQueue()"> Run queue</button>
      </div>

      <h3>Overview</h3>
      <table class="table">
        <tr>
          <th>Naam</th>
          <th>Extensie</th>
          <th>Input types</th>
          <th>Output types</th>
          <th>Add to queue</th>
          <th>Delete</th>
        </tr>
        <c:forEach var = "script" items="${scripts}">
          <tr>
            <td>${script.naam}</td>
            <td>${script.extension}</td>
          </tr>
        </c:forEach>
        <tr id="script1">
          <td>script1</td>
          <td>.py</td>
          <td></td>
          <td></td>
          <td><button type="button" onclick="addToQueue('script1')">Add to queue</button></td>
          <td><button type="button" onclick="deleteScript()"> X </button></td>
        </tr>
        <tr id="script2">
          <td>script2</td>
          <td>.r</td>
          <td></td>
          <td></td>
          <td><button type="button" onclick="addToQueue('script2')">Add to queue</button></td>
          <td><button type="button" onclick="deleteScript()"> X </button></td>
        </tr>

      </table>
    </div>

  </body>
</html>
