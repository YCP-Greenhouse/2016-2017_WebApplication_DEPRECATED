<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Greenhouse Settings</title>

    <link rel="stylesheet" type="text/css" href="../css/greenhouse.css">
    <link rel="stylesheet" type="text/css" href="../css/rangeslider.css">

    <script src="js/jquery3.1.1.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,400,700" rel="stylesheet">

    <script src="../js/automation.js"></script>
    <script src="../js/schedule.js"></script>
    <script src="../js/rangeslider.js"></script>


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
            // If user is logged in, display setting options
        } else {  %>
            <div class="automation-selection">
                <a href="/settings"><button id="automation" class="settings-button selected">AUTOMATION</button></a>
                <a href="/notifications"><button id="notifications" class="settings-button">NOTIFICATIONS</button></a>
            </div>

            <div class="top-controls-panel">
                <div class="top-panel-1"></div>
                <div class="top-panel-title">TABLES</div>
                <div class="top-panel-2"></div>
                <div class="top-panel-3">
                    <!-- Top table row -->
                    <div class="zone-instruction">Select a zone to configure light and water schedules</div>
                    <div class="big-table-left" id="zone-1">ZONE 1</div>
                    <div class="big-table-right" id="zone-2">ZONE 2</div>

                    <!-- Middle table row -->
                    <div class="middle-table-div">
                        <div class="small-table" style="margin-right: 20px;" id="zone-3">ZONE 3</div>
                        <div class="small-table" id="zone-4">ZONE 4</div>
                    </div>

                    <!-- Bottom table row -->
                    <div class="bottom-table-div">
                        <div class="small-table" style="margin-right: 20px;" id="zone-5">ZONE 5</div>
                        <div class="small-table" id="zone-6">ZONE 6</div>
                    </div>
                </div>

                <div class="light-control-panel">
                    <div class="light-left-corner"></div>
                    <div class="light-title">MAX LIGHT</div>
                    <div class="light-right-corner"></div>
                    <div class="light-body">
                        <div class="light-val">
                            <output id="light-val"></output>&nbsp;
                            <span class="fahrenheit">lux</span>
                        </div>

                        <input id="light-slider" type="range" step="100" min="0" max="65000" data-orientation="vertical">
                    </div>
                </div>
            </div>

            <div class="bottom-controls-panel">
                <!-- Manual Controls Section -->
                <div class="manual-panel-1"></div>
                <div class="manual-panel-title">MANUAL CONTROLS</div>
                <div class="manual-panel-2"></div>
                <div class="manual-panel-3">
                    <div id="light-switch-div" class="switch-div" style="order: 1; margin-left: 10px;">
                        <div class="switch-overlay" id="light-switch-overlay"></div>
                        <label class="switch switch-left-right" id="light-switch-label">
                            <input id="light-check" class="switch-input" type="checkbox" />
                            <span id="light-switch" class="switch-label" data-on="On" data-off="&nbsp;&nbsp;&nbsp;Off"></span>
                            <span class="switch-handle"></span>
                            <div class="control-caption">LIGHTS</div>
                        </label>
                    </div>

                    <div id="shades-switch-div" class="switch-div" style="order: 2">
                        <div class="switch-overlay"></div>
                        <label class="shade-switch shade-switch-left-right">
                            <input id="shade-check" class="shade-switch-input" name="shade-switch" type="checkbox" />
                            <span id="shade-switch" class="shade-switch-label" data-on="Extended" data-off="Retracted"></span>
                            <span class="shade-switch-handle"></span>
                            <div class="control-caption">SHADES</div>
                        </label>
                    </div>

                    <div id="fans-switch-div" class="switch-div" style="order: 3">
                        <div class="switch-overlay"></div>
                        <label class="switch switch-left-right">
                            <input id="fan-check" class="switch-input" type="checkbox" />
                            <span id="fan-switch" class="switch-label" data-on="On" data-off="&nbsp;&nbsp;&nbsp;Off"></span>
                            <span class="switch-handle"></span>
                            <div class="control-caption">FANS</div>
                        </label>
                    </div>

                    <div id="water-switch-div" class="switch-div" style="order: 4">
                        <div class="switch-overlay" id="water-switch-overlay"></div>
                        <label class="switch switch-left-right" id="water-switch-label">
                            <input id="water-check" class="switch-input" type="checkbox" />
                            <span id="water-switch" class="switch-label" data-on="On" data-off="&nbsp;&nbsp;&nbsp;Off"></span>
                            <span class="switch-handle"></span>
                            <div class="control-caption">WATER</div>
                        </label>
                    </div>

                </div>

                <!-- End Manual Controls Section -->

                <!-- Temperature Section -->

                <div class="manual-panel-1" style="margin-top:-10px; margin-left:30px;"></div>
                <div class="manual-light-title">TEMPERATURE</div>
                <div class="manual-panel-4"></div>
                <div class="manual-panel-5">
                    <div style="float: left; width: 50%;">
                        <div class="temperature-label">
                            <span class="temperature-value" id="temperature-low"></span>
                            <span class="temp-f">F</span>
                            <div class="control-caption-2">LOW</div>
                        </div>

                        <div class="temperature-buttons">
                            <div class="temperature-button" id="temp-low-increase">+</div>
                            <div class="temperature-button" id="temp-low-decrease">-</div>
                        </div>
                    </div>

                    <div style="float: left; width: 50%;">
                        <div class="temperature-label">
                            <span class="temperature-value" id="temperature-high"></span>
                            <span class="temp-f">F</span>
                            <div class="control-caption-2">HIGH</div>
                        </div>

                        <div class="temperature-buttons">
                            <div class="temperature-button" id="temp-high-increase">+</div>
                            <div class="temperature-button" id="temp-high-decrease">-</div>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>
    </div>

    <!-- Hidden by default -->
    <div id="zone-modal" class="zone-modal">
        <div class="zone-modal-content">
            <div class="schedule-title" id="zone-title">SCHEDULE</div>
            <button id="schedule-close" class="schedule-close-button">CLOSE</button>

            <div id="calendar" class="calendar-div">
                <table class="schedule-calendar" id="calendar-table" cellspacing="0">
                    <thead id="schedule-heading"></thead>

                    <tbody id="schedule-body"></tbody>

                </table>
            </div>

            <div id="schedule-modal" class="add-schedule-modal">
                <div class="add-schedule">
                    <span id="modal-title" class="schedule-modal-title"></span><br>
                    <div class="schedule-modal-text">
                        <span>Start Time: </span><input type="time" class="time-input" id="start-time" onclick="timeUpdate()"><br>
                        <span>End Time: </span><input type="time" class="time-input" id="end-time" onclick="timeUpdate()"><br>
                        <span>Hours: </span><input type="number" style="width: 92px;" min="1" max="24" class="time-input" id="hours" onclick="hourUpdate()"><br>

                        <div id="schedule-div" style="height: 35px; margin-bottom: 2px;">

                            <span>Schedule: <span class="time-input" style="margin-left: 10px;">Water<input id="water-radio" type="radio" name="schedule" value="water"></span><span class="time-input">Light<input id="light-radio" type="radio" name="schedule" value="light"></span></span><br>

                        </div>

                        <div id="schedule-description"class="schedule-description"></div>

                        <div style="margin-top: 40px; height: 30px;" id="type-div">
                            Type:
                            <span class="time-input" style="margin-left: 5px;">Blocked<input id="blocked-radio" type="radio" name="type" value="blocked"></span>
                            <span class="time-input" style="margin-left: 5px;">Sensors<input id="sensors-radio" type="radio" name="type" value="sensors"></span>
                            <span class="time-input" style="margin-left: 5px;">Constant<input id="constant-radio" type="radio" name="type" value="constant"></span><br><br><br>
                        </div>

                        <div id="type-description" class="schedule-description"></div>

                        <div id="sensor-threshold-div" class="sensor-threshold-div">

                        </div>

                        <input type="hidden" id="inverse" value="0">
                        <input type="hidden" id="schedule-date">
                        <input type="hidden" id="schedule-id">
                        <input type="hidden" id="schedule-type">
                        <input type="hidden" id="zoneid">
                        <input type="hidden" id="schedule-option">
                    </div>

                    <div class="schedule-buttons">
                        <button id="save-button" class="schedule-button">Save</button>
                        <button id="close-button" class="schedule-button">Close</button>
                        <button id="delete-button">Delete</button>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <div id="manual-control-alert" class="alert-div">
        Override schedule controls?
        <div class="button-div">
            <button class="alert-button red" style="order: 1" id="no">NO</button>
            <button class="alert-button green" style="order: 2" id="yes">YES</button>
        </div>

    </div>

</body>
</html>
