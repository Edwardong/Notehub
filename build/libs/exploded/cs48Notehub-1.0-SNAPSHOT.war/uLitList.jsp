<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
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
    String profileImageUrl;
    String email = "";
    String emailName = "";
    int followerNum = 0;
    int followingNum = 0;
    Map<String,String> descriptionMap = new HashMap<>();
    Map<String,String> yearMap = new HashMap<>();
    Map<String,String> monthMap = new HashMap<>();
    Map<String,String> tagMap = new HashMap<>();
    Map<String,String> urlMap = new HashMap<>();
    Map<String,String> dayMap = new HashMap<>();
    ArrayList<String> nameArray = new ArrayList<>();
    //tags array
    ArrayList<String> MathArray = new ArrayList<>();
    ArrayList<String> CSArray = new ArrayList<>();
    ArrayList<String> ArtArray = new ArrayList<>();
    ArrayList<String> LitArray = new ArrayList<>();
    ArrayList<String> BusArray = new ArrayList<>();
    ArrayList<String> StatArray = new ArrayList<>();
    ArrayList<String> HistoryArray = new ArrayList<>();
    ArrayList<String> PhysicsArray = new ArrayList<>();
    ArrayList<String> ChemArray = new ArrayList<>();

    //Get variables
    if (session.getAttribute("uemail") == null) {
        response.sendRedirect("index.jsp");
    }
    else if (session.getAttribute("uemail") != null) {
        email = (String) session.getAttribute("uemail");
        emailName = (String) session.getAttribute("username");
        profileImageUrl = (String) session.getAttribute("uimage");
    }
    if(session.getAttribute("ufollowerNum") != null){
        followerNum = (int) session.getAttribute("ufollowerNum");
    }
    if(session.getAttribute("ufollowingNum") != null){
        followingNum = (int) session.getAttribute("ufollowingNum");
    }

    if(session.getAttribute("udescriptionMap") != null){
        descriptionMap = (Map<String, String>) session.getAttribute("udescriptionMap");
    }

    if(session.getAttribute("udayMap") != null){
        dayMap = (Map<String, String>) session.getAttribute("udayMap");
    }

    if(session.getAttribute("uyearMap") != null){
        yearMap = (Map<String, String>) session.getAttribute("uyearMap");
    }

    if(session.getAttribute("umonthMap") != null){
        monthMap = (Map<String, String>) session.getAttribute("umonthMap");
    }
    if(session.getAttribute("uurlMap") != null){
        urlMap = (Map<String, String>) session.getAttribute("uurlMap");
    }

    if(session.getAttribute("utagMap") != null){
        tagMap = (Map<String, String>) session.getAttribute("utagMap");
    }

    if(session.getAttribute("unameArray") != null){
        nameArray = (ArrayList<String>) session.getAttribute("unameArray");
    }


    if(session.getAttribute("uChemArray") != null){
        ChemArray = (ArrayList<String>) session.getAttribute("uChemArray");
    }

    if(session.getAttribute("uPhysicsArray") != null){
        PhysicsArray = (ArrayList<String>) session.getAttribute("uPhysicsArray");
    }
    if(session.getAttribute("uHistoryArray") != null){
        HistoryArray = (ArrayList<String>) session.getAttribute("uHistoryArray");
    }
    if(session.getAttribute("uStatArray") != null){
        StatArray = (ArrayList<String>) session.getAttribute("uStatArray");
    }
    if(session.getAttribute("uBusArray") != null){
        BusArray = (ArrayList<String>) session.getAttribute("uBusArray");
    }
    if(session.getAttribute("uLitArray") != null){
        LitArray = (ArrayList<String>) session.getAttribute("uLitArray");
    }
    if(session.getAttribute("uArtArray") != null){
        ArtArray = (ArrayList<String>) session.getAttribute("uArtArray");
    }
    if(session.getAttribute("uCSArray") != null){
        CSArray = (ArrayList<String>) session.getAttribute("uCSArray");
    }
    if(session.getAttribute("uMathArray") != null){
        MathArray = (ArrayList<String>) session.getAttribute("uMathArray");
    }

    if(session.getAttribute("uLitArray") == null){
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
            <a class="navbar-brand" href="Request?update=true">NoteHub</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="userProfile.jsp">Home</a></li>
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
                            for(int i = 0; i < LitArray.size(); i++){
                        %>
                        <li>
                            <time datetime="2014-07-20">
                                <span class="day"><%=dayMap.get(LitArray.get(i))%></span>
                                <span class="month"><%=monthMap.get(LitArray.get(i))%></span>
                                <span class="year"><%=yearMap.get(LitArray.get(i))%></span>
                                <span class="time">ALL DAY</span>
                            </time>
                            <embed src=<%=urlMap.get(LitArray.get(i))+"#page=1&toolbar=0&navpanes=0&scrollbar=0"%> type="application/pdf" width=120 height=120>
                            <div class="info">
                                <h2 class="title"><a href=<%=urlMap.get(LitArray.get(i))%>><%=LitArray.get(i)%></a></h2>
                                <p class="desc"><%=descriptionMap.get(LitArray.get(i))%></p>
                            </div>
                            <div class="social">
                                <ul>
                                    <!--<li class="facebook" style="width:33%;"><a href="#delete"><span class="fa fa-trash"></span></a></li>-->
                                    <li class="facebook" style="width:34%;"><a href=<%=urlMap.get(LitArray.get(i))%> download><span class="fa fa-download"></span></a></li>
                                </ul>
                            </div>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="js/login.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="js/facebooklogin.js"></script>
<script src="js/profile.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>
