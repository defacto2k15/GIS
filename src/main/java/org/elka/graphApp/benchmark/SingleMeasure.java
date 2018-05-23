package org.elka.graphApp.benchmark;

import org.elka.graphApp.algorithms.AlgorithmType;
import org.elka.graphApp.generators.GraphGeneratorType;

/**
 * Created by defacto on 5/20/2018.
 */
public class SingleMeasure {

    private final int nodesCount;
    private final int edgesCount;
    private final AlgorithmType algorithmType;
    private final GraphGeneratorType generatorType;
    private final float dijkstraPropability;
    private final long time;
    private double firstPathWeight;
    private double secondPathWeight;
    private int wattsKParam;
    private int dijkstraTriesCount;

    public SingleMeasure(int nodesCount, int edgesCount, AlgorithmType algorithmType, GraphGeneratorType generatorType,
                         float dijkstraPropability, long time) {

        this.nodesCount = nodesCount;
        this.edgesCount = edgesCount;
        this.algorithmType = algorithmType;
        this.generatorType = generatorType;
        this.dijkstraPropability = dijkstraPropability;
        this.time = time;
    }


    public void setFirstPathWeight(double dijkstraFirstPathWeight) {
        this.firstPathWeight = dijkstraFirstPathWeight;
    }

    public void setSecondPathWeight(double dijkstraSecondPathWeight) {
        this.secondPathWeight = dijkstraSecondPathWeight;
    }

    public void setWattsKParam(int wattsKParam) {
        this.wattsKParam = wattsKParam;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public int getEdgesCount() {
        return edgesCount;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public GraphGeneratorType getGeneratorType() {
        return generatorType;
    }

    public float getDijkstraPropability() {
        return dijkstraPropability;
    }

    public long getTime() {
        return time;
    }

    public double getFirstPathWeight() {
        return firstPathWeight;
    }

    public double getSecondPathWeight() {
        return secondPathWeight;
    }

    public int getWattsKParam() {
        return wattsKParam;
    }

    public void setDijkstraTriesCount(int dijkstraTriesCount) {
        this.dijkstraTriesCount = dijkstraTriesCount;
    }

    public int getDijkstraTriesCount() {
        return dijkstraTriesCount;
    }
}
