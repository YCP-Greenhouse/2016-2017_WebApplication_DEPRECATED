// Get greenhouse state when page loads
$(document).ready( function() {


        $('#nav-list').addClass('nav');
        $('#nav-list').html('<ul><a href="index.html"><li class="nav-li"><img width="40" height="40" src="images/home.png"><span class="nav-item">HOME</span></li></a><a href="/settings"><li class="nav-active"><img width="40" height="40" src="images/gear.png"><span class="nav-item" style="letter-spacing: 1.5px;">SETTINGS</span></li></a><a href="sensors.html"><li class="nav-li"><img width="35" height="35" src="images/sensors.png"><span class="nav-item">SENSORS</span></li></a><a href="history.html"><li class="nav-li"><img width="35" height="35" src="images/history.png"><span class="nav-item">HISTORY</span></li></a><a href="/documentation"><li class="nav-li"><img style="margin-top:22px;" width="40" height="30" src="images/learn.png"><span class="nav-item">LEARN</span></li></a></ul>');

});