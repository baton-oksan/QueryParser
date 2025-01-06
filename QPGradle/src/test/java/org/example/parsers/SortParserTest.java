package org.example.parsers;

import org.example.OrderDirectionType;
import org.example.Sort;
import org.example.Source;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortParserTest {
    SortParser sortParser = new SortParser();

    @Test
    public void sortParserWithOrderDirection() {
        List<Sort> expectedSortList = new ArrayList<>();
        expectedSortList.add(new Sort(OrderDirectionType.DESC, new Source("table_name")));

        List<Sort> result = sortParser.parse("table_name DESC");
        assertAll(
                () -> assertEquals(expectedSortList.size(), result.size()),
                () -> assertEquals(expectedSortList.get(0).getOrderDirectionType(), result.get(0).getOrderDirectionType()),
                () -> assertEquals(expectedSortList.get(0).getOrderSource().getSourceTable(), result.get(0).getOrderSource().getSourceTable())
        );

    }

    @Test
    public void sortParserWithoutOrderDirection() {
        List<Sort> expectedSortList = new ArrayList<>();
        expectedSortList.add(new Sort(OrderDirectionType.ASC, new Source("table_name")));

        List<Sort> result = sortParser.parse("table_name");
        assertAll(
                () -> assertEquals(expectedSortList.size(), result.size()),
                () -> assertEquals(expectedSortList.get(0).getOrderDirectionType(), result.get(0).getOrderDirectionType()),
                () -> assertEquals(expectedSortList.get(0).getOrderSource().getSourceTable(), result.get(0).getOrderSource().getSourceTable())
        );
    }

    @Test
    public void parseSortSeveralEntries() {
        List<Sort> expectedSortList = new ArrayList<>();
        expectedSortList.add(new Sort(OrderDirectionType.ASC, new Source("table_name")));
        expectedSortList.add(new Sort(OrderDirectionType.DESC, new Source("table_name2")));

        List<Sort> result = sortParser.parse("table_name, table_name2 DESC");
        assertAll(
                () -> assertEquals(expectedSortList.size(), result.size()),
                () -> assertEquals(expectedSortList.get(0).getOrderDirectionType(), result.get(0).getOrderDirectionType()),
                () -> assertEquals(expectedSortList.get(0).getOrderSource().getSourceTable(), result.get(0).getOrderSource().getSourceTable()),
                () -> assertEquals(expectedSortList.get(1).getOrderDirectionType(), result.get(1).getOrderDirectionType()),
                () -> assertEquals(expectedSortList.get(1).getOrderSource().getSourceTable(), result.get(1).getOrderSource().getSourceTable())
        );

    }
  
}