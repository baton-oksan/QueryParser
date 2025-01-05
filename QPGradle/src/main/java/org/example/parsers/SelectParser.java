package org.example.parsers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectParser implements Parser<List<String>> {
    public List<String> parse(String selectString) {
        List<String> selectParseResult = Arrays.stream(selectString.split(","))
                .map(String::trim)
                .collect(Collectors.toCollection(ArrayList::new));
        return selectParseResult;
    }
}


