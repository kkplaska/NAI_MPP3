import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Operator {
    private List<Path> pathsToTrainFiles = new ArrayList<>();
    private final List<MyVector> testVectors = new ArrayList<>();
    private Map<String, Perceptron> perceptrons = new HashMap<>();

    public Operator(double alpha, int epochs) {
        this.loadPathsToTrainFiles();
        this.loadVectors();
        this.loadPerceptrons();
        perceptrons.forEach((k, v) -> v.learnByDataset(this.testVectors, alpha, epochs));

    }

    private void loadPathsToTrainFiles() {
        try(Stream<Path> pathStream = Files.walk(Paths.get("resources"))){
            pathsToTrainFiles = pathStream.filter(path -> path.toString()
                    .endsWith(".txt"))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public List<String> getLanguages(){
        if (pathsToTrainFiles.isEmpty()){
            return null;
        }
        return pathsToTrainFiles.stream()
                .map(Path::getParent)
                .map(Path::getFileName).distinct()
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    public void loadPerceptrons(){
        HashMap<String, Perceptron> perceptrons = new HashMap<>();
        this.getLanguages().forEach(lang -> perceptrons.put(lang, new Perceptron(26, lang)));
        this.perceptrons = perceptrons;
    }

    public void loadVectors(){
        this.pathsToTrainFiles.forEach(path -> this.testVectors.add(new MyVector(
                    getCharMapOfFile(path).values().stream().mapToDouble(val -> val).toArray(),
                    path.getParent().getFileName().toString()
        )));
    }

    public void recognizeAndSetGroup(MyVector vector){
        Map<String, Double> activationValues = this.perceptrons.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> pair.getValue().calculateNormalizeWeights(vector.getNormalizeVector())
        ));

        Map.Entry<String, Double> activationValue = activationValues.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(activationValues.entrySet().iterator().next());
                // Jeżeli nie znajdzie maxa, bierze pierwszą możliwą wartość
        vector.setGroup(activationValue.getKey());
    }

    public static Map<Character, Double> getCharMap(String inputString){
        inputString = inputString.replaceAll("[^A-Za-z]", "").toLowerCase();
        int strLength = inputString.length();

        Map<Character, Double> charMap = inputString.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(
                Function.identity(), // to samo c -> c
                Collectors.counting()
        )).entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> (double) e.getValue() / (double) strLength
        ));

        for (int i = 'a'; i <= 'z'; i++) {
            charMap.putIfAbsent((char) i, 0.0);
        }

        return charMap;
    }

    public static Map<Character, Double> getCharMapOfFile(Path pathToFile){
        try {
            return getCharMap(Files.readString(pathToFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
