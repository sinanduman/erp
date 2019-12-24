<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="erp.entity.Genel, erp.entity.User " %>
<%@ page import="erp.util.Util" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="img/favicon.ico">
    <title><%= Genel.TITLE %>
    </title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css?<%=Util.nanoTime%>">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/irfan.css?<%=Util.nanoTime%>">
    <link rel="stylesheet" href="css/fonts.css">
    <link rel="stylesheet" href="css/signin.css?<%=Util.nanoTime%>">
    <link rel="stylesheet" href="css/all.css">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    Boolean loggedin = (Boolean) session.getAttribute("loggedin");
    if (user != null && loggedin != null && loggedin) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    } else {
%>

<div class="container">

    <form class="form-signin text-center" action="login" method="post">

        <img alt="İrfan Plastik" src="img/ip_logo_mini.png" class="img-rounded"/>

        <h2 class="form-signin-heading">
            <span class="color-red loginclass">Sisteme Giriş</span>
        </h2>


        <div class="input-group" style="margin-bottom:-1px;width:100%;"><span class="input-group-addon" style="width:50px;"><i class="fa fa-user"></i></span><input type="text" name="username" class="form-control normal" placeholder="Kullanıcı Adı"/></div>

        <div class="input-group" style="margin-bottom:15px;width:100%;"><span class="input-group-addon" style="width:50px;"><i class="fa fa-key"></i></span><input type="password" name="password" class="form-control normal" placeholder="Şifre"></div>


        <div>
            <button class="btn btn-success" type="submit" name="submit" style="background-color: #5fb336"><i class="fa fa-sign-in-alt iki-bosluk"></i>Giriş Yap</button>
        </div>

    </form>

    <%
        String message = (String) request.getSession().getAttribute("message");
        String submit = request.getParameter("submit");
        if (message != null && !message.trim().equals("") && submit != null) {
    %>
    <div class="form-signin text-center alert alert-danger"
         style="margin-top: 10px;">
        <strong><c:out value="${message}"/></strong>
    </div>
    <%
            request.getSession().setAttribute("message", "");
        }
    %>

</div>


<%
    }
%>

<!-- /container -->

</body>
</html>
