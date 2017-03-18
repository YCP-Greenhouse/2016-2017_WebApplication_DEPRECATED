package model;

public class AutomationModel {
    int temperature, soilMoisture, lightIntesity, humidity;

    public AutomationModel() {

    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

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

}
