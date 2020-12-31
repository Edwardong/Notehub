<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: Zichen Sun
  Date: 9/2/2018
  Time: 上午1:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile Page</title>
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
    //variables initialization
    String profileImageUrl = "";
    String email = "";
    String emailName = "";
    int followerNum = 0;
    int followingNum = 0;
    int shared = 0;
    Map<String, String> descriptionMap = new HashMap<>();
    Map<String, Integer> yearMap = new HashMap<>();
    Map<String, String> monthMap = new HashMap<>();
    Map<String, String> tagMap = new HashMap<>();
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

    //number of files in each tag array
    String MathSize = "0";
    String CSSize = "0";
    String ArtSize = "0";
    String LitSize = "0";
    String BusSize = "0";
    String StatSize = "0";
    String HistorySize = "0";
    String PhysicsSize = "0";
    String ChemSize = "0";

    //Get variables
    if (session.getAttribute("uemail") == null || session.getAttribute("username") == null) {
        response.sendRedirect("index.jsp");
    }
    else if (session.getAttribute("uemail") != null) {
        email = (String) session.getAttribute("uemail");
        emailName = (String) session.getAttribute("username");
        profileImageUrl = (String) session.getAttribute("uimage");
    }
    if (session.getAttribute("ufollowerNum") != null) {
        followerNum = (int) session.getAttribute("ufollowerNum");
    }
    if (session.getAttribute("ufollowingNum") != null) {
        followingNum = (int) session.getAttribute("ufollowingNum");
    }
    if (session.getAttribute("ushared") != null)
        shared = (int) session.getAttribute("ushared") - 1;

    if (session.getAttribute("udescriptionMap") != null) {
        descriptionMap = (Map<String, String>) session.getAttribute("udescriptionMap");
    }

    if (session.getAttribute("uyearMap") != null) {
        yearMap = (Map<String, Integer>) session.getAttribute("uyearMap");
    }

    if (session.getAttribute("umonthMap") != null) {
        monthMap = (Map<String, String>) session.getAttribute("umonthMap");
    }

    if (session.getAttribute("utagMap") != null) {
        tagMap = (Map<String, String>) session.getAttribute("utagMap");
    }

    if (session.getAttribute("unameArray") != null) {
        nameArray = (ArrayList<String>) session.getAttribute("unameArray");
    }

    //getting arrays with different tags
    if (session.getAttribute("uChemArray") != null) {
        ChemArray = (ArrayList<String>) session.getAttribute("uChemArray");
        ChemSize = String.valueOf(ChemArray.size());
    }

    if (session.getAttribute("uPhysicsArray") != null) {
        PhysicsArray = (ArrayList<String>) session.getAttribute("uPhysicsArray");
        PhysicsSize = String.valueOf(PhysicsArray.size());
    }
    if (session.getAttribute("uHistoryArray") != null) {
        HistoryArray = (ArrayList<String>) session.getAttribute("uHistoryArray");
        HistorySize = String.valueOf(HistoryArray.size());
    }
    if (session.getAttribute("uStatArray") != null) {
        StatArray = (ArrayList<String>) session.getAttribute("uStatArray");
        StatSize = String.valueOf(StatArray.size());
    }
    if (session.getAttribute("uBusArray") != null) {
        BusArray = (ArrayList<String>) session.getAttribute("uBusArray");
        BusSize = String.valueOf(BusArray.size());
    }
    if (session.getAttribute("uLitArray") != null) {
        LitArray = (ArrayList<String>) session.getAttribute("uLitArray");
        LitSize = String.valueOf(LitArray.size());
    }
    if (session.getAttribute("uArtArray") != null) {
        ArtArray = (ArrayList<String>) session.getAttribute("uArtArray");
        ArtSize = String.valueOf(ArtArray.size());
    }
    if (session.getAttribute("uCSArray") != null) {
        CSArray = (ArrayList<String>) session.getAttribute("uCSArray");
        CSSize = String.valueOf(CSArray.size());
    }
    if (session.getAttribute("uMathArray") != null) {
        MathArray = (ArrayList<String>) session.getAttribute("uMathArray");
        MathSize = String.valueOf(MathArray.size());
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
                <li class="active"><a href="#">Home</a></li>
                <!--li><a href="#search">Search</a></li-->
                <li><a href="https://github.com/delinsun/Notehub">About</a></li>
                <form class="navbar-form navbar-left" role="search" action="Request">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search NoteHub" id="keyword"
                               name="SearchedUsername"
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
                <li><a id = "btnLogout" href="Request?logout=true">logout <i class="fa fa-sign-out"></i></a></li>
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
                                        <input type="submit" class="btn btn-primary btn-block" function="login()" onclick="login()" value="Sign in">
                                    </div>
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox"> keep me logged-in
                                        </label>
                                    </div>


                            </div>
                            <!--
                            <div id="user_div" class="loggedin-div">

                                <h3>Welcome User</h3>
                                <p id="user_para">Welcome to Firebase web login Example. You're currently logged
                                    in.</p>

                                <button onclick="logout()">Logout</button>
                            </div>
                            -->
                        </div>
        </div>
        </li>
        </ul>
        </li>


        </ul>
    </div>
    </div>
</nav>

<div class="container">
    <hr class="">
    <div id="mainProfile" class="container target">
        <div class="row">
            <div class="col-sm-10">
                <h1 id="bigName" class=""><%=emailName%>
                </h1>

                <br>
            </div>
            <div class="col-sm-2">
                <a href="/users" class="pull-right"><img title="profile image" class="img-circle img-responsive"
                                                         src=<%=profileImageUrl%>></a>

            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-sm-3">
                <!--left col-->
                <ul class="list-group">
                    <li class="list-group-item text-muted">Activity <i class="fa fa-dashboard fa-1x"></i>

                    </li>
                    <li class="list-group-item text-right"><span class="pull-left"><strong
                            class="">Email</strong></span> <%=email%>
                    </li>
                    <li class="list-group-item text-right"><span class="pull-left"><strong
                            class="">Shares</strong></span> <%=shared%>
                    </li>
                    <li class="list-group-item text-right"><span class="pull-left"><strong
                            class="">Following</strong></span>
                        <a class="dropdown-toggle" data-toggle="dropdown"><%=followerNum%>
                        </a>
                        <ul id="follow-dp-following" class="dropdown-menu">
                            <li>
                                <div class="row">
                                    <div class="col-md-12">
                                        <p>user1</p>
                                        <p>user2</p>
                                        <p>user3</p>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="list-group-item text-right"><span class="pull-left"><strong
                            class="">Followers</strong></span>
                        <a class="dropdown-toggle" data-toggle="dropdown"><%=followerNum%>
                        </a>
                        <ul id="follow-dp-follower" class="dropdown-menu">
                            <li>
                                <div class="row">
                                    <div class="col-md-12">
                                        <p>user1</p>
                                        <p>user2</p>
                                        <p>user3</p>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!--/col-3-->
            <div class="col-sm-9" style="" contenteditable="false">
                <div class="panel panel-default target">
                    <div class="panel-heading" contenteditable="false">Notes Repository</div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="thumbnail">
                                    <a href="uMathList.jsp">
                                        <img alt="300x200"
                                             src="http://study.com/cimages/course-image/linear-algebra-syllabus-lesson-plans_175046_large.jpg">
                                    </a>
                                    <div class="caption">
                                        <h3>
                                            Mathematics
                                        </h3>
                                        <p align="right">
                                            <%="Total: "+MathSize%>
                                        </p>

                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="thumbnail">
                                    <a href="uCSList.jsp">
                                        <img alt="300x200"
                                             src="https://cdn-images-1.medium.com/max/2000/1*LgaStRUic1JjYfhdYplClg.jpeg">
                                    </a>
                                    <div class="caption">
                                        <h3>
                                            Computer Science
                                        </h3>
                                        <p align="right">
                                            <%="Total: "+CSSize%>
                                        </p>

                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="thumbnail">
                                    <a href="uArtList.jsp">
                                        <img alt="300x200"
                                             src="https://axerosolutions.com/attachment?file=UZkB7AwtFeOsAZEgxrSeAg%3D%3D">
                                    </a>
                                    <div class="caption">
                                        <h3>
                                            Art & Music
                                        </h3>
                                        <p align="right">
                                            <%="Total: "+ArtSize%>
                                        </p>
                                    </div>
                                </div>

                            </div>

                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="thumbnail">
                                    <a href="uBusList.jsp">
                                        <img alt="300x200"
                                             src="https://salespop.pipelinersales.com/wp-content/uploads/2014/04/the-benefit-of-web-services-to-a-crm-software.jpg">
                                    </a>
                                    <div class="caption">
                                        <h3>
                                            Business
                                        </h3>
                                        <p align="right">
                                            <%="Total: "+BusSize%>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="thumbnail">
                                    <a href="uStatList.jsp">
                                        <img alt="300x200"
                                             src="https://cdn.lynda.com/course/503930/503930-636173965766935316-16x9.jpg">
                                    </a>
                                    <div class="caption">
                                        <h3>
                                            Statistical Science
                                        </h3>
                                        <p align="right">
                                            <%="Total: "+StatSize%>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="thumbnail">
                                    <a href="uHistoryList.jsp">
                                        <img alt="300x200"
                                             src="http://study.com/cimages/course-image/virginia-sol-world-history-geography-to-1500-test-prep-practice_217975_large.jpg">
                                    </a>
                                    <div class="caption">

                                        <h3>
                                            World History
                                        </h3>
                                        <p align="right">
                                            <%="Total: "+HistorySize%>
                                        </p>
                                    </div>
                                </div>

                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="thumbnail">
                                        <a href="uPhysicsList.jsp">
                                            <img alt="300x200"
                                                 src="https://urbangeekz.com/wp-content/uploads/2015/10/Not-Rocket-Science-How-to-Uncomplicate-Your-Business-Model-837x480.jpg">
                                        </a>
                                        <div class="caption">
                                            <h3>
                                                Physics
                                            </h3>
                                            <p align="right">
                                                <%="Total: "+PhysicsSize%>
                                            </p>

                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="thumbnail">
                                        <a href="uChemList.jsp">
                                            <img alt="300x200"
                                                 src="https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://coursera-course-photos.s3.amazonaws.com/fa/6926005ea411e490ff8d4c5d4ff426/chemistry_logo.png">
                                        </a>
                                        <div class="caption">
                                            <h3>
                                                Chemistry
                                            </h3>
                                            <p align="right">
                                                <%="Total: "+ChemSize%>
                                            </p>

                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="thumbnail">
                                        <a href="uLitList.jsp">
                                            <img alt="300x200"
                                                 src="http://radon-mclean.org/wp-content/uploads/sites/82/2016/07/Literature-and-Novels.jpg">
                                        </a>
                                        <div class="caption">
                                            <h3>
                                                Literature
                                            </h3>
                                            <p align="right">
                                                <%="Total: "+LitSize%>
                                            </p>
                                        </div>
                                    </div>

                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div id="push"></div>
        </div>


        <script src="/plugins/bootstrap-select.min.js"></script>
        <script src="/codemirror/jquery.codemirror.js"></script>
        <script src="/beautifier.js"></script>
        <script>
            (function (i, s, o, g, r, a, m) {
                i['GoogleAnalyticsObject'] = r;
                i[r] = i[r] || function () {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date();
                a = s.createElement(o),
                    m = s.getElementsByTagName(o)[0];
                a.async = 1;
                a.src = g;
                m.parentNode.insertBefore(a, m)
            })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
            ga('create', 'UA-40413119-1', 'bootply.com');
            ga('send', 'pageview');
        </script>
        <script>
            jQuery.fn.shake = function (intShakes, intDistance, intDuration, foreColor) {
                this.each(function () {
                    if (foreColor && foreColor != "null") {
                        $(this).css("color", foreColor);
                    }
                    $(this).css("position", "relative");
                    for (var x = 1; x <= intShakes; x++) {
                        $(this).animate({
                            left: (intDistance * -1)
                        }, (((intDuration / intShakes) / 4)))
                            .animate({
                                left: intDistance
                            }, ((intDuration / intShakes) / 2))
                            .animate({
                                left: 0
                            }, (((intDuration / intShakes) / 4)));
                        $(this).css("color", "");
                    }
                });
                return this;
            };
        </script>
        <script>
            $(document).ready(function () {

                $('.tw-btn').fadeIn(3000);
                $('.alert').delay(5000).fadeOut(1500);

                $('#btnLogin').click(function () {
                    $(this).text("...");
                    $.ajax({
                        url: "/loginajax",
                        type: "post",
                        data: $('#formLogin').serialize(),
                        success: function (data) {
                            //console.log('data:'+data);
                            if (data.status == 1 && data.user) { //logged in
                                $('#menuLogin').hide();
                                $('#lblUsername').text(data.user.username);
                                $('#menuUser').show();
                                /*
                                $('#completeLoginModal').modal('show');
                                $('#btnYes').click(function() {
                                    window.location.href="/";
                                });
                                */
                            } else {
                                $('#btnLogin').text("Login");
                                prependAlert("#spacer", data.error);
                                $('#btnLogin').shake(4, 6, 700, '#CC2222');
                                $('#username').focus();
                            }
                        },
                        error: function (e) {
                            $('#btnLogin').text("Login");
                            console.log('error:' + JSON.stringify(e));
                        }
                    });
                });
                $('#btnRegister').click(function () {
                    $(this).text("Wait..");
                    $.ajax({
                        url: "/signup?format=json",
                        type: "post",
                        data: $('#formRegister').serialize(),
                        success: function (data) {
                            console.log('data:' + JSON.stringify(data));
                            if (data.status == 1) {
                                $('#btnRegister').attr("disabled", "disabled");
                                $('#formRegister').text('Thanks. You can now login using the Login form.');
                            } else {
                                prependAlert("#spacer", data.error);
                                $('#btnRegister').shake(4, 6, 700, '#CC2222');
                                $('#btnRegister').text("Sign Up");
                                $('#inputEmail').focus();
                            }
                        },
                        error: function (e) {
                            $('#btnRegister').text("Sign Up");
                            console.log('error:' + e);
                        }
                    });
                });

                $('.loginFirst').click(function () {
                    $('#navLogin').trigger('click');
                    return false;
                });

                $('#btnForgotPassword').on('click', function () {
                    $.ajax({
                        url: "/resetPassword",
                        type: "post",
                        data: $('#formForgotPassword').serializeObject(),
                        success: function (data) {
                            if (data.status == 1) {
                                prependAlert("#spacer", data.msg);
                                return true;
                            } else {
                                prependAlert("#spacer", "Your password could not be reset.");
                                return false;
                            }
                        },
                        error: function (e) {
                            console.log('error:' + e);
                        }
                    });
                });

                $('#btnContact').click(function () {

                    $.ajax({
                        url: "/contact",
                        type: "post",
                        data: $('#formContact').serializeObject(),
                        success: function (data) {
                            if (data.status == 1) {
                                prependAlert("#spacer", "Thanks. We got your message and will get back to you shortly.");
                                $('#contactModal').modal('hide');
                                return true;
                            } else {
                                prependAlert("#spacer", data.error);
                                return false;
                            }
                        },
                        error: function (e) {
                            console.log('error:' + e);
                        }
                    });
                    return false;
                });

                /*
                $('.nav .dropdown-menu input').on('click touchstart',function(e) {
                    e.stopPropagation();
                });
                */


            });
            $.fn.serializeObject = function () {
                var o = {};
                var a = this.serializeArray();
                $.each(a, function () {
                    if (o[this.name] !== undefined) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                });
                return o;
            };
            var prependAlert = function (appendSelector, msg) {
                $(appendSelector).after('<div class="alert alert-info alert-block affix" id="msgBox" style="z-index:1300;margin:14px!important;">' + msg + '</div>');
                $('.alert').delay(3500).fadeOut(1000);
            }
        </script>
        <!-- Quantcast Tag -->
        <script type="text/javascript">
            var _qevents = _qevents || [];

            (function () {
                var elem = document.createElement('script');
                elem.src = (document.location.protocol == "https:" ? "https://secure" : "http://edge") + ".quantserve.com/quant.js";
                elem.async = true;
                elem.type = "text/javascript";
                var scpt = document.getElementsByTagName('script')[0];
                scpt.parentNode.insertBefore(elem, scpt);
            })();

            _qevents.push({
                qacct: "p-0cXb7ATGU9nz5"
            });
        </script>
        <noscript>
            &amp;amp;amp;amp;amp;amp;amp;amp;amp;lt;div style="display:none;"&amp;amp;amp;amp;amp;amp;amp;amp;amp;gt;
            &amp;amp;amp;amp;amp;amp;amp;amp;amp;lt;img src="//pixel.quantserve.com/pixel/p-0cXb7ATGU9nz5.gif"
            border="0" height="1" width="1" alt="Quantcast"/&amp;amp;amp;amp;amp;amp;amp;amp;amp;gt;
            &amp;amp;amp;amp;amp;amp;amp;amp;amp;lt;/div&amp;amp;amp;amp;amp;amp;amp;amp;amp;gt;
        </noscript>
        <!-- End Quantcast tag -->
        <div id="completeLoginModal" class="modal hide">
            <div class="modal-header">
                <a href="#" data-dismiss="modal" aria-hidden="true" class="close">×</a>
                <h3>Do you want to proceed?</h3>
            </div>
            <div class="modal-body">
                <p>This page must be refreshed to complete your login.</p>
                <p>You will lose any unsaved work once the page is refreshed.</p>
                <br>
                <br>
                <p>Click "No" to cancel the login process.</p>
                <p>Click "Yes" to continue...</p>
            </div>
            <div class="modal-footer">
                <a href="#" id="btnYes" class="btn danger">Yes, complete login</a>
                <a href="#" data-dismiss="modal" aria-hidden="true" class="btn secondary">No</a>
            </div>
        </div>
        <div id="forgotPasswordModal" class="modal hide">
            <div class="modal-header">
                <a href="#" data-dismiss="modal" aria-hidden="true" class="close">×</a>
                <h3>Password Lookup</h3>
            </div>
            <div class="modal-body">
                <form class="form form-horizontal" id="formForgotPassword">
                    <div class="control-group">
                        <label class="control-label" for="inputEmail">Email</label>
                        <div class="controls">
                            <input name="_csrf" id="token" value="CkMEALL0JBMf5KSrOvu9izzMXCXtFQ/Hs6QUY=" type="hidden">
                            <input name="email" id="inputEmail" placeholder="you@youremail.com" required=""
                                   type="email">
                            <span class="help-block"><small>Enter the email address you used to sign-up.</small></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer pull-center">
                <a href="#" data-dismiss="modal" aria-hidden="true" class="btn">Cancel</a>
                <a href="#" data-dismiss="modal" id="btnForgotPassword" class="btn btn-success">Reset Password</a>
            </div>

        </div>
        <div id="upgradeModal" class="modal hide">
            <div class="modal-header">
                <a href="#" data-dismiss="modal" aria-hidden="true" class="close">×</a>
                <h4>Would you like to upgrade?</h4>
            </div>
            <div class="modal-body">
                <p class="text-center"><strong></strong></p>
                <h1 class="text-center">$4
                    <small>/mo</small>
                </h1>
                <p class="text-center">
                    <small>Unlimited plys. Unlimited downloads. No Ads.</small>
                </p>
                <p class="text-center"><img src="/assets/i_visa.png" alt="visa" width="50"> <img src="/assets/i_mc.png"
                                                                                                 alt="mastercard"
                                                                                                 width="50"> <img
                        src="/assets/i_amex.png" alt="amex" width="50"> <img src="/assets/i_discover.png" alt="discover"
                                                                             width="50"> <img src="/assets/i_paypal.png"
                                                                                              alt="paypal" width="50">
                </p>
            </div>
            <div class="modal-footer pull-center">
                <a href="/upgrade" class="btn btn-block btn-huge btn-success"><strong>Upgrade Now</strong></a>
                <a href="#" data-dismiss="modal" class="btn btn-block btn-huge">No Thanks, Maybe Later</a>
            </div>
        </div>
        <div id="contactModal" class="modal hide">
            <div class="modal-header">
                <a href="#" data-dismiss="modal" aria-hidden="true" class="close">×</a>
                <h3>Contact Us</h3>
                <p>suggestions, questions or feedback</p>
            </div>
            <div class="modal-body">
                <form class="form form-horizontal" id="formContact">
                    <input name="_csrf" id="token" value="CkMEALL0JBMf5KSrOvu9izzMXCXtFQ/Hs6QUY=" type="hidden">
                    <div class="control-group">
                        <label class="control-label" for="inputSender">Name</label>
                        <div class="controls">
                            <input name="sender" id="inputSender" class="input-large" placeholder="Your name"
                                   type="text">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputMessage">Message</label>
                        <div class="controls">
                            <textarea name="notes" rows="5" id="inputMessage" class="input-large"
                                      placeholder="Type your message here"></textarea>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputEmail">Email</label>
                        <div class="controls">
                            <input name="email" id="inputEmail" class="input-large"
                                   placeholder="you@youremail.com (for reply)" required="" type="text">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer pull-center">
                <a href="#" data-dismiss="modal" aria-hidden="true" class="btn">Cancel</a>
                <a href="#" data-dismiss="modal" aria-hidden="true" id="btnContact" role="button"
                   class="btn btn-success">Send</a>
            </div>
        </div>


        <script src="/plugins/bootstrap-pager.js"></script>
    </div>
</div>
<script src="js/login.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="js/facebooklogin.js"></script>
<script src="js/profile.js"></script>
<script src="js/firebase.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase-app.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase-auth.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase-database.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase-firestore.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase-messaging.js"></script>
</body>
</html>
