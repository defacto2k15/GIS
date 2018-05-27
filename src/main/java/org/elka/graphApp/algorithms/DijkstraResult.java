package org.elka.graphApp.algorithms;

import org.jgrapht.graph.GraphWalk;

import java.util.Map;

public class DijkstraResult<V, E extends MyWeightedEdge<V>> {

    private GraphWalk<V, E> shortestPath;
    private Map<V, Double> distanceMap;

    public DijkstraResult(GraphWalk<V, E> graphWalk, Map<V, Double> algWeight) {
        this.shortestPath = graphWalk;
        distanceMap = algWeight;
    }

    public GraphWalk<V, E> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(GraphWalk<V, E> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<V, Double> getDistanceMap() {
        return distanceMap;
    }

    public void setDistanceMap(Map<V, Double> distanceMap) {
        this.distanceMap = distanceMap;
    }
}
