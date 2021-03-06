package Regulators;

/* реализация скользящих средних.
 *
 * -Άνθρωπος
 */
public class MAVG {
    private double[] state;     //вектор внутреннего состояния
    private double[] ds;        //вектор добавок
    public double[] dnout;      //n-я производная фильтруемой величины. dnout[0] - "нулевая производная" - сама отфильтрованная величина
    private int order;          //порядок скользящей средней
    private int compensation;   //компенсация тренда?
    public double dt;
    public double tau;
    int step = 0;

    MAVG(int order, double dt, double tau, boolean compensation) {
        this.compensation = (compensation) ? 1 : 0;
        this.order = order + this.compensation;
        this.dt = dt;
        this.tau = tau;
        state = new double[this.order];
        ds = new double[this.order];
        dnout = new double[this.order];
        if (order > 4) System.out.println("Warning: Moving Average order > 4 is not recommended.");
    }

    public void mavg(double in) {
        if (step == 0){
            for (int i = 0; i < state.length; i++) {
                state[i] = in;
            }
        }
        double dem = dem(order);
        ds[0] = (in - state[0]) / dem;
        for (int i = 1; i < state.length; i++) {
            ds[i] = (state[i - 1] - state[i]) / dem;
        }
        for (int i = 0; i < state.length; i++) {
            state[i] = state[i] + ds[i];
        }
        dnout[0] = state[order - 1];
        for (int i = 1; i < order; i++) {
            dnout[i] = ds[order - i] / dt;
        }
        for (int m = 1; m < order - 1; m++) {
            for (int i = m + 1; i < order; i++) {
                dnout[order + m - i] = (dnout[order + m - i] - dnout[order + m - 1 - i]) / dem / dt;
            }
        }
        if (compensation == 1) {
            double tav = ((double) step - 1) * dt;
            for (int i = 0; i < order - 1; i++) {
                dnout[i] = dnout[i] + dnout[i + 1] * tav;
            }
        }
    }

    private void mavg1(double in){
        double dem = dem(order);
        ds[0] = (in - state[0]) / dem;
        state[0] = state[0] + ds[0];
        dnout[0] = state[0];
    }

    private void mavg2(double in){
        double dem = dem(order);
        ds[0] = (in - state[0]) / dem;
        ds[1] = (state[0] - state[1]) / dem;
        state[0] = state[0] + ds[0];
        state[1] = state[1] + ds[1];
        dnout[0] = state[1];
        dnout[1] = ds[0] / dt;
    }

    private double dem(int order) {
        int stepmax = (int) (tau / dt);
        step = (step > stepmax) ? stepmax : step+1;
        return ((step - 1) / order) + 1;
    }

}
