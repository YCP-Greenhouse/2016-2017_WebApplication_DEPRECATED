$(document).ready( function() {

    // Contacts functions
    getContacts();

    // Get error message
    getErrorMessages();

    // Get notification settings
    getNotificationSettings();

    // Check if browser is Raspberry Pi
    if ( navigator.platform.toLowerCase().indexOf('linux') >= 0 ) {
        $('#nav-list').addClass('nav-small');
        $('#nav-list').html('<ul><a href="/settings"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a></ul>');

    } else {
        $('#nav-list').addClass('nav');
        $('#nav-list').html('<ul><a href="index.html"><li class="nav-li"><img width="40" height="40" src="images/home.png"><span class="nav-item">HOME</span></li></a><a href="/settings"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a><a href="/documentation"><li class="nav-li"><img style="margin-top:22px;" width="40" height="30" src="images/learn.png"><span class="nav-item">LEARN</span></li></a></ul>');
    }

});

/*
window.setInterval( function() {
    // Get error message
    getErrorMessages();

}, 3000 ); */

function getContacts() {

    $.ajax({
        type: 'GET',
        url: '/api/contacts',
        success: function (response) {
            var data = JSON.parse(response);

            // Add contacts to table
            var table = document.getElementById('contact-table-body');
            table.innerHTML = "";

            for( var i=0; i<data.length; i++ ) {
                var row = table.insertRow(i);

                var col1 = row.insertCell(0);
                col1.innerHTML = '<input type="text" id="name-' + data[i].id + '" class="contact-input" value="' + data[i].name + '">';

                var col2 = row.insertCell(1);
                col2.innerHTML = '<input type="text" id="position-' + data[i].id + '" class="contact-input" value="' + data[i].position + '">';

                var col3 = row.insertCell(2);
                col3.innerHTML = '<input type="text" id="email-' + data[i].id + '" class="contact-input" value="' + data[i].email + '">';

                var col4 = row.insertCell(3);
                col4.innerHTML = '<input type="text" id="phone-' + data[i].id + '" class="contact-input" value="' + data[i].phone + '">';
            }
        }
    });
}

// Close schedule modal
$(document).on('click', '#contact-submit', function() {
    addContact();
});

function addContact() {
    $.ajax({
        type: 'POST',
        url: '/api/contacts',
        data: $('#contact-form').serialize(),
        success: function() {
            getContacts();
            $('#contact-form')[0].reset();
        }
    });
}

// Get all error messages
function getErrorMessages() {

    $.ajax({
        type: 'GET',
        url: 'api/error',
        success: function(response) {
            var data = JSON.parse(response);

            // Add errors to table
            var table = document.getElementById('error-table-body');
            table.innerHTML = "";

            for( var i=data.length-12; i<data.length; i++ ) {
                var row = table.insertRow(0);

                var col1 = row.insertCell(0);
                col1.innerHTML = data[i].message;

                var col2 = row.insertCell(1);
                col2.innerHTML = data[i].time;

            }
        }
    });
}

// Get notification settings
function getNotificationSettings() {
    $.ajax({
        type: 'GET',
        url: 'api/notifications',
        success: function(response) {
            var data = JSON.parse(response);

            if( data.tempNotify == 1 ) {
                $('#temperature').prop('checked', true);
            }

            if( data.soilNotify == 1 ) {
                $('#soil-moisture').prop('checked', true);
            }

            // Set limits
            $('#temperature-below').val(data.tempLow);
            $('#temperature-above').val(data.tempHigh);
            $('#soil-below').val(data.soilLow);
            $('#soil-above').val(data.soilHigh);
        }
    });
}

$(document).on('click', '#temperature', function() {
    setNotificationSettings();
});

$(document).on('click', '#soil-moisture', function() {
    setNotificationSettings();
})

$(document).on('click', '.settings-input', function() {
    setNotificationSettings();
})

