// Set when user clicks zone 1-6
var zone;
var saveZone = $('#zoneid').val();

function getSchedules(zone) {
    $.ajax({
        type: 'GET',
        url: 'api/schedule',
        success: function(response) {
            var data = JSON.parse(response);

            clearCalendar();
            populateHeading();
            drawCalendar();

            // Light zones are  1 = (1,2), 2 = (3,4), 3 = (5,6)
            var lightZone = 0;
            if( parseInt(zone) % 2 == 0 ) {
                lightZone = parseInt(zone)/2;
            } else {
                lightZone = (parseInt(zone)+1)/2;
            }

            // Populate light schedule
            for (var i = 0; i < data.lightschedule.length; i++) {

                if (data.lightschedule[i].zone == lightZone ) {
                    populateCalendar('light', data.lightschedule[i]);
                }
            }

            // Populate water schedule
            for (var i = 0; i < data.waterschedule.length; i++) {
                if (data.waterschedule[i].zone == zone ) {
                    populateCalendar('water', data.waterschedule[i]);
                }
            }
        }
    });
}

// Draws empty calendar
function drawCalendar() {

    var times = ['12am','1am','2am','3am','4am','5am','6am','7am','8am','9am','10am','11am','12pm','1pm','2pm','3pm','4pm','5pm','6pm','7pm','8pm','9pm','10pm','11pm'];

    var table = document.getElementById('schedule-body');

    for( var i=0; i<24; i++ ) {
        var row = table.insertRow(i);

        // Set time column
        var timeCol = row.insertCell(0);
        timeCol.innerHTML = "<span style='float:right; padding-right: 15px;'>" + times[i] + "</span>";

        // Add days of the week
        for( var j=1; j<=7; j++ ) {
            var dayCol = row.insertCell(j);

            // Set td id ( time - day )
            var id = i + "-" + (j-1);
            dayCol.innerHTML = "<div scheduletype='empty' id=" + id + " class='schedule-body'><div id=" + id + -1 +" class='blank left'></div><div id=" + id + -2 + " class='blank right'></div></div>";
        }
    }
}

// Input: schedule object
function populateCalendar(title, schedule) {

    // Convert start time to ( time - day ) format
    var time = schedule.start.split(":");

    // Set day to schedule day
    var day = new Date();
    day.setDate( day.getDate() + ( schedule.day-day.getDay()) );

    // Start at start time and color each row
    for( var i=0; i<schedule.hours; i++ ) {
        var hour = parseInt(time[0]) + i;

        // Returns div inside td not td!!!
        var td = document.getElementById( hour + "-" + day.getDay() );
        tdId = td.getAttribute('id');

        td.setAttribute('scheduleid', schedule.id);

        if( title === 'light' ) {

            var blankDiv = document.getElementById( tdId + -1 );
            blankDiv.setAttribute('scheduleid', schedule.id);
            blankDiv.setAttribute('schedule', title);
            blankDiv.setAttribute('scheduletype', schedule.type);
            //blankDiv.setAttribute('inverse', schedule.inverse );
            blankDiv.setAttribute('threshold', schedule.threshold);

            if( schedule.type == 'blocked' ) {
                blankDiv.classList.add('light-blocked');
                if( i==0 ) {
                    blankDiv.innerText = "NO LIGHTS";
                }
            } else {
                blankDiv.classList.add('light-event');
                if( i==0 ) {
                    blankDiv.innerText = "LIGHTS ON";
                }
            }

            // Remove 'clear' classes
            blankDiv.classList.remove("blank");
            blankDiv.classList.remove("left");

        } else if( title === 'water' ) {

            var blankDiv = document.getElementById( tdId + -2 );
            blankDiv.setAttribute('scheduleid', schedule.id);
            blankDiv.setAttribute('schedule', title);
            blankDiv.setAttribute('scheduletype', schedule.type);
            //blankDiv.setAttribute('inverse', schedule.inverse );
            blankDiv.setAttribute('threshold', schedule.threshold);

            if( schedule.type == 'blocked' ) {
                blankDiv.classList.add('water-blocked');
                if( i==0 ) {
                    blankDiv.innerText = "NO WATER";
                }
            } else {
                blankDiv.classList.add('water-event');
                if( i==0 ) {
                    blankDiv.innerText = "WATER ON";
                }
            }

            // Remove 'clear' classes
            blankDiv.classList.remove("blank");
            blankDiv.classList.remove("left");
        }
    }
}

