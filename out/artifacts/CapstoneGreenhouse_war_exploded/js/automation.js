// Threshold Variables
var light;
var temperature;
var humidity;
var moisture;

// State Variables
var lightSwitch;
var shadeSwitch;
var fanSwitch;
var waterSwitch;
var vents;
var heater;

// Sliders
var slider;
var slider2;
var slider3;
var slider4;



// Query API every 3 seconds
window.setInterval( function() {

    $.ajax({
        type: 'GET',
        url: '/api/state',
        success: function(response) {
            var data = JSON.parse(response);

            // Set UI switches to returned values
            if( data.lights != lightSwitch ) {
                $('#light-switch').trigger('click');
                lightSwitch = data.lights;
            }

            if( data.shades != shadeSwitch ) {
                $('#shade-switch').trigger('click');
                shadeSwitch = data.shades;
            }

            if( data.fans != fanSwitch ) {
                $('#fan-switch').trigger('click');
                fanSwitch = data.fans;
            }

            if( data.pump != waterSwitch ) {
                $('#water-switch').trigger('click');
                waterSwitch = data.pump;
            }
        }
    });

    updateAutomationValues();

}, 3000 );

$(document).ready( function() {

    // Check if browser is Raspberry Pi
    if ( navigator.platform.toLowerCase().indexOf('linux') >= 0 ) {
        $('#nav-list').addClass('nav-small');
        $('#nav-list').html('<ul><a href="/settings"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');

    } else {
        $('#nav-list').addClass('nav');
        $('#nav-list').html('<ul><a href="index.html"><li class="nav-li"><img width="40" height="40" src="images/home.png"><span class="nav-item">HOME</span></li></a><a href="#"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a><a href="/documentation"><li class="nav-li"><img style="margin-top:22px;" width="40" height="30" src="images/learn.png"><span class="nav-item">LEARN</span></li></a></ul>');
    }

    // Get state values
    $.ajax({
        type: 'GET',
        url: '/api/state',
        success: function(response) {
            var data = JSON.parse(response);

            // Set UI switches to returned values
            if( data.lights ) {
                $('#light-switch').trigger('click');
            }

            if( data.shades ) {
                $('#shade-switch').trigger('click');
            }

            if( data.fans ) {
                $('#fan-switch').trigger('click');
            }

            if( data.pump ) {
                $('#water-switch').trigger('click');
            }

            lightSwitch = data.lights;
            shadeSwitch = data.shades;
            fanSwitch = data.fans;
            waterSwitch = data.pump;
            vents = data.vents;
            heater = data.heater;
        }
    });

    getAutomationValues();

    // Button click handlers
    $('.touch-button').click( function() {
        var id = $(this)[0].id;
        var pw = $('#password').val();

        $('#password').val('');
        $('#password').val( pw + id );
    });

    // Clear password form
    $('#clear').click( function() {
        $('#password').val('');
    })

    $('#login').click( function() {
        $('#login-form').submit();
    })


});

function updateAutomationValues() {
    $.ajax({
        type: 'GET',
        url: '/api/automation',
        success: function (response) {
            var data = JSON.parse(response);

            // Set values
            light = data.light;
            temperature = data.temperature;
            humidity = data.humidity;
            moisture = data.moisture;

            if( parseInt(light) != slider.getValue() ) {
                slider.setValue(parseInt(light));
            }

            if( parseInt(temperature) != slider2.getValue() ) {
                slider2.setValue(parseInt(temperature));
            }

            if( parseInt(humidity) != slider3.getValue() ) {
                slider3.setValue(parseInt(humidity));
            }

            if( parseInt(moisture) != slider4.getValue() ) {
                slider4.setValue(parseInt(moisture));
            }
        }
    });
}

function getAutomationValues() {
    // Get automation values
    $.ajax({
        type: 'GET',
        url: '/api/automation',
        success: function(response) {
            var data = JSON.parse(response);

            // Set values
            light = data.light;
            temperature = data.temperature;
            humidity = data.humidity;
            moisture = data.moisture;

            // Declare sliders
            slider = $('#light-slider').CircularSlider({
                radius: 95,
                innerCircleRatio: '0.6',
                handleDist: 100,
                min: 0,
                max: 13000,
                value: parseInt(light),
                clockwise: true,
                labelSuffix: "",
                labelPrefix: "",
                shape: "Half Circle",
                touch: true,
                animate: true,
                animateDuration : 360,
                selectable: false,
                slide: function(ui, value) {},
                onSlideEnd: function(ui, value) {},
                formLabel: undefined,
                title: 'LIGHT',
                type: 'light',
                unit: 'lux'
            });

            slider2 = $('#temperature-slider').CircularSlider({
                radius: 95,
                innerCircleRatio: '0.6',
                handleDist: 200,
                min: 60,
                max: 111,
                value: parseInt(temperature),
                clockwise: true,
                labelSuffix: "",
                labelPrefix: "",
                shape: "Half Circle",
                touch: true,
                animate: true,
                animateDuration : 360,
                selectable: false,
                slide: function(ui, value) {},
                onSlideEnd: function(ui, value) {},
                formLabel: undefined,
                title: 'TEMPERATURE',
                type: 'temperature',
                unit: '° F'
            });

            slider3 = $('#humidity-slider').CircularSlider({
                radius: 95,
                innerCircleRatio: '0.6',
                handleDist: 200,
                min: 0,
                max: 101,
                value: parseInt(humidity),
                clockwise: true,
                labelSuffix: "",
                labelPrefix: "",
                shape: "Half Circle",
                touch: true,
                animate: true,
                animateDuration : 360,
                selectable: false,
                slide: function(ui, value) {},
                onSlideEnd: function(ui, value) {},
                formLabel: undefined,
                title: 'HUMIDITY',
                type: 'humidity'
            });

            slider4 = $('#moisture-slider').CircularSlider({
                radius: 95,
                innerCircleRatio: '0.6',
                handleDist: 200,
                min: 0,
                max: 101,
                value: parseInt(moisture),
                clockwise: true,
                labelSuffix: "",
                labelPrefix: "",
                shape: "Half Circle",
                touch: true,
                animate: true,
                animateDuration : 360,
                selectable: false,
                slide: function(ui, value) {},
                onSlideEnd: function(ui, value) {},
                formLabel: undefined,
                title: 'SOIL MOISTURE',
                type: 'moisture',
                unit: '%'
            });
        }
    });
}




