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

        // Prepare graph parameters
        int numOfNodes = Integer.parseInt(args[0]);
        int numOfEdges = Integer.parseInt(args[1]);
        // Generate graph
        GraphHelper graphHelper = new GraphHelper();
        Graph<Integer, DefaultWeightedEdge> graph = graphHelper.generateGraph(numOfNodes, numOfEdges);
        graphHelper.printBasicInfo(graph);

        // Initialize classes
        Input input = new Input(0, numOfNodes - 1, graph);
        DijkstraBinaryHeap dijkstraBH = new DijkstraBinaryHeap();
        BellmanFord bellmanFord = new BellmanFord();
        FloydWarshall floydWarshall = new FloydWarshall();
        // AStar aStar = new AStar();
        DijkstraFibonacciHeap dijkstraFH = new DijkstraFibonacciHeap();
        Johnson johnson = new Johnson();

        // Run Dijkstra BH
        dijkstraBH.dijkstraBinaryHeap(input);
        Instant start = Instant.now();
        Output dijskstraBHOutput = dijkstraBH.dijkstraBinaryHeap(input);
        Instant finish = Instant.now();
        long dijkstraBHExecutionTime = Duration.between(start, finish).toMillis();

         // Run Bellman-Ford
        bellmanFord.bellmanFord(input);
        start = Instant.now();
        Output bellmanFordOutput = dijkstraBH.dijkstraBinaryHeap(input);
        finish = Instant.now();
        long bellmanFordExecutionTime = Duration.between(start, finish).toMillis();

         // Run Floyd-Warshall
        floydWarshall.floydWarshall(input);
        start = Instant.now();
        Output floydWarshallOutput = dijkstraBH.dijkstraBinaryHeap(input);
        finish = Instant.now();
        long floydWarshallExecutionTime = Duration.between(start, finish).toMillis();

        // Run Dijkstra FH
        dijkstraFH.dijkstraFibonacciHeap(input);
        start = Instant.now();
        Output dijkstraFHOutput = dijkstraFH.dijkstraFibonacciHeap(input);
        finish = Instant.now();
        long dijkstraFHExecutionTime = Duration.between(start, finish).toMillis();

        // Run Johnson
        johnson.johnson(input);
        start = Instant.now();
        Output johnsonOutput = johnson.johnson(input);
        finish = Instant.now();
        long johnsonExecutionTime = Duration.between(start, finish).toMillis();

        // Print results
        Output.printOutputInformation(dijskstraBHOutput, "Dijkstra BH", dijkstraBHExecutionTime);
        Output.printOutputInformation(bellmanFordOutput, "Bellman-Ford", bellmanFordExecutionTime);
        Output.printOutputInformation(floydWarshallOutput, "Floyd-Warshall", floydWarshallExecutionTime);
        Output.printOutputInformation(dijkstraFHOutput, "Dijkstra FH", dijkstraFHExecutionTime);
        Output.printOutputInformation(johnsonOutput, "Johnson", johnsonExecutionTime);

        System.out.println("---------------------------------------Algorihtem Correctness-------------------------------------");
        System.out.println("Dijkstra BH correct: " + OutputVerifier.verifyOutput(input.getGraph(), dijskstraBHOutput));
        System.out.println("Bellman-Ford correct: " + OutputVerifier.verifyOutput(input.getGraph(), bellmanFordOutput));
        System.out.println("Floyd-Warshall correct: " + OutputVerifier.verifyOutput(input.getGraph(), floydWarshallOutput));
        System.out.println("Dijkstra FH correct: " + OutputVerifier.verifyOutput(input.getGraph(), dijkstraFHOutput));
        System.out.println("Johnson correct: " + OutputVerifier.verifyOutput(input.getGraph(), johnsonOutput));
    }
}