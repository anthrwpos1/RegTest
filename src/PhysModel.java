import java.util.Random;

public class PhysModel {
    public double initialTOut = 17;   //Cgd
    public Regulator regulator;
    private double dt = 0.1;          //sec
    private int N = 100000;           //1
    private double Q_out_room = 50;   //W/K
    private double C_room = 300000;   //J/K
    private double Q_heater_room = 30;//W/K
    private double C_heater = 500;    //J/K
    private double P_heater_max = 1500;//W
    private double t_sensor = 60;     //sec
    private double fluct_amp;
    private double fluct_time;
    private Random random;
    public double[] timeArray = new double[N];
    public double[] tSensorArray = new double[N];
    public double[] tOutArray = new double[N];

    PhysModel(Random random, Regulator regulator, double fluct_amp, double fluct_time) {
        this.random = random;
        this.regulator = regulator;
        this.fluct_amp = fluct_amp;
        this.fluct_time = fluct_time;
    }

    public void model() {
        double tOut = initialTOut;
        double tRoom = initialTOut;
        double tHeater = initialTOut;
        double tSensor = initialTOut;
        double sensorErr = 0;
        for (int i = 0; i < N; i++) {
            tOut = tOut + (random.nextDouble() - 0.5) * dt;
            tRoom = tRoom + (tHeater - tRoom) * Q_heater_room / C_room * dt + (tOut - tRoom) * Q_out_room / C_room * dt;
            tHeater = tHeater + (tRoom - tHeater) * Q_heater_room / C_heater * dt + limit(regulator.control(tSensor)) * P_heater_max / C_heater;
            sensorErr = sensorErr * (1 - dt / fluct_time) + (random.nextDouble() - 0.5) * fluct_amp * dt;
            tSensor = tSensor + (tRoom - tSensor) / t_sensor * dt + sensorErr;
            tSensorArray[i] = tSensor;
            timeArray[i] = i*dt;
            tOutArray[i] = tOut;
        }
    }

    private double limit(double in) {
        if (in < 0) return 0;
        if (in > 1) return 1;
        return in;
    }

}

