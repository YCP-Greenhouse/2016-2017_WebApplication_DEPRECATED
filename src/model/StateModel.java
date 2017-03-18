package model;

public class StateModel {
    // State variables for greenhouse components
    // True = On, False = Off
    private boolean lights;
    private boolean heaters;
    private boolean waterPump;
    private boolean fans;
    private boolean vents;
    private boolean shades;

    // Set all values to false
    public StateModel(){
        lights = false;
        heaters = false;
        waterPump = false;
        fans = false;
        vents = false;
        shades = false;
    }

    public void setLights(boolean lights) {
        this.lights = lights;
    }

    public boolean getLights() {
        return lights;
    }

    public void toggleLights() {
        lights = !lights;
    }

    public void setHeaters(boolean heaters) {
        this.heaters = heaters;
    }

    public boolean getHeaters() {
        return heaters;
    }

    public void toggleHeaters() {
        heaters = !heaters;
    }

    public void setWaterPump(boolean waterPump) {
        this.waterPump = waterPump;
    }

    public boolean getWaterPump() {
        return waterPump;
    }

    public void toggleWaterPump() {
        waterPump = !waterPump;
    }

    public void setFans(boolean fans) {
        this.fans = fans;
    }

    public boolean getFans() {
        return fans;
    }

    public void toggleFans() {
        fans = !fans;
    }

    public void setVents(boolean vents) {
        this.vents = vents;
    }

    public boolean getVents() {
        return vents;
    }

    public void toggleVents() {
        vents = !vents;
    }

    public void setShades(boolean shades) {
        this.shades = shades;
    }

    public boolean getShades() {
        return shades;
    }

}
