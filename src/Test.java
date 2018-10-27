import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random random = new Random();//создаем источник случайных чисел
        PID pid = new PID(3, 900, 60, 0.1, 60, 20);  //создаем ПИД-регулятор
        PhysModel model = new PhysModel(random, pid, 0.001, 60);   //создаем физическую модель, передаем ей регулятор.
        model.model();//моделируем
        int pointsDisplay = 10000;
        for (int i = 0; i < 100000 - pointsDisplay; i += 100) {
            int from = (i > pointsDisplay) ? i : 0;
            int to = (i > pointsDisplay) ? i + pointsDisplay : i;
            Diagramm.figure("Figure 1");
            Diagramm.hold(false);
            Diagramm.plot(subArray(model.timeArray, from, to), subArray(model.tSensorArray, from, to));                  //рисуем график температуры в комнате
            Diagramm.hold(true);                                             //наложение графиков
            Diagramm.plot(subArray(model.timeArray, from, to), subArray(model.tOutArray, from, to));                     //рисуем график наружной температуры
            Diagramm.setStatusText(String.format("power = %f", model.powArray[to]));
//            Diagramm.figure("Figure 2");
//            Diagramm.plot(model.timeArray, model.powArray);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Diagramm.close("Figure 1");
    }

    public static double[] subArray(double[] array, int from, int to) {
        double[] newArray = new double[to - from];
        for (int i = from; i < to; i++) {
            newArray[i - from] = array[i];
        }
        return newArray;
    }
}

//class NoReg implements Regulator{
//    public double control(double t_sensor) {
//        return 0;
//    }
//}