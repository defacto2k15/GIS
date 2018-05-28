package org.elka.graphApp.parsing;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.devtools.common.options.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class CommaSeparatedFloats implements Converter<List<Float>> {
    private static final char COMMA_SEPARATOR = ',';
    private final Splitter splitter;

    public CommaSeparatedFloats() {
        this.splitter = Splitter.on(COMMA_SEPARATOR);
    }

    @Override
    public List<Float> convert(String input) {
        Iterable<String> split = splitter.split(input);
        if ("".equals(input)) {
            return ImmutableList.of();
        }
        List<Float> integers = new ArrayList<>();
        for (String value : split) {
            if (NumberUtils.isCreatable(value)) {
                integers.add(Float.valueOf(value));
            }
        }
        return integers;
    }

    @Override
    public String getTypeDescription() {
        return "comma -separated list of options";
    }
}
