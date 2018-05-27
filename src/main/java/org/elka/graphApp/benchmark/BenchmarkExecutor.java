package org.elka.graphApp.benchmark;

import org.elka.graphApp.MyWeightedEdge;
import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.DijkstraSolvingExtendedResult;
import org.elka.graphApp.algorithms.SurballeBasesdAlgorithmSolver;
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
        int startSize = configuration.getMinNodeCount();
        int endSize = configuration.getMaxNodeCount();
        int testsCount = configuration.getTestsCount();

        List<Float> erdosPropabilitiesPerTest = configuration.getErdosPropabilitiesPerTest();

        MyErdosRenyiGraphGenerator erdosRenyiGenerator = new MyErdosRenyiGraphGenerator();

        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> dijkstraSolver = new
                DijkstraBasedAlgorithmSolver<>();
        SurballeBasesdAlgorithmSolver<Integer, MyWeightedEdge<Integer>> surballeSolver = new
                SurballeBasesdAlgorithmSolver<>();

        AtomicInteger loopsCompleted = new AtomicInteger(0);

        return IntStream.range(0, testsCount).boxed()
                .parallel()
                .flatMap(c -> {
            List<SingleMeasure> measures = new ArrayList<>();
            float i = startSize + c*(endSize-startSize)/testsCount;
            int size = Math.round(i);
            System.out.println("Test = Ilość węzłów: " + size);

            int j = 0;
            for (float propability : erdosPropabilitiesPerTest) {
                int seed = Math.round(i * 123.312f * (j + 1));
                Graph<Integer, MyWeightedEdge<Integer>> graph
                        = erdosRenyiGenerator.Generate(size, propability, seed);
                int maxTriesCount = 10;

                List<Integer> vertices = new ArrayList<>(graph.vertexSet());

                Random shuffleRandom = new Random(seed);
                for(int k = 0; k < configuration.getPerGraphTestsCount(); k++) {
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
                        measure.setDijkstraTime(end-start);

                        if(result.getPaths().size() > 0 ){
                            measure.setDijkstraFirstPathWeight(result.getPaths().get(0).getWeight());
                        }
                        if(result.getPaths().size() > 1 ){
                            measure.setDijkstraSecondPathWeight(result.getPaths().get(1).getWeight());
                        }
                    }

                    if (configuration.shouldTestSurballe()) {
                        long start = System.nanoTime();
                        List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                                = surballeSolver.findTwoShortestPaths(graph, startVertex, endVertex);
                        long end = System.nanoTime();

                        measure.setSuurballeTime(end-start);

                        if(outPaths.size() > 0){
                            measure.setSuurballeFirstPathWeight(outPaths.get(0).getWeight());
                        }
                        if(outPaths.size() > 1 ){
                            measure.setSuurballeSecondPathWeight(outPaths.get(1).getWeight());
                        }
                    }
                    measures.add(measure);
                }

                j++;
            }

            MyWattsStrogatzGraphGenerator wattsStrogatzGenerator = new MyWattsStrogatzGraphGenerator();
            List<Float> wattsPropabilitiesPerTest = configuration.getWattsPropabilitiesPerTest();
            List<Float> wattsLerpKParamsPerTest = configuration.getWattsLerpKParamsPerTest();
            List<Integer> wattsKParamsPerTest = configuration.getWattsKParamsPerTest();

            if (wattsLerpKParamsPerTest != null) {
                wattsKParamsPerTest = wattsLerpKParamsPerTest.stream()
                        .map(p -> Math.round(lerp((float) Math.log(size), size, p)))
                        .map(k -> (int)roundEven(k))
                        .filter(k -> k < size && k > Math.log(size) )
                        .distinct()
                        .collect(Collectors.toList());
            }
            for (float propability : wattsPropabilitiesPerTest) {
                for (int kParam : wattsKParamsPerTest) {
                    int seed = Math.round(i * 123.312f * (j + 1));
                    Graph<Integer, MyWeightedEdge<Integer>> graph
                            = wattsStrogatzGenerator.Generate(size, kParam, propability, seed);
                    int maxTriesCount = 10;

                    List<Integer> vertices = new ArrayList<>(graph.vertexSet());

                    Random shuffleRandom = new Random(seed);
                    for(int k = 0; k < configuration.getPerGraphTestsCount(); k++) {
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
                            measure.setDijkstraTime(end-start);

                            if(result.getPaths().size() > 0 ){
                                measure.setDijkstraFirstPathWeight(result.getPaths().get(0).getWeight());
                            }
                            if(result.getPaths().size() > 1 ){
                                measure.setDijkstraSecondPathWeight(result.getPaths().get(1).getWeight());
                            }
                        }

                        if (configuration.shouldTestSurballe()) {
                            long start = System.nanoTime();
                            List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths
                                    = surballeSolver.findTwoShortestPaths(graph, startVertex, endVertex);
                            long end = System.nanoTime();

                            measure.setSuurballeTime(end-start);

                            if(outPaths.size() > 0){
                                measure.setSuurballeFirstPathWeight(outPaths.get(0).getWeight());
                            }
                            if(outPaths.size() > 1 ){
                                measure.setSuurballeSecondPathWeight(outPaths.get(1).getWeight());
                            }
                        }
                        measures.add(measure);
                    }
                    j++;
                }

            }
            int loopsCompletedAfterThis = loopsCompleted.addAndGet(1);
            System.out.println("Test = Completed "+(100*((float)loopsCompletedAfterThis)/testsCount)+"% done");
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
