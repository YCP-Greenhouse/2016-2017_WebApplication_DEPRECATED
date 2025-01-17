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

// Schedule variables
var lightSchedule = {};
var waterSchedule = {};
var lightOverride = false;
var waterOverride = false;

// Query API every 3 seconds
var isPaused = false;

// Automation Values
var lightVal = 0;

window.setInterval( function() {

    getAutomationValues();

    $.ajax({
        type: 'GET',
        url: '/api/manualcontrols',
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

            if( data.water != waterSwitch ) {
                $('#water-switch').trigger('click');
                waterSwitch = data.water;
            }
        }
    });

    if( !isPaused ) {
        updateAutomationValues();
    }

    // Handle manual control button clicks. Placed inside interval so it wouldn't fire on page load
    $(document).on('click', '.switch-div', function() {

        // If switch is disabled
        if( $(this).find('.disabled').length > 0 ) {
            $('#manual-control-alert').css('top', $(this).position().top-120 );
            $('#manual-control-alert').css('left', $(this).position().left-33 );
            $('#manual-control-alert').css('display','block');
            $('#manual-control-alert').attr('type', $(this)[0].id.split('-')[0]);
        } else {
            $('#manual-control-alert').css('display','none');
        }

    });

}, 3000 );


$(document).ready( function() {

    // Check if browser is Raspberry Pi
    if ( navigator.platform.toLowerCase().indexOf('linux') >= 0 ) {
        $('#nav-list').addClass('nav-small');
        $('#nav-list').html('<ul><a href="/settings"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');

    } else {
        $('#nav-list').addClass('nav');
        $('#nav-list').html('<ul><a href="index.html"><li class="nav-li"><img width="40" height="40" src="images/home.png"><span class="nav-item">HOME</span></li></a><a href="/settings"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');
    }

    // Get automation values
    getAutomationValues();

    // Get state values
    $.ajax({
        type: 'GET',
        url: '/api/manualcontrols',
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

            if( data.water ) {
                $('#water-switch').trigger('click');
            }

            // Set switch statuses
            lightSwitch = data.lights;
            shadeSwitch = data.shades;
            fanSwitch = data.fans;
            waterSwitch = data.water;
            lightOverride = data.lightoverride;
            waterOverride = data.wateroverride;
        }
    });



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
        // Pause update timer
        isPaused = true;

        var el = $('#temperature-low');
        var val = 1;
        timeout = window.setInterval( function() { tempChange(el,val,0) }, 150);

    }).on('mouseup', function() {
        isPaused = false;
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

        // Pause update timer
        isPaused = true;

        var el = $('#temperature-low');
        var val = -1;
        timeout = window.setInterval( function() { tempChange(el,val,0) }, 150);

    }).on('mouseup', function() {
        isPaused = false;
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
        // Pause update timer
        isPaused = true;

        var el = $('#temperature-high');
        var val = 1;
        timeout = window.setInterval( function() { tempChange(el,val,1) }, 150);

    }).on('mouseup', function() {
        isPaused = false;
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
        // Pause update timer
        isPaused = true;

        var el = $('#temperature-high');
        var val = -1;
        timeout = window.setInterval( function() { tempChange(el,val,1) }, 150);

    }).on('mouseup', function() {
        isPauded = false;
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
                url: '/api/manualcontrols',
                data: {
                    lights: $('#light-check').is(":checked"),
                    shades: $('#shade-check').is(":checked"),
                    fans: $('#fan-check').is(":checked"),
                    water: $('#water-check').is(":checked")
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

    $('#zone-1').click(function() {
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
        $('#zone-title').text("ZONE 1 SCHEDULE");

        // Set water as default radio option
        $('#schedule-type').val('water');
        $('#water-radio').attr('checked','checked');

        // Set zone ID
        $('#zoneid').val(1);
        getSchedules($('#zoneid').val());
    });

    $('#zone-2').click(function() {
        //$('#container').css("transform", "scale(1.02)");
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
        $('#zone-title').text("ZONE 2 SCHEDULE");

        // Set water as default radio option
        $('#schedule-type').val('water');
        $('#water-radio').attr('checked','checked');

        // Set zone ID
        $('#zoneid').val(2);
        getSchedules($('#zoneid').val());
    });

    $('#zone-3').click(function() {
        //$('#container').css("transform", "scale(1.02)");
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
        $('#zone-title').text("ZONE 3 SCHEDULE");

        // Set water as default radio option
        $('#schedule-type').val('water');
        $('#water-radio').attr('checked','checked');

        // Set zone ID
        $('#zoneid').val(3);
        getSchedules($('#zoneid').val());
    });

    $('#zone-4').click(function() {
        //$('#container').css("transform", "scale(1.02)");
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
        $('#zone-title').text("ZONE 4 SCHEDULE");

        // Set water as default radio option
        $('#schedule-type').val('water');
        $('#water-radio').attr('checked','checked');

        // Set zone ID
        $('#zoneid').val(4);
        getSchedules($('#zoneid').val());
    });

    $('#zone-5').click(function() {
        //$('#container').css("transform", "scale(1.02)");
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
        $('#zone-title').text("ZONE 5 SCHEDULE");

        // Set water as default radio option
        $('#schedule-type').val('water');
        $('#water-radio').attr('checked','checked');

        // Set zone ID
        $('#zoneid').val(5);
        getSchedules($('#zoneid').val());
    });

    $('#zone-6').click(function() {
        //$('#container').css("transform", "scale(1.02)");
        $('#container').css("filter","blur(4px)");

        $('#zone-modal').css("display", "block");
        $('#zone-title').text("ZONE 6 SCHEDULE");

        // Set water as default radio option
        $('#schedule-type').val('water');
        $('#water-radio').attr('checked','checked');

        // Set zone ID
        $('#zoneid').val(6);
        getSchedules($('#zoneid').val());
    });
});

// Update light slider bar
function updateOutput(el, val) {
    lightVal = val;
    el.textContent = val;
}

// Changes the high and low temperature values
// Input: element, value to increment/decrement, high or low
function tempChange(el, val, type) {

        // If type == 1, update tempHigh
        if (parseInt(type) == 1) {
            if( parseInt(val) == 1 ) {
                if( temphigh < 120 )
                    temphigh++;

                el.html('');
                el.html(temphigh + "&deg;");
            } else {
                if( temphigh > 50 && ( temphigh-templow > 5 ))
                    temphigh--;

                el.html('');
                el.html(temphigh + "&deg;");
            }

        // If type == 0, update tempLow
        } else {
            if( parseInt(val) == 1 ) {
                if( parseInt(templow) < parseInt(temphigh)-5 ) {
                    templow++;
                }

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

            // Set values to be displayed
            light = data.ShadeLim;
            templow = data.TempLow;
            temphigh = data.TempHigh;
            humidity = data.humidity;
            moisture = data.moisture;

            $('#temperature-low').html(templow + "&deg;");
            $('#temperature-high').html(temphigh + "&deg;");
            $('#light-val').html(light);

            $('input[type="range"]').val(light).change();
        }
    });
}