function clearCalendar() {
    $('#schedule-body').html("");
    $('#schedule-heading').html("");
}

function populateHeading() {
    var days = ['SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY'];

    var today = new Date();
    var startDay = new Date();

    startDay.setDate( today.getDate() - today.getDay() );

    // Append dates to days for table headers
    var header = document.getElementById('schedule-heading');
    var row = header.insertRow(0);

    var cell = row.insertCell(-1);
    cell.innerHTML = " ";

    for( var i=0; i<7; i++ ) {
        var cell = row.insertCell(-1);

        cell.classList.add('schedule-heading');
        cell.innerHTML = "<th>" + days[i] + " " + (startDay.getMonth()+1) + "/" + startDay.getDate() + "</th>";

        startDay.setDate( startDay.getDate() + 1 );
    }
}


// Handle click events
$(document).on('click', '.blank', function() {

    if( $(this)[0].children.length > 1 ) {
        return;
    }

    // Set saveZone variable ( For light, need to convert from 1-6 to 1-3
    var zoneId = parseInt($('#zoneid').val());
    if( zoneId % 2 == 0 ) {
        saveZone = zoneId / 2;
    } else {
        saveZone = (zoneId+1)/2;
    }

    var lightZone = 1;

    // Set light zone text
    if( zoneId > 2 && zoneId <= 4) {
        lightZone = 3;
    } else if( zoneId > 4 ) {
        lightZone = 5;
    }

    // Set schedule type
    $('#schedule-type').val('light');
    document.querySelector("input[value='light']").checked = true;
    $('#schedule-description').html('Sets light schedule for Zones ' + lightZone + ' and ' + (lightZone+1) );
    $('#schedule-description').show();

    // Display modal
    $('#schedule-modal').css("display", "block");

    // Hide schedule delete button
    $('#delete-button').css("display", "none");


    $('#modal-title').text('New Schedule');

    // Get start time
    var tdId = $(this)[0].id;       // Gets id as (h-d)
    var splitId = tdId.split('-');  // Splits id into h and d

    $('#schedule-id').val( $(this).attr("scheduleid") );

    // Find day of column clicked
    var today = new Date();
    var dayDifference = splitId[1] - today.getDay();

    if( dayDifference != 0 ) {
        today.setDate(today.getDate() + dayDifference);
    }

    // Hidden value to set day
    $('#schedule-date').val(splitId[1]);

    var checkId = splitId[0];

    // Iterate down through rows to find start of schedule event
    var startHour = checkId;

    // Get end time
    var checkId = splitId[0];
    checkId++;

    var endHour = checkId;

    if( startHour < 10 ) {
        startHour = "0" + startHour;
    }

    $('#start-time').val(startHour+":00:00");


    if( endHour < 10 ) {
        endHour = "0" + endHour;
    }

    $('#end-time').val(endHour+":00:00");

    // Calculate hours
    var hours = endHour - startHour;
    $('#hours').val(hours);
});

$(document).on('click', '.light-event', function() {
    drawLightScheduleModal($(this));
});

$(document).on('click', '.light-blocked', function() {
    drawLightScheduleModal($(this));
});

