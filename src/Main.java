import common.GraphHelper;
import common.CustomGraphInput;
import common.JGraphTInput;
import common.Output;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GraphHelper graphHelper = new GraphHelper();

        System.out.println("=== Shortest Path Algorithm Runner ===\n");

        // Step 1: Choose graph implementation
        System.out.println("Select graph implementation:");
        System.out.println("  1. Custom Graph Class");
        System.out.println("  2. JGraphT Library");
        int graphImpl = readInt(1, 2);

        // Step 2: Choose graph source
        System.out.println("\nSelect graph source:");
        System.out.println("  1. Artificial (generated)");
        System.out.println("  2. Real-life (SNAP dataset)");
        int graphSource = readInt(1, 2);

        // Step 3: Choose algorithm (same prompt regardless of implementation)
        int algorithmChoice = readAlgorithmChoice();

        if (graphImpl == 1) {
            runCustomGraph(graphHelper, graphSource, algorithmChoice);
        } else {
            runJGraphT(graphHelper, graphSource, algorithmChoice);
        }

        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Algorithm selection (shared)
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
    // Custom Graph Class
    // -------------------------------------------------------------------------

    private static void runCustomGraph(GraphHelper graphHelper, int graphSource, int algorithmChoice) {
        CustomGraphInput input = buildCustomInput(graphHelper, graphSource);
        Output output;
        Instant start, finish;

        switch (algorithmChoice) {
        case 1 -> {
            algorithms.graph.DijkstraBinaryHeap dbh = new algorithms.graph.DijkstraBinaryHeap();
            dbh.dijkstraBinaryHeap(input); // warmup
            start = Instant.now();
            output = dbh.dijkstraBinaryHeap(input);
            finish = Instant.now();
        }
        case 2 -> {
            algorithms.graph.DijkstraFibonacciHeap dfh = new algorithms.graph.DijkstraFibonacciHeap();
            dfh.dijkstraFibonacciHeap(input); // warmup
            start = Instant.now();
            output = dfh.dijkstraFibonacciHeap(input);
            finish = Instant.now();
        }
        case 3 -> {
            algorithms.graph.BellmanFord bf = new algorithms.graph.BellmanFord();
            bf.bellmanFord(input); // warmup
            start = Instant.now();
            output = bf.bellmanFord(input);
            finish = Instant.now();
        }
        case 4 -> {
            algorithms.graph.Johnson j = new algorithms.graph.Johnson();
            j.johnson(input); // warmup
            start = Instant.now();
            output = j.johnson(input);
            finish = Instant.now();
        }
        default -> {
            algorithms.graph.FloydWarshall fw = new algorithms.graph.FloydWarshall();
            fw.floydWarshall(input); // warmup
            start = Instant.now();
            output = fw.floydWarshall(input);
            finish = Instant.now();
        }
        }

        printResults(output, algorithmName(algorithmChoice), Duration.between(start, finish).toMillis());
    }

    private static CustomGraphInput buildCustomInput(GraphHelper graphHelper, int graphSource) {
        if (graphSource == 1) {
            double[] params = readArtificialParams();
            int numOfNodes = (int) params[0];
            double density = params[1];
            int seed = (int) params[2];
            System.out.printf("%nGenerating artificial graph: %d nodes, %.4f density, seed %d%n", numOfNodes, density, seed);
            int[] srcAndTarget = graphHelper.srcTargetGenerator(numOfNodes, seed);
            return new CustomGraphInput(srcAndTarget[0], srcAndTarget[1], graphHelper.generateCustomGraph(numOfNodes, density, seed));
        } else {
            String filePath = readFilePath();
            System.out.println("Loading SNAP graph from: " + filePath);
            throw new UnsupportedOperationException("SNAP loading not yet implemented for custom graph class.");
        }
    }

    // -------------------------------------------------------------------------
    // JGraphT
    // -------------------------------------------------------------------------

    private static void runJGraphT(GraphHelper graphHelper, int graphSource, int algorithmChoice) {
        JGraphTInput input = buildJGraphTInput(graphHelper, graphSource);
        Output output;
        Instant start, finish;

        switch (algorithmChoice) {
        case 1 -> {
            algorithms.jgpraht.DijkstraBinaryHeap dbh = new algorithms.jgpraht.DijkstraBinaryHeap();
            dbh.dijkstraBinaryHeap(input); // warmup
            start = Instant.now();
            output = dbh.dijkstraBinaryHeap(input);
            finish = Instant.now();
        }
        case 2 -> {
            algorithms.jgpraht.DijkstraFibonacciHeap dfh = new algorithms.jgpraht.DijkstraFibonacciHeap();
            dfh.dijkstraFibonacciHeap(input); // warmup
            start = Instant.now();
            output = dfh.dijkstraFibonacciHeap(input);
            finish = Instant.now();
        }
        case 3 -> {
            algorithms.jgpraht.BellmanFord bf = new algorithms.jgpraht.BellmanFord();
            bf.bellmanFord(input); // warmup
            start = Instant.now();
            output = bf.bellmanFord(input);
            finish = Instant.now();
        }
        case 4 -> {
            algorithms.jgpraht.Johnson j = new algorithms.jgpraht.Johnson();
            j.johnson(input); // warmup
            start = Instant.now();
            output = j.johnson(input);
            finish = Instant.now();
        }
        default -> {
            algorithms.jgpraht.FloydWarshall fw = new algorithms.jgpraht.FloydWarshall();
            fw.floydWarshall(input); // warmup
            start = Instant.now();
            output = fw.floydWarshall(input);
            finish = Instant.now();
        }
        }

        printResults(output, algorithmName(algorithmChoice), Duration.between(start, finish).toMillis());
    }

    private static JGraphTInput buildJGraphTInput(GraphHelper graphHelper, int graphSource) {
        if (graphSource == 1) {
            double[] params = readArtificialParams();
            int numOfNodes = (int) params[0];
            double density = params[1];
            int seed = (int) params[2];
            System.out.printf("%nGenerating artificial graph: %d nodes, %.4f density, seed %d%n", numOfNodes, density, seed);
            int[] srcAndTarget = GraphHelper.srcTargetGenerator(numOfNodes, seed);
            return new JGraphTInput(srcAndTarget[0], srcAndTarget[1], graphHelper.generateGraph(numOfNodes, density, seed));
        } else {
            String filePath = readFilePath();
            System.out.println("Loading SNAP graph from: " + filePath);
            return graphHelper.generateSNAPGraph(filePath, 1);
        }
    }

    // -------------------------------------------------------------------------
    // Shared print
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
        System.out.print("Density (0.0 to 1.0): ");
        double density = readDouble(0.0, 1.0);
        System.out.print("Seed: ");
        int seed = readInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new double[]{nodes, density, seed};
    }

    private static String readFilePath() {
        System.out.println("\nAvailable SNAP datasets:");
        System.out.println("  1. resources/social-networks/facebook_combined.txt");
        System.out.println("  2. resources/internet-p2p-networks/p2p-Gnutella04.txt");
        System.out.println("  3. resources/road-networks/roadNet-TX.txt");
        System.out.println("  4. resources/autonomous-systems/as20000102.txt");
        System.out.println("  5. Enter custom path");
        int choice = readInt(1, 5);
        return switch (choice) {
            case 1 -> "resources/social-networks/facebook_combined.txt";
            case 2 -> "resources/internet-p2p-networks/p2p-Gnutella04.txt";
            case 3 -> "resources/road-networks/roadNet-TX.txt";
            case 4 -> "resources/autonomous-systems/as20000102.txt";
            default -> { System.out.print("Enter path: "); yield scanner.nextLine().trim(); }
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