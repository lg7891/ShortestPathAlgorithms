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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

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

    public Graph<Integer, DefaultWeightedEdge> generateGraph(int numOfNodes, double density, int seed) {
        // Supplier to generate unique vertex IDs (0,1,2,...)
        Supplier<Integer> vSupplier = new Supplier<>() {
            private int id = 0;

            @Override
            public Integer get() {
                return id++;
            }
        };

        long maxEdges = (long) numOfNodes * (numOfNodes - 1) / 2;
        long edges = Math.round(density * maxEdges);
        edges = Math.min(edges, maxEdges);

        int numOfEdges = Math.toIntExact(edges);

        Graph<Integer, DefaultWeightedEdge> graph =
                new SimpleWeightedGraph<>(vSupplier, SupplierUtil.createDefaultWeightedEdgeSupplier());

        GnmRandomGraphGenerator<Integer, DefaultWeightedEdge> generator =
                new GnmRandomGraphGenerator<>(numOfNodes, numOfEdges);

        generator.generateGraph(graph);

        return addWeights(graph, seed);
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

    public JGraphTInput generateSNAPGraph(String filePath, int seed) {
        Graph<Integer, DefaultWeightedEdge> weightedGraph = new SimpleWeightedGraph<>(
                DefaultWeightedEdge.class);

        Map<Integer, Integer> idMap = new HashMap<>();
        int nextId = 0;
        int numOfNodes = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                // Read node count (edges count intentionally ignored)
                if (line.startsWith("# Nodes:")) {
                    String[] parts = line.split("\\s+");
                    numOfNodes = Integer.parseInt(parts[2]);
                    continue;
                }

                // Skip comments and empty lines
                if (line.startsWith("#") || line.isBlank()) {
                    continue;
                }

                // Parse edge
                String[] parts = line.split("\\s+");
                int srcOriginal = Integer.parseInt(parts[0]);
                int dstOriginal = Integer.parseInt(parts[1]);

                // Ignore self-loops
                if (srcOriginal == dstOriginal) {
                    continue;
                }

                // Map original IDs to compact IDs
                idMap.putIfAbsent(srcOriginal, nextId++);
                idMap.putIfAbsent(dstOriginal, nextId++);

                int src = idMap.get(srcOriginal);
                int dst = idMap.get(dstOriginal);

                // Canonical undirected edge
                int u = Math.min(src, dst);
                int v = Math.max(src, dst);

                weightedGraph.addVertex(u);
                weightedGraph.addVertex(v);

                // Prevent duplicate edges from directed input
                if (!weightedGraph.containsEdge(u, v)) {
                    weightedGraph.addEdge(u, v);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }

        // Assign weights after construction
        addWeights(weightedGraph, seed);

        // Pick two distinct random vertices
        List<Integer> vertices = new ArrayList<>(weightedGraph.vertexSet());

        if (vertices.size() < 2) {
            throw new IllegalStateException("Graph must contain at least two vertices");
        }

        Random random = new Random(seed);

        int srcIndex = random.nextInt(vertices.size());
        int dstIndex;
        do {
            dstIndex = random.nextInt(vertices.size());
        } while (dstIndex == srcIndex);

        int src = vertices.get(srcIndex);
        int dst = vertices.get(dstIndex);

        return new JGraphTInput(src, dst, weightedGraph);
    }

    public Graph<Integer, DefaultWeightedEdge> addWeights(
            Graph<Integer, DefaultWeightedEdge> graph, int seed) {
        // Assign random weights to each edge
        Random random = new Random(seed);
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            double weight = 1 + random.nextInt(10);
            graph.setEdgeWeight(edge, weight);
        }

        return graph;
    }

    public CustomGraph generateCustomGraph(int numOfNodes, double density, int seed) {
        Random random = new Random(seed);
        long maxEdges = (long) numOfNodes * (numOfNodes - 1) / 2;
        int edges = (int) Math.min(Math.round(density * maxEdges), maxEdges);

        // Collect edges first
        Set<Long> usedEdges = new HashSet<>(edges * 2);
        int[] us = new int[edges], vs = new int[edges], ws = new int[edges];
        int count = 0;

        while (count < edges) {
            int u = random.nextInt(numOfNodes);
            int v = random.nextInt(numOfNodes);
            if (u == v)
                continue;
            long key = ((long) Math.min(u, v) << 32) | Math.max(u, v);
            if (!usedEdges.add(key))
                continue;
            us[count] = u;
            vs[count] = v;
            ws[count] = random.nextInt(10) + 1;
            count++;
        }

        // Count degree of each node (each undirected edge contributes to 2 nodes)
        int[] degree = new int[numOfNodes];
        for (int i = 0; i < edges; i++) {
            degree[us[i]]++;
            degree[vs[i]]++;
        }

        // Build CSR offset array
        int[] adjOffset = new int[numOfNodes + 1];
        for (int i = 0; i < numOfNodes; i++) {
            adjOffset[i + 1] = adjOffset[i] + degree[i];
        }

        // Fill adjacency arrays
        int totalAdj = adjOffset[numOfNodes]; // == 2 * edges
        int[] adjTarget = new int[totalAdj];
        int[] adjWeight = new int[totalAdj];
        int[] cursor = Arrays.copyOf(adjOffset, numOfNodes); // write pointer per node

        for (int i = 0; i < edges; i++) {
            int u = us[i], v = vs[i], w = ws[i];
            adjTarget[cursor[u]] = v;
            adjWeight[cursor[u]++] = w;
            adjTarget[cursor[v]] = u;
            adjWeight[cursor[v]++] = w;
        }

        return new CustomGraph(numOfNodes, edges, adjOffset, adjTarget, adjWeight);
    }

    public static int[] srcTargetGenerator(int numOfNodes, int seed) {
        Random random = new Random(seed);
        int src = random.nextInt(numOfNodes);
        int target = random.nextInt(numOfNodes);

        return new int[] { src, target };
    }
}
