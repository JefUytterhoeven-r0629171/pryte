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
    <title>pryte</title>
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css">
    <link rel="stylesheet" href="css/style.css">
    <script type="text/javascript" src="js/scriptVariables.js"></script>
    <script type="text/javascript" src="js/scriptQueue.js"></script>
  </head>
  <body>
  <script>
      window.onload = getAllScripts();
  </script>
    <div class="split left">
      <input type="range" min="0" max="1" value="0" class="slider" id="myRange" onchange="SplitLeftMode()">
      <div id="uploadscript">
      <h3>Upload script</h3>
      <form id="addScriptForm" enctype="multipart/form-data" method="POST" action="Controller?action=UploadFile">
        <div class="form-group">
          <input class="form-control-file" type="file" id="file" name="file"/>
        </div>
        <hr/>
        <div id="inputDiv">
          <div class="form-group">

          </div>
        </div>
        <button type="button" onclick="addInputVariable()">+ input var</button>
        <button type="button" onclick="deleteInputVar()">- input var</button>
        <hr/>
        <div id="outputDiv">
          <div class="form-group">

          </div>
        </div>
        <button type="button" onclick="addOutputVariable()">+ output var</button>
        <button type="button" onclick="deleteOutputVar()">- output var</button>
        <hr/>
        <br><br>
        <input type="submit" name="Submit" value="voeg toe" />
      </form>
      </div>
      <div id="queue" style="display: none;" >
        <h3>Queue</h3>
        <table class="table">
          <thead>
            <tr>
              <th>Naam</th>
              <th>Input</th>
              <th>Output</th>
            </tr>
          </thead>
          <tbody id="queueTable">

          </tbody>
        </table>
        <button type="button" onclick="runQueue()">Run queue</button>
        <button type="button" onclick="removeFromQueue()">Remove last item</button>
        <br>
        <h3>Output</h3>
        <ol id="outputList" type="1">

        </ol>
      </div>
    </div>
    <div class="split right">
      <h3>Overview</h3>
      <table class="table">
        <thead>
          <tr>
            <th>Naam</th>
            <th>Extensie</th>
            <th>Input types</th>
            <th>Output types</th>
            <th>Add to queue</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody id="availablescriptstable">

        </tbody>

      </table>
    </div>
  </body>
</html>
