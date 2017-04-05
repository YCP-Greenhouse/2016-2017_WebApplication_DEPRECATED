/*$(document).ready( function() {
    zone = 1;
    getSchedules(1);
});*/

// Set when user clicks zone 1-6
var zone;

function getSchedules(zone) {
    //this.zone = zone;
    $.ajax({
        type: 'GET',
        url: 'api/schedule',
        success: function(response) {
            var data = JSON.parse(response);

            clearCalendar();
            populateHeading();
            drawCalendar();

            // Populate light schedule
            for (var i = 0; i < data.lightschedule.length; i++) {
                if (data.lightschedule[i].zone == zone) {
                    populateCalendar('light', data.lightschedule[i]);
                }
            }

            // Populate water schedule
            for (var i = 0; i < data.waterschedule.length; i++) {
                if (data.lightschedule[i].zone == zone) {
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
            blankDiv.setAttribute('scheduletype', title);
            blankDiv.classList.add('light-event');

            // Remove 'clear' classes
            blankDiv.classList.remove("blank");
            blankDiv.classList.remove("left");

            if( i==0 ) {
                blankDiv.innerText = "LIGHTS ON";
            }

        } else if( title === 'water' ) {

            var blankDiv = document.getElementById( tdId + -2 );
            blankDiv.setAttribute('scheduleid', schedule.id);
            blankDiv.setAttribute('scheduletype', title);
            blankDiv.classList.add('water-event');

            // Remove 'clear' classes
            blankDiv.classList.remove("blank");
            blankDiv.classList.remove("left");


            if( i==0 ) {
                blankDiv.innerText = "WATER ON";
            }
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

    // Tests
    var testDate = new Date();
    //testDate.setDate( today.getDate() - 10 );
    //startDay.setDate( testDate.getDate() - testDate.getDay() );

    //today.setDate(testDate.getDate());

    //console.log("Test day: " + days[today.getDay()] + " " + (today.getMonth()+1) + "/" + today.getDate());
    //console.log("Start day: " + days[startDay.getDay()] + " " + (startDay.getMonth()+1) + "/" + startDay.getDate());

    // Append dates to days for table headers
    var header = document.getElementById('schedule-heading');
    var row = header.insertRow(0);

    var cell = row.insertCell(-1);
    cell.innerHTML = " ";

    for( var i=0; i<7; i++ ) {
        //console.log( days[i] + " " + (startDay.getMonth()+1) + "/" + startDay.getDate() );

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

    // Display modal
    $('#schedule-modal').css("display", "block");

    // Hide schedule delete button
    $('#delete-button').css("display", "none");


    $('#modal-title').text('New Schedule');

    // Get start time
    var tdId = $(this)[0].id;       // Gets id as (h-d)
    var splitId = tdId.split('-');  // Splits id into h and d
    var startFound = false;

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
    var endFound = false;
    var checkId = splitId[0];
    checkId++;

    var endHour = checkId;

    // Convert start ID into hour
    var hour = convertMilitaryToStandardTime(startHour);
    if( hour == 0 ) {
        hour = 12;
    }
    var ampm = getAmPm(startHour);

    $('#start-time').val(hour+ampm);

    // Convert end ID into hour
    hour = convertMilitaryToStandardTime(endHour);
    if( hour == 0 ) {
        hour = 12;
    }
    ampm = getAmPm(endHour);

    $('#end-time').val(hour+ampm);

    // Calculate hours
    var hours = endHour - startHour;
    $('#hours').val(hours);
});

$(document).on('click', '.light-event', function() {

    // Display modal
    $('#schedule-modal').css("display", "block");

    $('#modal-title').text('');
    $('#modal-title').text('Edit Light Event');

    // Show schedule delete button
    $('#delete-button').css("display", "block");

    // Get start time
    var tdId = $(this)[0].id;       // Gets id as (h-d)
    var splitId = tdId.split('-');  // Splits id into h and d
    var startFound = false;

    $('#schedule-id').val( $(this).attr("scheduleid") );

    // Set schedule type
    $('#schedule-type').val('light');

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
        if( !$('#' + checkId + '-' + splitId[1] + '-1' ).hasClass('light-event') ) {
            startFound = true;
            checkId++;
        }
    }

    var startHour = checkId;

    // Get end time
    var endFound = false;
    var checkId = splitId[0];

    //console.log(checkId);

    while( !endFound ) {
        checkId++;

        if( checkId > 23) {
            break;
        }

        // If element doesn't have 'light-event' class, break loop
        if( !$('#' + checkId + '-' + splitId[1] + '-1' ).hasClass('light-event') ) {
            endFound = true;
        }
    }

    var endHour = checkId;

    // Convert start ID into hour
    var hour = convertMilitaryToStandardTime(startHour);
    if( hour == 0 ) {
        hour = 12;
    }
    var ampm = getAmPm(startHour);

    $('#start-time').val(hour+ampm);

    // Convert end ID into hour
    hour = convertMilitaryToStandardTime(endHour);
    if( hour == 0 ) {
        hour = 12;
    }
    ampm = getAmPm(endHour);

    $('#end-time').val(hour+ampm);

    // Calculate hours
    var hours = endHour - startHour;
    $('#hours').val(hours);

});

$(document).on('click', '.water-event', function() {

    // Display modal
    $('#schedule-modal').css("display", "block");

    $('#modal-title').text('');
    $('#modal-title').text('Edit Water Event');

    // Show schedule delete button
    $('#delete-button').css("display", "block");

    // Set schedule type
    $('#schedule-type').val('water');

    // Get start time
    var tdId = $(this)[0].id;       // Gets id as (h-d)
    var splitId = tdId.split('-');  // Splits id into h and d
    var startFound = false;

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
    while( !startFound ) {
        var checkId = checkId - 1;

        if( checkId < 1 ) {
            break;
        }

        // If element doesn't have 'light-event' class, break loop
        if( !$('#' + checkId + '-' + splitId[1] + '-2' ).hasClass('water-event') ) {
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
        if( !$('#' + checkId + '-' + splitId[1] + '-2' ).hasClass('water-event') ) {
            endFound = true;
        }
    }

    var endHour = checkId;

    // Convert start ID into hour
    var hour = convertMilitaryToStandardTime(startHour);
    if( hour == 0 ) {
        hour = 12;
    }
    var ampm = getAmPm(startHour);

    $('#start-time').val(hour+ampm);

    // Convert end ID into hour
    hour = convertMilitaryToStandardTime(endHour);
    if( hour == 0 ) {
        hour = 12;
    }
    ampm = getAmPm(endHour);

    $('#end-time').val(hour+ampm);

    // Calculate hours
    var hours = endHour - startHour;
    $('#hours').val(hours);

});


function convertMilitaryToStandardTime(hour) {
    if( hour > 12) {
        hour = hour-12;
    }

    if( hour < 0) {
        hour = 0;
    }

    return hour;
}

function getAmPm(hour) {
    if( hour > 11 && hour < 24 ) {
        return "pm";
    } else {
        return "am";
    }
}

// Close schedule modal
$(document).on('click', '#close-button', function() {
    $('#schedule-modal').css("display", "none");
});

$(document).on('click', '#schedule-close', function() {
    clearCalendar();
    $('#container').css("filter","blur(0px)");
    $('#zone-modal').css("display", "none");
});

$(document).on('click', '#save-button', function() {
    zone = $('#zoneid').val();

    $.ajax({
        type: 'POST',
        url: 'api/schedule',
        data: {
            start: $('#start-time').val(),
            end: $('#end-time').val(),
            hours: $('#hours').val(),
            inverse: $('#inverse').val(),
            id: $('#schedule-id').val(),
            zoneid: $('#zoneid').val(),
            date: $('#schedule-date').val(),
            type: $('#schedule-type').val()
        },
        success: function() {
            clearCalendar();
            getSchedules(zone);
        }
    });
});

// Set type
$(document).on('click', '#water-radio', function() {
    $('#schedule-type').val('water');
});

$(document).on('click', '#light-radio', function() {
    $('#schedule-type').val('light');
});

// Delete schedule
$(document).on('click','#delete-button', function() {
    $.ajax({
        type: 'POST',
        url: 'api/schedule',
        data: {
            action: 'delete',
            id: $('#schedule-id').val(),
            type: $('#schedule-type').val()
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

