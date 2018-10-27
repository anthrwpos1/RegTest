/* Простой "тупой" ПИД-регулятор
 * Такой алгоритм используется почти повсеместно.
 *
 * -Άνθρωπος
 */

public class PID implements Regulator {
    public double t_filtre; //время фильтрации
    private double t_f;
    private double dt_f;
    private double d2t_f;
    public double xp, ti, td, dt, tSet;     //пропорциональный коэффициент, время интегрирования и дифференцирования, шаг времени, уставка
    private double state;
    private MAVG mavg;

    PID(double xp, double ti, double td, double dt, double t_filtre, double tSet) {
        this.xp = xp;
        this.ti = ti;
        this.td = td;
        this.dt = dt;
        this.t_filtre = t_filtre;
        this.tSet = tSet;
        mavg = new MAVG(3, dt, t_filtre, true);
    }

    @Override
    public double control(double t_sensor) {
        mavg.mavg(t_sensor);
        t_f = mavg.dnout[0];
        double err = tSet - t_f;
        dt_f = mavg.dnout[1];
        d2t_f = mavg.dnout[2];
        state = state + (err / ti - dt_f - d2t_f * td) / xp;
        return state;
    }
}