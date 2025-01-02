package org.example;

import org.example.parsers.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SQLParser {
    //private static ArrayList<String> keywords =  new ArrayList<String>(Arrays.asList("select", "from", "where", "left join", "right join", "limit", "offset", "group by", "order by", "and", "in", "asc", "desc", "as", "on"));
    private static final int SELECT_GROUP = 1;
    private static final int FROM_GROUP = 2;
    private static final int JOIN_GROUP = 3;
    private static final int WHERE_GROUP = 4;
    private static final int GROUP_BY_GROUP = 5;
    private static final int HAVING_GROUP = 6;
    private static final int ORDER_GROUP = 7;
    private static final int LIMIT_GROUP = 8;
    private static final int OFFSET_GROUP = 9;
    public static Map<String, String> subqueriesMap = new HashMap<String, String>();
    private static SelectParser selectParser;
    private static FromParser fromParser;
    private static JoinParser joinParser;
    private static WhereParser whereParser;
    private static GroupParser groupParser;
    private static HavingParser havingParser;
    private static SortParser sortParser;


    public static Query parseQuery (String inputQuery) {
        String workingQuery = inputQuery;
        workingQuery = extractSubqueries(workingQuery);
        //получили query без subqueries


        Pattern queryPattern = Pattern.compile(
                "\\(?SELECT\\s+(.*?)\\s+FROM\\s+(\\w+)(\\s+(?:INNER|LEFT|RIGHT|FULL)?\\s*JOIN\\s+\\S+.*?)?" +
                        "(?:\\s+WHERE\\s+(.*?))?(?:\\s+GROUP BY\\s+(.*?))?(?:\\s+HAVING\\s+(.*?))?(?:\\s+ORDER BY\\s+(.*?))?" +
                        "(?:\\s+LIMIT\\s+(\\d+))?(?:\\s+OFFSET\\s+(\\d+))?\\)?;",
                Pattern.CASE_INSENSITIVE
        );
        Matcher queryMatcher = queryPattern.matcher(workingQuery);

        Query query = new Query();
        initParsers();

        if(queryMatcher.find()) {
            if (queryMatcher.group(SELECT_GROUP) != null)
                query.setColumns(selectParser.parse(queryMatcher.group(SELECT_GROUP)));
             else
                 System.out.println("Invalid query");

            if (queryMatcher.group(FROM_GROUP) != null) {
                query.setFromSources(fromParser.parse(queryMatcher.group(FROM_GROUP)));
            } else
                System.out.println("Invalid query");

            if (queryMatcher.group(JOIN_GROUP) != null)
                query.setJoins(joinParser.parse(queryMatcher.group(JOIN_GROUP)));

            if (queryMatcher.group(WHERE_GROUP) != null)
                query.setWheres(whereParser.parse(queryMatcher.group(WHERE_GROUP)));

            if (queryMatcher.group(GROUP_BY_GROUP) != null)
                query.setGroupByColumns(groupParser.parse(queryMatcher.group(GROUP_BY_GROUP)));

            if (queryMatcher.group(HAVING_GROUP) != null)
                query.setHavingClauses(havingParser.parse(queryMatcher.group(HAVING_GROUP)));

            if (queryMatcher.group(ORDER_GROUP) != null)
                query.setSortColumns(sortParser.parse(queryMatcher.group(ORDER_GROUP)));

            if (queryMatcher.group(LIMIT_GROUP) != null)
                query.setLimit(Integer.parseInt(queryMatcher.group(LIMIT_GROUP)));

            if (queryMatcher.group(OFFSET_GROUP) != null)
                query.setOffset(Integer.parseInt(queryMatcher.group(OFFSET_GROUP)));
        }

        return query;
    }

    private static void initParsers() {
        selectParser = new SelectParser();
        fromParser = new FromParser();
        joinParser = new JoinParser();
        whereParser = new WhereParser();
        groupParser = new GroupParser();
        havingParser = new HavingParser();
        sortParser = new SortParser();
    }

    private static String extractSubqueries (String workingQuery) {
        Pattern subqueryPattern = Pattern.compile("(\\(\\s*SELECT\\s+.*?\\s+FROM\\s+.*?\\))", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = subqueryPattern.matcher(workingQuery);
        int subqueryCounter = 1;
        while (matcher.find()) {
            String subquery = matcher.group(1);
            String subqueryTrimmed = subquery.substring(1, subquery.length() - 1).trim();
            subqueryTrimmed += ";";
            String placeholder = "<Subquery_" + subqueryCounter + ">";
            subqueriesMap.put(placeholder, subqueryTrimmed);
            workingQuery = workingQuery.replace(subquery, placeholder);
            subqueryCounter++;
        }
        return workingQuery;
    }

}
