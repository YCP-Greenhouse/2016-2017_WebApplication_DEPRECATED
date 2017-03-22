package model;

public class SensorModel {

    int zone, entryId;
    double temperature, light, humidity, probe1, probe2;
    String sampleTime;

    public SensorModel() {}

    public void setZone(int zone) {

        this.zone = zone;
    }

    public int getZone() {
        return zone;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setProbe1(double probe1) {
        this.probe1 = probe1;
    }

    public double getProbe1() {
        return probe1;
    }

    public void setProbe2(double probe2){
        this.probe2 = probe2;
    }

    public double getProbe2() {
        return probe2;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getLight() {
        return light;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    public String getSampleTime() {
        return sampleTime;
    }


}
