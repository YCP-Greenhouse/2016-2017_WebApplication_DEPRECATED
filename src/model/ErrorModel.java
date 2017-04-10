package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ErrorModel {
    String message, time;
    int code;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTime( String time ) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setCode( int code ) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
