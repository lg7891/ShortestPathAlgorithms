package common;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;

public class OutputVerifier {

  public static boolean verifyOutput(Graph<Integer, DefaultWeightedEdge> graph, Output output) {

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
    double computedPrice = 0.0;
    for (int i = 0; i < path.size() - 1; i++) {
      Integer u = path.get(i);
      Integer v = path.get(i + 1);

      DefaultWeightedEdge edge = graph.getEdge(u, v);
      if (edge == null) {
        System.out.printf("Error: No edge exists between %d and %d%n", u, v);
        return false;
      }

      computedPrice += graph.getEdgeWeight(edge);
    }

    // Case 4: Compare computed and reported path costs
    if (Math.abs(computedPrice - output.totalPathPrice) > 1e-6) {
      System.out.printf(
              "Error: Reported totalPathPrice (%d) does not match computed path cost (%.2f)%n",
              output.totalPathPrice, computedPrice
      );
      return false;
    }

    return true;
  }
}
