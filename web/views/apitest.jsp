<html>
<head>
  <title>Goode Greenhouse</title>

  <link rel="stylesheet" type="text/css" href="css/apitest.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="js/apitest.js"></script>

</head>
<body>

<% // If session is null, display login window
  String user = null;
  if( session.getAttribute("user") == null ) {
%>
<div class="login-div">
  <form action="/login" method="post">
    <label>Username</label><input type="text" name="username">
    <label>Password</label><input type="password" name="password">
    <button type="submit">Login</button>
  </form>
</div>


<%
  // If user is logged in, display API forms
  } else {
      user = (String) session.getAttribute("user");%>
<div class="username-div">
  Welcome ${user}
</div>

<div id="set-state-div" class="set-state-div">
  <b>Set State</b><br>Pi to Web App<br><br>
  <form id="set-state-form">
    Lights: <input type="checkbox" name="lights" id="lights-check" class="state-check"><br>
    Fans: <input type="checkbox" name="fans" id="fans-check" class="state-check"><br>
    Water Pump: <input type="checkbox" name="pump" id="pump-check" class="state-check"><br>
    Heater: <input type="checkbox" name="heater" id="heater-check" class="state-check"><br>
    Vents: <input type="checkbox" name="vents" id="vent-check" class="state-check"><br>
    Shades: <input type="checkbox" name="shades" id="shade-check" class="state-check">
  </form>
</div>


<div class="set-sensor-div">
  <form id="set-sensor-form" action="#" method="post">

    <b>Sensor API Values</b><br> Pi to Web App


    <div class="sensor-row">
      <div class="sensor-div">
        <b>Zone 1</b><br>
        Light: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights1"><br>
        Temperature: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature1"><br>
        Humidity: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity1"><br>
        Moisture: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture1">
      </div>

      <div class="sensor-div">
        <b>Zone 2</b><br>
        Light:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights2"><br>
        Temperature:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature2"><br>
        Humidity:  <input type="text" size="7" style="height: 18px;"class="sensor-span" id="set-humidity2"><br>
        Moisture:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture2">
      </div>
    </div>

    <div class="sensor-row">
      <div class="sensor-div">
        <b>Zone 3</b><br>
        Light:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights3"><br>
        Temperature:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature3"><br>
        Humidity:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity3"><br>
        Moisture:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture3">
      </div>

      <div class="sensor-div">
        <b>Zone 4</b><br>
        Light:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights4"><br>
        Temperature:  <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature4"><br>
        Humidity: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity4"><br>
        Moisture: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture4">
      </div>
    </div>

    <div class="sensor-row">
      <div class="sensor-div">
        <b>Zone 5</b><br>
        Light: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights5"><br>
        Temperature: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature5"><br>
        Humidity: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity5"><br>
        Moisture: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture5">
      </div>

      <div class="sensor-div">
        <b>Zone 6</b><br>
        Light: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights6"><br>
        Temperature: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature6"><br>
        Humidity: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity6"><br>
        Moisture: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture6">
      </div>
    </div>

    <div class="sensor-row">
      <div class="sensor-div">
        <b>Zone 7</b><br>
        Light: <input type="text" size="7" style="height: 18px;"class="sensor-span" id="set-lights7"><br>
        Temperature: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature7"><br>
        Humidity: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity7"><br>
        Moisture: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture7">
      </div>

      <div class="sensor-div">
        <b>Zone 8</b><br>
        Light: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-lights8"><br>
        Temperature: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-temperature8"><br>
        Humidity: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-humidity8"><br>
        Moisture: <input type="text" size="7" style="height: 18px;" class="sensor-span" id="set-moisture8">
      </div>
    </div>

    <br>
    <div id="sensor-submit" class="sensor-submit" style="">Submit</div>
    <div id="sensor-clear" class="sensor-submit" style="">Clear</div>
    <br>

  </form>
</div>

<div id="set-automation-div" class="set-automation-div">
  <b>Set Automation</b><br>Pi to Web App<br><br>
  <form id="set-automation-form">
    Lights: <input type="text" name="light" id="auto-lights" style="float:right;"><br>
    Temperature: <input type="text" name="temperature" id="auto-temperature" style="float:right;"><br>
    Humidity: <input type="text" name="humidity" id="auto-humidity" style="float:right;"><br>
    Moisture: <input type="text" name="moisture" id="auto-moisture" style="float:right;">
  </form>

  <br>
  <div id="automation-submit" class="sensor-submit" style="">Submit</div>
  <div id="automation-clear" class="sensor-submit" style="">Clear</div>
  <br>
</div>

<div class="set-error-div">
  <b>Set Error</b><br>Pi to Web App<br><br>
  <form id="set-error-form">
    Code: <input type="text" name="code" id="set-error-code" style="float:right;"><br>
    Message: <input type="text" name="message" id="set-error-message" style="float:right;">
  </form>

  <br>
  <div id="error-submit" class="sensor-submit" style="">Submit</div>
  <div id="error-clear" class="sensor-submit" style="">Clear</div>
  <br>

</div>



<% } %>




