import Regulators.Regulator;

import java.util.Random;

/*  Физическая модель помещения с нагревателем, охлаждаемым теплопотерями в окружающую среду.
 *
 *  -Άνθρωπος
 */
public class PhysModel {
    public double initialTOut = 20;   //Начальная температура, градус цельсия
    public Regulator regulator;
    private double dt = 0.1;          //шаг времени моделирования, сек.
    private int n;           //число моделируемых точек
    private double qOutRoom = 50;   //Теплопроводность стен комнаты, Вт/кельвин
    private double cRoom = 900000;   //Теплоемкость комнаты, Дж/кельвин
    private double qHeaterRoom = 30;//Теплопроводность нагревателя, Вт/кельвин
    private double cHeater = 3600;    //Теплоемкость нагревателя, Дж/кельвин
    private double pHeaterMax = 1500;//Мощность нагревателя, Вт
    private double tSensor = 6;     //Инерция датчика, сек
    private double fluctAmp;           //амплитуда флуктуаций температуры измеряемой датчиком, градусов в секунду
    private double fluctTime;          //время ослабления накопленной флуктуации датчика, сек.
    private Random random;
    public double[] timeArray;
    public double[] tSensorArray;
    public double[] tRoomArray;
    public double[] tOutArray;
    public double[] powArray;
    public double[] pEArray;
    public double[] wHArray;
    private double tout;

    PhysModel(Random random, Regulator regulator, double fluctAmp, double fluctTime, int n) {
        this.n = n;
        this.random = random;
        this.regulator = regulator;
        this.fluctAmp = fluctAmp;
        this.fluctTime = fluctTime;
        timeArray = new double[n];
        tSensorArray = new double[n];
        tRoomArray = new double[n];
        tOutArray = new double[n];
        powArray = new double[n];
        pEArray = new double[n];
        wHArray = new double[n];
    }

    public double model(double set, double initialOut, double initialIn) {
        double err = 0;
        tout = initialOut;
        double tOut;
        double tRoom = initialIn;
        double tHeater = initialIn;
        double tSensor = initialIn;
        double pHeater;
        double sensorErr = 0;
        double dSense0, dSense1, dSense2;
        double sens0=initialIn, sens1=initialIn, sens2=initialIn;
        for (int i = 0; i < n; i++) {
            tOut = setTOut((double) i * dt);
            tRoom = tRoom + (tHeater - tRoom) * qHeaterRoom / cRoom * dt + (tOut - tRoom) * qOutRoom / cRoom * dt;
            pHeater = limit(regulator.control(tSensor)) * pHeaterMax;
            tHeater = tHeater + (tRoom - tHeater) * qHeaterRoom / cHeater * dt + pHeater / cHeater * dt;
            sensorErr = sensorErr * (1 - dt / fluctTime) + (random.nextDouble() - 0.5) * fluctAmp * dt;
            dSense0 = (tRoom - sens0) / this.tSensor * dt;
            dSense1 = (sens0 - tSensor) / this.tSensor * dt;
//            dSense2 = (sens1 - tSensor) / this.tSensor * dt;
            sens0 += dSense0;
//            sens1 += dSense1;
//            sens2 += dSense2;
            tSensor += dSense1 + sensorErr;
            tSensorArray[i] = tSensor;
            tRoomArray[i] = tRoom;
            timeArray[i] = i * dt;
            tOutArray[i] = tOut;
            powArray[i] = pHeater;
            pEArray[i] = (tRoom - tOut) * qOutRoom;
            wHArray[i] = (tHeater - tRoom) * qHeaterRoom;
            err += Math.pow((tRoom - set),2)/n;
        }
        return Math.sqrt(err); // среднеквадратичное отклонение
    }

    private double limit(double in) {
        if (in < 0) return 0;
        if (in > 1) return 1;
        return in;
    }

    public double setTOut(double time){
        tout = tout + (random.nextDouble() - 0.5) * dt;
        return tout;
    }
}

