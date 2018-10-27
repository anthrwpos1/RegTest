public class PhysReg implements Regulator {
    public double t_filtre;
    public double c, cH, q, tSet, dt, tau, pMax;
    private double wH, pe, p;
    private MAVG mavg;

    PhysReg(double c, double cH, double q, double tSet, double t_filtre, double dt, double tau, double pMax) {
        this.c = c;
        this.cH = cH;
        this.q = q;
        this.tSet = tSet;
        this.t_filtre = t_filtre;
        this.dt = dt;
        this.tau = tau;
        this.pMax = pMax;
        p = 0;
        mavg = new MAVG(2, dt, t_filtre, true);
    }

    @Override
    public double control(double t_sensor) {
        mavg.mavg(t_sensor);
        double tH = wH / cH + tSet;
        double wr = c * (mavg.dnout[0] - tSet);
        wH = wH + q * (mavg.dnout[0] - tH) * dt + p * dt;
        pe = q * (tH - mavg.dnout[0]) - mavg.dnout[1] * c;
        double wset = cH * pe / q;
        p = pe + (wset - wH - wr) / tau * dt;
        if (p < 0) p = 0;
        if (p > pMax) p = pMax;
        return p / pMax;
    }
}
