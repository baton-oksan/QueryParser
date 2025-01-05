package org.example.parsers;

import org.example.Join;
import org.example.Source;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoinParserTest {
    JoinParser joinParser = new JoinParser();

    @Test
    public void parseJoinWithJoinTypeSpecification() {
        List<Join> expectedList = new ArrayList<>();
        expectedList.add(new Join(new Source("book"), "LEFT", "(author.id = book.author_id)"));

        List<Join> result = joinParser.parse("LEFT JOIN book ON (author.id = book.author_id)");

        assertAll(
                () -> assertEquals(expectedList.size(), result.size()),
                () -> assertEquals(expectedList.get(0).getJoinSource().getSourceTable(), result.get(0).getJoinSource().getSourceTable()),
                () -> assertEquals(expectedList.get(0).getJoinType(), result.get(0).getJoinType()),
                () -> assertEquals(expectedList.get(0).getOnCondition(), result.get(0).getOnCondition())
        );

    }

    @Test
    public void parseJoinWithoutJoinTypeSpecification() {
        List<Join> expectedList = new ArrayList<>();
        expectedList.add(new Join(new Source("book"), "(author.id = book.author_id)"));

        List<Join> result = joinParser.parse("JOIN book ON (author.id = book.author_id)");

        assertAll(
                () -> assertEquals(expectedList.size(), result.size()),
                () -> assertEquals(expectedList.get(0).getJoinSource().getSourceTable(), result.get(0).getJoinSource().getSourceTable()),
                () -> assertEquals(expectedList.get(0).getJoinType(), result.get(0).getJoinType()),
                () -> assertEquals(expectedList.get(0).getOnCondition(), result.get(0).getOnCondition())
        );

    }

}