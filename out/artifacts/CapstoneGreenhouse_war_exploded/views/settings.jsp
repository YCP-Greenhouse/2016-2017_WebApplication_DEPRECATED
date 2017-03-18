<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Greenhouse Settings</title>

    <link rel="stylesheet" type="text/css" href="../css/greenhouse.css">
    <link rel="stylesheet" type="text/css" href="../css/circular-slider.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,400,700" rel="stylesheet">

    <script src="../js/automation.js"></script>
    <script src="../js/circular-slider.js"></script>
</head>
<body>
    <div class="container">
        <div class="title-div">
            <span class="subtitle">GOODE ELEMENTARY</span><br>
            <span class="title-bold">GREENHOUSE</span>
        </div>

        <div id="nav-list"></div>

        <div class="automation-selection">
            <span class="automation-option active">AUTOMATION SETTINGS  </span>|
            <span class="automation-option"> SCHEDULE</span>
        </div>

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

                <!-- <button type="submit">Login</button> -->

        </div>

        <%
            // If user is logged in, display setting options
        } else {  %>
        <div class="automation-container">
            <div class="title-slider-div">
                <div class="title-slider-1"></div>
                <div class="title-slider-2">&nbsp;THRESHOLDS </div>
                <div class="title-slider-3"></div>
            </div>

            <div class="slider-div">
                <div id="light-slider" class="slider"></div>
                <div id="temperature-slider" class="slider"></div>
                <!-- <div id="humidity-slider" class="slider"></div> -->
                <div id="moisture-slider" class="slider"></div>
            </div>

            <div class="manual-controls">
                <div class="manual-panel-1"></div>
                <div class="manual-panel-2">
                    <div class="controls-title">MANUAL CONTROLS</div>
                    <label class="light-switch switch-left-right">
                        <input id="light-check" class="switch-input" type="checkbox" />
                        <span id="light-switch" class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                    </label>
                    <div class="control-caption">LIGHTS</div>
                </div>
                <div class="manual-panel-3">
                    <label class="switch switch-left-right">
                        <input id="shade-check" class="switch-input" name="shade-switch" type="checkbox" />
                        <span id="shade-switch" class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                        <div class="control-caption">SHADES</div>
                    </label>


                    <label class="switch switch-left-right">
                        <input id="fan-check" class="switch-input" type="checkbox" />
                        <span id="fan-switch" class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                        <div class="control-caption">FANS</div>
                    </label>

                    <label class="switch switch-left-right">
                        <input id="water-check" class="switch-input" type="checkbox" />
                        <span id="water-switch" class="switch-label" data-on="On" data-off="Off"></span>
                        <span class="switch-handle"></span>
                        <div class="control-caption">WATER</div>
                    </label>
                </div>
            </div>
        </div>
        <% } %>

    </div>
</body>
</html>
