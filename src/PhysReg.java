public class PhysReg implements Regulator {
    public double t_filtre;
    public double d, bhta, tU, tX, tSet, dt;
    private double wH, pe, p;
    private MAVG TFiltre;
    private MAVG PEFiltre;

    PhysReg(double d, double tU, double tX, double dt, double tSet) {
        this.d = d;
        this.tU = tU;
        this.tX = tX;
        this.dt = dt;
        this.tSet = tSet;
        bhta = 0;
        p = 0;
        TFiltre = new MAVG(2, dt, t_filtre, true);
        PEFiltre = new MAVG(1, dt, tX, false);
    }

    @Override
    public double control(double t_sensor) {
        TFiltre.mavg(t_sensor);
        double err = tSet - TFiltre.dnout[0];
        double dT = TFiltre.dnout[1];
        wH = ((1-bhta) * p + bhta * pe - wH) / tU * dt + wH;
        PEFiltre.mavg(wH - (1-bhta) * tX * dT);
        pe = PEFiltre.dnout[0];
        return d * err / tX + d * dT * tU / tX + pe;
    }
}
