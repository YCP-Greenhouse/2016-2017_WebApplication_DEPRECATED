// Threshold Variables
var light;
var templow;
var temphigh;
var humidity;
var moisture;

// State Variables
var lightSwitch;
var shadeSwitch;
var fanSwitch;
var waterSwitch;
var vents;
var heater;

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

    // Login button click handlers
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

    // Temperature button handlers
    var timeout = 0;
    $('#temp-low-increase').mousedown(function() {
        var el = $('#temperature-low');
        var val = 1;
        timeout = window.setInterval( function() { tempChange(el,val,0) }, 150);

    }).on('mouseup', function() {
        clearTimeout(timeout);
        submitAutomationValues();
    });

    $('#temp-low-increase').click(function() {
        var el = $('#temperature-low');
        var val = 1;
        tempChange(el,val,0);
        submitAutomationValues();
    });

    $('#temp-low-decrease').mousedown(function() {
        var el = $('#temperature-low');
        var val = -1;
        timeout = window.setInterval( function() { tempChange(el,val,0) }, 150);

    }).on('mouseup', function() {
        clearTimeout(timeout);
        submitAutomationValues();
    });

    $('#temp-low-decrease').click(function() {
        var el = $('#temperature-low');
        var val = -1;
        tempChange(el,val,0);
        submitAutomationValues();
    });

    $('#temp-high-increase').mousedown(function() {
        var el = $('#temperature-high');
        var val = 1;
        timeout = window.setInterval( function() { tempChange(el,val,1) }, 150);

    }).on('mouseup', function() {
        clearTimeout(timeout);
        submitAutomationValues();
    });

    $('#temp-high-increase').click(function() {
        var el = $('#temperature-high');
        var val = 1;
        tempChange(el,val,1);
        submitAutomationValues();
    });

    $('#temp-high-decrease').mousedown(function() {
        var el = $('#temperature-high');
        var val = -1;
        timeout = window.setInterval( function() { tempChange(el,val,1) }, 150);

    }).on('mouseup', function() {
        clearTimeout(timeout);
        submitAutomationValues();
    });

    $('#temp-high-decrease').click(function() {
        var el = $('#temperature-high');
        var val = -1;
        tempChange(el,val,1);
        submitAutomationValues();
    });



    // State button click handler
    // jQuery fires this multiple times on 'click' event.
    // .off changes number of firings from 4 to 2
    // First fire is of values before click
    // Second fire is new values
    // Do all this to submit only the updated values one time.
    $('.bottom-controls-panel').off('click').on('click', function () {
        // If values haven't changed
        if ($('#light-check').is(":checked") == lightSwitch && $('#shade-check').is(":checked") == shadeSwitch && $('#fan-check').is(":checked") == fanSwitch && $('#water-check').is(":checked") == waterSwitch) {
            // Do nothing
        } else if (lightSwitch != undefined && shadeSwitch != undefined && fanSwitch != undefined && waterSwitch != undefined) {
            $.ajax({
                type: 'POST',
                url: '/api/state',
                data: {
                    lights: $('#light-check').is(":checked"),
                    shades: $('#shade-check').is(":checked"),
                    fans: $('#fan-check').is(":checked"),
                    pump: $('#water-check').is(":checked"),
                    vents: vents,
                    heater: heater
                }
            });

            // Set current values
            lightSwitch = $('#light-check').is(":checked");
            shadeSwitch = $('#shade-check').is(":checked");
            fanSwitch = $('#fan-check').is(":checked");
            waterSwitch = $('#water-check').is(":checked");
        }
    });

    // Zone modal click events
    /*
    $('#zone-1').click(function() {
        $('#container').css("transform", "scale(1.02)");
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
    });

    $('#zone-2').click(function() {
        //$('#zone-modal').css("display", "block");
        $('#container').css("transform", "scale(1)");
        $('#container').css("filter","blur(0px)");

        $('#zone-modal').css("display", "none");

    });

    $('.big-table, .small-table, .zone-modal-close').click(function() {
        $('.container').toggleClass('blur');
        $('.zone-modal').toggleClass('close');
        $('.zone-modal-close').toggleClass('close');

        $('#zone-title').text($(this).text());
        drawScheduleTable($('#water-schedule-table'));
    });*/
});

function drawScheduleTable(el) {
    var data = el.html();
    el.append

}

// Update light slider bar
function updateOutput(el, val) {
    el.textContent = val;
}

// Changes the high and low temperature values
// Input: element, value to increment/decrement, high or low
function tempChange(el, val, type) {

        if (parseInt(type) == 1) {
            if( parseInt(val) == 1 ) {
                if( temphigh < 120 )
                    temphigh++;

                el.html('');
                el.html(temphigh + "&deg;");
            } else {
                if( temphigh > 50)
                    temphigh--;

                el.html('');
                el.html(temphigh + "&deg;");
            }
        } else {
            if( parseInt(val) == 1 ) {
                if( templow < temphigh )
                    templow++;

                el.html('');
                el.html(templow + "&deg;");
            } else {
                if( templow > 50 )
                    templow--;

                el.html('');
                el.html(templow + "&deg;");
            }
        }
}

function updateAutomationValues() {
    $.ajax({
        type: 'GET',
        url: '/api/automation',
        success: function (response) {
            var data = JSON.parse(response);

            // Set values
            light = data.light;
            templow = data.templow;
            temphigh = data.temphigh;
            humidity = data.humidity;
            moisture = data.moisture;

            $('#temperature-low').html(templow + "&deg;");
            $('#temperature-high').html(temphigh + "&deg;");
        }
    });
}

function submitAutomationValues() {
    $.ajax({
        type: 'POST',
        url: '/api/automation',
        data: {
            temphigh: $('#temperature-high').text().replace('°',''),
            templow: $('#temperature-low').text().replace('°',''),
            light: $('#light-val').val()
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
            templow = data.templow;
            temphigh = data.temphigh;
            humidity = data.humidity;
            moisture = data.moisture;

            $('#temperature-low').html(templow + "&deg;");
            $('#temperature-high').html(temphigh + "&deg;");
            $('#light-val').html(light);
            $('#light-slider').val(light);


            // Light slider
            var $element = $('input[type="range"]');
            var $output = $('output');

            $element
                .rangeslider({
                    polyfill: false,
                    onInit: function() {
                        updateOutput($output[0], this.value);
                    }
                })
                .on('input', function() {
                    updateOutput($output[0], this.value);
                    submitAutomationValues();
                });
        }
    });
}