// Input: div object
function drawLightScheduleModal(el) {
    var id = el[0].id;
    var scheduleId = el.attr("scheduleid");

    // Set saveZone variable ( For light, need to convert from 1-6 to 1-3
    var sZ = parseInt($('#zoneid').val());
    if( sZ % 2 == 0 ) {
        saveZone = sZ / 2;
    } else {
        saveZone = (sZ+1)/2;
    }

    // Display modal
    $('#schedule-modal').css("display", "block");

    $('#modal-title').text('');
    $('#modal-title').text('Edit Light Schedule');

    // Show schedule delete button
    $('#delete-button').css("display", "block");

    // Get start time
    var tdId = id;       // Gets id as (h-d)
    var splitId = tdId.split('-');  // Splits id into h and d
    var startFound = false;

    // Set scheduleId
    $('#schedule-id').val( scheduleId );

    // Set schedule
    $('#schedule-type').val('light');
    document.querySelector("input[value='light']").checked = true;
    $('#schedule-description').html('Sets light schedule for Zones ' + saveZone + ' and ' + (saveZone+1) );
    $('#schedule-description').show();

    // Set schedule type
    var scheduleType = el.attr('scheduletype');
    var scheduleClass = 'light-event';

    if( scheduleType == 'constant' ) {
        document.querySelector("input[value='constant']").checked = true;
        displayConstantMessage();
    } else if( scheduleType == 'sensors' ) {
        document.querySelector("input[value='sensors']").checked = true;
        displaySensorsMessage(el);
    } else if( scheduleType == 'blocked' ) {
        document.querySelector("input[value='blocked']").checked = true;
        displayBlockedMessage();
        scheduleClass = 'light-blocked';
    }

    // Find day of column clicked
    var today = new Date();
    var dayDifference = splitId[1] - today.getDay();

    if( dayDifference != 0 ) {
        today.setDate(today.getDate() + dayDifference);
    }

    // Hidden value to set day
    $('#schedule-date').val(splitId[1]);

    var checkId = splitId[0];

    // Iterate down through rows to find start of schedule event
    while( !startFound ) {
        var checkId = checkId - 1;

        if( checkId < 1 ) {
            break;
        }

        // If element doesn't have 'light-event' class, break loop
        if( !$('#' + checkId + '-' + splitId[1] + '-1' ).hasClass(scheduleClass) ) {
            startFound = true;
            checkId++;
        }
    }

    var startHour = checkId;

    // Get end time
    var endFound = false;
    var checkId = splitId[0];

    while( !endFound ) {
        checkId++;

        if( checkId > 23) {
            break;
        }

        // If element doesn't have 'light-event' class, break loop
        if( !$('#' + checkId + '-' + splitId[1] + '-1' ).hasClass(scheduleClass) ) {
            endFound = true;
        }
    }

    var endHour = checkId;

    if( startHour < 10 ) {
        startHour = "0" + startHour;
    }

    $('#start-time').val(startHour+":00:00");


    if( endHour < 10 ) {
        endHour = "0" + endHour;
    }

    $('#end-time').val(endHour+":00:00");

    // Calculate hours
    var hours = endHour - startHour;
    $('#hours').val(hours);

}

$(document).on('click', '.water-event', function() {
    drawWaterScheduleModal($(this));
});

$(document).on('click', '.water-blocked', function() {
    drawWaterScheduleModal($(this));
});

// Input: div's id and schedule's id
function drawWaterScheduleModal(el) {
    var id = el[0].id;
    var scheduleId = el.attr("scheduleid");

    // Set save zone variable ( For water, set to zoneid )
    saveZone = $('#zoneid').val();

    // Display modal
    $('#schedule-modal').css("display", "block");

    $('#modal-title').text('');
    $('#modal-title').text('Edit Water Schedule');

    // Show schedule delete button
    $('#delete-button').css("display", "block");

    // Get start time
    var tdId = id;       // Gets id as (h-d)
    var splitId = tdId.split('-');  // Splits id into h and d
    var startFound = false;

    // Set scheduleId
    $('#schedule-id').val( scheduleId );

    // Set schedule
    $('#schedule-type').val('water');
    document.querySelector("input[value='water']").checked = true;
    $('#schedule-description').show();
    $('#schedule-description').html('Sets water schedule for Zone ' + saveZone );

    // Set schedule type
    var scheduleType = el.attr('scheduletype');
    var scheduleClass = 'water-event';

    if( scheduleType == 'constant' ) {
        document.querySelector("input[value='constant']").checked = true;
        displayConstantMessage();
    } else if( scheduleType == 'sensors' ) {
        document.querySelector("input[value='sensors']").checked = true;
        displaySensorsMessage(el);
    } else if( scheduleType == 'blocked' ) {
        document.querySelector("input[value='blocked']").checked = true;
        displayBlockedMessage();
        scheduleClass = 'water-blocked';
    }

    // Find day of column clicked
    var today = new Date();
    var dayDifference = splitId[1] - today.getDay();

    if( dayDifference != 0 ) {
        today.setDate(today.getDate() + dayDifference);
    }

    // Hidden value to set day
    $('#schedule-date').val(splitId[1]);

    var checkId = splitId[0];

    // Iterate down through rows to find start of schedule event
    while( !startFound ) {
        var checkId = checkId - 1;

        if( checkId < 1 ) {
            break;
        }

        // If element doesn't have 'light-event' class, break loop
        if( !$('#' + checkId + '-' + splitId[1] + '-2' ).hasClass(scheduleClass) ) {
            startFound = true;
            checkId++;
        }
    }

    var startHour = checkId;

    // Get end time
    var endFound = false;
    var checkId = splitId[0];

    while( !endFound ) {
        checkId++;

        if( checkId > 23) {
            break;
        }

        // If element doesn't have 'water-event' class, break loop
        if( !$('#' + checkId + '-' + splitId[1] + '-2' ).hasClass(scheduleClass) ) {
            endFound = true;
        }
    }

    var endHour = checkId;

    if( startHour < 10 ) {
        startHour = "0" + startHour;
    }

    $('#start-time').val(startHour+":00:00");


    if( endHour < 10 ) {
        endHour = "0" + endHour;
    }

    $('#end-time').val(endHour+":00:00");

    // Calculate hours
    var hours = endHour - startHour;
    $('#hours').val(hours);

}

