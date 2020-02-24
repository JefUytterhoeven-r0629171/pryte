<%--
  Created by IntelliJ IDEA.
  User: jefuy
  Date: 24-Feb-20
  Time: 10:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
    <link rel="stylesheet" href="css/LoginStyle.css">
    <script type="text/javascript" src="js/Login.js"></script>
</head>
<body>
<link href='https://fonts.googleapis.com/css?family=Open+Sans:700,600' rel='stylesheet' type='text/css'>

<form method="post" action="Controller?action=Login">
    <div class="box">
        <h1>PryteApp</h1>

        <input type="email" name="usr" value="gebruiker"  class="email" />

        <input type="password" name="pass" value="Password" class="email" />

        <input class="btn" type="submit" name="Submit" value="login" />


    </div> <!-- End Box -->

</form>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js" type="text/javascript"></script>
</body>
</html>
