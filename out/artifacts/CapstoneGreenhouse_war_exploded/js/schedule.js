$(document).ready( function() {
    zone = 1;
    getSchedules(1);
});

var zone = 0;


// TODO: Add functionality for Schedule API to return schedules by zone
function getSchedules(zone) {
    $.ajax({
        type: 'GET',
        url: 'api/schedule',
        success: function(response) {
            var data = JSON.parse(response);

            populateHeading();
            drawCalendar();

            // Populate light schedule
            for( var i=0; i<data.lightschedule.length; i++ ) {
                if( data.lightschedule[i].zone == zone ) {
                    //console.log( data.lightschedule[i]);
                    populateCalendar( data.lightschedule[i] );
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
        timeCol.innerHTML = times[i];

        // Add days of the week
        for( var j=1; j<=7; j++ ) {
            var dayCol = row.insertCell(j);

            // Set td id ( time - day )
            var id = i + "-" + (j-1);
            dayCol.innerHTML = "<div id=" + id + " class='schedule-body'></div>";
        }
    }
}

// Input: schedule object
function populateCalendar(schedule) {

    // Convert start time to ( time - day ) format
    var startString = schedule.start.split(" ");
    var date = startString[0].split("/");
    var time = startString[1].split(":");

    // Get TD with id ( time - day )
    var day = new Date();
    day.setDate( date[2] );

    // If month is different, change month
    if( date[1] > (day.getMonth()+1) ) {
        day.setMonth(day.getMonth() + 1);
    } else if( date[1] < (day.getMonth()+1) ) {
        day.setMonth(day.getMonth() - 1);
    }

    // Start at start time and color each row
    for( var i=0; i<schedule.hours; i++ ) {
        var hour = parseInt(time[0]) + i;

        var td = document.getElementById( hour + "-" + day.getDay() );
        td.setAttribute('scheduleid', schedule.id);

        if( i==0 ) {
            td.innerText = "LIGHTS ON";
        }
        td.classList.add('light-event');
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

// Open schedule modal
$(document).on('click', '.schedule-body', function() {

    // TODO: change schedule type
    $('#schedule-type').val('light');

    // Display modal
    $('#schedule-modal').css("display", "block");
    //console.log($(this));

    // Set zone ID
    $('#zoneid').val(1);

    // Get start time
    var tdId = $(this)[0].id;
    var splitId = tdId.split('-');
    var startFound = false;

    $('#schedule-id').val( $(this).attr("scheduleid") );

    // Find day of column clicked
    var today = new Date();
    var dayDifference = splitId[1] - today.getDay();

    if( dayDifference != 0 ) {
        today.setDate(today.getDate() + dayDifference);
    }

    $('#schedule-date').val( (today.getYear()+1900) + "/" + (today.getMonth()+1) + "/" + today.getDate() );

    var checkId = splitId[0];

    // Iterate down through rows to find start of schedule event
    while( !startFound ) {
        var checkId = checkId - 1;
        //var checkTd = document.getElementById( checkId + "-" + splitId[1] );
        //console.log("CheckId: " + checkId );

        if( checkId < 1 ) {
            break;
        }

        // If split has more than 1 class, td is a schedule event
        var split = $('#' + checkId + '-' + splitId[1] ).attr("class").split(" ");

        if( split.length < 2 ) {
            startFound = true;
            checkId++;
        }
    }

    //console.log("Start time: " + checkId + "-" + splitId[1] );
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

        // If split has more than 1 class, td is a schedule event
        var split = $('#' + checkId + '-' + splitId[1] ).attr("class").split(" ");

        if( split.length < 2 ) {
            endFound = true;
        }
    }

    //console.log("End time: " + checkId + "-" + splitId[1] );
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
    $('#container').css("filter","blur(0px)");
    $('#zone-modal').css("display", "none");
});

$(document).on('click', '#save-button', function() {
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
            type: $('#schedule-type').val(),
            apikey: '44ffe28b-f470-4bc0-8ee9-38fce01438ce'
        },
        success: function() {
            clearCalendar();
            getSchedules(zone);
        }
    });
});

