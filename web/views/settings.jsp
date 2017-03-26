<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Greenhouse Settings</title>

    <link rel="stylesheet" type="text/css" href="../css/greenhouse.css">
    <link rel="stylesheet" type="text/css" href="../css/rangeslider.css">

    <script src="js/jquery3.1.1.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato:100,300,400,700" rel="stylesheet">

    <script src="../js/automation.js"></script>
    <script src="../js/rangeslider.js"></script>

</head>
<body>
    <div class="container" id="container">
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
            <div class="top-controls-panel">
                <div class="top-panel-1"></div>
                <div class="top-panel-title">GREENHOUSE</div>
                <div class="top-panel-2"></div>
                <div class="top-panel-3">
                    <!-- Top table row -->
                    <div class="big-table" id="zone-1">ZONE 1</div>
                    <div class="big-table" id="zone-2">ZONE 2</div>

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
                    <div class="light-title">LIGHT</div>
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
                <div class="manual-panel-2">
                    <div class="controls-title">CONTROLS</div>
                    <label class="light-switch switch-left-right">
                        <input id="light-check" class="switch-input" type="checkbox" />
                        <span id="light-switch" class="switch-label" data-on="On" data-off="&nbsp;&nbsp;&nbsp;Off"></span>
                        <span class="switch-handle"></span>
                    </label>
                    <div class="control-caption">LIGHTS</div>
                </div>
                <div class="manual-panel-3">
                    <label class="switch switch-left-right">
                        <input id="shade-check" class="switch-input" name="shade-switch" type="checkbox" />
                        <span id="shade-switch" class="switch-label" data-on="Open" data-off="Closed"></span>
                        <span class="switch-handle"></span>
                        <div class="control-caption">SHADES</div>
                    </label>


                    <label class="switch switch-left-right">
                        <input id="fan-check" class="switch-input" type="checkbox" />
                        <span id="fan-switch" class="switch-label" data-on="On" data-off="&nbsp;&nbsp;&nbsp;Off"></span>
                        <span class="switch-handle"></span>
                        <div class="control-caption">FANS</div>
                    </label>

                    <label class="switch switch-left-right">
                        <input id="water-check" class="switch-input" type="checkbox" />
                        <span id="water-switch" class="switch-label" data-on="On" data-off="&nbsp;&nbsp;&nbsp;Off"></span>
                        <span class="switch-handle"></span>
                        <div class="control-caption">WATER</div>
                    </label>
                </div>
                <!-- End Manual Controls Section -->

                <!-- Temperature Section -->
                <div class="manual-panel-1"></div>
                <div class="manual-panel-5">
                    <div class="controls-title">TEMPERATURE</div>
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
                <div class="manual-panel-4">
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
                <!-- End Temperature Section -->
            </div>


        <% } %>
    </div>

    <div id="zone-modal" class="zone-modal close">
        <div class="zone-modal-content">
            <!--
            <span class="zone-title" id="zone-title"></span>
            <div class="modal-title">WATER SCHEDULE</div>
            <table class="modal-table" id="water-schedule-table">
                <thead>
                    <tr>
                        <th> </th>
                        <th>SUNDAY</th>
                        <th>MONDAY</th>
                        <th>TUESDAY</th>
                        <th>WEDNESDAY</th>
                        <th>THURSDAY</th>
                        <th>FRIDAY</th>
                        <th>SATURDAY</th>
                    </tr>
                </thead>



            </table>
            -->
        </div>
    </div>
    <div id="zone-modal-close" class="zone-modal-close close"></div>
</body>
</html>
