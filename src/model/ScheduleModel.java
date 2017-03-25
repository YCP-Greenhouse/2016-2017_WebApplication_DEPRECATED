package model;

public class ScheduleModel {
    int id, zoneID;
    double hours;
    String startTime, endTime;

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
}
