package org.elka.graphApp;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.elka.graphApp.algorithms.SurballeBasedAlgorithmSolver;
import org.elka.graphApp.benchmark.BenchmarkConfiguration;
import org.elka.graphApp.benchmark.BenchmarkExecutor;
import org.elka.graphApp.benchmark.SingleMeasure;
import org.elka.graphApp.display.LayoutMode;
import org.elka.graphApp.display.SimpleGraphWindow;
import org.elka.graphApp.generators.MySimpleGraphGenerator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.io.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class App {
    public static void main(String[] args) throws ImportException, CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        App app = new App();
//        app.run();
        app.runBenchmark();
    }

    private SimpleGraphWindow window;

    private Graph<Integer, MyWeightedEdge<Integer>> getExampleGraph(Graph<Integer, MyWeightedEdge<Integer>> g) {
        LinkedList<MyWeightedEdge<Integer>> copy = new LinkedList<MyWeightedEdge<Integer>>();
        for (MyWeightedEdge<Integer> e : g.edgeSet()) {
            copy.add(e);
        }
        g.removeAllEdges(copy);
        LinkedList<Integer> copy2 = new LinkedList<>();
        for (Integer v : g.vertexSet()) {
            copy2.add(v);
        }
        g.removeAllVertices(copy2);


        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addVertex(6);

        addEdge(g, 1, 2, 1);
        addEdge(g, 1, 3, 2);
        addEdge(g, 3, 4, 2);
        addEdge(g, 2, 4, 1);
        addEdge(g, 2, 5, 2);
        addEdge(g, 5, 6, 2);
        addEdge(g, 4, 6, 1);

        return g;
    }

    private void addEdge(Graph<Integer, MyWeightedEdge<Integer>> g, int v1, int v2, int wegith) {
        MyWeightedEdge<Integer> edge7 = g.addEdge(v1, v2);
        g.setEdgeWeight(edge7, wegith);
        MyWeightedEdge<Integer> edge1 = g.addEdge(v2, v1);
        g.setEdgeWeight(edge1, wegith);
    }

    public void runBenchmark() throws ImportException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        BenchmarkExecutor executor = new BenchmarkExecutor();
        List<SingleMeasure> measures = executor.Execute(new BenchmarkConfiguration());
        Writer writer = new FileWriter("result.csv");
        StatefulBeanToCsv<SingleMeasure> beanToCsv = new StatefulBeanToCsvBuilder<SingleMeasure>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
        beanToCsv.write(measures);
        writer.close();
    }

    public void run() throws ImportException {
        MySimpleGraphGenerator generator = new MySimpleGraphGenerator();
        Graph<Integer, MyWeightedEdge<Integer>> generatedGraph
                = generator.Generate(6, 0.6, 123);

        SurballeBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> solver = new SurballeBasedAlgorithmSolver<>();
        List<Integer> vertices = new ArrayList<>(generatedGraph.vertexSet());

        DrawGraph(generatedGraph, LayoutMode.Circle);
        List<GraphPath<Integer, MyWeightedEdge<Integer>>> shortestWalks = solver.findTwoShortestPaths(generatedGraph, vertices.get(0), vertices.get(5));

        window.colorShortestPaths(shortestWalks);
    }

    public Graph<Integer, MyWeightedEdge<Integer>> WriteGraphToCsvAndReadGraphFromCsv(Graph<Integer, MyWeightedEdge<Integer>> graph) throws ImportException {
        CSVExporter<Integer, MyWeightedEdge<Integer>> exporter = new CSVExporter<>();
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
        Graph<Integer, MyWeightedEdge<Integer>> graph2 =
                new SimpleDirectedWeightedGraph<Integer, MyWeightedEdge<Integer>>(graph.getEdgeFactory());
        importer.importGraph(graph2, reader);

        return graph2;
    }

    public <T1, T2> void DrawGraph(Graph<T1, T2> graph, LayoutMode layoutMode) {
        JGraphXAdapter<T1, T2> adapter = new JGraphXAdapter<>(graph);
        window = new SimpleGraphWindow(adapter, layoutMode);
        window.DrawGraph();
    }
}
