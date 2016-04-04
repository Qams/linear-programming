package pl.edu.agh;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ProblemParser problemParser = new ProblemParser("output.json", "third.json");
        problemParser.generateJSONDualProblem();

    }
}