// Close schedule modal
$(document).on('click', '#close-button', function() {

    // Clear schedule type
     var ele = document.getElementsByName("type");
     for(var i=0;i<ele.length;i++)
     ele[i].checked = false;

    $('#schedule-modal').css("display", "none");
    $('#schedule-description').hide();
});

// Close schedule create/edit window
$(document).on('click', '#schedule-close', function() {
    clearCalendar();
    $('#container').css("filter","blur(0px)");
    $('#zone-modal').css("display", "none");
});

// User presses water radio option for schedule create/edit
$(document).on('click', '#water-radio', function() {
    $('#schedule-type').val('water');

    // Set saveZone variable ( For water, just set to zone ID )
    saveZone = $('#zoneid').val();

    $('#schedule-description').show();
    $('#schedule-description').html('Sets water schedule for Zone ' + saveZone );

    // Clear type radios and description
    var ele = document.getElementsByName("type");
    for(var i=0;i<ele.length;i++)
        ele[i].checked = false;

    $('#type-description').hide();
    $('#sensor-threshold-div').html('');

});

// User presses light radio option for schedule create/edit
$(document).on('click', '#light-radio', function() {
    $('#schedule-type').val('light');

    var zoneId = parseInt($('#zoneid').val());
    var lightZone = 1;

    if( zoneId > 2 && zoneId <= 4) {
        lightZone = 3;
    } else if( zoneId > 4 ) {
        lightZone = 5;
    }

    // Clear type radios and description
    var ele = document.getElementsByName("type");
    for(var i=0;i<ele.length;i++)
        ele[i].checked = false;

    $('#type-description').hide();
    $('#sensor-threshold-div').html('');


    $('#schedule-description').show();
    $('#schedule-description').html('Sets light schedule for Zone ' + lightZone + ' and ' + (lightZone+1) );
});

// User selects blocked for schedule type ( constant/sensor/blocked )
$(document).on('click', '#blocked-radio', function() {
    displayBlockedMessage();
});

function displayBlockedMessage() {
    $('#schedule-option').val('blocked');

    // Clear sensors div
    $('#sensors-threshold-div').html('');
    $('#sensor-threshold-div').html('');

    $('#type-description').show();
    $('#type-description').html('Prevent ' + $('#schedule-type').val() + ' from being turned on during this time');
}

// User selects sensors for schedule type ( constant/sensor/blocked )
$(document).on('click', '#sensors-radio', function() {
    displaySensorsMessage();
});

function displaySensorsMessage(el) {
    $('#schedule-option').val('sensors');

    // Clear threshold div
    $('#sensors-threshold-div').html("");

    var threshold;
    if( el != undefined ) {
        threshold = el.attr('threshold');
    }

    var text = '';
    var obj = '';
    if( $('#schedule-type').val() == 'light' ) {
        obj = 'Lights';
        text = 'light';
        displayLightThresholds(threshold);
    } else {
        obj = 'Water';
        text = 'soil moisture';
        displayWaterThresholds(threshold);
    }

    $('#type-description').show();
    $('#type-description').html( obj + ' will only turn on when the ' + text + ' sensors fall below the following threshold:');
}

