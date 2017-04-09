$(document).ready( function() {

    // Contacts functions
    getContacts();

    // Get error message
    getErrorMessage();

});


function getContacts() {

    $.ajax({
        type: 'GET',
        url: '/api/contacts',
        success: function (response) {
            var data = JSON.parse(response);
            console.table(data);

            // Add contacts to table
            var table = document.getElementById('contact-table-body');
            table.innerHTML = "";

            for( var i=0; i<data.length; i++ ) {
                var row = table.insertRow(i);

                var col1 = row.insertCell(0);
                col1.innerHTML = data[i].name;

                var col2 = row.insertCell(1);
                col2.innerHTML = data[i].position;

                var col3 = row.insertCell(2);
                col3.innerHTML = data[i].email;

                var col4 = row.insertCell(3);
                col4.innerHTML = data[i].phone;
            }
        }
    });
}

// Close schedule modal
$(document).on('click', '#contact-submit', function() {
    addContact();
});

function addContact() {
    console.log("Add Contact");
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

// Submit error message
$(document).on('click', '#error-submit', function() {
    $.ajax({
        type: 'POST',
        url: '/api/error',
        data: { apikey: '44ffe28b-f470-4bc0-8ee9-38fce01438ce', message: $('#set-error-message').val(), code: $('#set-error-code').val() },
        success: function() {
            getErrorMessage();
        }
    })
});
