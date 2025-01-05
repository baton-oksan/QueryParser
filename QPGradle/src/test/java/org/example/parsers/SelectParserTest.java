package org.example.parsers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class SelectParserTest {
    public static SelectParser parser;

    @BeforeAll
    public static void initSelectParser() {
        parser = new SelectParser();
    }

    @Test
    public void parseSelectOneColumn() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("column_name");

        List<String> result = parser.parse("column_name");
        assertEquals(1, result.size());
    }

    @Test
    public void parseSelectOneColumnWithSpaces() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("column_name");

        List<String> result = parser.parse(" column_name ");

        assertAll (
                () -> assertEquals(expectedResult.get(0), result.get(0)),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    public void parseSelectSeveralColumns() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("column_name1");
        expectedResult.add("column_name2");
        expectedResult.add("column_name3");

        List<String> result = parser.parse("column_name1, column_name2, column_name3");
        assertEquals(3, result.size());
    }

    @Test
    public void parseSelectWithExtraSpaces() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("column_name1");
        expectedResult.add("column_name2");
        expectedResult.add("column_name3");

        List<String> result = parser.parse(" column_name1,  column_name2, column_name3  ");
        assertEquals(3, result.size());
    }

    @Test
    public void parseSelectWithExtraSpacesCheckElements() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("column_name1");
        expectedResult.add("column_name2");
        expectedResult.add("column_name3");
        List<String> result = parser.parse(" column_name1,  column_name2, column_name3  ");
        assertEquals(expectedResult.get(0), result.get(0));
    }

}