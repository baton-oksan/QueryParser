package org.example.parsers;

import org.example.SQLParser;
import org.example.Source;
import org.example.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionParser implements Parser<List<Condition>> {
    public List<Condition> parse(String conditionString) {
        //если строка содержит between, то с помощью регулярки и группы, достать этот between и заменить на пробел
        //далее сплиттим получившуюся строку по слову AND или OR и получаем каждое условие по отдельности
        List<Condition> whereList = new ArrayList<>();
        if (conditionString.contains("between") || conditionString.contains("BETWEEN")) {
            Pattern betweenPattern = Pattern.compile("(\\S+)\\s+(BETWEEN)\\s+(\\S+\\s+AND\\s+\\S+)\\s+(AND)?");
            Matcher betweenMatch = betweenPattern.matcher(conditionString);
            while (betweenMatch.find()) {
                Condition whereClause = new Condition(betweenMatch.group(1), betweenMatch.group(2), new Source(betweenMatch.group(3)));
                whereList.add(whereClause);
            }
            conditionString = betweenMatch.replaceAll("").trim();
        }

        if (conditionString.length() > 3) {
            String[] whereSplit = conditionString.split(" AND | OR ", Pattern.CASE_INSENSITIVE);
            for (int i = 0; i < whereSplit.length; i++) {
                String whereItem = whereSplit[i];
                String[] whereItemToArray = whereItem.split(" ");
                //если whereItemToArray[2] содержит  if (fromString.contains("<Subquery_")) , то надо создать объект org.example.Query и засунуть его аргументом в конструктор
                if (whereItemToArray[2].contains("<Subquery_")) {
                    String subquery = SQLParser.subqueriesMap.get(whereItemToArray[2]);
                    Source source = new Source(SQLParser.parseQuery(subquery));
                    whereList.add(new Condition(whereItemToArray[0], whereItemToArray[1], source));
                } else
                    whereList.add(new Condition(whereItemToArray[0], whereItemToArray[1], new Source(whereItemToArray[2])));
            }
        }
        return whereList;
    }

}
