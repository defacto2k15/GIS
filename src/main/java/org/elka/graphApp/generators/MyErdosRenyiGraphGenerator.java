package org.elka.graphApp.generators;

import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.util.Random;

/**
 * Created by defacto on 5/2/2018.
 */
public class MyErdosRenyiGraphGenerator  {

    private static final int MAX_WEIGHT = 100;

    public Graph<Integer,MyWeightedEdge<Integer>> Generate(int nodesCount, double propabilityOfEdge, int seed){
        GnpRandomGraphGenerator<Integer,MyWeightedEdge<Integer>> generator
                = new GnpRandomGraphGenerator<>(nodesCount, propabilityOfEdge, seed);
        Graph<Integer, MyWeightedEdge<Integer>> firstGraph =
                new SimpleGraph<>((e1, e2) -> new MyWeightedEdge<>());
        final int[] lastInt = {0};
        generator.generateGraph(firstGraph, () -> (lastInt[0]++), null);

        Graph<Integer, MyWeightedEdge<Integer>> outGraph
                = new SimpleDirectedWeightedGraph<>((e1,e2)-> new MyWeightedEdge<>());

        Random random = new Random(seed);
        firstGraph.vertexSet().forEach(outGraph::addVertex);
        firstGraph.edgeSet().forEach(e -> {
            Integer source = e.getSource();
            Integer target = e. getTarget();
            if(random.nextBoolean()){
                source = e.getTarget();
                target = e.getSource();
            }
            outGraph.addEdge(source, target, new MyWeightedEdge<>());
        });

        for (MyWeightedEdge<Integer> edge : outGraph.edgeSet()) {
            outGraph.setEdgeWeight(edge, Math.floor(random.nextDouble()* MAX_WEIGHT));
        }
        return outGraph;
    }
}
