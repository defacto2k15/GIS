package org.elka.graphApp;

import org.elka.graphApp.algorithms.DijkstraBasedAlgorithmSolver;
import org.elka.graphApp.algorithms.MySimpleRandomGraphGenerator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
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

}
