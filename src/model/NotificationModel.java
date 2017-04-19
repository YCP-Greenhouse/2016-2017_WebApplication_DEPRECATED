package model;

public class NotificationModel {
    int notifyTemp, notifySoil;
    int tempHigh, tempLow, soilHigh, soilLow;

    public void setNotifyTemp( int notifyTemp ) {
        this.notifyTemp = notifyTemp;
    }

    public int getNotifyTemp() {
        return notifyTemp;
    }

    public void setNotifySoil(int notifySoil) {
        this.notifySoil = notifySoil;
    }

    public int getNotifySoil() {
        return notifySoil;
    }

    public void setTempHigh(int tempHigh) {
        this.tempHigh = tempHigh;
    }

    public int getTempHigh() {
        return tempHigh;
    }

    public void setTempLow(int tempLow) {
        this.tempLow = tempLow;
    }

    public int getTempLow() {
        return tempLow;
    }

    public void setSoilHigh(int soilHigh) {
        this.soilHigh = soilHigh;
    }

    public int getSoilHigh() {
        return soilHigh;
    }

    public void setSoilLow(int soilLow){
        this.soilLow = soilLow;
    }

    public int getSoilLow() {
        return soilLow;
    }
}
