public class Test {
    public static void main(String[] args) {
        Diagramm.plot(new double[] {1,2,3,4,5}, new double[] {9,0,1,6,4});
        Diagramm.hold(true);
        Diagramm.plot(new double[] {9,3,4}, new double[] {4,5,1});
    }
}
