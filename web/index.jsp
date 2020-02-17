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
    <link rel="stylesheet" type="text/css" href="style.css">
    <script type="text/javascript" src="js/scriptVariables.js"></script>
  </head>
  <body>

  <form id="addScriptForm" enctype="multipart/form-data" method="POST" action="Controller?action=UploadFile">
    <label for="file">Upload script</label>
    <input type="file" id="file" name="file"/>

    <label for="fileNaam">File naam</label>
    <input type="text" id="fileNaam"/>

    <div id="inputDiv">
      <label for="input1">Type input variable 1</label>:
      <input type="text" name="input" id="input1"><br>
    </div>
    <button type="button" onclick="addInputVariable()">+ input var</button>

    <div id="outputDiv">
      <label for="input1">Type output variable 1</label>:
      <input type="text" name="output" id="output1"><br>
    </div>

    <button type="button" onclick="addOutputVariable()">+ output var</button>
    <input class="button" type="submit" name="Submit" value="voeg toe" />
  </form>
  <table>
    <tr>
      <th>Naam</th>
      <th>Extensie</th>
      <th>Input types</th>
      <th>Output types</th>
      <th>Add to queue</th>
      <th>Delete</th>
    </tr>
    <%--<c:forEach var = "script" items="scripts">
      <tr>
        <td>${script.naam}</td>
        <td>${script.extension}</td>
      </tr>
    </c:forEach>--%>
    <tr>
      <td>script1</td>
      <td>.py</td>
      <td></td>
      <td></td>
      <td><button type="button" onclick="addToQueue()">Add to queue</button></td>
      <td><button type="button" onclick="deleteScript()"> X </button></td>
    </tr>
    <tr>
      <td>script2</td>
      <td>.r</td>
      <td></td>
      <td></td>
      <td><button type="button" onclick="addToQueue()">Add to queue</button></td>
      <td><button type="button" onclick="deleteScript()"> X </button></td>
    </tr>

  </table>
  </body>
</html>
