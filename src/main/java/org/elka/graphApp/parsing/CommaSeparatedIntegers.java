package org.elka.graphApp.parsing;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.devtools.common.options.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommaSeparatedIntegers implements Converter<List<Integer>> {

    private final Splitter splitter;
    private final static char COMMA_SEPARATOR = ',';

    public CommaSeparatedIntegers() {
        this.splitter = Splitter.on(COMMA_SEPARATOR);
    }

    @Override
    public List<Integer> convert(String input) {
        Iterable<String> split = splitter.split(input);
        if ("".equals(input)) {
            return ImmutableList.of();
        }
        List<Integer> integers = new ArrayList<>();
        for (String value : split) {
            if (StringUtils.isNumeric(value)) {
                integers.add(Integer.valueOf(value));
            }
        }
        return integers;
    }

    @Override
    public String getTypeDescription() {
        return "comma - separated list of options";
    }
}
