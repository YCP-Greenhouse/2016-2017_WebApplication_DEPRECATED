// Get greenhouse state when page loads
$(document).ready( function() {
    getGreenhouseTemperatureData();

    // Set Average Temperature button as selected
    $('#greenhouse-temperature').focus();

    // Check if browser is in full screen
    if (navigator.platform.toLowerCase().indexOf('linux') >= 0) {
        $('#nav-list').addClass('nav-small');
        $('#nav-list').html('<ul><a href="/settings"><li class="nav-li"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-active"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');

    } else {
        $('#nav-list').addClass('nav');
        $('#nav-list').html('<ul><a href="index.html"><li class="nav-li"><img width="40" height="40" src="images/home.png"><span class="nav-item">HOME</span></li></a><a href="/settings"><li class="nav-li"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-active"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');
    }
});

function getZoneTemperatureData() {
    $.ajax({
        type: 'GET',
        url: '/api/sensors?&time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var zone1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 1'
            };

            var zone2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 2'
            };

            var zone3 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 3'
            };

            var zone4 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 4'
            };

            var zone5 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 5'
            };

            var zone6 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 6'
            };

            for( var i=0; i<data.length; i++ ) {
                if( data[i].zone == 1 ) {
                    zone1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone1.y.push( data[i].temperature );
                    zone1.text.push( data[i].temperature + "° F");
                } else if( data[i].zone == 2 ) {
                    zone2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone2.y.push( data[i].temperature );
                    zone2.text.push( data[i].temperature + "° F");
                } else if( data[i].zone == 3 ) {
                    zone3.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone3.y.push( data[i].temperature );
                    zone3.text.push( data[i].temperature + "° F");
                } else if( data[i].zone == 4 ) {
                    zone4.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone4.y.push( data[i].temperature );
                    zone4.text.push( data[i].temperature + "° F");
                } else if( data[i].zone == 5 ) {
                    zone5.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone5.y.push( data[i].temperature );
                    zone5.text.push( data[i].temperature + "° F");
                } else if( data[i].zone == 6 ) {
                    zone6.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone6.y.push( data[i].temperature );
                    zone6.text.push( data[i].temperature + "° F");
                }
            }

            drawPlot( [zone1,zone2,zone3,zone4,zone5,zone6], "ZONE TEMPERATURE", "Temperature", "" );
        }
    });
}

function getZoneHumidityData() {
    $.ajax({
        type: 'GET',
        url: '/api/sensors?&time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var zone1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 1'
            };

            var zone2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 2'
            };

            var zone3 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 3'
            };

            var zone4 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 4'
            };

            var zone5 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 5'
            };

            var zone6 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 6'
            };

            for( var i=0; i<data.length; i++ ) {
                if( data[i].zone == 1 ) {
                    zone1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone1.y.push( data[i].humidity );
                    zone1.text.push( data[i].humidity + "%");
                } else if( data[i].zone == 2 ) {
                    zone2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone2.y.push( data[i].humidity );
                    zone2.text.push( data[i].humidity + "%");
                } else if( data[i].zone == 3 ) {
                    zone3.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone3.y.push( data[i].humidity );
                    zone3.text.push( data[i].humidity + "%");
                } else if( data[i].zone == 4 ) {
                    zone4.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone4.y.push( data[i].humidity );
                    zone4.text.push( data[i].humidity + "%");
                } else if( data[i].zone == 5 ) {
                    zone5.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone5.y.push( data[i].humidity );
                    zone5.text.push( data[i].humidity + "%");
                } else if( data[i].zone == 6 ) {
                    zone6.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone6.y.push( data[i].humidity );
                    zone6.text.push( data[i].humidity + "%");
                }
            }

            drawPlot( [zone1,zone2,zone3,zone4,zone5,zone6], "ZONE HUMIDITY", "Humidity", "" );
        }
    });
}

function getZoneLightData() {
    $.ajax({
        type: 'GET',
        url: '/api/sensors?&time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var zone1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 1'
            };

            var zone2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 2'
            };

            var zone3 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 3'
            };

            var zone4 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 4'
            };

            var zone5 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 5'
            };

            var zone6 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 6'
            };

            for( var i=0; i<data.length; i++ ) {
                if( data[i].zone == 1 ) {
                    zone1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone1.y.push( data[i].light );
                    zone1.text.push( data[i].light + " lux");
                } else if( data[i].zone == 2 ) {
                    zone2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone2.y.push( data[i].light );
                    zone2.text.push( data[i].light + " lux");
                } else if( data[i].zone == 3 ) {
                    zone3.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone3.y.push( data[i].light );
                    zone3.text.push( data[i].light + " lux");
                } else if( data[i].zone == 4 ) {
                    zone4.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone4.y.push( data[i].light );
                    zone4.text.push( data[i].light + " lux");
                } else if( data[i].zone == 5 ) {
                    zone5.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone5.y.push( data[i].light );
                    zone5.text.push( data[i].light + " lux");
                } else if( data[i].zone == 6 ) {
                    zone6.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone6.y.push( data[i].light );
                    zone6.text.push( data[i].light + " lux");
                }
            }

            drawPlot( [zone1,zone2,zone3,zone4,zone5,zone6], "ZONE LIGHT", "Lux", "" );
        }
    });
}

