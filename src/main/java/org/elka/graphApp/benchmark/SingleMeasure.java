package org.elka.graphApp.benchmark;

import org.elka.graphApp.generators.GraphGeneratorType;

/**
 * Created by defacto on 5/20/2018.
 */
public class SingleMeasure {

    private final int nodesCount;
    private final int edgesCount;
    private final GraphGeneratorType generatorType;
    private final float generatorPropability;
    private int wattsKParam;
    private int dijkstraTriesCount;
    private long dijkstraTime;
    private long suurballeTime;
    private double dijkstraFirstPathWeight;
    private double dijkstraSecondPathWeight;
    private double suurballeFirstPathWeight;
    private double suurballeSecondPathWeight;

    public SingleMeasure(int nodesCount, int edgesCount,GraphGeneratorType
            generatorType, float
            generatorPropability) {

        this.nodesCount = nodesCount;
        this.edgesCount = edgesCount;
        this.generatorType = generatorType;
        this.generatorPropability = generatorPropability;
    }

    public void setDijkstraTriesCount(int dijkstraTriesCount) {
        this.dijkstraTriesCount = dijkstraTriesCount;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public int getEdgesCount() {
        return edgesCount;
    }

    public GraphGeneratorType getGeneratorType() {
        return generatorType;
    }

    public float getGeneratorPropability() {
        return generatorPropability;
    }

    public int getWattsKParam() {
        return wattsKParam;
    }

    public void setWattsKParam(int wattsKParam) {
        this.wattsKParam = wattsKParam;
    }

    public int getDijkstraTriesCount() {
        return dijkstraTriesCount;
    }

    public long getDijkstraTime() {
        return dijkstraTime;
    }

    public void setDijkstraTime(long dijkstraTime) {
        this.dijkstraTime = dijkstraTime;
    }

    public long getSuurballeTime() {
        return suurballeTime;
    }

    public void setSuurballeTime(long suurballeTime) {
        this.suurballeTime = suurballeTime;
    }

    public double getDijkstraFirstPathWeight() {
        return dijkstraFirstPathWeight;
    }

    public void setDijkstraFirstPathWeight(double dijkstraFirstPathWeight) {
        this.dijkstraFirstPathWeight = dijkstraFirstPathWeight;
    }

    public double getDijkstraSecondPathWeight() {
        return dijkstraSecondPathWeight;
    }

    public void setDijkstraSecondPathWeight(double dijkstraSecondPathWeight) {
        this.dijkstraSecondPathWeight = dijkstraSecondPathWeight;
    }

    public double getSuurballeFirstPathWeight() {
        return suurballeFirstPathWeight;
    }

    public void setSuurballeFirstPathWeight(double suurballeFirstPathWeight) {
        this.suurballeFirstPathWeight = suurballeFirstPathWeight;
    }

    public double getSuurballeSecondPathWeight() {
        return suurballeSecondPathWeight;
    }

    public void setSuurballeSecondPathWeight(double suurballeSecondPathWeight) {
        this.suurballeSecondPathWeight = suurballeSecondPathWeight;
    }
}
