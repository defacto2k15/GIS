package org.elka.graphApp.runner;

import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.elka.graphApp.algorithms.SurballeBasedAlgorithmSolver;
import org.elka.graphApp.benchmark.BenchmarkConfiguration;
import org.elka.graphApp.display.SimpleGraphWindow;
import org.elka.graphApp.parsing.GraphReader;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;

public class SimpleRunner {
    public static void runOnUserInput(BenchmarkConfiguration options) {
        Graph<Integer, MyWeightedEdge<Integer>> graph = GraphReader.readGraphFromFile(options.getInputFile());
        if (options.isSurballe()) {
            runSurballe(graph);
        }
        if (options.isDijkstra()) {
            runDijkstra(graph);
        }
    }

    public static void runSurballe(Graph<Integer, MyWeightedEdge<Integer>> graph) {
        SurballeBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> solver = new SurballeBasedAlgorithmSolver<>();
        List<Integer> vertices = new ArrayList<>(graph.vertexSet());
        SimpleGraphWindow simpleGraphWindow = new SimpleGraphWindow(graph);
        simpleGraphWindow.DrawGraph();
        List<GraphPath<Integer, MyWeightedEdge<Integer>>> shortestWalks = solver.findTwoShortestPaths(graph, vertices.get(0), vertices.get(vertices.size() - 1));
        simpleGraphWindow.colorShortestPaths(shortestWalks);
    }

    public static void runDijkstra(Graph<Integer, MyWeightedEdge<Integer>> graph) {
        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> solver = new DijkstraBasedAlgorithmSolver<>();
        List<Integer> vertices = new ArrayList<>(graph.vertexSet());
        SimpleGraphWindow simpleGraphWindow = new SimpleGraphWindow(graph);
        simpleGraphWindow.DrawGraph();
        List<GraphPath<Integer, MyWeightedEdge<Integer>>> shortestWalks = solver.findTwoShortestPaths(graph, vertices.get(0), vertices.get(vertices.size() - 1), 10);
        simpleGraphWindow.colorShortestPaths(shortestWalks);
    }
}
