package Regulators;

import Regulators.MAVG;

public class Adap {
    private MAVG filtre, mid;
    public double dt;
    private double valmid;
    private double val;
    private double dval;

    public Adap(double pre, double adap, double dt) {
        filtre = new MAVG(2, dt, pre, false);
        mid = new MAVG(1, dt, adap, false);
        this.dt = dt;
    }

    public void adap(double input, double dt) {
        filtre.dt = dt;
        filtre.mavg(input);
        val = filtre.dnout[0];
        dval = filtre.dnout[1];
        mid.dt = dt;
        mid.mavg(val);
        valmid = mid.dnout[0];
    }
}
