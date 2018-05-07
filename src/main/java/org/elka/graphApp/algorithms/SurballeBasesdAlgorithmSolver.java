package org.elka.graphApp.algorithms;

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

public class SurballeBasesdAlgorithmSolver<V, E extends MyWeightedEdge<V>> {

    public List<GraphPath<V, E>> findTwoShortestPaths(Graph<V, E> graph, V startVertex, V endVertex) {
        DijkstraResult firstDijkstraResult = dijkstra(graph, startVertex, endVertex);
        GraphWalk shortestPath1 = firstDijkstraResult.getShortestPath();
        if (shortestPath1 == null) {
            return Collections.emptyList();
        }

        Map<Vertex, Double> distanceMap = firstDijkstraResult.getDistanceMap();
        graph.edgeSet().forEach(
                e -> graph.setEdgeWeight(e,
                        graph.getEdgeWeight(e) - distanceMap.get(e.getTarget()) + distanceMap.get(e.getSource())));

        reversePath(graph, shortestPath1);
        // Dijkstra po raz drugi
        DijkstraResult secondDijkstraResult = dijkstra(graph, startVertex, endVertex);
        GraphWalk shortestPath2 = secondDijkstraResult.getShortestPath();
        if (secondDijkstraResult == null) {
            return Collections.emptyList();
        }
        List<MyWeightedEdge<V>> edgeList1 = shortestPath1.getEdgeList();
        List<MyWeightedEdge<V>> edgeList2 = shortestPath2.getEdgeList();
        List<MyWeightedEdge<V>> firstPath = untwinePaths(edgeList1, edgeList2);
        List<MyWeightedEdge<V>> secondPath = untwinePaths(edgeList2, edgeList1);

        //cleaning
        graph.edgeSet().forEach(
                e -> graph.setEdgeWeight(e,
                        graph.getEdgeWeight(e) + distanceMap.get(e.getTarget()) - distanceMap.get(e.getSource())));

        Double firstPathLength = calculatePathLength(graph, firstPath);
        Double secondPathLength = calculatePathLength(graph, secondPath);
        GraphWalk<V, E> result1 = new GraphWalk<>(graph, startVertex, endVertex, (List<E>) firstPath, firstPathLength);
        GraphWalk<V, E> result2 = new GraphWalk<>(graph, startVertex, endVertex, (List<E>) secondPath, secondPathLength);
        reversePath(graph, shortestPath1);
        return Arrays.asList(result1, result2);
    }

    private Double calculatePathLength(Graph<V, E> graph, List<MyWeightedEdge<V>> path) {
        Double pathLength = 0.0;
        for (MyWeightedEdge<V> edge : path) {
            E edgeInGraph = graph.getEdge(edge.getSource(), edge.getTarget());
            if (edgeInGraph != null) {
                pathLength += edgeInGraph.getWeight();
            } else {
                pathLength += graph.getEdge(edge.getTarget(), edge.getSource()).getWeight();
            }
        }
        return pathLength;
    }

    private void reversePath(Graph<V, E> graph, GraphWalk shortestPath1) {
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

    protected List<MyWeightedEdge<V>> untwinePaths(List<MyWeightedEdge<V>> edges1, List<MyWeightedEdge<V>> edges2) {
        List<MyWeightedEdge<V>> edges = new ArrayList<>();
        List<MyWeightedEdge<V>> curr = edges1;
        List<MyWeightedEdge<V>> other = edges2;

        Map<V, MyWeightedEdge<V>> mapCurr = edges1.stream().collect(Collectors.toMap(MyWeightedEdge<V>::getSource, Function.identity()));
        Map<V, MyWeightedEdge<V>> mapOther = edges2.stream().collect(Collectors.toMap(MyWeightedEdge<V>::getSource, Function.identity()));
        for (ListIterator<MyWeightedEdge<V>> iter = curr.listIterator(); iter.hasNext(); ) {
            MyWeightedEdge<V> edge = iter.next();
            if (mapOther.containsKey(edge.getTarget()) &&
                    mapOther.get(edge.getTarget()).getTarget() == edge.getSource()) {
                /// jeżeli istnieje odwrócona krawędż drugiej mapie
                for (ListIterator<MyWeightedEdge<V>> it = other.listIterator(); it.hasNext(); ) {
                    MyWeightedEdge<V> edgeTmp = it.next();
                    if (edgeTmp.getSource() == edge.getTarget()) {
                        iter = it;
                        break;
                    }
                }
                List<MyWeightedEdge<V>> tmp = curr;
                curr = other;
                other = tmp;
                Map<V, MyWeightedEdge<V>> tmpMap = mapCurr;
                mapCurr = mapOther;
                mapOther = tmpMap;
            } else {
                edges.add(edge);
            }
        }
        return edges;
    }

    private DijkstraResult dijkstra(Graph<V, E> graph, V startVertex, V endVertex) {
        DijkstraShortestPath<V, E> dijkstraSolver = new DijkstraShortestPath<V, E>(graph);
        ShortestPathAlgorithm.SingleSourcePaths<V, E> paths = dijkstraSolver.getPaths(startVertex);
        Map<V, Double> distanceMap = new HashMap<>();
        graph.vertexSet().forEach(v -> distanceMap.put(v, paths.getWeight(v)));
        GraphPath<V, E> shortestPath = paths.getPath(endVertex);
        if (shortestPath == null) {
            return new DijkstraResult<>(null, null);
        }
        GraphWalk<V, E> graphWalk = new GraphWalk<>(graph, startVertex, endVertex, shortestPath.getEdgeList(),
                shortestPath.getWeight());
        return new DijkstraResult<>(graphWalk, distanceMap);
    }
}