package org.example.parsers;

import org.example.query.Source;
import org.example.query.Condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionParser implements Parser<List<Condition>> {
    private List<Condition> conditionsList = new ArrayList<>();

    public List<Condition> parse(String conditionString) {
        if (conditionString.contains("between") || conditionString.contains("BETWEEN"))
              conditionString = parseBetween(conditionString);

        if (conditionString.length() > 3) //if we have something in our string after extracting between
            parseCondition(conditionString);

        return conditionsList;
    }

    String parseBetween(String conditionString) {
        //если строка содержит between, то с помощью регулярки и группы, достать этот between и заменить на пробел
        //далее сплиттим получившуюся строку по слову AND или OR и получаем каждое условие по отдельности
        String[] splittedCondition = conditionString.split(" ");
        Pattern betweenPattern;
        if (splittedCondition.length > 5) {
            betweenPattern = Pattern.compile("(?:AND\\s+)?(\\S+)\\s+(BETWEEN)\\s+(\\S+\\s+AND\\s+\\S+)(?:\\s+AND)?", Pattern.CASE_INSENSITIVE);
        } else {
            betweenPattern = Pattern.compile("(\\S+)\\s+(BETWEEN)\\s+(\\S+\\s+AND\\s+\\S+)", Pattern.CASE_INSENSITIVE);
        }

        Matcher betweenMatch = betweenPattern.matcher(conditionString);

        while (betweenMatch.find()) {
            Condition conditionClause = new Condition(betweenMatch.group(1), betweenMatch.group(2), new Source(betweenMatch.group(3)));
            conditionsList.add(conditionClause);
        }
        conditionString = betweenMatch.replaceAll("").trim();
        return conditionString;
    }

    void parseCondition(String conditionString) {
        String[] conditionSplit = conditionString.split(" AND | OR ", Pattern.CASE_INSENSITIVE);
        for (int i = 0; i < conditionSplit.length; i++) {
            String conditionItem = conditionSplit[i];
            String[] conditionItemToArray = conditionItem.split(" ");
            //если conditionItemToArray[2] содержит  if (fromString.contains("<Subquery_")) , то надо создать объект org.example.query.Query и засунуть его аргументом в конструктор
            if (conditionItemToArray[2].contains("<Subquery_")) {
                String subquery = SQLParser.subqueriesMap.get(conditionItemToArray[2]);
                Source source = new Source(SQLParser.parseQuery(subquery));
                conditionsList.add(new Condition(conditionItemToArray[0], conditionItemToArray[1], source));
            } else
                conditionsList.add(new Condition(conditionItemToArray[0], conditionItemToArray[1],
                        new Source(String.join(" ", Arrays.copyOfRange(conditionItemToArray, 2, conditionItemToArray.length)))));
        }
    }
}
