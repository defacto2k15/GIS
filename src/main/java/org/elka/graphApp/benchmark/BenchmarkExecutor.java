package org.elka.graphApp.benchmark;

import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.DijkstraSolvingExtendedResult;
import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.elka.graphApp.algorithms.AlgorithmType;
import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.SurballeBasedAlgorithmSolver;
import org.elka.graphApp.generators.GraphGeneratorType;
import org.elka.graphApp.generators.MyErdosRenyiGraphGenerator;
import org.elka.graphApp.generators.MyWattsStrogatzGraphGenerator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by defacto on 5/20/2018.
 */
public class BenchmarkExecutor {
    public List<SingleMeasure> Execute(BenchmarkConfiguration configuration) {
        int startSize = configuration.minNodeCount;
        int endSize = configuration.maxNodeCount;
        int testsCount = configuration.testsCount;

        MyErdosRenyiGraphGenerator erdosRenyiGenerator = new MyErdosRenyiGraphGenerator();

        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> dijkstraSolver = new
                DijkstraBasedAlgorithmSolver<>();
        SurballeBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> surballeSolver = new
                SurballeBasedAlgorithmSolver<>();

        AtomicInteger loopsCompleted = new AtomicInteger(0);

        return IntStream.range(0, testsCount).boxed()
                .parallel()
                .flatMap(c -> {
                    List<SingleMeasure> measures = new ArrayList<>();
                    for (float i = startSize; i < endSize; i += (endSize - startSize) / (float) testsCount) {
                        int size = Math.round(i);
                        System.out.println("[Test]  Ilość węzłów: " + size);

                        int j = 0;
                        for (float propability : configuration.erdosProbabilitiesPerTest) {
                            int seed = Math.round(i * 123.312f * (j + 1));
                            Graph<Integer, MyWeightedEdge<Integer>> graph
                                    = erdosRenyiGenerator.Generate(size, propability, seed);
                            int maxTriesCount = 10;

                            List<Integer> vertices = new ArrayList<>(graph.vertexSet());

                            Random shuffleRandom = new Random(seed);
                            for (int k = 0; k < configuration.testsPerGraphCount; k++) {
                                Collections.shuffle(vertices, shuffleRandom);
                                Integer startVertex = vertices.get(0);
                                Integer endVertex = vertices.get(1);

                                SingleMeasure measure = new SingleMeasure(vertices.size(), graph.edgeSet().size(),
                                        GraphGeneratorType.Erdos, propability);

                                if (configuration.shouldTestDijkstra()) {
                                    long start = System.nanoTime();
                                    DijkstraSolvingExtendedResult<Integer, MyWeightedEdge<Integer>> result
                                            = dijkstraSolver.extendedFindTwoShortestPaths(graph, startVertex, endVertex, maxTriesCount);
                                    long end = System.nanoTime();

                                    measure.setDijkstraTriesCount(result.getTriesCount());
                                    measure.setDijkstraTime(end - start);

                                    if (result.getPaths().size() > 0) {
                                        measure.setDijkstraFirstPathWeight(result.getPaths().get(0).getWeight());
                                    }
                                    if (result.getPaths().size() > 1) {
                                        measure.setDijkstraSecondPathWeight(result.getPaths().get(1).getWeight());
                                    }
                                }

                                if (configuration.shouldTestSurballe()) {
                                    long start = System.nanoTime();
                                    List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                                            = surballeSolver.findTwoShortestPaths(graph, startVertex, endVertex);
                                    long end = System.nanoTime();

                                    measure.setSuurballeTime(end - start);

                                    if (outPaths.size() > 0) {
                                        measure.setSuurballeFirstPathWeight(outPaths.get(0).getWeight());
                                    }
                                    if (outPaths.size() > 1) {
                                        measure.setSuurballeSecondPathWeight(outPaths.get(1).getWeight());
                                    }
                                }
                                measures.add(measure);
                            }

                            j++;
                        }

                        MyWattsStrogatzGraphGenerator wattsStrogatzGenerator = new MyWattsStrogatzGraphGenerator();
                        List<Float> wattsPropabilitiesPerTest = configuration.wattsProbabilitiesPerTest;
                        List<Float> wattsLerpKParamsPerTest = configuration.wattsLerpKParamsPerTest;
                        List<Integer> wattsKParamsPerTest = configuration.wattsKParamsPerTest;

                        if (wattsLerpKParamsPerTest != null) {
                            wattsKParamsPerTest = wattsLerpKParamsPerTest.stream()
                                    .map(p -> Math.round(lerp((float) Math.log(size), size, p)))
                                    .map(k -> (int) roundEven(k))
                                    .filter(k -> k < size && k > Math.log(size))
                                    .distinct()
                                    .collect(Collectors.toList());
                        }
                        for (int kParam : wattsKParamsPerTest) {
                            for (float propability : wattsPropabilitiesPerTest) {
                                int seed = Math.round(i * 123.312f * (j + 1));
                                Graph<Integer, MyWeightedEdge<Integer>> graph
                                        = wattsStrogatzGenerator.Generate(size, kParam, propability, seed);
                                int maxTriesCount = 10;

                                List<Integer> vertices = new ArrayList<>(graph.vertexSet());

                                Random shuffleRandom = new Random(seed);
                                for (int k = 0; k < configuration.testsPerGraphCount; k++) {
                                    Collections.shuffle(vertices, shuffleRandom);
                                    Integer startVertex = vertices.get(0);
                                    Integer endVertex = vertices.get(1);

                                    SingleMeasure measure = new SingleMeasure(vertices.size(), graph.edgeSet().size(),
                                            GraphGeneratorType.Watts, propability);
                                    measure.setWattsKParam(kParam);

                                    if (configuration.shouldTestDijkstra()) {
                                        long start = System.nanoTime();
                                        DijkstraSolvingExtendedResult<Integer, MyWeightedEdge<Integer>> result
                                                = dijkstraSolver.extendedFindTwoShortestPaths(graph, startVertex, endVertex, maxTriesCount);
                                        long end = System.nanoTime();

                                        measure.setDijkstraTriesCount(result.getTriesCount());
                                        measure.setDijkstraTime(end - start);

                                        if (result.getPaths().size() > 0) {
                                            measure.setDijkstraFirstPathWeight(result.getPaths().get(0).getWeight());
                                        }
                                        if (result.getPaths().size() > 1) {
                                            measure.setDijkstraSecondPathWeight(result.getPaths().get(1).getWeight());
                                        }
                                    }

                                    if (configuration.shouldTestSurballe()) {
                                        long start = System.nanoTime();
                                        List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                                                = surballeSolver.findTwoShortestPaths(graph, startVertex, endVertex);
                                        long end = System.nanoTime();

                                        measure.setSuurballeTime(end - start);

                                        if (outPaths.size() > 0) {
                                            measure.setSuurballeFirstPathWeight(outPaths.get(0).getWeight());
                                        }
                                        if (outPaths.size() > 1) {
                                            measure.setSuurballeSecondPathWeight(outPaths.get(1).getWeight());
                                        }
                                    }
                                    measures.add(measure);
                                }
                                j++;
                            }

                        }
                        int loopsCompletedAfterThis = loopsCompleted.addAndGet(1);
                        System.out.println("[Test] = Ukończono" + (100 * ((float) loopsCompletedAfterThis) /
                                testsCount) + "%");
                    }
                    return measures.stream();
                }).collect(Collectors.toList());
    }

    float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    long roundEven(double d) {
        return Math.round(d / 2) * 2;
    }
}
