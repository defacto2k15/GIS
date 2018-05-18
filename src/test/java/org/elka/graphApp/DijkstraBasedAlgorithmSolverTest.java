package org.elka.graphApp;

import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.MySimpleRandomGraphGenerator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by defacto on 5/2/2018.
 */
public class DijkstraBasedAlgorithmSolverTest {

    @Test
    public void longTest(){

        for(int j = 0; j < 8000; j++) {
            int seed = j +400;
            Random random = new Random(seed);
            int nodesCount = random.nextInt(15) +5 ;
            double probability = +0.4 + random.nextDouble() * 0.6f;

            Graph<Integer, MyWeightedEdge<Integer>> testGraph =
                    MySimpleRandomGraphGenerator.Generate(nodesCount, probability, seed);

            List<Integer> vertices = new ArrayList<>(testGraph.vertexSet());
            for (int i = 0; i < 10; i++) {
                Random shuffleRandom = new Random(seed + i);
                System.out.println("J is "+j+" i is "+i);
                Collections.shuffle(vertices, shuffleRandom);
                Integer startVertex = vertices.get(0);
                Integer endVertex = vertices.get(1);
                testPathFindingWithTwoMethods(testGraph, startVertex, endVertex);
            }
        }
    }

    @Test
    public void carefulTest() {
        int seed = 21 + 400;
        Random random = new Random(seed);
        int nodesCount = random.nextInt(3) + 3;
        double probability = +0.4 + random.nextDouble() * 0.6f;

        Graph<Integer, MyWeightedEdge<Integer>> testGraph =
                MySimpleRandomGraphGenerator.Generate(nodesCount, probability, seed);

        List<Integer> vertices = new ArrayList<>(testGraph.vertexSet());
        Random shuffleRandom = new Random(seed + 1);
        Collections.shuffle(vertices, shuffleRandom);
        Integer startVertex = vertices.get(0);
        Integer endVertex = vertices.get(1);
        testPathFindingWithTwoMethods(testGraph, startVertex, endVertex);
    }

    @Test
    public void simpleTestWithPath(){
        Graph<Integer, MyWeightedEdge<Integer>> testGraph =
                MySimpleRandomGraphGenerator.Generate(5, 1, 44);
        Integer startVertex = new ArrayList<Integer>(testGraph.vertexSet()).get(0);
        Integer endVertex = new ArrayList<Integer>(testGraph.vertexSet()).get(4);
//        App.DrawGraph(testGraph);

        testPathFindingWithTwoMethods(testGraph, startVertex, endVertex);
    }

    @Test
    public void simpleTestWithFirstPathBlocked(){
        Graph<Integer, MyWeightedEdge<Integer>> testGraph =
                new SimpleDirectedWeightedGraph<>((e1, e2) -> new MyWeightedEdge<Integer>());

        testGraph.addVertex(1);
        testGraph.addVertex(2);
        testGraph.addVertex(3);
        testGraph.addVertex(4);
        testGraph.addVertex(5);

        addEdge(testGraph, 1,2, 7);
        addEdge(testGraph, 2 ,3, 6);
        addEdge(testGraph, 3,5, 4);
        addEdge(testGraph, 1,4, 5);
        addEdge(testGraph, 2,4, 3);
        addEdge(testGraph, 4,5, 8);
        addEdge(testGraph, 4,3, 2);

        Integer startVertex = new ArrayList<>(testGraph.vertexSet()).get(0);
        Integer endVertex = new ArrayList<>(testGraph.vertexSet()).get(4);

        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> mySolver = new DijkstraBasedAlgorithmSolver<>();
        List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths =
                mySolver.findTwoShortestPaths(testGraph, startVertex, endVertex, 10);

        Assert.assertEquals(2, outPaths.size());
        Assert.assertThat(outPaths.get(0).getVertexList(), org.hamcrest.CoreMatchers.is(Arrays.asList(1,2,3,5)));
        Assert.assertThat(outPaths.get(1).getVertexList(), org.hamcrest.CoreMatchers.is(Arrays.asList(1,4,5)));
    }

    private void testPathFindingWithTwoMethods(Graph<Integer, MyWeightedEdge<Integer>> testGraph, Integer startVertex, Integer endVertex){
        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> mySolver = new DijkstraBasedAlgorithmSolver<>();
        DijkstraShortestPath<Integer, MyWeightedEdge<Integer>> libSolver
                = new DijkstraShortestPath<Integer, MyWeightedEdge<Integer>>(testGraph);
        GraphPath<Integer, MyWeightedEdge<Integer>> path2 = libSolver.getPath(startVertex, endVertex);
        GraphPath<Integer, MyWeightedEdge<Integer>> path1
                = mySolver.findShortestPath(testGraph, startVertex, endVertex);

        if( path1 != null && path2 != null ) {
            Assert.assertEquals("P1 is "+path1+" P2 is "+path2,path1.getWeight(), path2.getWeight(), 0.01);
        }else {
            Assert.assertNull(path1);
            Assert.assertNull(path2);
        }
    }

    private void addEdge(Graph<Integer, MyWeightedEdge<Integer>> g, int v1, int v2, int wegith) {
        MyWeightedEdge<Integer> edge7 = g.addEdge(v1, v2);
        g.setEdgeWeight(edge7, wegith);
        MyWeightedEdge<Integer> edge1 = g.addEdge(v2, v1);
        g.setEdgeWeight(edge1, wegith);
    }

    @Test
    public void longBenchmark(){
        DijkstraBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> mySolver = new DijkstraBasedAlgorithmSolver<>();
        long start = System.nanoTime();
        for(int j = 0; j < 100; j++) {
//            System.out.println("J is "+j);
            int seed = j +400;
            Random random = new Random(seed);
            int nodesCount = random.nextInt(100) +20 ;
            double probability = +0.2 + random.nextDouble() * 0.8f;

            Graph<Integer, MyWeightedEdge<Integer>> testGraph =
                    MySimpleRandomGraphGenerator.Generate(nodesCount, probability, seed);

            List<Integer> vertices = new ArrayList<>(testGraph.vertexSet());
            for (int i = 0; i < 10; i++) {
                Random shuffleRandom = new Random(seed + i);
                Collections.shuffle(vertices, shuffleRandom);
                Integer startVertex = vertices.get(0);
                Integer endVertex = vertices.get(1);
//                System.out.println("Size: "+mySolver.findTwoShortestPaths(testGraph, startVertex, endVertex, 10)
//                        .size());
            }
        }
        System.out.println("Method took "+(System.nanoTime()-start));
    }

}
