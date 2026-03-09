package common;

public class CustomGraph {
    public final int numOfNodes;
    public final int numOfEdges;

    // CSR format: for node u, its neighbors are in
    // adjTarget[adjOffset[u] .. adjOffset[u+1]-1]
    // with weights in adjWeight[same range]
    public final int[] adjOffset;   // length: numOfNodes + 1
    public final int[] adjTarget;   // length: 2 * numOfEdges (undirected)
    public final int[] adjWeight;   // length: 2 * numOfEdges

    public CustomGraph(int numOfNodes, int numOfEdges, int[] adjOffset, int[] adjTarget, int[] adjWeight) {
        this.numOfNodes = numOfNodes;
        this.numOfEdges = numOfEdges;
        this.adjOffset = adjOffset;
        this.adjTarget = adjTarget;
        this.adjWeight = adjWeight;
    }
}
