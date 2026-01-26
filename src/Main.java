import algorithms.*;
import common.GraphHelper;
import common.Input;
import common.Output;
import common.OutputVerifier;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {

        // Initialize graph helper
        GraphHelper graphHelper = new GraphHelper();
        Input input;

        if (args.length > 0) {
            /**
             *  Option 1: Artificially generated graph
             *  Uses number program arguments (number of nodes, number of edges) as input to generate
             *  a weighted graph.
             */
            input = getArtificialInput(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            /**
             *  Option 2: Graph from real life
             *  Set file path to SNAP file. Generator provides the required Input all data is retrieved from the file.
             */
//            input = getSNAPInput("resources/social-networks/facebook_combined.txt");
            input = getSNAPInput("resources/roadNet-CA.txt");
        }

        /**
         *  Print basic information about the graph (number of nodes, number of edges,
         *  list of nodes and edges, connections)
         */
        graphHelper.printBasicInfo(input.getGraph());

        runTests(
                input,
                true, // Run Dijsktra BH
                true, // Run Dijkstra FH
                false, // Run Bellman-Ford
                false, // Run Johnson
                false  // Run Floyd-Warshall
        );
    }

    private static Input getSNAPInput(String filePath) {
        GraphHelper graphHelper = new GraphHelper();
        return graphHelper.generateSNAPGraph(filePath);
    }

    private static Input getArtificialInput(int numOfNodes, int numOfEdges) {
        GraphHelper graphHelper = new GraphHelper();
        return new Input(0, numOfNodes - 1, graphHelper.generateGraph(numOfNodes, numOfEdges));
    }

    private static void runTests (Input input, boolean dbh, boolean dfh, boolean bf, boolean j, boolean fw) {

        Graph<Integer, DefaultWeightedEdge> graph = input.getGraph();

        if (dbh) {
            System.out.println("----------------------------- Running Dijkstra BH ---------------------------");
            DijkstraBinaryHeap dijkstraBH = new DijkstraBinaryHeap();
            dijkstraBH.dijkstraBinaryHeap(input);
            Instant start = Instant.now();
            Output dijskstraBHOutput = dijkstraBH.dijkstraBinaryHeap(input);
            Instant finish = Instant.now();
            long dijkstraBHExecutionTime = Duration.between(start, finish).toMillis();
            printResults(dijskstraBHOutput, "Dijkstra BH", dijkstraBHExecutionTime, graph);
        }

        if (dfh) {
            System.out.println("----------------------------- Running Dijkstra FH ---------------------------");
            DijkstraFibonacciHeap dijkstraFH = new DijkstraFibonacciHeap();
            dijkstraFH.dijkstraFibonacciHeap(input);
            Instant start = Instant.now();
            Output dijkstraFHOutput = dijkstraFH.dijkstraFibonacciHeap(input);
            Instant finish = Instant.now();
            long dijkstraFHExecutionTime = Duration.between(start, finish).toMillis();
            printResults(dijkstraFHOutput, "Dijsktra FBH", dijkstraFHExecutionTime, graph);
        }

        if (bf) {
            System.out.println("----------------------------- Running Bellman-Ford ---------------------------");
            BellmanFord bellmanFord = new BellmanFord();
            bellmanFord.bellmanFord(input);
            Instant start = Instant.now();
            Output bellmanFordOutput = bellmanFord.bellmanFord(input);
            Instant finish = Instant.now();
            long bellmanFordExecutionTime = Duration.between(start, finish).toMillis();
            printResults(bellmanFordOutput, "Bellman-Ford", bellmanFordExecutionTime, graph);
        }

        if (j) {
            System.out.println("----------------------------- Running Johnson ---------------------------");
            Johnson johnson = new Johnson();
            johnson.johnson(input);
            Instant start = Instant.now();
            Output johnsonOutput = johnson.johnson(input);
            Instant finish = Instant.now();
            long johnsonExecutionTime = Duration.between(start, finish).toMillis();
            printResults(johnsonOutput, "Johnson", johnsonExecutionTime, graph);
        }

        if (fw) {
            System.out.println("----------------------------- Running Floyd-Warshall ---------------------------");
            FloydWarshall floydWarshall = new FloydWarshall();
            floydWarshall.floydWarshall(input);
            Instant start = Instant.now();
            Output floydWarshallOutput = floydWarshall.floydWarshall(input);
            Instant finish = Instant.now();
            long floydWarshallExecutionTime = Duration.between(start, finish).toMillis();
            printResults(floydWarshallOutput, "Floyd-Warshall", floydWarshallExecutionTime, graph);
        }
    }

    private static void printResults(Output output, String algorithm, long executionTime, Graph<Integer, DefaultWeightedEdge> graph) {
        Output.printOutputInformation(output, algorithm, executionTime);
        System.out.println(algorithm + " correct: " + OutputVerifier.verifyOutput(graph, output));
        System.out.println("------------------------------------------------------------------------------");
        System.out.println();
    }
}