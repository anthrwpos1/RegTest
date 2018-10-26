import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;

/*  Простенький класс по рисованию графиков.

    -Άνθρωπος
 */

public class Diagramm {
    private static TreeMap<String, Figure> figures = new TreeMap<>();//окна с графиками
    private static Figure currentFigure;//активное окно

    public static void figure(String figureName) {//метод смены активного окна
        if (currentFigure == null) {
            Figure figure = new Figure(figureName);
            figures.put(figureName, figure);
            currentFigure = figure;
        } else currentFigure = searchFigureByName(figureName);
    }

    //Todo: сделать плот с цветом
    public static void plot(double[] x, double[] y) {//метод вывода графика заданного парой массивов
        if (currentFigure == null) figure("Figure 1");
        currentFigure.setPlotData(new PlotData(x, y));
    }

    public static void hold(boolean h) {//true = накладывать графики друг на друга, false - заменять старые новыми
        if (currentFigure != null) currentFigure.hold = h;
    }
    //Todo: сделать возможность осей в одном масштабе

    private static Figure searchFigureByName(String figureName) {
        Figure figure = figures.get(figureName);
        if (figure == null) {
            figure = new Figure(figureName);
            figures.put(figureName, figure);
        }
        return figure;
    }
}

class Figure extends JFrame {
    public boolean hold = false;
    private PlotPanel plotArea;
    private JPanel statusPanel;

    Figure(String figureName) {
        setTitle(figureName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addElements();
        Container pane = getContentPane();
        GroupLayout lo = new GroupLayout(pane);
        pane.setLayout(lo);
        makeLayout(lo);
        pack();
        setVisible(true);
    }

    private void addElements() {//добавление элементов окна
        statusPanel = new JPanel();
        statusPanel.setMaximumSize(new Dimension(1920, 30));
        JLabel label = new JLabel("Status string");//Todo: сделать чтобы сюда писалось ченить полезное
        statusPanel.add(label);
        plotArea = new PlotPanel();
        plotArea.setMinimumSize(new Dimension(400, 300));
        plotArea.setBackground(new Color(192, 192, 192));
    }

    private void makeLayout(GroupLayout lo) {//расположение элементов
        lo.setHorizontalGroup(lo.createParallelGroup()
                .addComponent(statusPanel)
                .addComponent(plotArea));
        lo.setVerticalGroup(lo.createSequentialGroup()
                .addComponent(statusPanel)
                .addComponent(plotArea));
    }

    public void setPlotData(PlotData pd) {//задание данных графиков
        if (hold) plotArea.addPlot(pd);
        else plotArea.setPlot(pd);
    }
}

class PlotPanel extends JPanel {//область построения
    private ArrayList<PlotData> plotDatae = new ArrayList<>();
    private Axes axes;                  //Todo: сделать возможность дополнительных осей
    private boolean autoAxes = true;    //Todo: добавить возможности масштабирования
    private int s = 15;//отступ под оси
    // Todo: назвать нормально переменные

    private Axes getLimits(PlotData pd) {//автоопределение пределов
        return new Axes(
                pd.min(pd.xArray),
                pd.max(pd.xArray),
                pd.min(pd.yArray),
                pd.max(pd.yArray));
    }

    public void setPlot(PlotData pd) {//замена графика
        plotDatae = new ArrayList<>();
        plotDatae.add(pd);
        axes = getLimits(pd);
    }

    public void addPlot(PlotData pd) {//добавление графика
        plotDatae.add(pd);
        axes.refresh(getLimits(pd));
    }

