package common;

import org.jgrapht.Graph;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.util.SupplierUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphHelper {

    public Graph<Integer, DefaultEdge> generateTestGraph(int numOfNodes) {
        // Create a simple graph with Integer nodes and DefaultEdge
        Graph<Integer, DefaultEdge> simpleGraph = new SimpleGraph<>(DefaultEdge.class);

        // Add vertices
        for (int i = 0; i < numOfNodes; i++) {
            simpleGraph.addVertex(i);
        }

        // Connect vertices in line
        // This method is more for testing purposes and will be removed later
        for (int i = 0; i < numOfNodes - 1; i++) {
            simpleGraph.addEdge(i, i + 1);
        }

        return simpleGraph;
    }

    public Graph<Integer, DefaultEdge> generateUnweightedGraph(int numOfNodes, int numOfEdges) {
        // Supplier to generate unique vertex IDs (0,1,2,...)
        Supplier<Integer> vSupplier = new Supplier<>() {
            private int id = 0;
            @Override
            public Integer get() {
                return id++;
            }
        };

        // Create empty simple graph
        Graph<Integer, DefaultEdge> simpleGraph =
                new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(), false);

        // Initialize random graph generator
        GnmRandomGraphGenerator<Integer, DefaultEdge> generator =
                new GnmRandomGraphGenerator<>(numOfNodes, numOfEdges);

        // Generate the graph
        generator.generateGraph(simpleGraph);

        return simpleGraph;
    }

    public Graph<Integer, DefaultWeightedEdge> generateGraph(int numOfNodes, int numOfEdges) {
        // Supplier to generate unique vertex IDs (0,1,2,...)
        Supplier<Integer> vSupplier = new Supplier<>() {
            private int id = 0;
            @Override
            public Integer get() {
                return id++;
            }
        };

        // Create a weighted undirected graph
        Graph<Integer, DefaultWeightedEdge> weightedGraph =
                new SimpleWeightedGraph<>(vSupplier, SupplierUtil.createDefaultWeightedEdgeSupplier());

        // Initialize random graph generator
        GnmRandomGraphGenerator<Integer, DefaultWeightedEdge> generator =
                new GnmRandomGraphGenerator<>(numOfNodes, numOfEdges);

        // Generate graph
        generator.generateGraph(weightedGraph);

        // Assign random weights to each edge
        weightedGraph = addWeights(weightedGraph);

        return weightedGraph;
    }

    public static void printGraph(Graph<Integer, DefaultWeightedEdge> graph) {
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            System.out.println(graph.getEdgeSource(edge) + " -- " +
                    graph.getEdgeTarget(edge) + " : " +
                    graph.getEdgeWeight(edge));
        }
    }

    public static void printBasicInfo(Graph<Integer, DefaultWeightedEdge> graph) {
        System.out.println("------------------------------GRAPH INFORMATION------------------------------");
        System.out.println("Number of nodes: " + graph.vertexSet().size());
        System.out.println("Node list: " + graph.vertexSet());
        System.out.println();
        System.out.println("Number of edges: " + graph.edgeSet().size());
        System.out.println("Edge list: " + graph.edgeSet());
        System.out.println();
        System.out.println("Graph connections:");
        printGraph(graph);
        System.out.println("-----------------------------------------------------------------------------");
    }

    public Input generateSNAPGraph(String filePath) {

        Graph<Integer, DefaultWeightedEdge> weightedGraph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        int numOfNodes = 0;
        int numOfEdges = 0;
        // This is supposed to replace the importer
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("# Nodes:")) {
                    System.out.println("splitting line: " + line);
                    String[] splitLine = line.split(" Edges: ");
                    numOfEdges = Integer.parseInt(splitLine[1]);
                    numOfNodes = Integer.parseInt(splitLine[0].split(" ")[2]);
                }

                if (line.startsWith("#") || line.isBlank()) continue;

                String[] parts = line.split("\\s+");
                Integer src = Integer.parseInt(parts[0]);
                Integer dst = Integer.parseInt(parts[1]);

                if (src.equals(dst)) {
                    continue; // skip self-loop
                }

                weightedGraph.addVertex(src);
                weightedGraph.addVertex(dst);

                DefaultWeightedEdge e = weightedGraph.addEdge(src, dst);
                if (e != null) {
                    weightedGraph.setEdgeWeight(e, Math.random());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }

        System.out.println("NumOfNodes: " + numOfNodes);
        System.out.println("NumOfEdges: " + numOfEdges);

        weightedGraph = addWeights(weightedGraph);

        return new Input(0, numOfNodes - 1, weightedGraph);
    }
    
    public Graph<Integer, DefaultWeightedEdge> addWeights(Graph<Integer, DefaultWeightedEdge> graph) {
        // Assign random weights to each edge
        Random random = new Random();
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            double weight = 1 + random.nextInt(10);
            graph.setEdgeWeight(edge, weight);
        }
        
        return graph;
    }
}
