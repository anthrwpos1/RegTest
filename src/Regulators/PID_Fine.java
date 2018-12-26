package Regulators;

/* Моя традиционная реализация ПИД с более умным управлением интегральной составляющей
 *
 * -Άνθρωπος
 */

public class PID_Fine implements Regulator {
    public double t_filtre; //время фильтрации
    private double t_f;
    private double dt_f;
    public double xp, ti, td, dt, tSet;     //пропорциональный коэффициент, время интегрирования и дифференцирования, шаг времени, уставка
    private double xi,state;
    private MAVG TFiltre;

    public PID_Fine(double xp, double ti, double td, double dt, double t_filtre, double tSet) {
        this.xp = xp;
        this.ti = ti;
        this.td = td;
        this.dt = dt;
        this.t_filtre = t_filtre;
        this.tSet = tSet;
        TFiltre = new MAVG(2, dt, t_filtre, true);
    }


    @Override
    public double control(double t_sensor) {
        TFiltre.mavg(t_sensor);
        t_f = TFiltre.dnout[0];
        dt_f = TFiltre.dnout[1];
        double err = tSet - t_f;
        if ((state > 0 || err > 0) && (state < 1 || err < 0)){
            xi = xi + err / xp / ti * dt;
        }
        state = err / xp - dt_f * td / xp + xi;
        return state;
    }
}
