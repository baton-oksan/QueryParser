package org.example.parsers;

import org.example.Source;
import org.example.WhereClause;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HavingParser implements Parser<List<WhereClause>> {
    public List<WhereClause> parse(String havingString) {
        List<WhereClause> havingList = new ArrayList<>();
        if (havingString.contains("between") || havingString.contains("BETWEEN")) {
            Pattern betweenPattern = Pattern.compile("(\\S+)\\s+(BETWEEN)\\s+(\\S+\\s+AND\\s+\\S+)\\s+(AND)?");
            Matcher betweenMatch = betweenPattern.matcher(havingString);
            while (betweenMatch.find()) {
                WhereClause whereClause = new WhereClause(betweenMatch.group(1), betweenMatch.group(2), new Source(betweenMatch.group(3)));
                havingList.add(whereClause);
            }
            havingString = betweenMatch.replaceAll("").trim();
        }

        if (havingString.length() > 3) {
            String[] whereSplit = havingString.split(" AND | OR ", Pattern.CASE_INSENSITIVE);
            for (int i = 0; i < whereSplit.length; i++) {
                String whereItem = whereSplit[i];
                String[] whereItemToArray = whereItem.split(" ");
                //если whereItemToArray[2] содержит  if (fromString.contains("<Subquery_")) , то надо создать объект org.example.Query и засунуть его аргументом в конструктор
                havingList.add(new WhereClause(whereItemToArray[0], whereItemToArray[1], new Source(whereItemToArray[2])));
            }
        }
        return havingList;
    }
}
