package org.elka.graphApp.algorithms;

import org.elka.graphApp.ForbiddenNodesRegister;
import org.elka.graphApp.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphWalk;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by defacto on 5/1/2018.
 */
public class DijkstraBasedAlgorithmSolver<V,E extends MyWeightedEdge<V>> {
    private final double INFINITY = 999999;

    public GraphPath<V,E> findShortestPath(Graph<V,E> graph, V startVertex, V endVertex) {
        return findShortestPath(new MaskedGraph<>(graph), startVertex, endVertex);
    }

    public GraphPath<V,E> findShortestPath(MaskedGraph<V,E> graph, V startVertex, V endVertex){
        Map<V, Double> algWeight = graph.vertices().
                collect( Collectors.toMap(Function.identity(), o -> INFINITY));
        algWeight.put(startVertex, 0.0);

        PriorityQueue<DistancedVertex<V>> queue = new PriorityQueue<>(
                Comparator.comparing(DistancedVertex::getDistanceFromStart));
        Map<V, V> predecessors = new HashMap<>();
        queue.add(new DistancedVertex<>(startVertex, 0));
        Map<V, Boolean> vertexWasUsed = graph.vertices().collect(Collectors.toMap(Function.identity(), o -> false));

        do {
            DistancedVertex<V> u = queue.poll();
            if(u.equals(endVertex)){
                break;
            }
            vertexWasUsed.put(u.innerVertex, true);

            graph.outgoingEdgesOf(u.innerVertex)
                    .filter(edge -> !vertexWasUsed.get(edge.getTarget()))
                    .forEach(edge -> {
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
            });
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

        return new GraphWalk<V, E>(graph.getInnerGraph(), startVertex, endVertex, shortestPath, pathWeight);
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


    public List<GraphPath<V,E>> findTwoShortestPaths(Graph<V, E> originalGraph, V startVertex, V endVertex, int
            maxTriesCount) {
        GraphPath<V, E> firstPath = findShortestPath(originalGraph, startVertex, endVertex);
        if (firstPath == null) {
            return Collections.emptyList();
        }

        MaskedGraph<V, E> workGraph = new MaskedGraph<>(originalGraph);
        RemovePathInternalVertices(firstPath, workGraph);
        GraphPath<V, E> secondPath = findShortestPath(workGraph, startVertex, endVertex);
        if(secondPath != null ){
            return Arrays.asList(firstPath, secondPath);
        }
        if(firstPath.getVertexList().size()==2){
            return Collections.singletonList(firstPath);
        }

        GraphPath<V, E> originalFirstPath = firstPath;

        ForbiddenNodesRegister<V,E> register = new ForbiddenNodesRegister<V,E>(firstPath);
        for(int i=maxTriesCount; i > 0; i--){
            workGraph = new MaskedGraph<>(originalGraph);
            List<V> forbiddenNodes = register.GetNextForbiddenNodes();
            if(forbiddenNodes == null){ // no more forbidden nodes
                break;
            }
            for( V node : forbiddenNodes){
                workGraph.removeVertex(node);
            }

            firstPath = findShortestPath(workGraph,  startVertex, endVertex);
            if (firstPath == null) {
                continue;
            }

            MaskedGraph<V,E> secondPathGraph = new MaskedGraph<>(originalGraph);
            RemovePathInternalVertices(firstPath, secondPathGraph);
            secondPath = findShortestPath(secondPathGraph, startVertex, endVertex);
            if(secondPath != null ){
                return Arrays.asList(firstPath, secondPath);
            }
            register.AddNewTraversedPath(firstPath);
        }
        return Collections.singletonList(originalFirstPath);
    }

    private void RemovePathInternalVertices(GraphPath<V, E> path, MaskedGraph<V, E> graph) {
        if(path.getVertexList().size() == 2 ){
            graph.removeEdge(graph.getEdge(path.getStartVertex(), path.getEndVertex()));
        }else {
            path.getVertexList().stream().skip(1).limit(path.getVertexList().size() - 2)
                    .forEach(graph::removeVertex);
        }
    }

    private class MaskedGraph<V, E extends MyWeightedEdge<V>>{
        private Graph<V,E> graph;
        private Set<V> maskedVectices;
        private Set<E> maskedEdges;

        private MaskedGraph(Graph<V, E> graph) {
            this.graph = graph;
            maskedVectices = new HashSet<>();
            maskedEdges = new HashSet<>();
        }

        public Stream<V> vertices() {
            return graph.vertexSet().stream().filter(v -> !maskedVectices.contains(v));
        }

        public E getEdge(V source, V target) {
            return graph.getEdge(source, target);
        }

        public Stream<E> outgoingEdgesOf(V vertex) {
            return graph.outgoingEdgesOf(vertex).stream()
                    .filter(c -> !maskedVectices.contains(c.getTarget()))
                    .filter(c -> !maskedEdges.contains(c));
        }

        public double getEdgeWeight(E edge) {
            return graph.getEdgeWeight(edge);
        }

        public Graph<V, E> getInnerGraph() {
            return graph;
        }

        public void removeEdge(E edge) {
            maskedEdges.add(edge);
        }

        public void removeVertex(V vertex) {
            maskedVectices.add(vertex);
        }
    }
}

