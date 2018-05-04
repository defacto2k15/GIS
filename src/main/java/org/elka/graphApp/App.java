package org.elka.graphApp;

import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.MySimpleRandomGraphGenerator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

/**
 * Hello world!NoSuchMethodError: org.jgrapht.Graph.outgoingEdgesOf(
 *
 */
public class App {
    public static void main(String[] args) throws ImportException {
        App app = new App();
        app.run();
    }

    private SimpleGraphWindow window;

    public void run() throws ImportException {
        Graph<Integer, MyWeightedEdge<Integer>> generatedGraph
                = MySimpleRandomGraphGenerator.Generate(6, 0.6, 123);
        generatedGraph = WriteGraphToCsvAndReadGraphFromCsv(generatedGraph);
        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> solver = new DijkstraBasedAlgorithmSolver();
        List<Integer> vertices = new ArrayList<>(generatedGraph.vertexSet());

        DrawGraph(generatedGraph);

//        GraphPath<Integer, MyWeightedEdge<Integer>> walk = solver.findShortestPath(generatedGraph, vertices.get(2), vertices.get(0));
//        window.colorShortestPath(walk);

        List<GraphPath<Integer, MyWeightedEdge<Integer>>> shortestWalks = solver.findTwoShortestPaths(generatedGraph, vertices.get(2), vertices.get(0));
        window.colorShortestPaths(shortestWalks);
    }

    public Graph<Integer, MyWeightedEdge<Integer>> WriteGraphToCsvAndReadGraphFromCsv(Graph<Integer, MyWeightedEdge<Integer>> graph) throws ImportException {
        CSVExporter<Integer,MyWeightedEdge<Integer>> exporter = new CSVExporter<>();
        exporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_EDGE_WEIGHTS, true);
        exporter.setFormat(CSVFormat.MATRIX);
        Writer writer = new CharArrayWriter();

        exporter.exportGraph(graph, writer);

        System.out.println(writer.toString());

        CSVImporter<Integer, MyWeightedEdge<Integer>> importer = new CSVImporter<>(
                (String s, Map<String, Attribute> map) -> (Integer.parseInt(s)),
                (v1, v2, s, map) -> graph.getEdgeFactory().createEdge(v1, v2));
        importer.setFormat(CSVFormat.MATRIX);
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_EDGE_WEIGHTS, true);

        Reader reader = new CharArrayReader(writer.toString().toCharArray());
        Graph<Integer,MyWeightedEdge<Integer>> graph2 =
                new SimpleDirectedWeightedGraph<Integer, MyWeightedEdge<Integer>>(graph.getEdgeFactory());
        importer.importGraph(graph2, reader);

        return graph2;
    }

    public <T1, T2> void DrawGraph(Graph<T1, T2> graph) {
        JGraphXAdapter<T1, T2> adapter = new JGraphXAdapter<>(graph);
        window = new SimpleGraphWindow(adapter);
        window.DrawGraph();
    }

    private static <T1,T2> void DrawGraphWithShortestPath(Graph<T1, T2> graph, GraphPath<T1, T2> shortestPath){

    }
}
