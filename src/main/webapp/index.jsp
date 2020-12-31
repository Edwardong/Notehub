<!DOCTYPE html>
<html>
<head>
    <title>NoteHub</title>
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css"
          href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet"
          type="text/css">

    <link rel="stylesheet" type="text/css" href="css/main.css">


    <script type="text/javascript" src="js/search.js"></script>


</head>
<body>
<%
    if (session.getAttribute("uemail") != null) {
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
                            <table id="content_table" bgcolor="black" border="0" cellspacing="0" cellpadding="0">
                                <tbody id="content_table_body">
                                <!--how to show the search result-->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </ul>
            <ul class="nav navbar-nav navbar-right">

                <li><a href="login.html">Sign in<i class="fa fa-sign-in"></i></a></li>
                <!--

                <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Login <i
                        class="fa fa-user"></i></a>

                    <ul id="login-dp" class="dropdown-menu">
                        <li>
                            <div class="row">
                                <div class="col-md-12">
                                    Login via
                                    <div class="social-buttons">
                                        <button id="loginBtn" class="btn btn-fb"><i class="fa fa-facebook"></i> Facebook
                                        </button>
                                        <button %href="#" class="btn btn-tw"><i class="fa fa-twitter"></i> Twitter
                                        </button>
                                    </div>
                                    or
                                    <form class="form" role="form" action="Request" name="form1" accept-charset="UTF-8"
                                          id="login-div">
                                        <div class="form-group">
                                            <label class="sr-only" for="exampleInputEmail2">Email address</label>
                                            <input type="email" class="form-control" id="exampleInputEmail2"
                                                   name="param1" placeholder="Email address" required>
                                        </div>
                                        <div class="form-group">
                                            <label class="sr-only" for="exampleInputPassword2">Password</label>
                                            <input type="password" class="form-control" id="exampleInputPassword2"
                                                   placeholder="Password" required>
                                            <div class="help-block text-right"><a href="">Forget the password ?</a>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" class="btn btn-primary btn-block" function="login()"

                                                   onclick="login()" value="Sign in"></input>
                                        </div>
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox"> keep me logged-in
                                            </label>
                                        </div>
                                </div>

                                <div id="user_div" class="loggedin-div">

                                    <h3>Welcome User</h3>
                                    <p id="user_para">Welcome to Firebase web login Example. You're currently logged
                                        in.</p>

                                    <button onclick="logout()">Logout</button>
                                </div>
                            </div>
                            <div class="bottom text-center">
                                New here ? <a href="register.html"><b>Join Us</b></a>
                            </div>
        -->
        </div>
        </li>
        </ul>
        </li>


        </ul>
    </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <div class="content">
                <h1>NoteHub</h1>
                <h3>Take notes for everything better, together</h3>
                <hr>
                <a href="login.html">
                    <button class="btn btn-default btn-lg"><i class="fa fa-bookmark"></i> Get Started!</button>
                </a>

            </div>
        </div>
    </div>
</div>
<script src="js/login.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="js/facebooklogin.js"></script>
</body>
</html>
