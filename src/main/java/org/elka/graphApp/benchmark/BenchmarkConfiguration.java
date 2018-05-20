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

    public int getMinNodeCount() {
        return 10;
    }

    public int getMaxNodeCount() {
        return 100;
    }

    public int getTestsCount() {
        return 600;
    }

    public List<Float> getErdosPropabilitiesPerTest() {
        return Arrays.asList(0.8f);
    }

    public List<Float> getWattsPropabilitiesPerTest() {
        return getErdosPropabilitiesPerTest();
    }

    public List<Integer> getWattsKParamsPerTest() {
        return Arrays.asList(8);
    }

    public boolean shouldTestSurballe() {
        return false;
    }
}
