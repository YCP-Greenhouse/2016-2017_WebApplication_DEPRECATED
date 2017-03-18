// Get greenhouse state when page loads
$(document).ready( function() {
    getSensorValues();

});

var lastSensorData = [];

// Get sensor values
function getSensorValues() {
    $.ajax({
        type: 'GET',
        url: 'api/sensors',
        success: function(response) {
            lastSensorData = JSON.parse(response);
            createNewData();
        }
    });
}

window.setInterval( function() {

    createNewData();

}, 1000 * 60 * 5 );



function createNewData() {
    var currentSensorData = [];
    for (var i = 0; i < lastSensorData.length; i++) {
        var obj = {};
        obj.zone = lastSensorData[i].zone;
        var light = parseInt(lastSensorData[i].light);
        var moisture = parseInt(lastSensorData[i].moisture);
        var temperature = parseInt(lastSensorData[i].temperature);
        var humidity = parseInt(lastSensorData[i].humidity);

        var num = Math.floor((Math.random() * 5 + 1));

        if( num == 1 ) {
            light += 500;
            moisture += 5;
            temperature += 3;
            humidity += 3;
        } else if( num == 2 ) {
            light += 100;
            moisture += 1;
            temperature += 1;
            humidity += 1;
        } else if( num == 3 ) {
            light -= 100;
            moisture -= 1;
            temperature -= 1;
            humidity -= 1;
        } else if( num == 4 ) {
            light -= 500;
            moisture -= 5;
            temperature -= 3;
            humidity -= 3;
        } else if( num == 5 ) {
            // do nothing
        }

        obj.light = light;
        obj.moisture = moisture;
        obj.temperature = temperature;
        obj.humidity = humidity;

        currentSensorData.push(obj);
    }

    lastSensorData = currentSensorData;
    postSensorData(currentSensorData);
}

function postSensorData( data ) {
    $.ajax({
        type: 'POST',
        url: '/api/sensors',
        data: { data: data, apikey: '44ffe28b-f470-4bc0-8ee9-38fce01438ce' },
        success: function() {

        }
    });
}