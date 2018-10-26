public class PhysModel {
    public double initialTOut=17;   //Cgd
    private double dt=0.1;          //sec
    private int N=100000;           //1
    private double Q_out_room=50;   //W/K
    private double C_room=300000;   //J/K
    private double Q_heater_room=30;//W/K
    private double C_heater=500;    //J/K
    private double P_heater_max=1500;//W
    private double t_sensor=60;     //sec
    private double fluct_amp;
    private double fluct_time;
    public Regulator regulator;


}

