package Regulators;

/* Мой регулятор. Использует ПД-регулятор в качестве основы
 * и авторский способ вычисления добавки, компенсирующей
 * статическую ошибку ПД-регулятора
 *
 * -Άνθρωπος
 */

import java.util.ArrayList;

public class PhysReg implements Regulator {
    public double t_filtre, pe_filtre;
    public double d, bhta, tU, tX, tSet, dt;
    private double wH;
    private double pe;
    private double p;
    private MAVG tFiltre;
    private MAVG pEFiltre;
    private Adap adap;
    public ArrayList<Double> wHArr;
    public ArrayList<Double> pEArr;
    public ArrayList<Double> eff;

    public PhysReg(double d, double tU, double tX, double dt, double tSet, double t_filtre, double pe_filtre) {
        this.d = d;
        this.tU = tU;
        this.tX = tX;
        this.dt = dt;
        this.tSet = tSet;
        this.t_filtre = t_filtre;
        this.pe_filtre = pe_filtre;
        bhta = 0;
        p = 0;
        tFiltre = new MAVG(2, dt, t_filtre, true);
        pEFiltre = new MAVG(1, dt, pe_filtre, true);
        adap = new Adap(t_filtre, pe_filtre, dt);
        wHArr = new ArrayList<Double>();
        pEArr = new ArrayList<Double>();
        eff = new ArrayList<>();
    }

    @Override
    public double control(double t_sensor) {
        tFiltre.mavg(t_sensor);
        double err = tSet - tFiltre.dnout[0];
        double dT = tFiltre.dnout[1];
        wH = ((1 - bhta) * p + bhta * pe - wH) / tU * dt + wH;
        double pE0 = wH - (1 - bhta) * d * dT;
        double currentEff = adap.adap(pE0, dt) + 1e-6;
        eff.add(currentEff);
        pEFiltre.tau = t_filtre / (currentEff);
        pEFiltre.mavg(pE0);
        pe = pEFiltre.dnout[0];
        double pRaw = d * err / tX - d * dT * tU / tX + pe;
        p = pRaw;
        if (p < 0) p = 0;
        if (p > 1) p = 1;
        wHArr.add(wH);
        pEArr.add(pe);
        return pRaw;
    }
}
