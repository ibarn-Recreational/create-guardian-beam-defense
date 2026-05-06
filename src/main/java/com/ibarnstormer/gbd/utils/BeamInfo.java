package com.ibarnstormer.gbd.utils;

public class BeamInfo {
    private int tick;
    private float chargeSpeed;

    public BeamInfo(float chargeSpeed) {
        this.tick = 0;
        this.chargeSpeed = chargeSpeed;
    }

    public BeamInfo(int tick, float chargeSpeed) {
        this.tick = tick;
        this.chargeSpeed = chargeSpeed;
    }

    public void tickBeam() {
        if(tick < chargeSpeed + 2) tick++;
    }

    public int getTick() {
        return tick;
    }

    public float getChargeSpeed() {
        return chargeSpeed;
    }

    public void updateChargeSpeed(float newSpeed) {
        chargeSpeed = newSpeed;
    }

}