// User selects constant for schedule type ( constant/sensor/blocked )
$(document).on('click', '#constant-radio', function() {
    displayConstantMessage();
});

function displayConstantMessage() {
    $('#schedule-option').val('constant');

    // Clear threshold div
    $('#sensor-threshold-div').html('');

    var obj = '';
    if( $('#schedule-type').val() == 'light' ) {
        obj = 'Lights';
    } else {
        obj = 'Water';
    }

    $('#type-description').show();
    $('#type-description').html( obj + ' will remain on for scheduled duration');
}

function displayLightThresholds(threshold) {
    var value = 10000;
    if( threshold != undefined ) {
        value = threshold;
    }


    $('#sensor-threshold-div').html('<label>lux: </label><input style="margin-left:5px; width: 70px;" type="number" name="light-threshold" id="threshold" value=' + value + '>');
}

function displayWaterThresholds(threshold) {
    var value=30;
    if( threshold != undefined ) {
        value = threshold;
    }


    $('#sensor-threshold-div').html('<label>Soil moisture: </label><input style="margin-left:5px; width: 40px;" type="number" name="water-threshold" id="threshold" value='+ value +'>%');
}

// Post schedule data to backend
$(document).on('click', '#save-button', function() {
    zone = $('#zoneid').val();

    // Need to dynamically create data object depending on schedule type
    var data = {};
    data.start = $('#start-time').val();
    data.end = $('#end-time').val();
    data.hours = $('#hours').val();
    data.inverse = $('#inverse').val();
    data.id = $('#schedule-id').val();
    data.zoneid = saveZone;
    data.date = $('#schedule-date').val();
    data.schedule = $("#schedule-div input[type='radio']:checked").val();
    data.type = $("#type-div input[type='radio']:checked").val();

    // Check for empty radio button
    if( data.type == undefined ) {
        data.type = 'constant';
    }

    // If user selects sensors, add threshold
    if( data.type == 'sensors' ) {
        data.threshold = $('#threshold').val();
    }

    $.ajax({
        type: 'POST',
        url: 'api/schedule',
        data: data,
        success: function() {
            clearCalendar();
            getSchedules(zone);
        }
    });
});

// Delete schedule
$(document).on('click','#delete-button', function() {
    $.ajax({
        type: 'POST',
        url: 'api/schedule',
        data: {
            action: 'delete',
            id: $('#schedule-id').val(),
            schedule: $('#schedule-type').val()
        },
        success: function() {
            // Clear calendar
            clearCalendar();

            // Redraw schedule
            getSchedules($('#zoneid').val());

            // Hide schedule modal
            $('#schedule-modal').css("display", "none");
        }
    })
});


// Recalculate hours when user changes start/end time when creating/editing schedule
function timeUpdate() {
    var startTime = $('#start-time').val().split(":")[0];
    var endTime = $('#end-time').val().split(":")[0];
    var hours = endTime - startTime;

    if( hours < 0 || hours > 24) {
        hours = "0";
    }

    if( hours == 0 ) {
        var newStart = parseInt($('#end-time').val().split(":")[0]) + 1;

        if( newStart < 10 ) {
            newStart = "0" + newStart;
        }
        $('#end-time').val(newStart+":00:00");
    }

    startTime = $('#start-time').val().split(":")[0];
    endTime = $('#end-time').val().split(":")[0];
    hours = endTime - startTime;

    if( hours < 0 || hours > 24) {
        hours = "0";
    }

    $('#hours').val( hours );
}

// Recalculate end time when user changes number of hours
function hourUpdate() {
    var startTime = $('#start-time').val().split(":")[0];
    var endTime = parseInt(startTime) + parseInt($('#hours').val());

    if( endTime > 23 ) {
        endTime = 23;
        $('hours').val(parseInt($('#hours').val()-1));
    }

    if( endTime < 10 ) {
        endTime = "0" + endTime;
    }

    $('#end-time').val(endTime+":00:00");
}



