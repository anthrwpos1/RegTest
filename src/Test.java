import Regulators.PhysReg;

import java.awt.*;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        int n = 10000;
        double dt = 0.1;
        double[] wH;
        double[] pE;
        Random r = new Random();
        PhysReg reg = new PhysReg(200, 17, 100, 0.1, 20, 60);
        reg.bhta = 500/300500;
        PhysModel pm = new PhysModel(r, reg, 0, 1, n);
        pm.model(20,17,20);
        wH = reg.wHArr.stream().mapToDouble(d -> d*1500).toArray();
        pE = reg.pEArr.stream().mapToDouble(d -> d*1500).toArray();
        Diagramm.plot(pm.timeArray, pE, Color.RED);
        Diagramm.hold(true);
        Diagramm.plot(pm.timeArray,wH, Color.GREEN);
        Diagramm.plot(pm.timeArray,pm.powArray,Color.CYAN);
        Diagramm.plot(pm.timeArray,pm.wHArray, new Color(192,255,192));
        Diagramm.plot(pm.timeArray,pm.pEArray, new Color(255,192,192));
    }
}
