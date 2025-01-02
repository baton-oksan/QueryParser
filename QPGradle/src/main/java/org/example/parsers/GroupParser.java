package org.example.parsers;

import java.util.ArrayList;
import java.util.List;

public class GroupParser implements Parser<List<String>> {
    public List<String> parse(String groupByString) {
        List<String> groupTables = new ArrayList<>();
        String[] groupByTablesArray = groupByString.split(",");
        for (String table: groupByTablesArray) {
            groupTables.add(table.trim());
        }
        return groupTables;
    }
}
