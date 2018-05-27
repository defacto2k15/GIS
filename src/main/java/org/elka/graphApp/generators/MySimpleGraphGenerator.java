package org.elka.graphApp.generators;

import org.elka.graphApp.algorithms.MyWeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by defacto on 5/2/2018.
 */
public class MySimpleGraphGenerator {

    private static final int MAX_WEIGHT = 100;

    public Graph<Integer,MyWeightedEdge<Integer>> Generate(int nodesCount, double propabilityOfEdge, int seed){
        GnpRandomGraphGenerator<Integer, MyWeightedEdge<Integer>> generator
                = new GnpRandomGraphGenerator<>(nodesCount, propabilityOfEdge, seed);

        Graph<Integer, MyWeightedEdge<Integer>> generatedGraph =
                new SimpleDirectedWeightedGraph<>((e1, e2) -> new MyWeightedEdge<Integer>());
        final int[] lastInt = {0};
        generator.generateGraph(generatedGraph, () -> (lastInt[0]++), null);

        Random random = new Random(seed);
        for (MyWeightedEdge edge : generatedGraph.edgeSet()) {
            generatedGraph.setEdgeWeight(edge, Math.floor(random.nextDouble()* MAX_WEIGHT));
        }
        RemoveBidirectionalEdges(generatedGraph);

        return generatedGraph;
    }

     private static <T1,T2 extends MyWeightedEdge> void RemoveBidirectionalEdges(Graph<T1,T2> graph) {
        HashMap<T1, List<T1>> neighbourVertices = new HashMap<>();
        List<T2> edgesToRemove = new ArrayList<>();

        for(T2 edge : graph.edgeSet()){
            T1 source = (T1)edge.getSource();
            T1 target = (T1)edge.getTarget();

            if( !neighbourVertices.containsKey(source)){
                neighbourVertices.put(source, new ArrayList<T1>());
            }
            if( !neighbourVertices.containsKey(target)){
                neighbourVertices.put(target, new ArrayList<T1>());
            }

            if(neighbourVertices.get(target).contains(source) || edge.getTarget().equals(edge.getSource())){
                edgesToRemove.add(edge);
            }else {
                neighbourVertices.get(source).add(target);
            }
        }
        edgesToRemove.forEach(graph::removeEdge);
    }
}
