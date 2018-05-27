package org.elka.graphApp.benchmark;

import org.elka.graphApp.MyWeightedEdge;
import org.elka.graphApp.algorithms.AlgorithmType;
import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.SurballeBasedAlgorithmSolver;
import org.elka.graphApp.generators.GraphGeneratorType;
import org.elka.graphApp.generators.MyErdosRenyiGraphGenerator;
import org.elka.graphApp.generators.MyWattsStrogatzGraphGenerator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.*;

/**
 * Created by defacto on 5/20/2018.
 */
public class BenchmarkExecutor {
    public List<SingleMeasure> Execute(BenchmarkConfiguration configuration) {
        List<SingleMeasure> measures = new ArrayList<>();

        int startSize = configuration.getMinNodeCount();
        int endSize = configuration.getMaxNodeCount();
        int testsCount = configuration.getTestsCount();

        List<Float> erdosProbabilitiesPerTest = configuration.getErdosProbabilitiesPerTest();
        List<Float> wattsProbabilitiesPerTest = configuration.getWattsProbabilitiesPerTest();
        List<Integer> wattsKParamsPerTest = configuration.getWattsKParamsPerTest();

        MyErdosRenyiGraphGenerator erdosRenyiGenerator = new MyErdosRenyiGraphGenerator();
        MyWattsStrogatzGraphGenerator wattsStrogatzGenerator = new MyWattsStrogatzGraphGenerator();

        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> dijkstraSolver = new
                DijkstraBasedAlgorithmSolver<>();
        SurballeBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> surballeSolver = new
                SurballeBasedAlgorithmSolver<>();
        for (float i = startSize; i < endSize; i += (endSize - startSize) / (float) testsCount) {
            int size = Math.round(i);
            System.out.println("Test = Ilość węzłów: " + size);

            int j = 0;
            for (float propability : erdosProbabilitiesPerTest) {
                int seed = Math.round(i * 123.312f * (j + 1));
                Graph<Integer, MyWeightedEdge<Integer>> graph
                        = erdosRenyiGenerator.Generate(size, propability, seed);
                int maxTriesCount = 10;

                List<Integer> vertices = new ArrayList<>(graph.vertexSet());

                Random shuffleRandom = new Random(seed);
                Collections.shuffle(vertices, shuffleRandom);
                Integer startVertex = vertices.get(0);
                Integer endVertex = vertices.get(1);


                if (configuration.shouldTestDijkstra()) {
                    long start = System.nanoTime();
                    List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                            = dijkstraSolver.findTwoShortestPaths(graph, startVertex, endVertex, maxTriesCount);
                    long end = System.nanoTime();

                    SingleMeasure measure = new SingleMeasure(vertices.size(), graph.edgeSet().size(),
                            AlgorithmType.Dijkstra, GraphGeneratorType.Erdos, propability, end - start);
                    if (outPaths.size() > 0) {
                        measure.setDijkstraFirstPathWeight(outPaths.get(0).getWeight());
                    }
                    if (outPaths.size() > 1) {
                        measure.setDijkstraSecondPathWeight(outPaths.get(1).getWeight());
                    }
                    measures.add(measure);
                }

                if (configuration.shouldTestSurballe()) {
                    long start = System.nanoTime();
                    List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                            = surballeSolver.findTwoShortestPaths(graph, startVertex, endVertex);
                    long end = System.nanoTime();

                    SingleMeasure measure = new SingleMeasure(vertices.size(), graph.edgeSet().size(),
                            AlgorithmType.Surballe, GraphGeneratorType.Erdos, propability, end - start);
                    if (outPaths.size() > 0) {
                        measure.setDijkstraFirstPathWeight(outPaths.get(0).getWeight());
                    }
                    if (outPaths.size() > 1) {
                        measure.setDijkstraSecondPathWeight(outPaths.get(1).getWeight());
                    }
                    measures.add(measure);

                }

                j++;
            }

            for (int kParam : wattsKParamsPerTest) {
                for (float propability : wattsProbabilitiesPerTest) {
                    int seed = Math.round(i * 123.312f * (j + 1));
                    Graph<Integer, MyWeightedEdge<Integer>> graph
                            = wattsStrogatzGenerator.Generate(size, kParam, propability, seed);
                    int maxTriesCount = 10;

                    List<Integer> vertices = new ArrayList<>(graph.vertexSet());

                    Random shuffleRandom = new Random(seed);
                    Collections.shuffle(vertices, shuffleRandom);
                    Integer startVertex = vertices.get(0);
                    Integer endVertex = vertices.get(1);

                    if (configuration.shouldTestDijkstra()) {
                        long start = System.nanoTime();
                        List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                                = dijkstraSolver.findTwoShortestPaths(graph, startVertex, endVertex, maxTriesCount);
                        long end = System.nanoTime();

                        SingleMeasure measure = new SingleMeasure(vertices.size(), graph.edgeSet().size(),
                                AlgorithmType.Dijkstra, GraphGeneratorType.Watts, propability, end - start);
                        measure.setWattsKParam(kParam);
                        if (outPaths.size() > 0) {
                            measure.setDijkstraFirstPathWeight(outPaths.get(0).getWeight());
                        }
                        if (outPaths.size() > 1) {
                            measure.setDijkstraSecondPathWeight(outPaths.get(1).getWeight());
                        }
                        measures.add(measure);
                    }

                    if (configuration.shouldTestSurballe()) {
                        long start = System.nanoTime();
                        List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                                = surballeSolver.findTwoShortestPaths(graph, startVertex, endVertex);
                        long end = System.nanoTime();

                        SingleMeasure measure = new SingleMeasure(vertices.size(), graph.edgeSet().size(),
                                AlgorithmType.Surballe, GraphGeneratorType.Watts, propability, end - start);
                        measure.setWattsKParam(kParam);
                        if (outPaths.size() > 0) {
                            measure.setDijkstraFirstPathWeight(outPaths.get(0).getWeight());
                        }
                        if (outPaths.size() > 1) {
                            measure.setDijkstraSecondPathWeight(outPaths.get(1).getWeight());
                        }
                        measures.add(measure);

                    }
                    j++;
                }

            }
        }
        return measures;
    }
}
