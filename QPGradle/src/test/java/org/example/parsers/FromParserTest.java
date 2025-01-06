package org.example.parsers;

import org.example.query.Source;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FromParserTest {
    FromParser fromParser = new FromParser();

    @Test
    public void parseFromTableName() {
        List<Source> expectedResult = new ArrayList<>();
        expectedResult.add(new Source("table_name"));

        List<Source> result = fromParser.parse("table_name");

        assertAll(
                () -> assertEquals(expectedResult.get(0).getSourceTable(), result.get(0).getSourceTable()),
                () -> assertEquals(expectedResult.size(), result.size())
        );

    }

    @Test
    public void parseFromTableNameWithSpaces() {
        List<Source> expectedResult = new ArrayList<>();
        expectedResult.add(new Source("table_name"));

        List<Source> result = fromParser.parse("  table_name  ");

        assertAll(
                () -> assertEquals(expectedResult.get(0).getSourceTable(), result.get(0).getSourceTable()),
                () -> assertEquals(expectedResult.size(), result.size())
        );
    }

//    @Test
//    public void parseFromSubquery() {
//        List<Source> expectedResult = new ArrayList<>();
//        //SELECT id FROM departments
//        List<String> subqueryColumns = new ArrayList<>();
//        subqueryColumns.add("id");
//        List<Source> subquerySource = new ArrayList<>();
//        subquerySource.add(new Source("departments"));
//        expectedResult.add(new Source(new Query.QueryBuilder().columns(subqueryColumns).fromSources(subquerySource).build()));
//
//        Map<String, String> subqueriesMapTest = new HashMap<String, String>();
//        subqueriesMapTest.put("<Subquery_1>", "SELECT id FROM departments");
//    }





}