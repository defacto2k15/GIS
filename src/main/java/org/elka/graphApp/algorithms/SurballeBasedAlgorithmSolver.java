package org.elka.graphApp.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.GraphWalk;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurballeBasedAlgorithmSolver<V, E extends MyWeightedEdge<V>> {

    public List<GraphPath<V, E>> findTwoShortestPaths(Graph<V, E> graph, V startVertex, V endVertex) {
        Map<E, Double> weightsCache = graph.edgeSet().stream()
                .collect(Collectors.toMap(c -> c, c -> c.getWeight()));
        DijkstraResult<V, E> firstDijkstraResult = dijkstra(graph, startVertex, endVertex);
        if (firstDijkstraResult == null) {
            return Collections.emptyList();
        }
        GraphWalk<V, E> shortestPath1 = firstDijkstraResult.getShortestPath();
        Map<V, Double> distanceMap = firstDijkstraResult.getDistanceMap();
        graph.edgeSet().forEach(
                e -> graph.setEdgeWeight(e,
                        graph.getEdgeWeight(e) - distanceMap.get(e.getTarget()) + distanceMap.get(e.getSource())));

        reversePath(graph, shortestPath1);
        // Dijkstra po raz drugi
        DijkstraResult<V, E> secondDijkstraResult = dijkstra(graph, startVertex, endVertex);
        if (secondDijkstraResult == null) {
            return Collections.emptyList();
        }
        GraphWalk<V, E> shortestPath2 = secondDijkstraResult.getShortestPath();

        List<E> edgeList1 = shortestPath1.getEdgeList();
        List<E> edgeList2 = shortestPath2.getEdgeList();
        List<E> firstPath = untwinePaths(edgeList1, edgeList2);
        List<E> secondPath = untwinePaths(edgeList2, edgeList1);
        Double firstPathLength = calculatePathLength(weightsCache, firstPath);
        Double secondPathLength = calculatePathLength(weightsCache, secondPath);
        //cleaning
        graph.edgeSet().forEach(
                e -> graph.setEdgeWeight(e,
                        graph.getEdgeWeight(e) + distanceMap.get(e.getTarget()) - distanceMap.get(e.getSource())));

        GraphWalk<V, E> result1 = new GraphWalk<V, E>(graph, startVertex, endVertex, firstPath, firstPathLength);
        GraphWalk<V, E> result2 = new GraphWalk<>(graph, startVertex, endVertex, secondPath, secondPathLength);
        reversePath(graph, shortestPath1);
        return Arrays.asList(result1, result2);
    }

    private Double calculatePathLength(Map<E, Double> edgeWeights, List<E> path) {
        Double pathLength = 0.0;
        for (E edge : path) {
            pathLength += edgeWeights.get(edge);
        }
        return pathLength;
    }

    private void reversePath(Graph<V, E> graph, GraphWalk<V, E> shortestPath1) {
        // Założenie że graf nie jest multigrafem
        // odwracanie najkrótszej ścieżki w grafie.
        graph.removeAllEdges(shortestPath1.getEdgeList());
        List<E> edgeList = shortestPath1.getEdgeList();
        edgeList.forEach(e -> {
            E e1 = graph.addEdge(e.getTarget(), e.getSource());
            if (e1 != null) {
                graph.setEdgeWeight(e1, e.getWeight());
            }
        });
    }

    private List<E> untwinePaths(List<E> edges1, List<E> edges2) {
        List<E> edges = new ArrayList<>();
        List<E> curr = edges1;
        List<E> other = edges2;

        Map<V, E> mapCurr = edges1.stream().collect(Collectors.toMap(E::getSource, Function.identity()));
        Map<V, E> mapOther = edges2.stream().collect(Collectors.toMap(E::getSource, Function.identity()));
        for (ListIterator<E> iter = curr.listIterator(); iter.hasNext(); ) {
            E edge = iter.next();
            if (mapOther.containsKey(edge.getTarget()) &&
                    mapOther.get(edge.getTarget()).getTarget() == edge.getSource()) {
                /// jeżeli istnieje odwrócona krawędż drugiej mapie
                for (ListIterator<E> it = other.listIterator(); it.hasNext(); ) {
                    E edgeTmp = it.next();
                    if (edgeTmp.getSource() == edge.getTarget()) {
                        iter = it;
                        break;
                    }
                }
                List<E> tmp = curr;
                curr = other;
                other = tmp;
                Map<V, E> tmpMap = mapCurr;
                mapCurr = mapOther;
                mapOther = tmpMap;
            } else {
                edges.add(edge);
            }
        }
        return edges;
    }

    private DijkstraResult<V, E> dijkstra(Graph<V, E> graph, V startVertex, V endVertex) {
        DijkstraShortestPath<V, E> dijkstraSolver = new DijkstraShortestPath<V, E>(graph);
        ShortestPathAlgorithm.SingleSourcePaths<V, E> paths = dijkstraSolver.getPaths(startVertex);
        Map<V, Double> distanceMap = new HashMap<>();
        graph.vertexSet().forEach(v -> distanceMap.put(v, paths.getWeight(v)));
        GraphPath<V, E> shortestPath = paths.getPath(endVertex);
        if (shortestPath == null) {
            return null;
        }
        GraphWalk<V, E> graphWalk = new GraphWalk<>(graph, startVertex, endVertex, shortestPath.getEdgeList(),
                shortestPath.getWeight());
        return new DijkstraResult<>(graphWalk, distanceMap);
    }
}