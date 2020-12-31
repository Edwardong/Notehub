<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="javax.naming.directory.SearchControls" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile Page</title>
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css"
          href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet"
          type="text/css">
    <link rel="stylesheet" type="text/css" href="css/pdfList.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <script type="text/javascript" src="js/search.js"></script>
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
<body>
<%
    //variables initialization
    ArrayList<String> SearchArray = new ArrayList<>();
    ArrayList<String> SearchDesArray = new ArrayList<>();
    ArrayList<String> SearchMonthArray = new ArrayList<>();
    ArrayList<String> SearchYearArray = new ArrayList<>();
    ArrayList<String> SearchDayArray = new ArrayList<>();
    ArrayList<String> SearchUrlArray = new ArrayList<>();
    ArrayList<String> SearchUserArray = new ArrayList<>();

    String Searchedkeyword = "";
    //Get variables
    if (session.getAttribute("Searchedkeyword") != null) {
        Searchedkeyword = (String) session.getAttribute("Searchedkeyword");
    }
    if (session.getAttribute("SearchArray") != null) {
        SearchArray = (ArrayList<String>) session.getAttribute("SearchArray");
    }
    if (session.getAttribute("SearchDesArray") != null) {
        SearchDesArray = (ArrayList<String>) session.getAttribute("SearchDesArray");
    }
    if (session.getAttribute("SearchMonthArray") != null) {
        SearchMonthArray = (ArrayList<String>) session.getAttribute("SearchMonthArray");
    }
    if (session.getAttribute("SearchYearArray") != null) {
        SearchYearArray = (ArrayList<String>) session.getAttribute("SearchYearArray");
    }
    if (session.getAttribute("SearchDayArray") != null) {
        SearchDayArray = (ArrayList<String>) session.getAttribute("SearchDayArray");
    }
    if (session.getAttribute("SearchUrlArray") != null) {
        SearchUrlArray = (ArrayList<String>) session.getAttribute("SearchUrlArray");
    }
    if (session.getAttribute("SearchUserArray") != null) {
        SearchUserArray = (ArrayList<String>) session.getAttribute("SearchUserArray");
    }
    if (session.getAttribute("SearchArray") == null) {
        response.sendRedirect("userProfile.jsp");
    }

%>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.jsp">NoteHub</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="index.jsp">Home</a></li>
                <!--li><a href="#search">Search</a></li-->
                <li><a href="https://github.com/delinsun/Notehub">About</a></li>
                <form class="navbar-form navbar-left" role="search" action="Request">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search NoteHub" id="keyword" name="SearchedUsername"
                               onkeyup="getMoreContents()" onblur="keywordBlur()" onfocus="getMoreContents()">
                        <div id="popDiv">
                            <table id="content_table" style = "color:white" bgcolor="black" border="0" cellspacing="0" cellpadding="0">
                                <tbody id="content_table_body">
                                <!--how to show the search result-->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div id="mainProfile" class="container target">

        <div id="pdflist" class="container">
            <div class="row">
                <div class="[ col-xs-12 col-sm-offset-2 col-sm-8 ]">
                    <ul id="ull" class="event-list">
                        <%
                            int count = 0;
                            for (int i = 0; i < SearchArray.size(); i++) {
                                if (!(SearchArray.get(i).contains(Searchedkeyword))) {
                                    continue;
                                }
                                count++;
                        %>
                        <li>
                            <time datetime="2014-07-20">
                                <span class="day"><%=SearchDayArray.get(i)%></span>
                                <span class="month"><%=SearchMonthArray.get(i)%></span>
                                <span class="year"><%=SearchYearArray.get(i)%></span>
                                <span class="time">ALL DAY</span>
                            </time>
                            <embed src=<%=SearchUrlArray.get(i)+"#page=1&toolbar=0&navpanes=0&scrollbar=0"%> type="application/pdf"
                                   width=120 height=120>
                            <div class="info">
                                <h2 class="title"><a href=<%=(String)SearchUrlArray.get(i)%>><%=SearchArray.get(i)%></a></h2>
                                <p class="desc"><a href = <%="Request?SearchedUsername="+(SearchUserArray.get(i).replace(' ','+'))%>><%=SearchUserArray.get(i)%></a>
                                </p>
                                <p class="desc"><%=SearchDesArray.get(i)%>
                                </p>
                            </div>
                            <div class="social">
                                <ul>
                                    <!--<li class="facebook" style="width:33%;"><a href="#delete"><span class="fa fa-trash"></span></a></li>-->
                                    <li class="facebook" style="width:34%;"><a
                                            href=<%=SearchUrlArray.get(i)%> download><span
                                            class="fa fa-download"></span></a></li>
                                </ul>
                            </div>
                        </li>
                        <%
                            }
                            if (count == 0) {
                        %>
                        <!--div class="container">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="error-template"-->
                        <center>
                                        <h1 style="color:white;"> Oops!</h1>
                                        <h2  style="color:white;"> 404 Not Found</h2>
                                        <div class="error-details" style="color:white;">
                                            <%="Could not find files with the given word "+Searchedkeyword%>
                                        </div>
                                        <div class="error-actions" style="color:white; margin-top: 10px;">
                                            <a onclick="goBack()" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-home"></span>
                                                Go Back </a>
                                        </div>
                            <center>
                                        <!--/div>
                                    </div>
                                </div>
                            </div>
                        </div-->
                        <%
                            }%>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function goBack() {
        window.history.back();
    }
</script>
<script src="js/login.js"></script>
<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="js/facebooklogin.js"></script>
<script src="js/profile.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>
