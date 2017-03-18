package model;

public class SensorModel {

    int zone, entryId;
    double moisture, temperature, light, humidity;
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

    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }

    public double getMoisture() {
        return moisture;
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
