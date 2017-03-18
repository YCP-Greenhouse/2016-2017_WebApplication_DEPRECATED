// Get greenhouse state when page loads
$(document).ready( function() {
    getState();
    getSensorValues();
    getAutomationValues();
    getErrorMessage();
});

// Query APIs every 3 seconds

window.setInterval( function() {

    // Get state values
    getState();

    // Get sensor values
    getSensorValues();

    // Get Automation values
    getAutomationValues();

    // Get Error Message
    getErrorMessage();

}, 3000 );

// Get greenhouse state
function getState() {

    $.ajax({
        type: 'GET',
        url: 'api/state',
        success: function(response) {
            var data = JSON.parse(response);

            // Set checkboxes under 'Set State'
            $("#lights-check").prop("checked", data.lights);
            $("#fans-check").prop("checked", data.fans);
            $("#pump-check").prop("checked", data.pump);
            $("#heater-check").prop("checked", data.heater);
            $("#vent-check").prop("checked", data.vents);
            $("#shade-check").prop("checked", data.shades);

            // Set text fields under 'Current State'
            $("#lights-state").text(BoolToOnOff(data.lights));
            $("#fans-state").text(BoolToOnOff(data.fans));
            $("#pump-state").text(BoolToOnOff(data.pump));
            $("#heater-state").text(BoolToOnOff(data.heater));
            $("#vent-state").text(BoolToOpenClosed(data.vents));
            $("#shade-state").text(BoolToOpenClosed(data.shades));

        }
    });
}

// Submit state form when checkbox is toggled
$(document).on('click', '.state-check', function() {
    $.ajax({
        type: 'POST',
        url: '/api/state',
        data: $('#set-state-form').serialize(),
        success: function(response) {
            getState();
        }
    });
});

// Get sensor values
function getSensorValues() {

    $.ajax({
        type: 'GET',
        url: 'api/sensors',
        success: function(response) {
            var data = JSON.parse(response);

            // Set zone values
            for( var i=1; i<=data.length; i++ ) {
                $("#lights" + i).text( data[i-1].light );
                $("#temperature" + i).text( data[i-1].temperature );
                $("#humidity" + i).text( data[i-1].humidity );
                $("#moisture" + i).text( data[i-1].moisture );
            }

            // Set "Last Entry"
            $("#last-time").text( data[0].sampletime);

        }
    });
}

// Submit sensor values
$(document).on('click', '#sensor-submit', function() {
    // Convert form data into array of JSON objects
    var vals = [];

    vals.push({ zone: 1 , light: $('#set-lights1').val() , temperature: $('#set-temperature1').val(), humidity: $('#set-humidity1').val(), moisture: $('#set-moisture1').val() });
    vals.push({ zone: 2 , light: $('#set-lights2').val() , temperature: $('#set-temperature2').val(), humidity: $('#set-humidity2').val(), moisture: $('#set-moisture2').val() });
    vals.push({ zone: 3 , light: $('#set-lights3').val() , temperature: $('#set-temperature3').val(), humidity: $('#set-humidity3').val(), moisture: $('#set-moisture3').val() });
    vals.push({ zone: 4 , light: $('#set-lights4').val() , temperature: $('#set-temperature4').val(), humidity: $('#set-humidity4').val(), moisture: $('#set-moisture4').val() });
    vals.push({ zone: 5 , light: $('#set-lights5').val() , temperature: $('#set-temperature5').val(), humidity: $('#set-humidity5').val(), moisture: $('#set-moisture5').val() });
    vals.push({ zone: 6 , light: $('#set-lights6').val() , temperature: $('#set-temperature6').val(), humidity: $('#set-humidity6').val(), moisture: $('#set-moisture6').val() });
    vals.push({ zone: 7 , light: $('#set-lights7').val() , temperature: $('#set-temperature7').val(), humidity: $('#set-humidity7').val(), moisture: $('#set-moisture7').val() });
    vals.push({ zone: 8 , light: $('#set-lights8').val() , temperature: $('#set-temperature8').val(), humidity: $('#set-humidity8').val(), moisture: $('#set-moisture8').val() });


    /* Set values to make testing easier
    vals.push({ zone: 1 , light: 1 , temperature: 1, humidity: 1, moisture: 1 });
    vals.push({ zone: 2 , light: 2 , temperature: 2, humidity: 2, moisture: 2 });
    vals.push({ zone: 3 , light: 3 , temperature: 3, humidity: 3, moisture: 3 });
    vals.push({ zone: 4 , light: 4 , temperature: 4, humidity: 4, moisture: 4 });
    vals.push({ zone: 5 , light: 5 , temperature: 5, humidity: 5, moisture: 5 });
    vals.push({ zone: 6 , light: 6 , temperature: 6, humidity: 6, moisture: 6 });
    vals.push({ zone: 7 , light: 7 , temperature: 7, humidity: 7, moisture: 7 });
    vals.push({ zone: 8 , light: 8 , temperature: 8, humidity: 8, moisture: 8 });*/



    $.ajax({
        type: 'POST',
        url: '/api/sensors',
        data: { data: vals, apikey: 'asddd' },
        success: function() {
            getSensorValues();
        }
    });
});

// Clear sensor form
$(document).on('click', '#sensor-clear', function() {
    $("#set-sensor-form")[0].reset();
});

// Convert 'True' 'False' to 'On' 'Off'
function BoolToOnOff(input) {
    if( input ) {
        return 'On';
    } else {
        return 'Off';
    }
}

function BoolToOpenClosed(input) {
    if( input ) {
        return 'Open';
    } else {
        return 'Closed';
    }
}



// Get greenhouse automation settings
function getAutomationValues() {

    $.ajax({
        type: 'GET',
        url: 'api/automation',
        success: function(response) {
            var data = JSON.parse(response);

            // Set values
            $("#light-automation").text(data.light);
            $("#temperature-automation").text(data.temperature);
            $("#humidity-automation").text(data.humidity);
            $("#moisture-automation").text(data.moisture);

        }
    });
}

// Submit automation values
$(document).on('click', '#automation-submit', function() {
    $.ajax({
        type: 'POST',
        url: '/api/automation',
        data: { apikey: $('#apikey').val(), light: $('#auto-lights').val(), temperature: $('#auto-temperature').val(), humidity: $('#auto-humidity').val(), moisture: $('#auto-moisture').val() },
        success: function() {
            getAutomationValues();
        }
    });
});

// Clear automation form
$(document).on('click', '#automation-clear', function() {
    $("#set-automation-form")[0].reset();
});

// Submit error message
$(document).on('click', '#error-submit', function() {
    $.ajax({
        type: 'POST',
        url: '/api/error',
        data: { apikey: $('#apikey').val(), message: $('#set-error-message').val(), code: $('#set-error-code').val() },
        success: function() {
            getErrorMessage();
        }
    })
});

// Get last error message
function getErrorMessage() {

    $.ajax({
        type: 'GET',
        url: 'api/error',
        success: function(response) {
            var data = JSON.parse(response);

            // Set values
            $("#error-message").text(data.message);
            $("#error-code").text(data.code);
            $("#error-time").text(data.time);
        }
    });
}

// Clear error form
$(document).on('click', '#error-clear', function() {
    $("#set-error-form")[0].reset();
});