function getZoneSoilMoistureData() {
    $.ajax({
        type: 'GET',
        url: '/api/sensors?&time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var zone1probe1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 1 Probe 1'
            };

            var zone1probe2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 1 Probe 2'
            }

            var zone2probe1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 2 Probe 1'
            };

            var zone2probe2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 2 Probe 2'
            }

            var zone3probe1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 3 Probe 1'
            };

            var zone3probe2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 3 Probe 2'
            };

            var zone4probe1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 4 Probe 1'
            };

            var zone4probe2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 4 Probe 2'
            };

            var zone5probe1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 5 Probe 1'
            };

            var zone5probe2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 5 Probe 2'
            };

            var zone6probe1 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 6 Probe 1'
            };

            var zone6probe2 = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                hoverinfo: 'text+x',
                name: 'Zone 6 Probe 2'
            };


            for( var i=0; i<data.length; i++ ) {
                if( data[i].zone == 1 ) {
                    // Probe 1
                    zone1probe1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone1probe1.y.push( data[i].probe1 );
                    zone1probe1.text.push( data[i].probe1 + "%");

                    // Probe 2
                    zone1probe2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone1probe2.y.push( data[i].probe2 );
                    zone1probe2.text.push( data[i].probe2 + "%");

                } else if( data[i].zone == 2 ) {
                    // Probe 1
                    zone2probe1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone2probe1.y.push( data[i].probe1 );
                    zone2probe1.text.push( data[i].probe1 + "%");

                    // Probe 2
                    zone2probe2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone2probe2.y.push( data[i].probe2 );
                    zone2probe2.text.push( data[i].probe2 + "%");
                } else if( data[i].zone == 3 ) {
                    // Probe 1
                    zone3probe1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone3probe1.y.push( data[i].probe1 );
                    zone3probe1.text.push( data[i].probe1 + "%");

                    // Probe 2
                    zone3probe2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone3probe2.y.push( data[i].probe2 );
                    zone3probe2.text.push( data[i].probe2 + "%");
                } else if( data[i].zone == 4 ) {
                    // Probe 1
                    zone4probe1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone4probe1.y.push( data[i].probe1 );
                    zone4probe1.text.push( data[i].probe1 + "%");

                    // Probe 2
                    zone4probe2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone4probe2.y.push( data[i].probe2 );
                    zone4probe2.text.push( data[i].probe2 + "%");
                } else if( data[i].zone == 5 ) {
                    // Probe 1
                    zone5probe1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone5probe1.y.push( data[i].probe1 );
                    zone5probe1.text.push( data[i].probe1 + "%");

                    // Probe 2
                    zone5probe2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone5probe2.y.push( data[i].probe2 );
                    zone5probe2.text.push( data[i].probe2 + "%");
                } else if( data[i].zone == 6 ) {
                    // Probe 1
                    zone6probe1.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone6probe1.y.push( data[i].probe1 );
                    zone6probe1.text.push( data[i].probe1 + "%");

                    // Probe 2
                    zone6probe2.x.push( data[i].sampletime.replace( /\//g, "-") );
                    zone6probe2.y.push( data[i].probe2 );
                    zone6probe2.text.push( data[i].probe2 + "%");
                }
            }

            drawPlot( [zone1probe1,zone1probe2, zone2probe1, zone2probe2, zone3probe1, zone3probe2, zone4probe1, zone4probe2, zone5probe1, zone5probe2, zone6probe1, zone6probe2 ], "ZONE SOIL MOISTURE", "", "" );
        }
    });
}

function getGreenhouseTemperatureData() {
    $.ajax({
        type: 'GET',
        url: '/api/averages?time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var tableData = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                fill: 'tozeroy',
                fillcolor: 'rgba(255,255,255,0.1)',
                marker: {
                    size: 8,
                    color: 'white'
                },
                hoverinfo: 'text+x'
            };

            for( var i=0; i<data.length; i++ ) {
                tableData.x.push( data[i].sampletime.replace( /\//g, "-") );
                tableData.y.push( data[i].temperature );
                tableData.text.push( data[i].temperature + "° F");
            }

            drawPlot( [tableData], "GREENHOUSE TEMPERATURE", "Temperature", "" );
        }
    });
}