    public void paintComponent(Graphics g) {//отрисовка Todo: добавить сетку
        super.paintComponent(g);            //          Todo: переделать графику через Swing.Graphics2D
        if (plotDatae != null) {
            for (PlotData plotData : plotDatae) {
                g.setColor(plotData.lineColor);
                for (int i = 0; i < (plotData.xArray.length - 1); i++) {
                    int x1 = toScreenXTransform(plotData.xArray[i]);
                    int y1 = toScreenYTransform(plotData.yArray[i]);
                    int x2 = toScreenXTransform(plotData.xArray[i + 1]);
                    int y2 = toScreenYTransform(plotData.yArray[i + 1]);
                    g.drawLine(x1, y1, x2, y2);
                }
                g.drawLine(s, (getHeight() - s), getWidth() - s, (getHeight() - s));
                g.drawLine(getWidth() - s, (getHeight() - s), getWidth() - s - 10, (getHeight() - s) + 5);
                g.drawLine(getWidth() - s, (getHeight() - s), getWidth() - s - 10, (getHeight() - s) - 5);
                g.drawLine(s, (getHeight() - s), s, s);
                g.drawLine(s, s, s - 5, s + 10);
                g.drawLine(s, s, s + 5, s + 10);
                double[] xmarks = axes.getxMarks();
                double[] ymarks = axes.getyMarks();
                for (int i = 0; i < xmarks.length; i++) {
                    int xmark = toScreenXTransform(xmarks[i]);
                    g.drawLine(xmark, (getHeight() - s - 5), xmark, (getHeight() - s + 5));
                    g.drawString(String.valueOf(xmarks[i]), xmark, (getHeight() - s));
                }
                for (int i = 0; i < ymarks.length; i++) {
                    int ymark = toScreenYTransform(ymarks[i]);
                    g.drawLine(s - 5, ymark, s + 5, ymark);
                    g.drawString(String.valueOf(ymarks[i]), s, ymark);
                }
            }
        }
    }

    private int toScreenXTransform(double x) {
        double xmin = axes.xmin;
        double dx = axes.xmax - axes.xmin;
        return (int) ((x - xmin) / dx * (double) (getWidth() - s)) + s;
    }

    private int toScreenYTransform(double y) {
        double ymin = axes.ymin;
        double dy = axes.ymax - axes.ymin;
        return getHeight() - (int) ((y - ymin) / dy * (double) (getHeight() - s)) - s;
    }
}

class PlotData {
    public Color lineColor = new Color(0, 0, 0);
    public double[] xArray;
    public double[] yArray;
    private int plotStyle;

    PlotData(double[] xArray, double[] yArray) {
        this.xArray = xArray;
        this.yArray = yArray;
    }

    public double min(double[] array) {
        double min = Double.MAX_VALUE;
        for (double current : array) {
            if (current < min) min = current;
        }
        return min;
    }

    public double max(double[] array) {
        double max = -Double.MAX_VALUE;
        for (double current : array) {
            if (current > max) max = current;
        }
        return max;
    }
}

class Axes {
    private static final double[] mantissList = {0.75, 1.0, 1.5, 2, 2.5, 3, 4, 5, 7.5};
    private static double[] logMantissList;

    static {
        logMantissList = new double[mantissList.length];
        for (int i = 0; i < mantissList.length; i++) {
            logMantissList[i] = Math.log10(mantissList[i]);
        }
    }

    public Double xmin, xmax, ymin, ymax;
    private int xcnum = 6;
    private int ycnum = 6;
    private double[] xMarks;
    private double[] yMarks;

    Axes(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        xMarks = axisMarks(xcnum, xmin, xmax);
        yMarks = axisMarks(ycnum, ymin, ymax);
    }

    private double[] axisMarks(int cnum, double min, double max) {
        double pow = Math.floor(Math.log10((max - min) * 10 / cnum));
        double en = (Math.log10((max - min) * 10 / cnum) - pow);
        double enerr = 2;
        double curerr;
        double mantiss = 0;
        for (int i = 0; i < mantissList.length; i++) {
            if ((curerr = Math.abs(en - logMantissList[i])) < enerr) {
                enerr = curerr;
                mantiss = mantissList[i];
            }
        }
        double delay = Math.pow(10, (pow - 1)) * mantiss;
        double markmin = Math.ceil(min / delay) * delay;
        double markmax = Math.floor(max / delay) * delay;
        int marksnum = (int) Math.round((markmax - markmin) / delay);
        double[] marks = new double[marksnum];
        for (int i = 0; i < marksnum; i++) {
            marks[i] = markmin + delay * (double) i;
        }
        return marks;
    }

    public void refresh(Axes newAxes) {
        if (newAxes.xmin < xmin) xmin = newAxes.xmin;
        if (newAxes.xmax > xmax) xmax = newAxes.xmax;
        if (newAxes.ymin < ymin) ymin = newAxes.ymin;
        if (newAxes.ymax > ymax) ymax = newAxes.ymax;
        xMarks = axisMarks(xcnum, xmin, xmax);
        yMarks = axisMarks(ycnum, ymin, ymax);
    }

    public double[] getxMarks() {
        return xMarks;
    }

    public double[] getyMarks() {
        return yMarks;
    }
}