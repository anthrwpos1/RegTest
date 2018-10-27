// реализация скользящих средних.
public class MAVG {
    private double[] state;
    private double[] ds;
    public double[] dnout;
    private int order;
    private int compensation;
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
        double dem = dem(order);
        ds[0] = (in - state[0]) / dem;
        for (int i = 1; i < state.length; i++) {
            ds[i] = (state[i-1] - state[i]) / dem;
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

    private double dem(int order) {
        step += (step > (int) tau / dt) ? 0 : 1;
        return ((step - 1) / order + 1);
    }

}
