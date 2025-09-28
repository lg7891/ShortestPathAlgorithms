import algorithms.BellmanFord;
import algorithms.DijkstraBinaryHeap;
import common.Input;
import common.Output;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int numOfNodes = 5;
        int numOfEdges = 7;

//        Initialize classes
        Input input = new Input(numOfNodes, numOfEdges);
        DijkstraBinaryHeap dijkstraBinaryHeap = new DijkstraBinaryHeap();
        BellmanFord bellmanFord = new BellmanFord();

//        Generate graph
        Graph<Integer, DefaultWeightedEdge> graph = input.generateGraph(input.getNumOfNodes(), input.getNumOfEdges());
        Input.printBasicInfo(graph);

//        Run Dijkstra BH
        dijkstraBinaryHeap.dijkstraBinaryHeap(graph);
        Instant start = Instant.now();
        Output dijskstraBinrayHeapOutput = dijkstraBinaryHeap.dijkstraBinaryHeap(graph);
        Instant finish = Instant.now();
        long dijkstraBinaryHeapExecutionTime = Duration.between(start, finish).toMillis();

//         Run Bellman-Ford
        bellmanFord.bellmanFord(graph);
        start = Instant.now();
        Output bellmanFordOutput = dijkstraBinaryHeap.dijkstraBinaryHeap(graph);
        finish = Instant.now();
        long bellmanFordExecutionTime = Duration.between(start, finish).toMillis();

//        Print results
        Output.printOutputInformation(dijskstraBinrayHeapOutput, "Dijkstra BH", dijkstraBinaryHeapExecutionTime);
        Output.printOutputInformation(bellmanFordOutput, "Bellman-Ford", bellmanFordExecutionTime);
    }
}