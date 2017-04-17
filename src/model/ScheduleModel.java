package model;

public class ScheduleModel {
    int id, zoneID, inverse, day, threshold;
    double hours;
    String startTime, endTime, type;

    public void setId( int id ) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setZoneID( int zoneID ) {
        this.zoneID = zoneID;
    }

    public int getZoneID() {
        return zoneID;
    }

    public void setStartTime( String startTime ) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime( String endTime ) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getHours() {
        return hours;
    }

    public void setInverse(int inverse) {
        this.inverse = inverse;
    }

    public int getInverse() {
        return inverse;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }
}
