package MonteCarlo;

import java.util.Random;

/* Метод Монте-Карло для нахождения оптимальных параметров
 *
 * -Άνθρωπος
 */
public class MonteCarlo {
    private double errMin;
    private Random r;
    private VecOp bestArg;
    private VecOp factor;
    private double dec = 1;
    private Functional error;

    public MonteCarlo(Random r, VecOp factor, Functional error) {
        this.r = r;//Ссылка на герератор случайных чисел
        this.factor = factor;//число в районе которого возможна смена знака
        this.error = error;//функционал ошибки
    }

    public VecOp runMethod(int iterMax, double errMax, VecOp initValue, double errDecay) {
        int len = initValue.getLen();
        bestArg = initValue;
        errMin = error.functional(initValue);
        int iter = 0;
        VecOp add = new VecOp(new double[len]);
        VecOp mul = new VecOp(new double[len]);
        VecOp testArg;
        double err;
        while (iter < iterMax && errMin > errMax) {
            for (int i = 0; i < add.getLen(); i++) {
                add.vect[i] = (r.nextDouble() - 0.5) / dec;
                mul.vect[i] = ((r.nextDouble() * 2 - 1) / dec) + 1;
            }
            add.mul(factor);
            testArg = bestArg.clone();
            testArg.mul(mul);
            testArg.add(add);
            err = error.functional(testArg);
            dec++;
            errMin = errMin * errDecay;
            if (err < errMin) {
                errMin = err;
                bestArg = testArg;
                dec = 1;
                showprocess(bestArg, errMin, iter);
                //System.out.printf("V = I*%f+%f\nerror = %f, iter = %d\n",bestArg.vect[0],bestArg.vect[1],errMin,iter);
            }
            iter++;
        }
        System.out.println("errMin = " + errMin);
        return bestArg;
    }

    public void showprocess(VecOp arg, double error,int iter) {
    }
}

