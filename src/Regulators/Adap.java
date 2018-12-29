package Regulators;

import Regulators.MAVG;

public class Adap {
    private MAVG filtre, mid;
    public double dt, tAdap;
    private double vol;

    public Adap(double pre, double adap, double dt) {
        filtre = new MAVG(2, dt, pre, true);
        mid = new MAVG(1, dt, adap, false);
        this.dt = dt;
        this.tAdap = adap;
    }

    public double adap(double input, double dt) {
        filtre.dt = dt;
        filtre.mavg(input);
        double val = filtre.dnout[0];
        double dval = filtre.dnout[1];
        mid.dt = dt;
        mid.mavg(val);
        double valmid = mid.dnout[0];
        vol = vol * (1 - dt / tAdap) + Math.abs(dval);
        double move = val - valmid;
        return move * move / (move * move + vol * vol) * 2;
    }
}
