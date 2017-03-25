package model;

import java.util.ArrayList;

public class AutomationModel {
    int tempLow, tempHigh, soilMoisture, lightIntesity, humidity;
    ArrayList<ScheduleModel> waterSchedule = new ArrayList<>();
    ArrayList<ScheduleModel> lightSchedule = new ArrayList<>();

    public AutomationModel() {

    }

    public void setTempLow(int tempLow) {
        this.tempLow = tempLow;
    }

    public int getTempLow() {
        return tempLow;
    }

    public void setTempHigh(int tempHigh ) { this.tempHigh = tempHigh; }

    public int getTempHigh() { return tempHigh; }

    public void setSoilMoisture(int soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public int getSoilMoisture() {
        return soilMoisture;
    }

    public void setLightIntesity(int lightIntesity) {
        this.lightIntesity = lightIntesity;
    }

    public int getLightIntesity() {
        return lightIntesity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void addWaterSchedule( ScheduleModel schedule ) {
        this.waterSchedule.add(schedule);
    }

    public ArrayList<ScheduleModel> getWaterSchedule() {
        return waterSchedule;
    }

    public void addLightSchedule( ScheduleModel schedule ) {
        this.lightSchedule.add(schedule);
    }

    public ArrayList<ScheduleModel> getLightSchedule() {
        return lightSchedule;
    }
}
