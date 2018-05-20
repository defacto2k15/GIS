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
    private double dijkstraFirstPathWeight;
    private double dijkstraSecondPathWeight;
    private int wattsKParam;

    public SingleMeasure(int nodesCount, int edgesCount, AlgorithmType algorithmType, GraphGeneratorType generatorType,
                         float dijkstraPropability, long time) {

        this.nodesCount = nodesCount;
        this.edgesCount = edgesCount;
        this.algorithmType = algorithmType;
        this.generatorType = generatorType;
        this.dijkstraPropability = dijkstraPropability;
        this.time = time;
    }


    public void setDijkstraFirstPathWeight(double dijkstraFirstPathWeight) {
        this.dijkstraFirstPathWeight = dijkstraFirstPathWeight;
    }

    public void setDijkstraSecondPathWeight(double dijkstraSecondPathWeight) {
        this.dijkstraSecondPathWeight = dijkstraSecondPathWeight;
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

    public double getDijkstraFirstPathWeight() {
        return dijkstraFirstPathWeight;
    }

    public double getDijkstraSecondPathWeight() {
        return dijkstraSecondPathWeight;
    }

    public int getWattsKParam() {
        return wattsKParam;
    }
}
