package org.example.parsers;

import org.example.query.Condition;
import org.example.query.Source;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConditionParserTest {
    ConditionParser conditionParser = new ConditionParser();

    @Test
    public void parseBetweenIfItIsTheOnlyCondition() {
        String expectedResult = "";
        String testData = "salary BETWEEN 50000 AND 100000";

        String result = conditionParser.parseBetween(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void parseBetweenIfItIsNotTheOnlyCondition() {
        String expectedResult = "job_title = 'developer'";
        String testData = "salary BETWEEN 50000 AND 100000 AND job_title = 'developer'";

        String result = conditionParser.parseBetween(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void parseBetweenIfItIsNotTheOnlyConditionAndIsInTheEnd() {
        String expectedResult = "job_title = 'developer'";
        String testData = "job_title = 'developer' AND salary BETWEEN 50000 AND 100000";

        String result = conditionParser.parseBetween(testData);
        assertEquals(expectedResult, result);
    }


    @Test
    public void parseConditionWithoutSubquery() {
        List<Condition> expectedConditions = new ArrayList<>();
        expectedConditions.add(new Condition("job_title", "=", new Source("'developer'")));

        List<Condition> resultConditions = conditionParser.parse("job_title = 'developer'");

        assertAll(
                () -> assertEquals(expectedConditions.get(0).getSource().getSourceTable(), resultConditions.get(0).getSource().getSourceTable()),
                () -> assertEquals(expectedConditions.get(0).getColumnName(), resultConditions.get(0).getColumnName()),
                () -> assertEquals(expectedConditions.get(0).getOperator(), expectedConditions.get(0).getOperator())
        );
    }

    @Test
    public void parseBetweenIfAnotherConditionConsistsOfMoreThanThreeWords() {
        String expectedResult = "location = 'New York'";
        String testData = "location = 'New York' AND salary BETWEEN 50000 AND 100000";

        String result = conditionParser.parseBetween(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void parseConditionThatConsistsOfMoreThanThreeWords() {
        List<Condition> expectedConditions = new ArrayList<>();
        expectedConditions.add(new Condition("location", "=", new Source("'New York'")));

        List<Condition> resultConditions = conditionParser.parse("location = 'New York'");
        assertAll(
                () -> assertEquals(expectedConditions.get(0).getSource().getSourceTable(), resultConditions.get(0).getSource().getSourceTable()),
                () -> assertEquals(expectedConditions.get(0).getColumnName(), resultConditions.get(0).getColumnName()),
                () -> assertEquals(expectedConditions.get(0).getOperator(), expectedConditions.get(0).getOperator())
        );
    }

}