package org.elka.graphApp.benchmark;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import lombok.Getter;
import lombok.Setter;
import org.elka.graphApp.parsing.CommaSeparatedFloats;
import org.elka.graphApp.parsing.CommaSeparatedIntegers;

import java.util.List;

/**
 * Created by defacto on 5/20/2018.
 */
@Getter
@Setter
public class BenchmarkConfiguration extends OptionsBase {

    @Option(
            name = "help",
            abbrev = 'h',
            help = "Prints usage info.",
            defaultValue = "true"
    )
    public boolean help;

    @Option(
            name = "minNodeCount",
            abbrev = 'n',
            help = "This sets minimal node count in tests",
            defaultValue = "10"
    )
    public int minNodeCount;
    @Option(
            name = "maxNodeCount",
            abbrev = 'm',
            help = "This sets maximal nodes count in tests",
            defaultValue = "100"
    )
    public int maxNodeCount;
    @Option(
            name = "dijkstra",
            abbrev = 'd',
            help = "This flag enables dijkstra tests",
            defaultValue = "true"
    )
    public boolean dijkstraTests;
    @Option(
            name = "surballe",
            abbrev = 's',
            help = "This flag enables surballe tests",
            defaultValue = "false"
    )
    public boolean surballeTests;
    @Option(
            name = "tests",
            abbrev = 't',
            help = "Sets number of tests",
            defaultValue = "600"
    )
    public int testsCount;


    @Option(
            name = "erodosProbabilities",
            abbrev = 'e',
            help = "This sets erdos probabilities per test",
            allowMultiple = true,
            converter = CommaSeparatedFloats.class,
            defaultValue = "0.8f"
    )
    public List<Float> erdosProbabilitiesPerTest;
    @Option(
            name = "wattsProbabilities",
            abbrev = 'w',
            help = "This sets watts probabilities per test",
            converter = CommaSeparatedFloats.class,
            allowMultiple = true,
            category = "startup",
            defaultValue = "0.8f"
    )
    public List<Float> wattsProbabilitiesPerTest;

    @Option(
            name = "kWatts",
            abbrev = 'k',
            help = "This sets kWatts params per test",
            allowMultiple = true,
            defaultValue = "8",
            converter = CommaSeparatedIntegers.class
    )
    public List<Integer> wattsKParamsPerTest;


    public boolean shouldTestDijkstra() {
        return dijkstraTests;
    }

    public boolean shouldTestSurballe() {
        return surballeTests;
    }


}
