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
        Graph<Integer, MyWeightedEdge<Integer>> graph = GraphReader.readGraphFromFile(options.inputFile);
        List<GraphPath<Integer, MyWeightedEdge<Integer>>> graphPaths = null;
        if (options.surballe) {
            graphPaths = runSurballe(graph, options);
        }
        if (options.dijkstra) {
            graphPaths = runDijkstra(graph, options);
        }

        if(graphPaths != null ){
            writeGraphPathsInfo(graphPaths);
            if(options.shouldShowWindow){
                drawGraph(graph, graphPaths);
            }
        }
    }

    private static void writeGraphPathsInfo(List<GraphPath<Integer, MyWeightedEdge<Integer>>> graphPaths) {
        System.out.println("Wyniki poszukiwania:");
        System.out.println("Znaleziono ścieżek: "+graphPaths.size());
        for(int i = 0; i < graphPaths.size(); i++){
            System.out.println("Ściezka "+i+": koszt: "+graphPaths.get(i).getWeight());
            System.out.print("Poszczególne segmenty ścieżki: ");
            for( Integer v : graphPaths.get(i).getVertexList()){
                System.out.print(v+" - ");
            }
            System.out.println();
        }
    }

    public static List<GraphPath<Integer, MyWeightedEdge<Integer>>> runSurballe(Graph<Integer, MyWeightedEdge<Integer>> graph, BenchmarkConfiguration configuration) {
        SurballeBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> solver = new SurballeBasedAlgorithmSolver<>();
        List<Integer> vertices = new ArrayList<>(graph.vertexSet());
        return solver.findTwoShortestPaths(graph,
                vertices.get(configuration.startIndex), vertices.get(configuration.endIndex));
    }

    public static List<GraphPath<Integer, MyWeightedEdge<Integer>>> runDijkstra(Graph<Integer, MyWeightedEdge<Integer>> graph, BenchmarkConfiguration configuration) {
        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> solver = new DijkstraBasedAlgorithmSolver<>();
        List<Integer> vertices = new ArrayList<>(graph.vertexSet());
        return solver.findTwoShortestPaths(graph,
                vertices.get(configuration.startIndex), vertices.get(configuration.endIndex), 10);
    }

    private static <V,E> void drawGraph(Graph<V,E> graph, List<GraphPath<V,E>> shortestWalks){
        SimpleGraphWindow simpleGraphWindow = new SimpleGraphWindow(graph);
        simpleGraphWindow.DrawGraph();
        simpleGraphWindow.colorShortestPaths(shortestWalks);
    }
}
