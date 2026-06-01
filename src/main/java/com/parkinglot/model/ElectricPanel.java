package com.parkinglot.model;

public class ElectricPanel {

    private final String panelId;
    private boolean inUse;

    public ElectricPanel(String panelId) {
        this.panelId = panelId;
        this.inUse = false;
    }

    public String getPanelId() {
        return panelId;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void startCharging() {
        inUse = true;
    }

    public void stopCharging() {
        inUse = false;
    }

    @Override
    public String toString() {
        return "ElectricPanel{" + panelId + ", inUse=" + inUse + '}';
    }
}