function getGreenhouseHumidityData() {
    $.ajax({
        type: 'GET',
        url: '/api/averages?time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var tableData = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                fill: 'tozeroy',
                fillcolor: 'rgba(255,255,255,0.1)',
                marker: {
                    size: 8,
                    color: 'white'
                },
                hoverinfo: 'text+x'
            };

            for( var i=0; i<data.length; i++ ) {
                tableData.x.push( data[i].sampletime.replace( /\//g, "-") );
                tableData.y.push( data[i].humidity );
                tableData.text.push( data[i].humidity + "%");
            }

            drawPlot( [tableData], "GREENHOUSE HUMIDITY", "Humidity", "" );
        }
    });
}

function getGreenhouseLightData() {
    $.ajax({
        type: 'GET',
        url: '/api/averages?time=a',
        success: function(response) {
            var data = JSON.parse(response);

            var tableData = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                fill: 'tozeroy',
                fillcolor: 'rgba(255,255,255,0.1)',
                marker: {
                    size: 8,
                    color: 'white'
                },
                hoverinfo: 'text+x'
            };

            for( var i=0; i<data.length; i++ ) {
                tableData.x.push( data[i].sampletime.replace( /\//g, "-") );
                tableData.y.push( data[i].light );
                tableData.text.push( data[i].light + " lux");
            }

            drawPlot( [tableData], "GREENHOUSE LIGHT", "", "" );
        }
    });
}

function getOutdoorTemperature() {
    $.ajax({
        type: 'GET',
        url: '/api/weather',
        success: function(response) {
            var data = JSON.parse(response);

            var tableData = {
                x: [],
                y: [],
                text: [],
                mode: 'lines',
                fill: 'tozeroy',
                fillcolor: 'rgba(255,255,255,0.1)',
                marker: {
                    size: 8,
                    color: 'white'
                },
                hoverinfo: 'text+x'
            };

            for( var i=0; i<data.length; i++ ) {
                tableData.x.push( data[i].sampletime.replace( /\//g, "-") );
                tableData.y.push( data[i].temp );
                tableData.text.push( data[i].temp + "° F");
            }

            drawPlot( [tableData], "OUTDOOR TEMPERATURE", "Temperature", "" );
        }
    });
}

$(document).on('click', '#outdoor-temperature', function() {
    getOutdoorTemperature();
});

$(document).on('click', '#greenhouse-temperature', function() {
    getGreenhouseTemperatureData( 1, "a" );
});

$(document).on('click', '#greenhouse-humidity', function() {
    getGreenhouseHumidityData();
});

$(document).on('click', '#greenhouse-light', function() {
    getGreenhouseLightData();
});

$(document).on('click', '#zone-temperature', function() {
    getZoneTemperatureData();
});

$(document).on('click', '#zone-humidity', function() {
    getZoneHumidityData();
});

$(document).on('click', '#zone-light', function() {
    getZoneLightData();
});

$(document).on('click', '#zone-moisture', function() {
    getZoneSoilMoistureData();
});

function drawPlot( tableData, chart_title, yaxis_title, xaxis_title ) {

    console.table(tableData);

    var layout = {
        title: chart_title,
        paper_bgcolor: 'rgba(255,255,255,0)',
        plot_bgcolor: 'rgba(0,0,0,0)',
        font: {
           color: 'rgba(255,255,255,0.8)'
        },
        yaxis: {
            title: yaxis_title,
            showgrid: false,
            showline: true,
            linecolor: 'rgba(255,255,255,0.4)'
        },
        xaxis: {
            title: xaxis_title,
            showgrid: false,
            showline: true,
            linecolor: 'rgba(255,255,255,0.4)'
        },

    };

    Plotly.newPlot('chart', tableData, layout);
}

// Converts military time to standard time
function convertTime( time ) {
    time = time.split(':');

    // Get hours and minutes
    var hours = Number(time[0]);
    var minutes = Number(time[1]);

    // calculate
    if( hours > 0) {
        var timeValue = "" + ((hours > 12) ? hours - 12 : hours);  // get hours
    } else {
        var timeValue = "12";
    }
    timeValue += (minutes < 10) ? ":0" + minutes : ":" + minutes;  // get minutes
    //timeValue += (seconds < 10) ? ":0" + seconds : ":" + seconds;  // get seconds
    timeValue += (hours >= 12) ? " pm" : " am";  // get AM/PM

    return timeValue;
}