import MonteCarlo.Functional;
import MonteCarlo.*;
import MonteCarlo.VecOp;
import Regulators.PID_Fine;
import Regulators.PhysReg;

import java.util.Random;

/*  Здесь ищем оптимальные пераметры регуляторов
 *
 */

public class OptiFind {
    public static void main(String[] args) {
        Random r = new Random();//создаем генератор случайных чисел
        double dt = 0.1;        //квант времени моделирования (должен совпадать с таковым для физмодели)
        double tSet = 20;       //уставка
        Functional err = new Functional() {//Вычисление функционала ошибки, который минимизирует метод
            @Override
            public double functional(VecOp function) {
                double[] param = function.vect; //создаем вектор параметров, которые хотим подогнать
//                double xp = param[0];
//                double tI = param[1];
//                double tD = param[2];
//                double tF = param[3];
//                PID_Fine p = new PID_Fine(xp, tI, tD, dt, tF, tSet);
                double d = param[0];
                double tU = param[1];           //получаем сами параметры
                double tX = param[2];
                double bhta = param[3];
                PhysReg p = new PhysReg(d, tU, tX, dt, tSet, 1, tX);   //создаем регулятор с тестовыми параметрами
                p.bhta = bhta;
                PhysModel model = new PhysModel(r, p, 0.0001, 6,30000){
                    @Override
                    public double setTOut(double time) {
//                        return super.setTOut(time);
                        if (time < 1500) return 10;
                        return 0;
                    }
                }; //создаем физмодель, передаем ей регулятор
                return model.model(tSet,17,20);       //моделируем, получаем среднеквадратичное отклонение от уставки
            }
        };
        MonteCarlo mc = new MonteCarlo(r, new VecOp(new double[]{0, 0, 0, 0}), err) {
            @Override
            public void showprocess(VecOp arg, double err, int iter) {
                System.out.printf("D = %f, tU = %f, tX = %f, tF = %f, err = %f on iter = %d\n",
                        arg.vect[0], arg.vect[1], arg.vect[2], arg.vect[3], err, iter);
//                System.out.printf("xP = %f, tI = %f, tD = %f, tF = %f, err = %f on iter = %d\n",
//                        arg.vect[0], arg.vect[1], arg.vect[2], arg.vect[3], err, iter);
            }
        };//подгоняем
        mc.runMethod(10000, 0, new VecOp(new double[]{60, 16, 60, 1}), 1.001);//итог работы
//        mc.runMethod(10000, 0, new VecOp(new double[]{1, 100, 16, 1}), 1.001);//итог работы
    }
}
