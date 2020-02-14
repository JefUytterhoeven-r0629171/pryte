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
  </head>
  <body>
  <<a href="Controller?action=Test">please pleas click me</a>

  <form id="addoefenmatriaalform" enctype="multipart/form-data" name="addoefenmatriaalform" method="POST" action="Controller?action=RunFile">
  <td><label for="file">first file you wanna run :  </label></td>
  <td><input type="file" name="file"/> </td>
    <td><input type="text" name="naam"/> </td>

    <td><input class="button" type="submit" name="Submit" value="voeg toe" /></td>
  </form>
  </body>
</html>
