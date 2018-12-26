import Regulators.PID_Fine;
import Regulators.PhysReg;

import java.awt.*;
import java.util.Random;

/* Испытательный стенд регуляторов
 *
 * -Ανθρωπος
 */

public class Main {
    static PhysModel model1;
    static PhysModel model2;

    public static void main(String[] args) {
        int n = 100000;
        Random random = new Random();//создаем источник случайных чисел
        long seed = random.nextLong();
        random.setSeed(seed);
        PID_Fine pid = new PID_Fine(0.027, 256, 90, 0.1, 0.0001, 20);  //создаем ПИД-регулятор
        //создаем физическую модель, передаем ей регулятор.
        model1 = new PhysModel(random, pid, 0.000, 6, n) {
            @Override
            public double setTOut(double time) {
//                return super.setTOut(time);
                return functionT(time);
            }
        };
        System.out.printf("sum error model 1 = %f\n", model1.model(20, 17, 20));//моделируем, выводим отклонение
        random.setSeed(seed);
        PhysReg pr = new PhysReg(72, 85, 1.5, 0.1, 20, 0.3);
        //Создаем еще модель с тем-же генератором случайных чисел, но другим регулятором.
        model2 = new PhysModel(random, pr, 0.000, 6, n) {
            @Override
            public double setTOut(double time) {
//                return super.setTOut(time);
                return functionT(time);
            }
        };
        System.out.printf("sum error model 2 = %f\n", model2.model(20,17,20));//моделируем еще раз.
//        showProcess(10000, n, 100);
//        Diagramm.close("Figure 1");
//        Diagramm.plot(model1.timeArray,model1.tOutArray);
        Diagramm.plot(model1.timeArray, model1.tSensorArray, Color.RED);
        Diagramm.hold(true);
        Diagramm.plot(model2.timeArray, model2.tSensorArray, Color.BLACK);
    }

    static double functionT(double time) {
        if (time < 5000) return 10;
        return 0;
    }

    public static double[] subArray(double[] array, int from, int to) {
        double[] newArray = new double[to - from];
        for (int i = from; i < to; i++) {
            newArray[i - from] = array[i];
        }
        return newArray;
    }

    public static void showProcess(int pointsDisplay, int points, int step) {
        for (int i = 1; i < points - pointsDisplay; i += step) {
            int from = (i > pointsDisplay) ? i - pointsDisplay : 0;
            int to = i;
            Diagramm.figure("Figure 1");
            Diagramm.hold(false);
            Diagramm.plot(subArray(model1.timeArray, from, to), subArray(model1.tSensorArray, from, to));                  //рисуем график температуры в комнате
            Diagramm.hold(true);                                             //наложение графиков
            Diagramm.plot(subArray(model2.timeArray, from, to), subArray(model2.tSensorArray, from, to), new Color(255, 0, 0));
            Diagramm.plot(subArray(model1.timeArray, from, to), subArray(model1.tOutArray, from, to));                     //рисуем график наружной температуры
            Diagramm.setStatusText(String.format("power1 = %f, power2 = %f", model1.powArray[to], model2.powArray[to]));
//            Diagramm.figure("Figure 2");
//            Diagramm.plot(model.timeArray, model.powArray);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//class NoReg implements Regulators.Regulator{
//    public double control(double t_sensor) {
//        return 0;
//    }
//}