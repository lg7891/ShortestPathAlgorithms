import algorithms.*;
import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int numOfNodes = 2000;
        int numOfEdges = 2800;

        // Initialize classes
        Input input = new Input(numOfNodes, numOfEdges);
        DijkstraBinaryHeap dijkstraBH = new DijkstraBinaryHeap();
        BellmanFord bellmanFord = new BellmanFord();
        FloydWarshall floydWarshall = new FloydWarshall();
        // AStar aStar = new AStar();
        DijkstraFibonacciHeap dijkstraFH = new DijkstraFibonacciHeap();
        Johnson johnson = new Johnson();

        // Generate graph
        Graph<Integer, DefaultWeightedEdge> graph = input.generateGraph(input.getNumOfNodes(), input.getNumOfEdges());
        Input.printBasicInfo(graph);

        // Run Dijkstra BH
        dijkstraBH.dijkstraBinaryHeap(graph);
        Instant start = Instant.now();
        Output dijskstraBHOutput = dijkstraBH.dijkstraBinaryHeap(graph);
        Instant finish = Instant.now();
        long dijkstraBHExecutionTime = Duration.between(start, finish).toMillis();

         // Run Bellman-Ford
        bellmanFord.bellmanFord(graph);
        start = Instant.now();
        Output bellmanFordOutput = dijkstraBH.dijkstraBinaryHeap(graph);
        finish = Instant.now();
        long bellmanFordExecutionTime = Duration.between(start, finish).toMillis();

         // Run Floyd-Warshall
        floydWarshall.floydWarshall(graph);
        start = Instant.now();
        Output floydWarshallOutput = dijkstraBH.dijkstraBinaryHeap(graph);
        finish = Instant.now();
        long floydWarshallExecutionTime = Duration.between(start, finish).toMillis();

//         Run A*
//        aStar.aStar(graph);
//        start = Instant.now();
//        Output astarOutput = aStar.aStar(graph);
//        finish = Instant.now();
//        long aStarExecutionTime = Duration.between(start, finish).toMillis();

        // Run Dijkstra FH
        dijkstraFH.dijkstraFibonacciHeap(graph);
        start = Instant.now();
        Output dijkstraFHOutput = dijkstraFH.dijkstraFibonacciHeap(graph);
        finish = Instant.now();
        long dijkstraFHExecutionTime = Duration.between(start, finish).toMillis();

        // Run Johnson
        johnson.johnson(graph);
        start = Instant.now();
        Output johnsonOutput = johnson.johnson(graph);
        finish = Instant.now();
        long johnsonExecutionTime = Duration.between(start, finish).toMillis();

        // Print results
        Output.printOutputInformation(dijskstraBHOutput, "Dijkstra BH", dijkstraBHExecutionTime);
        Output.printOutputInformation(bellmanFordOutput, "Bellman-Ford", bellmanFordExecutionTime);
        Output.printOutputInformation(floydWarshallOutput, "Floyd-Warshall", floydWarshallExecutionTime);
//        Output.printOutputInformation(astarOutput, "AStar", aStarExecutionTime);
        Output.printOutputInformation(dijkstraFHOutput, "Dijkstra FH", dijkstraFHExecutionTime);
        Output.printOutputInformation(johnsonOutput, "Johnson", johnsonExecutionTime);
    }
}