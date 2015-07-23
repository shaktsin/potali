<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>ofCampus</title>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="/resources/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="/resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>

<body>
<nav class="white" role="navigation">
    <div class="nav-wrapper container">
        <a href = "index.html" class = "navbar-brand">
            <img src="/resources/images/ofcampusLogo.jpg" alt="ofcampusLogoLogo" height="96" width="96"/>
        </a>
        <ul class="right hide-on-med-and-down">
            <li class = "active"> <a href ="#">Home</a></li>
            <%--<li> <a href ="registerp.html">Register</a> </li>--%>
            <li> <a href ="#">About Us</a> </li>
            <li> <a href ="#">Contact Us</a> </li>
        </ul>

        <ul id="nav-mobile" class="side-nav">
            <li class = "active"> <a href ="#">Home</a></li>
            <%--<li> <a href ="registerp.html">Register</a> </li>--%>
            <li> <a href ="#">About Us</a> </li>
            <li> <a href ="#">Contact Us</a> </li>
        </ul>
        <a href="#" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>
    </div>
</nav>

<div id="index-banner" class="parallax-container">
    <div class="section no-pad-bot">
        <div class="container">
            <div class = "row">
                <br><br><br><br><br>
                <div class="col s12 m6 offset-m3" align = "center">
                    <h1 style = "font-size : 2.8rem; font-family: 'bebas neue', bebas, helvetica, sans-serif" class="header center white-text text-lighten-2">O F C A M P U S</h1>
                    <h5 style = "font-size: 1.6rem">OfCampus helps you discover and interact with your alumni based on your interests, location and professional path</h5>
                    <br>
                    <h5 style = "font-size: 1.6rem">Download our app and start connecting</h5>
                </div>
            </div>
            <div class="row center">
                <a target="_blank" href="http://play.google.com/store/apps/details?id=com.ofcampus">
                    <img class="android" src="/resources/images/android.jpg" height = "65"/>
                </a>
            </div>
            <br><br>

        </div>
    </div>
    <div class="parallax"><img src="/resources/images/background1.jpg" alt="Unsplashed background img 1"></div>
</div>


<div class="container">
    <div class="section">

        <!--   Icon Section   -->
        <div class="row">
            <div class="col s12 m4">
                <div class="icon-block">
                    <h2 class="center brown-text"><i class="material-icons">flash_on</i></h2>
                    <h5 class="center">Find your network</h5>
                    <p class="light">If your college is registered with us, you can login with the college provided login-id password. If not, you can write to us and we'll try to get your college on board</p>
                </div>
            </div>
            <div class="col s12 m4">
                <div class="icon-block">
                    <h2 class="center brown-text"><i class="material-icons">group</i></h2>
                    <h5 class="center">Join interest groups</h5>
                    <p class="light">Find and join circles based on your location and social & professional interests    </p>
                </div>
            </div>
            <div class="col s12 m4">
                <div class="icon-block">
                    <h2 class="center brown-text"><i class="material-icons">settings</i></h2>
                    <h5 class="center">Start networking</h5>
                    <p class="light">Participate in discussions, post job openings, upload resumes and classified ads or just ask for help!</p>
                </div>
            </div>

        </div>

    </div>
</div>

<footer class="page-footer teal" style="padding-top:0px; bottom:0; width:100%">
    <div class="footer-copyright" >
        <span class = "navbar-text pull-left" style="margin-left:20px"> Ofcampus &#169 2015  </span>
        <span class = "navbar-text right" style="margin-right:20px"> An IIT-ISB Initiative  </span>
    </div>
</footer>



<!--  Scripts-->
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="/resources/js/materialize.js"></script>
<script src="/resources/js/init.js"></script>

</body>
</html>
