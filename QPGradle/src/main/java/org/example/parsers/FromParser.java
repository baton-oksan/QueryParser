package org.example.parsers;

import org.example.query.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FromParser implements Parser<List<Source>> {
    public List<Source> parse(String fromString) {
        if (fromString.contains("<Subquery_")) {
            List<Source> sourceList = new ArrayList<>();
            String subquery = SQLParser.subqueriesMap.get(fromString);
            Source source = new Source(SQLParser.parseQuery(subquery));
            sourceList.add(source);
            return sourceList;
        } else {
            List<Source> fromTables = Arrays.stream(fromString.split(","))
                    .map(String::trim)
                    .map(Source::new)
                    .collect(Collectors.toList());
            return fromTables;
        }
    }
}
