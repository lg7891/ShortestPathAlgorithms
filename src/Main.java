import algorithms.BellmanFord;
import algorithms.DijkstraBinaryHeap;
import algorithms.DijkstraFibonacciHeap;
import algorithms.FloydWarshall;
import algorithms.Johnson;
import common.GraphHelper;
import common.Input;
import common.Output;
import common.OutputVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GraphHelper graphHelper = new GraphHelper();

        System.out.println("=== Shortest Path Algorithm Runner ===\n");

        // Step 1: Choose graph source
        System.out.println("Select graph source:");
        System.out.println("  1. Artificial (generated)");
        System.out.println("  2. Real-life (SNAP dataset)");
        int graphSource = readInt(1, 2);

        // Step 2: Choose algorithm
        int algorithmChoice = readAlgorithmChoice();

        runCustomGraph(graphHelper, graphSource, algorithmChoice);

        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Algorithm selection
    // -------------------------------------------------------------------------

    private static int readAlgorithmChoice() {
        System.out.println("\nSelect algorithm:");
        System.out.println("  1. Dijkstra Binary Heap");
        System.out.println("  2. Dijkstra Fibonacci Heap");
        System.out.println("  3. Bellman-Ford");
        System.out.println("  4. Johnson");
        System.out.println("  5. Floyd-Warshall");
        return readInt(1, 5);
    }

    private static String algorithmName(int choice) {
        return switch (choice) {
            case 1 -> "Dijkstra Binary Heap";
            case 2 -> "Dijkstra Fibonacci Heap";
            case 3 -> "Bellman-Ford";
            case 4 -> "Johnson";
            default -> "Floyd-Warshall";
        };
    }

    // -------------------------------------------------------------------------
    // Custom Graph Execution
    // -------------------------------------------------------------------------

    private static void runCustomGraph(GraphHelper graphHelper, int graphSource, int algorithmChoice) {
        Input input = buildCustomInput(graphHelper, graphSource);
        Output output;
        Instant start, finish;

        switch (algorithmChoice) {
        case 1 -> {
            DijkstraBinaryHeap dbh = new DijkstraBinaryHeap();
            dbh.dijkstraBinaryHeap(input); // warmup
            start = Instant.now();
            output = dbh.dijkstraBinaryHeap(input);
            finish = Instant.now();
        }
        case 2 -> {
            DijkstraFibonacciHeap dfh = new DijkstraFibonacciHeap();
            dfh.dijkstraFibonacciHeap(input); // warmup
            start = Instant.now();
            output = dfh.dijkstraFibonacciHeap(input);
            finish = Instant.now();
        }
        case 3 -> {
            BellmanFord bf = new BellmanFord();
            bf.bellmanFord(input); // warmup
            start = Instant.now();
            output = bf.bellmanFord(input);
            finish = Instant.now();
        }
        case 4 -> {
            Johnson j = new Johnson();
            j.johnson(input); // warmup
            start = Instant.now();
            output = j.johnson(input);
            finish = Instant.now();
        }
        default -> {
            FloydWarshall fw = new FloydWarshall();
            fw.floydWarshall(input); // warmup
            start = Instant.now();
            output = fw.floydWarshall(input);
            finish = Instant.now();
        }
        }

        long timeMs = Duration.between(start, finish).toMillis();

        printResults(output, algorithmName(algorithmChoice), timeMs);

        // Verify correctness
        boolean isValid = OutputVerifier.verifyOutput(input.getGraph(), output);

        System.out.println("\nVerification: " + (isValid ? "PASSED ✅" : "FAILED ❌"));    }

    private static Input buildCustomInput(GraphHelper graphHelper, int graphSource) {
        if (graphSource == 1) {
            double[] params = readArtificialParams();
            int numOfNodes = (int) params[0];
            double density = params[1];
            int seed = (int) params[2];

            System.out.printf("%nGenerating artificial graph: %d nodes, %.2f%% density, seed %d%n",
                    numOfNodes, density * 100, seed);

            int[] srcAndTarget = graphHelper.srcTargetGenerator(numOfNodes, seed);

            return new Input(
                    srcAndTarget[0],
                    srcAndTarget[1],
                    graphHelper.generateGraph(numOfNodes, density, seed)
            );
        } else {
            String filePath = readFilePath();
            System.out.println("Loading SNAP graph from: " + filePath);
            return graphHelper.generateSNAPGraph(filePath, graphSource);
        }
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private static void printResults(Output output, String name, long ms) {
        System.out.println("\n--- Results (" + name + ") ---");
        System.out.println("Execution time : " + ms + " ms");
        System.out.println("Shortest path  : " + output.getShortestPath());
        System.out.println("Total price    : " + output.getTotalPathPrice());
    }

    // -------------------------------------------------------------------------
    // Input helpers
    // -------------------------------------------------------------------------

    private static double[] readArtificialParams() {
        System.out.print("\nNumber of nodes: ");
        int nodes = readInt(1, Integer.MAX_VALUE);

        System.out.print("Density (0 to 100 %): ");
        double densityPercent = readDouble(0.0, 100.0);
        double density = densityPercent / 100.0;

        System.out.print("Seed: ");
        int seed = readInt(Integer.MIN_VALUE, Integer.MAX_VALUE);

        return new double[]{nodes, density, seed};
    }

    private static String readFilePath() {
        System.out.println("\nAvailable SNAP datasets:");
        System.out.println("  1. resources/social-networks/facebook_combined.txt");
        System.out.println("  2. resources/internet-p2p-networks/p2p-Gnutella04.txt");
        System.out.println("  3. resources/road-networks/roadNet-TX.txt");
        System.out.println("  4. resources/road-networks/roadNet-CA.txt");
        System.out.println("  5. resources/road-networks/roadNet-PA.txt");
        System.out.println("  6. resources/autonomous-systems/as20000102.txt");
        System.out.println("  7. Enter custom path");

        int choice = readInt(1, 7);

        return switch (choice) {
            case 1 -> "resources/social-networks/facebook_combined.txt";
            case 2 -> "resources/internet-p2p-networks/p2p-Gnutella04.txt";
            case 3 -> "resources/road-networks/roadNet-TX.txt";
            case 4 -> "resources/road-networks/roadNet-CA.txt";
            case 5 -> "resources/road-networks/roadNet-PA.txt";
            case 6 -> "resources/autonomous-systems/as20000102.txt";
            default -> {
                System.out.print("Enter path: ");
                yield scanner.nextLine().trim();
            }
        };
    }

    private static double readDouble(double min, double max) {
        while (true) {
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("Please enter a value between %.1f and %.1f: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Try again: ");
            }
        }
    }

    private static int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Try again: ");
            }
        }
    }
}