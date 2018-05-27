package org.elka.graphApp.benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by defacto on 5/20/2018.
 */
public class BenchmarkConfiguration {
    public boolean shouldTestDijkstra() {
        return true;
    }

    public boolean shouldTestSurballe() {
        return false;
    }

    public int getMinNodeCount() {
        return 10;
    }

    public int getMaxNodeCount() {
        return 200;
    }

    public int getTestsCount() {
        return 50;
    }

    public int getPerGraphTestsCount(){
        return 3;
    }

    public List<Float> getErdosPropabilitiesPerTest() {
        return Arrays.asList(0.2f, 0.4f, 0.6f, 0.8f, 0.9f);
    }

    public List<Float> getWattsPropabilitiesPerTest() {
        return getErdosPropabilitiesPerTest();
    }

    public List<Integer> getWattsKParamsPerTest() {
        return Arrays.asList(8);
    }

    public List<Float> getWattsLerpKParamsPerTest() {
        return Arrays.asList(0.2f, 0.4f, 0.6f, 0.8f);
    }

}
