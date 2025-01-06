package org.example.parsers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupParserTest {
    GroupParser groupParser = new GroupParser();

    @Test
    public void groupParserOneItem() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("author.name");

        List<String> result = groupParser.parse("author.name");
        assertEquals(expectedList.get(0), result.get(0));
    }

    @Test
    public void groupParserSeveralItems() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("author.name");
        expectedList.add("author.year");

        List<String> result = groupParser.parse("author.name, author.year");

        assertAll(
                () -> assertEquals(expectedList.get(0), result.get(0)),
                () -> assertEquals(expectedList.get(1), result.get(1)),
                () -> assertEquals(expectedList.size(), result.size())
        );
    }

    @Test
    public void groupParserSeveralItemsWithSpaces() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("author.name");
        expectedList.add("author.year");

        List<String> result = groupParser.parse("  author.name , author.year  ");

        assertAll(
                () -> assertEquals(expectedList.get(0), result.get(0)),
                () -> assertEquals(expectedList.get(1), result.get(1)),
                () -> assertEquals(expectedList.size(), result.size())
        );
    }



}