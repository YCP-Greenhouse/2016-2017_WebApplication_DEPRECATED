<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notifications</title>

    <link rel="stylesheet" type="text/css" href="../css/greenhouse.css">
    <link rel="stylesheet" type="text/css" href="../css/notifications.css">

    <script src="js/jquery3.1.1.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,400,700" rel="stylesheet">

    <script src="../js/notifications.js"></script>
</head>
<body>
    <div class="container" id="container">
        <div class="title-div">
            <span class="subtitle">GOODE ELEMENTARY</span><br>
            <span class="title-bold">GREENHOUSE</span>
        </div>

        <div id="nav-list"></div>



        <% // If session is null, display login window
            String user = null;
            if( session.getAttribute("user") == null ) {
        %>

        <div class="settings-login">
            <form action="/login" method="post" id="login-form">
                <input type="hidden" name="username" value="admin">
                <input id="password" type="password" name="password">
            </form>
            <div class="touch-pad">
                <button type="number" class="touch-button" id="1">1</button>
                <button type="number" class="touch-button" id="2">2</button>
                <button type="number" class="touch-button" id="3">3</button>
                <button type="number" class="touch-button" id="4">4</button>
                <button type="number" class="touch-button" id="5">5</button>
                <button type="number" class="touch-button" id="6">6</button>
                <button type="number" class="touch-button" id="7">7</button>
                <button type="number" class="touch-button" id="8">8</button>
                <button type="number" class="touch-button" id="9">9</button>
                <button type="number" class="action-button" id="clear">CLEAR</button>
                <button type="number" class="touch-button" id="0">0</button>
                <button type="number" class="action-button" id="login">LOGIN</button>
            </div>
        </div>

        <%
            // If user is logged in, display notification options
        } else {  %>
            <div class="automation-selection">
                <a href="/settings"><button id="automation" class="settings-button">AUTOMATION</button></a>
                <a href="/notifications"><button id="notifications" class="settings-button selected">NOTIFICATIONS</button></a>
            </div>

            <div class="notification-div">
                <div class="left-div">
                    <div class="settings-div">
                        <div class="contact-panel-1"></div>
                        <div class="contact-panel-title">SETTINGS</div>
                        <div class="contact-panel-2"></div>
                        <div class="contact-panel-3">
                            <div class="settings-options">
                                <input type="checkbox" id="temperature">
                                <label class="settings-label" for="temperature">Notify contacts when temperature is</label>
                                <div class="temperature-options">
                                    Below <input class="settings-input" type="number" id="temperature-below">
                                    Above <input class="settings-input" type="number" id="temperature-above">
                                </div><br><br>

                                <input type="checkbox" id="soil-moisture">
                                <label class="settings-label" for="soil-moisture">Notify contacts when soil moisture is</label>
                                <div class="temperature-options">
                                    Below <input class="settings-input" type="number" id="soil-below">
                                    Above <input class="settings-input" type="number" id="soil-above">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="error-div">
                        <div class="contact-panel-1"></div>
                        <div class="contact-panel-title">ERRORS</div>
                        <div class="contact-panel-2"></div>
                        <div class="contact-panel-3">
                            <table id="error-table" class="error-table" style="padding:5px;">
                                <thead>
                                <tr>
                                    <th style="width:70%;">Message</th>
                                    <th style="width:30%;">Time</th>
                                </tr>
                                </thead>
                                <tbody id="error-table-body"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="contact-div">
                    <div class="contact-panel-1"></div>
                    <div class="contact-panel-title">CONTACTS</div>
                    <div class="contact-panel-2"></div>
                    <div class="contact-panel-3">
                        <table id="contact-table" class="contact-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Position</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                </tr>
                            </thead>
                            <tbody id="contact-table-body"></tbody>
                        </table>
                    </div>
                </div>
            </div>
        <% } %>

    </div>
</body>
</html>