<div id="state-div" class="state-div">
  <b>State API Values</b> url: /api/state<br><br>
  Lights: <span class="state-span" id="lights-state"></span><br>
  Fans: <span class="state-span" id="fans-state"></span><br>
  Water Pump: <span class="state-span" id="pump-state"></span><br>
  Heater: <span class="state-span" id="heater-state"></span><br>
  Vents: <span class="state-span" id="vent-state"></span><br>
  Shades: <span class="state-span" id="shade-state"></span>
</div>


<div id="sensor-input-div" class="view-sensor-div">
  <b>Sensor API Values</b><br> url: /api/sensors
  <div class="last-time">Last Entry: <span id="last-time" style="margin-right: 35px;"></span></div>

  <div class="sensor-row">
    <div class="sensor-div">
      <b>Zone 1</b><br>
      Light: <span class="sensor-span" id="lights1"></span><br>
      Temperature: <span class="sensor-span" id="temperature1"></span><br>
      Humidity: <span class="sensor-span" id="humidity1"></span><br>
      Moisture: <span class="sensor-span" id="moisture1"></span>
    </div>

    <div class="sensor-div">
      <b>Zone 2</b><br>
      Light: <span class="sensor-span" id="lights2"></span><br>
      Temperature: <span class="sensor-span" id="temperature2"></span><br>
      Humidity: <span class="sensor-span" id="humidity2"></span><br>
      Moisture: <span class="sensor-span" id="moisture2"></span>
    </div>
  </div>

  <div class="sensor-row">
    <div class="sensor-div">
      <b>Zone 3</b><br>
      Light: <span class="sensor-span" id="lights3"></span><br>
      Temperature: <span class="sensor-span" id="temperature3"></span><br>
      Humidity: <span class="sensor-span" id="humidity3"></span><br>
      Moisture: <span class="sensor-span" id="moisture3"></span>
    </div>

    <div class="sensor-div">
      <b>Zone 4</b><br>
      Light: <span class="sensor-span" id="lights4"></span><br>
      Temperature: <span class="sensor-span" id="temperature4"></span><br>
      Humidity: <span class="sensor-span" id="humidity4"></span><br>
      Moisture: <span class="sensor-span" id="moisture4"></span>
    </div>
  </div>

  <div class="sensor-row">
    <div class="sensor-div">
      <b>Zone 5</b><br>
      Light: <span class="sensor-span" id="lights5"></span><br>
      Temperature: <span class="sensor-span" id="temperature5"></span><br>
      Humidity: <span class="sensor-span" id="humidity5"></span><br>
      Moisture: <span class="sensor-span" id="moisture5"></span>
    </div>

    <div class="sensor-div">
      <b>Zone 6</b><br>
      Light: <span class="sensor-span" id="lights6"></span><br>
      Temperature: <span class="sensor-span" id="temperature6"></span><br>
      Humidity: <span class="sensor-span" id="humidity6"></span><br>
      Moisture: <span class="sensor-span" id="moisture6"></span>
    </div>
  </div>

  <div class="sensor-row">
    <div class="sensor-div">
      <b>Zone 7</b><br>
      Light: <span class="sensor-span" id="lights7"></span><br>
      Temperature: <span class="sensor-span" id="temperature7"></span><br>
      Humidity: <span class="sensor-span" id="humidity7"></span><br>
      Moisture: <span class="sensor-span" id="moisture7"></span>
    </div>

    <div class="sensor-div">
      <b>Zone 8</b><br>
      Light: <span class="sensor-span" id="lights8"></span><br>
      Temperature: <span class="sensor-span" id="temperature8"></span><br>
      Humidity: <span class="sensor-span" id="humidity8"></span><br>
      Moisture: <span class="sensor-span" id="moisture8"></span>
    </div>
  </div>
</div>



<div class="automation-div">
  <b>Automation API Values</b><br>url: /api/automation<br><br>
  Light: <span class="state-span" id="light-automation"></span><br>
  Temperature: <span class="state-span" id="temperature-automation"></span><br>
  Humidity: <span class="state-span" id="humidity-automation"></span><br>
  Moisture: <span class="state-span" id="moisture-automation"></span>

</div>


<div class="error-div">
  <b>Error API</b><br>url: /api/error<br><br>
  Code: <span class="state-span" id="error-code"></span><br>
  Message: <span class="state-span" id="error-message"></span><br>
  Time: <span class="state-span" id="error-time"></span>
</div>



</body>
</html>