// Set notification settings
function setNotificationSettings() {
    var data = {};

    // Convert from on/off to 1/0
    if( $('#temperature').is(':checked') ) {
        data.tempNotify = 1;
    } else {
        data.tempNotify = 0;
    }

    if( $('#soil-moisture').is(':checked') ) {
        data.soilNotify = 1;
    } else {
        data.soilNotify = 0;
    }

    data.tempHigh = $('#temperature-above').val();
    data.tempLow = $('#temperature-below').val();
    data.soilHigh = $('#soil-above').val();
    data.soilLow = $('#soil-below').val();

    $.ajax({
        type: 'POST',
        url: '/api/notifications',
        data: data,
        success: function() {

        }
    })
}

var contactText = '';
var contactID;
$(document).on('click', '.contact-input', function() {
    $('#delete-contact-btn').text('DELETE CONTACT');
    $('#delete-contact-btn').css("display", "block");

    contactID = $(this)[0].id;
    contactText = $(this)[0].defaultValue;
});

// Update contacts when user updates text and leaves text box
$(document).on('focusout', '.contact-input', function() {

    // Hide delete button
    setTimeout(hideBtn, 100);

    // Only update if the text changes
    if( $(this)[0].value == contactText ) {
        return;
    }

    var split = $(this)[0].id.split('-');
    var id = split[1];

    var contact = {};
    contact.id = id;
    contact.name = $('#name-' + id ).val();
    contact.position = $('#position-' + id ).val();
    contact.email = $('#email-' + id ).val();
    contact.phone = $('#phone-' + id ).val();
    contact.action = "update";

    postContact(contact);
});

function postContact(contact) {
    $.ajax({
        type: 'POST',
        url: '/api/contacts',
        data: contact,
        success: function() {
            getContacts();
        }
    });
}

function deleteContact(contact) {

    $.ajax({
        type: 'POST',
        url: '/api/contacts',
        data: contact,
        success: function() {
            getContacts();
        }
    });
}

// Add new contact
$(document).on('click', '#add-contact-btn', function() {

    // Check if new contact row exists
    if( $('.new-contact-input').length > 0 ) {
        return;
    }

    $('#delete-contact-btn').text('CANCEL');
    $('#delete-contact-btn').css("display", "block");

    var table = document.getElementById('contact-table-body');

    var row = table.insertRow(0);

    var col1 = row.insertCell(0);
    col1.innerHTML = '<input type="text" id="new-name" name="name" class="new-contact-input">';

    var col2 = row.insertCell(1);
    col2.innerHTML = '<input type="text" id="new-position" name="position" class="new-contact-input">';

    var col3 = row.insertCell(2);
    col3.innerHTML = '<input type="text" id="new-email" name="email" class="new-contact-input">';

    var col4 = row.insertCell(3);
    col4.innerHTML = '<input type="text" id="new-phone" name="phone" class="new-contact-input">';

});

// Post new contact
var newContact = {};
$(document).on('focusout', '.new-contact-input', function() {

    // Hide cancel button
    //setTimeout(hideBtn, 1000);

    //console.log( $(this)[0].name );

    newContact.name = $('#new-name').val();
    newContact.position = $('#new-position').val();
    newContact.email = $('#new-email').val();
    newContact.phone = $('#new-phone').val();
    newContact.action = "add";

    if( newContact.name != "" && newContact.position != "" && newContact.email != "" && newContact.phone != "") {
        postContact(newContact);
    }
});

function hideBtn() {
    $('#delete-contact-btn').css("display", "none");
}

// Delete or Cancel user edit
$(document).on('click', '#delete-contact-btn', function() {

    // Delete contact
    if( $('#delete-contact-btn').text() == "DELETE CONTACT" ) {

        var contact = {}

        contact.id = contactID.split('-')[1];
        contact.action = "delete";

        deleteContact(contact);

        // Remove new line
    } else if( $('#delete-contact-btn').text() == "CANCEL" ) {
        $('#contact-table-body tr:first').remove();
    }

    $('#delete-contact-btn').css("display","none");
});


