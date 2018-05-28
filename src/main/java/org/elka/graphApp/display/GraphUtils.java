package org.elka.graphApp.display;

import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * Created by defacto on 5/2/2018.
 */
public class GraphUtils {
    public static <V, E extends MyWeightedEdge<V>> Graph<V, E> CopyDirectedWeightedGraph(Graph<V, E> ingraph) {
        Graph<V, E> newGraph = new SimpleDirectedWeightedGraph<V, E>(ingraph.getEdgeFactory());
        for (V vertex : ingraph.vertexSet()) {
            newGraph.addVertex(vertex);
        }
        for (E edge : ingraph.edgeSet()) {
            E newEdge = newGraph.getEdgeFactory().createEdge(edge.getSource(), edge.getTarget());
            newGraph.addEdge(edge.getSource(), edge.getTarget(), newEdge);
            newGraph.setEdgeWeight(newEdge, edge.getWeight());
        }
        return newGraph;
    }
}
