import java.util.Arrays;

public class MyVector {
    private final double[] vector;
    private double[] normalizeVector;
    private String group;

    public MyVector(double[] vector) {
        this.vector = vector;
        this.normalizeVector = null;
        this.group = null;
    }

    public MyVector(double[] vector, String group) {
        this.vector = vector;
        this.group = group;
    }

    public double[] getVector() {
        return vector;
    }

    public void setNormalizeVector() {
        double[] normalizeVector = new double[vector.length];
        double vectorLength = Math.sqrt(Arrays.stream(vector).map(val -> val * val).sum());
        for (int i = 0; i < vector.length; i++) {
            normalizeVector[i] = vector[i] / vectorLength;
        }
        this.normalizeVector = normalizeVector;
    }

    public double[] getNormalizeVector() {
        return normalizeVector;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Vector " + group + ": " + Arrays.toString(vector) + '\n';
    }
}
