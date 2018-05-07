package org.elka.graphApp.algorithms;

import org.elka.graphApp.GraphUtils;
import org.elka.graphApp.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.GraphWalk;
import sun.security.provider.certpath.Vertex;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by defacto on 5/1/2018.
 */
public class DijkstraBasedAlgorithmSolver<V,E extends MyWeightedEdge<V>> {
    private final double INFINITY = 999999;

    public GraphPath<V,E> findShortestPath(Graph<V,E> graph, V startVertex, V endVertex){

        Map<V, Double> algWeight = graph.vertexSet().stream().
                collect( Collectors.toMap(Function.identity(), o -> INFINITY));
        algWeight.put(startVertex, 0.0);

        PriorityQueue<DistancedVertex<V>> queue = new PriorityQueue<>(
                Comparator.comparing(DistancedVertex::getDistanceFromStart));
        Map<V, V> predecessors = new HashMap<>();
        queue.add(new DistancedVertex<>(startVertex, 0));
        Map<V, Boolean> vertexWasUsed = graph.vertexSet().stream().collect(Collectors.toMap(Function.identity(), o -> false));

        do {
            DistancedVertex<V> u = queue.poll();
            if(u.equals(endVertex)){
                break;
            }
            vertexWasUsed.put(u.innerVertex, true);

            for (E edge : graph.outgoingEdgesOf(u.innerVertex).stream()
                    .filter(edge -> !vertexWasUsed.get(edge.getTarget()))
                    .collect(Collectors.toList())) {
                V neighbour = edge.getTarget();
                double alt = algWeight.get(edge.getSource()) + graph.getEdgeWeight(edge);
                if (alt < algWeight.get(neighbour)) {
                    if (algWeight.containsKey(neighbour)) {
                        queue.remove(new DistancedVertex<>(neighbour, algWeight.get(neighbour)));
                    }
                    queue.add(new DistancedVertex<>(neighbour, alt));
                    algWeight.put(neighbour, alt);
                    predecessors.put(neighbour, u.getInnerVertex());
                }
            }
        }
        while(!queue.isEmpty());

        List<E> shortestPath = new ArrayList<>();
        V currentVertex = endVertex;
        double pathWeight = 0;
        do{
            V predecessor = predecessors.get(currentVertex);
            if( predecessor == null ){
                // no path was found
                return null;
            }
            pathWeight += graph.getEdgeWeight(graph.getEdge(predecessor, currentVertex));
            shortestPath.add(graph.getEdge(predecessor, currentVertex));
            currentVertex = predecessor;
        }
        while(currentVertex != startVertex);
        Collections.reverse(shortestPath);

        return new GraphWalk<V, E>(graph, startVertex, endVertex, shortestPath, pathWeight);
    }



    private class DistancedVertex<V> {
        private V innerVertex;
        private double distanceFromStart;

        public DistancedVertex(V innerVertex, double distanceFromStart) {
            this.innerVertex = innerVertex;
            this.distanceFromStart = distanceFromStart;
        }

        public V getInnerVertex() {
            return innerVertex;
        }

        public Double getDistanceFromStart() {
            return distanceFromStart;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DistancedVertex<?> that = (DistancedVertex<?>) o;

            if (Double.compare(that.distanceFromStart, distanceFromStart) != 0) return false;
            return innerVertex != null ? innerVertex.equals(that.innerVertex) : that.innerVertex == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = innerVertex != null ? innerVertex.hashCode() : 0;
            temp = Double.doubleToLongBits(distanceFromStart);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }


    public List<GraphPath<V,E>> findTwoShortestPaths(Graph<V, E> graph, V startVertex, V endVertex){
        GraphPath<V,E> firstPath = findShortestPath(graph, startVertex, endVertex);
        if(firstPath == null ){
            return Collections.emptyList();
        }else {
            Graph<V,E> workGraph = GraphUtils.CopyDirectedWeightedGraph(graph);
            if(firstPath.getVertexList().size() == 2 ){
                graph.removeEdge(graph.getEdge(firstPath.getStartVertex(), firstPath.getEndVertex()));
            }else {
                firstPath.getVertexList().stream().skip(1).limit(firstPath.getVertexList().size() - 2)
                        .forEach(workGraph::removeVertex);
            }

            GraphPath<V,E> secondPath = findShortestPath(workGraph, startVertex, endVertex);
            return Arrays.asList(firstPath,secondPath);
        }
    }
}