/*
function () {
    $.ajax({
        type: 'POST',
        url: '/api/automation',
        data: {
            temphigh: $('#temperature-high').text().replace('°',''),
            templow: $('#temperature-low').text().replace('°',''),
            light: $('#light-val').val()
        }
    });
} */


var automationData;
function getAutomationValues() {

    // Get automation values
    $.ajax({
        type: 'GET',
        url: '/api/automation',
        success: function(response) {
            var data = JSON.parse(response);
            var now = new Date();

            light = data.ShadeLim;

            // Check if there is an active light schedule
            if (data.LightSchedules.length > 0 && !lightOverride) {
                var start = data.LightSchedules[0].start.split(":")[0];
                var end = data.LightSchedules[0].end.split(":")[0];

                // Disabled manual control if there is an active schedule
                if (now.getHours() >= start && now.getHours() < end) {
                    lightSchedule.active = true;
                    lightSchedule.type = data.LightSchedules[0].type;

                    // If a schedule is in effect, turn off button
                    if (lightSwitch) {
                        $('#light-switch').trigger('click');
                        lightSwitch = !lightSwitch;
                    }

                    $('#light-switch-label').addClass("disabled");
                    $('#light-switch-overlay').height(100);
                } else {
                    lightSchedule.active = false;
                    $('#light-switch-label').removeClass("disabled");
                    $('#light-switch-overlay').height(0);
                }
            } else {
                lightSchedule.active = false;
                $('#light-switch-label').removeClass("disabled");
                $('#light-switch-overlay').height(0);
            }

            // Check if there is an active water schedule
            if (data.WaterSchedules.length > 0 && !waterOverride) {
                var start = data.WaterSchedules[0].start.split(":")[0];
                var end = data.WaterSchedules[0].end.split(":")[0]

                // Disabled manual control if there is an active schedule
                if (now.getHours() >= start && now.getHours() < end) {
                    waterSchedule.active = true;
                    waterSchedule.type = data.WaterSchedules[0].type;

                    // If water schedule is in effect, turn off switch
                    if (waterSwitch) {
                        $('#water-switch').trigger('click');
                        waterSwitch = !waterSwitch;
                    }

                    $('#water-switch-label').addClass("disabled");
                    $('#water-switch-overlay').height(100);
                } else {
                    waterSchedule.active = false;
                    $('#water-switch-label').removeClass("disabled");
                    $('#water-switch-overlay').height(0);
                }
            } else {
                waterSchedule.active = false;
                $('#water-switch-label').removeClass("disabled");
                $('#water-switch-overlay').height(0);
            }

            // Set values
            light = data.ShadeLim;
            templow = data.TempLow;
            temphigh = data.TempHigh;
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

function postManualControls() {
    $.ajax({
        type: 'POST',
        url: '/api/manualcontrols',
        data: {
            lights: $('#light-check').is(":checked"),
            shades: $('#shade-check').is(":checked"),
            fans: $('#fan-check').is(":checked"),
            water: $('#water-check').is(":checked"),
            lightoverride: lightOverride,
            wateroverride: waterOverride
        }
    });
}

// Handle schedule override click
$(document).on('click', "#yes", function() {

    console.log( $('#manual-control-alert').attr('type') );

    if( $('#manual-control-alert').attr('type') == 'light' ) {
        $('#light-switch-label').removeClass("disabled");
        $('#light-switch-overlay').height(0);

        $('#light-switch').trigger('click');
        lightSwitch = !lightSwitch;
        lightOverride = true;


    } else if( $('#manual-control-alert').attr('type') == 'water' ) {
        lightSchedule.active = false;
        $('#water-switch-label').removeClass("disabled");
        $('#water-switch-overlay').height(0);

        $('#water-switch').trigger('click');
        waterSwitch = !waterSwitch;
        waterOverride = true;
    }

    postManualControls();
});

$(document).on('click', "#no", function() {
    $('#manual-control-alert').css('display','none');
});





