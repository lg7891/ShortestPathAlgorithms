package common;

import java.util.ArrayList;

public class OutputVerifier {

    public static boolean verifyOutput(Graph graph, Output output) {
        ArrayList<Integer> path = output.shortestPath;

        // Case 1: No path found (totalPathPrice == -1)
        if (output.totalPathPrice == -1) {
            if (path == null || path.isEmpty()) {
                System.out.println("Info: No path found, which is valid.");
                return true;
            } else {
                System.out.println("Error: totalPathPrice == -1 but path is not empty.");
                return false;
            }
        }

        // Case 2: Path should exist but is missing
        if (path == null || path.isEmpty()) {
            System.out.println("Error: Path is empty or null but totalPathPrice != -1.");
            return false;
        }

        // Case 3: Check structural correctness
        int computedPrice = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int u = path.get(i);
            int v = path.get(i + 1);

            // Find edge between u and v using CSR adjacency arrays
            int edgeWeight = -1;
            for (int j = graph.adjOffset[u]; j < graph.adjOffset[u + 1]; j++) {
                if (graph.adjTarget[j] == v) {
                    edgeWeight = graph.adjWeight[j];
                    break;
                }
            }

            if (edgeWeight == -1) {
                System.out.printf("Error: No edge exists between %d and %d%n", u, v);
                return false;
            }

            computedPrice += edgeWeight;
        }

        // Case 4: Compare computed and reported path costs
        if (computedPrice != output.totalPathPrice) {
            System.out.printf(
                    "Error: Reported totalPathPrice (%d) does not match computed path cost (%d)%n",
                    output.totalPathPrice, computedPrice
            );
            return false;
        }

        return true;
    }
}
