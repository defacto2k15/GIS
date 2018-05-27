package org.elka.graphApp.algorithms;

import org.elka.graphApp.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurballeBasedAlgorithmSolverTest {

    @Test
    public void simpleTestWithFirstPathBlocked() {
        Graph<Integer, MyWeightedEdge<Integer>> testGraph =
                new SimpleDirectedWeightedGraph<>((e1, e2) -> new MyWeightedEdge<Integer>());

        testGraph.addVertex(1);
        testGraph.addVertex(2);
        testGraph.addVertex(3);
        testGraph.addVertex(4);
        testGraph.addVertex(5);

        addEdge(testGraph, 1, 2, 7);
        addEdge(testGraph, 2, 3, 6);
        addEdge(testGraph, 3, 5, 4);
        addEdge(testGraph, 1, 4, 5);
        addEdge(testGraph, 2, 4, 3);
        addEdge(testGraph, 4, 5, 8);
        addEdge(testGraph, 4, 3, 2);

        Integer startVertex = new ArrayList<>(testGraph.vertexSet()).get(0);
        Integer endVertex = new ArrayList<>(testGraph.vertexSet()).get(4);

        SurballeBasedAlgorithmSolver<Integer, MyWeightedEdge<Integer>> mySolver = new SurballeBasedAlgorithmSolver<>();
        List<GraphPath<Integer, MyWeightedEdge<Integer>>> outPaths =
                mySolver.findTwoShortestPaths(testGraph, startVertex, endVertex);

        Assert.assertEquals(2, outPaths.size());
        Assert.assertThat(outPaths.get(0).getVertexList(), org.hamcrest.CoreMatchers.is(Arrays.asList(1, 4, 3, 5)));
        Assert.assertThat(outPaths.get(1).getVertexList(), org.hamcrest.CoreMatchers.is(Arrays.asList(1, 2, 4, 5)));
    }


    private void addEdge(Graph<Integer, MyWeightedEdge<Integer>> g, int v1, int v2, int weight) {
        MyWeightedEdge<Integer> edge = g.addEdge(v1, v2);
        g.setEdgeWeight(edge, weight);
        MyWeightedEdge<Integer> edgeReversed = g.addEdge(v2, v1);
        g.setEdgeWeight(edgeReversed, weight);
    }

}