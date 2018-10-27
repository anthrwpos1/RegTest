import java.util.Random;

/*  Физическая модель помещения с нагревателем, охлаждаемым теплопотерями в окружающую среду.
 *
 *  -Άνθρωπος
 */
public class PhysModel {
    public double initialTOut = 17;   //Начальная температура, градус цельсия
    public Regulator regulator;
    private double dt = 0.1;          //шаг времени моделирования, сек.
    private int n = 100000;           //число моделируемых точек
    private double qOutRoom = 500;   //Теплопроводность стен комнаты, Вт/кельвин
    private double cRoom = 300000;   //Теплоемкость комнаты, Дж/кельвин
    private double qHeaterRoom = 30;//Теплопроводность нагревателя, Вт/кельвин
    private double cHeater = 500;    //Теплоемкость нагревателя, Дж/кельвин
    private double pHeaterMax = 1500;//Мощность нагревателя, Вт
    private double tSensor = 60;     //Инерция датчика, сек
    private double fluctAmp;           //амплитуда флуктуаций температуры измеряемой датчиком, градусов в секунду
    private double fluctTime;          //время ослабления накопленной флуктуации датчика, сек.
    private Random random;
    public double[] timeArray = new double[n];
    public double[] tSensorArray = new double[n];
    public double[] tOutArray = new double[n];
    public double[] powArray = new double[n];

    PhysModel(Random random, Regulator regulator, double fluctAmp, double fluctTime) {
        this.random = random;
        this.regulator = regulator;
        this.fluctAmp = fluctAmp;
        this.fluctTime = fluctTime;
    }

    public double model(double set) {
        double err = 0;
        double tOut = initialTOut;
        double tRoom = initialTOut;
        double tHeater = initialTOut;
        double tSensor = initialTOut;
        double pHeater;
        double sensorErr = 0;
        for (int i = 0; i < n; i++) {
            tOut = tOut + (random.nextDouble() - 0.5) * dt;
            tRoom = tRoom + (tHeater - tRoom) * qHeaterRoom / cRoom * dt + (tOut - tRoom) * qOutRoom / cRoom * dt;
            pHeater = limit(regulator.control(tSensor)) * pHeaterMax;
            tHeater = tHeater + (tRoom - tHeater) * qHeaterRoom / cHeater * dt + pHeater / cHeater;
            sensorErr = sensorErr * (1 - dt / fluctTime) + (random.nextDouble() - 0.5) * fluctAmp * dt;
            tSensor = tSensor + (tRoom - tSensor) / this.tSensor * dt + sensorErr;
            tSensorArray[i] = tSensor;
            timeArray[i] = i * dt;
            tOutArray[i] = tOut;
            powArray[i] = pHeater;
            err += Math.pow((tRoom - set),2)/n;
        }
        return Math.sqrt(err); // среднеквадратичное отклонение
    }

    private double limit(double in) {
        if (in < 0) return 0;
        if (in > 1) return 1;
        return in;
    }

}

