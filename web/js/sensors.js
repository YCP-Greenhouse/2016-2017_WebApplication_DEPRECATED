// Get greenhouse state when page loads
$(document).ready( function() {
    getSensorValues();

    // Check if browser is Raspberry Pi
    if (navigator.platform.toLowerCase().indexOf('linux') >= 0) {
        $('#nav-list').addClass('nav-small');
        $('#nav-list').html('<ul><a href="/settings"><li class="nav-li"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-active"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');

    } else {
        $('#nav-list').addClass('nav');
        $('#nav-list').html('<ul><a href="index.html"><li class="nav-li"><img width="40" height="40" src="images/home.png"><span class="nav-item">HOME</span></li></a><a href="/settings"><li class="nav-li"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-active"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');
    }
});

// Query API every 3 seconds
window.setInterval( function() {

    // Get sensor values
    getSensorValues();

}, 3000 );

// Get sensor values
function getSensorValues() {

    $.ajax({
        type: 'GET',
        url: 'api/sensors',
        success: function(response) {
            var data = JSON.parse(response);

            // Set zone values
            for( var i=1; i<=data.length; i++ ) {
                $("#light-" + i).text( data[i-1].light + " " );
                $("#temperature-" + i).html( data[i-1].temperature + "&deg; ");
                $("#humidity-" + i).text( data[i-1].humidity + " " );
                $("#probe1-" + i).text( data[i-1].probe1 + " ");
                $("#probe2-" + i).text( data[i-1].probe2 + " ");
            }
        }
    });
}