$(document).ready( function() {
    setDate();
    getWeather();
});

// Set date in UI
function setDate() {

    var date = new Date();

    $('#day').text( getDayOfWeek(date.getDay()).toUpperCase() );
    $('#date').text( getMonth(date.getMonth()).toUpperCase() + " " + date.getDate() + ", " + date.getFullYear() );
}

function getDayOfWeek( day ) {
    var days = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
    return days[day];
}

function getMonth( month ) {
    var months = ["January","February","March","April","May","June","July","August","September","October","November","December"];
    return months[month];
}


// Query OpenWeather API for current weather
function getWeather() {
    $.ajax({
        type: 'GET',
        url: 'http://api.openweathermap.org/data/2.5/weather?q=York,us&appid=f581e69b118ec3559415b4939e550e33',
        success: function(data) {
            console.log( data );
            $('#current-temperature').html( KelvinToFahrenheit(data.main.temp) + '&deg;<span class="fahrenheit">F</span>' );
            $('#temp-high').html( KelvinToFahrenheit(data.main.temp_max) + '&deg;<span class="fahrenheit-small">H</span>' );
            $('#temp-low').html( KelvinToFahrenheit(data.main.temp_min) + '&deg;<span class="fahrenheit-small">L</span>' );

            // Set weather icon
            var id = data.weather[0].id;

            // Thunderstorms
            if( id >= 200 && id < 300 ) {
                $('#weather-icon').addClass('thunderstorms');

            // Rain
            } else if( id >= 300 && id < 600  ) {
                $('#weather-icon').addClass('rain');

            // Snow
            } else if(id >= 600 && id < 700 ) {
                $('#weather-icon').addClass('snow');

            // Clear
            } else {
                $('#weather-icon').addClass('clear');
            }
        }
    });
}

function KelvinToFahrenheit( temp ) {
    return Math.round(temp * (9/5) - 459.67);
}
