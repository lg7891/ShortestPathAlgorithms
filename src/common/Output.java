package common;

import java.util.ArrayList;

public class Output {
    int totalPathPrice;
    ArrayList<Integer> shortestPath;

    public Output(int totalPathPrice, ArrayList<Integer> shortestPath) {
        this.totalPathPrice = totalPathPrice;
        this.shortestPath = shortestPath;
    }

    public int getTotalPathPrice() {
        return totalPathPrice;
    }

    public ArrayList<Integer> getShortestPath() {
        return shortestPath;
    }

    @Override
    public String toString() {
        return "shortestPath: " + shortestPath;
    }

    public static void printOutputInformation(Output output, String algorithemName, long executionTime) {
        System.out.println("------------------------------OUTPUT INFORMATION-----------------------------");
        System.out.println("Execution Time for algorithm " + algorithemName +" is: " + executionTime + "ms");
        System.out.println("Total path price: " + output.getTotalPathPrice());
        System.out.println("Shortest path: " + output.getShortestPath());
        System.out.println("-----------------------------------------------------------------------------");
    }
}
