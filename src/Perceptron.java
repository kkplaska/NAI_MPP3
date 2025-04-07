import java.util.Arrays;
import java.util.List;

public class Perceptron {
    private final MyVector weights;
    private double threshold;
    private final String group;

    public Perceptron(int dimension, String group) {
        this(new double[dimension], 0.0, group);
    }

    public Perceptron(double[] weights, double threshold, String group) {
        this.weights = new MyVector(weights, "Perceptron");
        this.threshold = threshold;
        this.group = group;
    }

    private int getPrediction(double[] weights, double[] input, double threshold){
        return calculate(weights, input, threshold) >= 0 ? 1 : 0; // 1 - jeśli zgadza się grupa
        // 0 - jeśli grupa się nie zgadza
    }

    private double calculate(double[] weights, double[] input, double threshold) {
        double sum = -threshold;
        for (int i = 0; i < input.length; i++){
            sum += input[i] * weights[i];
        }
        return sum;
    }

    public double calculateNormalizeWeights(double[] inputVector){
        return calculate(this.weights.getNormalizeVector(), inputVector, this.threshold);
    }

    private void learn(double[] inputVector, int decision, double alpha){
        int prediction = getPrediction(this.weights.getVector(), inputVector, this.threshold);
        int error = decision - prediction; // {0, 1}
        if(error != 0 && inputVector.length == weights.getVector().length){
            for (int i = 0; i < inputVector.length; i++) {
                weights.getVector()[i] += error * inputVector[i] * alpha;
            }
            threshold -= error * alpha;
        }
    }

    public void learnByDataset(List<MyVector> inputList, double alpha, int howManyTimes){
        for (int i = 0; i < howManyTimes; i++) {
            inputList.forEach(vec ->
                learn(
                    vec.getVector(),
                    vec.getGroup().equals(this.group) ? 1 : 0,
                    alpha
                )
            );
        }
        this.weights.setNormalizeVector();
    }

    @Override
    public String toString() {
        return "Perceptron{" +
                "group='" + group +
                "', weights=" + Arrays.toString(weights.getVector()) +
                ", threshold=" + threshold +
                "}\n";
    }
}
