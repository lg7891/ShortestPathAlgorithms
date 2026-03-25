package common;

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

public class GraphHelper {

    public Graph generateGraph(int numOfNodes, double density, int seed) {
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

        return new Graph(numOfNodes, edges, adjOffset, adjTarget, adjWeight);
    }

    public Input generateSNAPGraph(String filePath, int seed) {
        Map<Integer, Integer> idMap = new HashMap<>();
        int nextId = 0;

        Set<Long> seenEdges = new HashSet<>();
        List<int[]> edgeList = new ArrayList<>();

        Random random = new Random(seed); // use seed for reproducibility

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isBlank())
                    continue;

                String[] parts = line.split("\\s+");
                int srcOriginal = Integer.parseInt(parts[0]);
                int dstOriginal = Integer.parseInt(parts[1]);

                if (srcOriginal == dstOriginal)
                    continue;

                if (!idMap.containsKey(srcOriginal))
                    idMap.put(srcOriginal, nextId++);
                if (!idMap.containsKey(dstOriginal))
                    idMap.put(dstOriginal, nextId++);

                int u = idMap.get(srcOriginal);
                int v = idMap.get(dstOriginal);

                int lo = Math.min(u, v);
                int hi = Math.max(u, v);
                long key = ((long) lo << 32) | hi;

                if (seenEdges.add(key)) {
                    int weight = random.nextInt(10) + 1;
                    edgeList.add(new int[] { lo, hi, weight });
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }

        int V = nextId;
        int E = edgeList.size();

        if (V < 2) {
            throw new IllegalStateException("Graph must contain at least two vertices");
        }

        int[] degree = new int[V];
        for (int[] edge : edgeList) {
            degree[edge[0]]++;
            degree[edge[1]]++;
        }

        int[] adjOffset = new int[V + 1];
        for (int i = 0; i < V; i++) {
            adjOffset[i + 1] = adjOffset[i] + degree[i];
        }

        int[] adjTarget = new int[2 * E];
        int[] adjWeight = new int[2 * E];
        int[] cursor = Arrays.copyOf(adjOffset, V);

        for (int[] edge : edgeList) {
            int u = edge[0], v = edge[1], w = edge[2];
            adjTarget[cursor[u]] = v;
            adjWeight[cursor[u]++] = w;
            adjTarget[cursor[v]] = u;
            adjWeight[cursor[v]++] = w;
        }

        Graph graph = new Graph(V, E, adjOffset, adjTarget, adjWeight);

        int srcIndex = random.nextInt(V);
        int dstIndex;
        do {
            dstIndex = random.nextInt(V);
        } while (dstIndex == srcIndex);

        System.out.println("Number of nodes:  " + V + ", number of edges: " + E);

        return new Input(srcIndex, dstIndex, graph);
    }

    public static int[] srcTargetGenerator(int numOfNodes, int seed) {
        Random random = new Random(seed);
        int src = random.nextInt(numOfNodes);
        int target = random.nextInt(numOfNodes);

        return new int[] { src, target };
    }
}
