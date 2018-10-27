import java.util.Random;

public class Test {
    public static void main(String[] args) {
        MAVG mavg = new MAVG(3,0.1,30,true);
        int N = 2000;
        double[] t = new double[N];
        double[] y = new double[N];
        for (int i=0; i<N; i++){
            t[i] = i/10;
            if (t[i]<30) mavg.mavg(0);
            else mavg.mavg(1);
            y[i] = mavg.dnout[0];
        }
        Diagramm.plot(t,y);
    }
}

class NoReg implements Regulator{
    public double control(double t_sensor) {
        return 0;
    }
}