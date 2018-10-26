import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random r = new Random();
        PhysModel pm = new PhysModel(r,new NoReg());
        pm.model();
        Diagramm.plot(pm.timeArray, pm.tOutArray);
        Diagramm.hold(true);
        Diagramm.plot(pm.timeArray,pm.tSensorArray);
    }
}

class NoReg implements Regulator{
    public double control(double t_sensor) {
        return 0;
    }
}