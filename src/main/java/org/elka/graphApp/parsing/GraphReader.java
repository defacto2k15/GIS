package org.elka.graphApp.parsing;

import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.io.*;

import java.io.*;
import java.util.Map;

public class GraphReader {

    public final static String FILE_FORMAT =
            "File format:\n" +
                    "   V lines and V columns  \n" +
                    "   columns separated by comma \n" +
                    "   in n-th row and k-th column \n" +
                    "   weight of edge between vectors n and k \n"+
                    "	,,99.0,,,90.0\n"+
                    "	43.0,,,,,1.0\n"+
                    "	,66.0,,42.0,,\n"+
                    "	,,,,,29.0\n"+
                    "	98.0,38.0,,76.0,,65.0\n"+
                    "	,,37.0,,,";

    public static Graph<Integer, MyWeightedEdge<Integer>> readGraphFromFile(String fileName) {
        Graph<Integer, MyWeightedEdge<Integer>> graph =
                new SimpleDirectedWeightedGraph<>((e1, e2) -> new MyWeightedEdge<>());

        try (DataInputStream reader = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(fileName)))) {
            Integer numVertex = reader.readInt();
            Integer numEdges = reader.readInt();

            for (int i = 1; i < numVertex + 1; i++) {
                graph.addVertex(i);
            }
            for (int i = 0; i < numEdges; i++) {
                addEdge(graph, reader.readInt(), reader.readInt(), reader.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private static void addEdge(Graph<Integer, MyWeightedEdge<Integer>> g, int v1, int v2, int weight) {
        MyWeightedEdge<Integer> edge7 = g.addEdge(v1, v2);
        g.setEdgeWeight(edge7, weight);
        MyWeightedEdge<Integer> edge1 = g.addEdge(v2, v1);
        g.setEdgeWeight(edge1, weight);
    }

    public static void writeGraphToCsv(Graph<Integer, MyWeightedEdge<Integer>> graph, String fileName) {
        CSVExporter<Integer, MyWeightedEdge<Integer>> exporter = new CSVExporter<>();
        exporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_EDGE_WEIGHTS, true);
        exporter.setFormat(CSVFormat.MATRIX);
        try (Writer writer = new FileWriter(fileName)) {
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Graph<Integer, MyWeightedEdge<Integer>> importGraphFromCsv(String fileName) {
        Graph<Integer, MyWeightedEdge<Integer>> graph =
                new SimpleDirectedWeightedGraph<>((e1, e2) -> new MyWeightedEdge<Integer>());
        try (FileReader reader = new FileReader(fileName)) {
            CSVImporter<Integer, MyWeightedEdge<Integer>> importer = new CSVImporter<>(
                    (String s, Map<String, Attribute> map) -> (Integer.parseInt(s)),
                    (v1, v2, s, map) -> graph.getEdgeFactory().createEdge(v1, v2));
            importer.setFormat(CSVFormat.MATRIX);
            importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_EDGE_WEIGHTS, true);
            Graph<Integer, MyWeightedEdge<Integer>> graph2 =
                    new SimpleDirectedWeightedGraph<Integer, MyWeightedEdge<Integer>>(graph.getEdgeFactory());
            importer.importGraph(graph2, reader);
            return graph2;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImportException e) {
            e.printStackTrace();
        }
        return null;
    }


}
