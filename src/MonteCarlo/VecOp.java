package MonteCarlo;

public class VecOp {
    public double[] vect;
    private int len;

    public int getLen() {
        return len;
    }

    public VecOp(double[] vect) {
        this.vect = vect;
        len = vect.length;
    }

    public void add(double x) {
        for (int i = 0; i < len; i++) {
            vect[i] = vect[i] + x;
        }
    }

    public void add(VecOp x) {
        for (int i = 0; i < len; i++) {
            vect[i] = vect[i] + x.vect[i];
        }
    }

    public void sub(VecOp x) {
        for (int i = 0; i < len; i++) {
            vect[i] = vect[i] - x.vect[i];
        }
    }

    public void mul(double x) {
        for (int i = 0; i < len; i++) {
            vect[i] = vect[i] * x;
        }
    }

    public void mul(VecOp x) {
        for (int i = 0; i < len; i++) {
            vect[i] = vect[i] * x.vect[i];
        }
    }

    public void div(VecOp x) {
        for (int i = 0; i < len; i++) {
            vect[i] = vect[i] / x.vect[i];
        }
    }

    public VecOp clone (){
        return new VecOp(vect.clone());
    }

    public double sum(){
        double result = 0;
        for (double v : vect) {
            result += v;
        }
        return result;
    }
}
