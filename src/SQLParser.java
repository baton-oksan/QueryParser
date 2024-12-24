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
    public static Map<String, String> subqueriesMap;

    public static Query parseQuery (String inputQuery) {

        String workingQuery = inputQuery;

        subqueriesMap = new HashMap<String, String>();
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
        //получили query без subqueries


        Pattern queryPattern = Pattern.compile(
                "\\(?SELECT\\s+(.*?)\\s+FROM\\s+(\\w+)(\\s+(?:INNER|LEFT|RIGHT|FULL)?\\s*JOIN\\s+\\S+.*?)?" +
                        "(?:\\s+WHERE\\s+(.*?))?(?:\\s+GROUP BY\\s+(.*?))?(?:\\s+HAVING\\s+(.*?))?(?:\\s+ORDER BY\\s+(.*?))?" +
                        "(?:\\s+LIMIT\\s+(\\d+))?(?:\\s+OFFSET\\s+(\\d+))?\\)?;",
                Pattern.CASE_INSENSITIVE
        );
        Matcher queryMatcher = queryPattern.matcher(workingQuery);
        Query query = new Query();
        if(queryMatcher.find()) {
            if (queryMatcher.group(SELECT_GROUP) != null)
                query.setColumns(parseSelect(queryMatcher.group(SELECT_GROUP)));
             else
                 System.out.println("Invalid query");

            if (queryMatcher.group(FROM_GROUP) != null) {
                query.setFromSources(parseFromSubquery(queryMatcher.group(FROM_GROUP)));
            } else
                System.out.println("Invalid query");

            if (queryMatcher.group(JOIN_GROUP) != null)
                query.setJoins(parseJoin(queryMatcher.group(JOIN_GROUP)));

            if (queryMatcher.group(WHERE_GROUP) != null)
                query.setWheres(parseWhere(queryMatcher.group(WHERE_GROUP)));

            if (queryMatcher.group(GROUP_BY_GROUP) != null)
                query.setGroupByColumns(parseGroupBy(queryMatcher.group(GROUP_BY_GROUP)));

            if (queryMatcher.group(HAVING_GROUP) != null)
                query.setHavingClauses(parseHaving(queryMatcher.group(HAVING_GROUP)));

            if (queryMatcher.group(ORDER_GROUP) != null)
                query.setSortColumns(parseSort(queryMatcher.group(ORDER_GROUP)));

            if (queryMatcher.group(LIMIT_GROUP) != null)
                query.setLimit(Integer.parseInt(queryMatcher.group(LIMIT_GROUP)));

            if (queryMatcher.group(OFFSET_GROUP) != null)
                query.setOffset(Integer.parseInt(queryMatcher.group(OFFSET_GROUP)));
        }

        return query;
    }

    private static ArrayList<String> parseSelect(String selectString) {
        ArrayList<String> selectParseResult = Arrays.stream(selectString.split(","))
                .map(String::trim)
                .collect(Collectors.toCollection(ArrayList::new));
        return selectParseResult;
    }

    private static List<Source> parseFromSubquery (String fromString) {
        if (fromString.contains("<Subquery_")) {
            List<Source> sourceList = new ArrayList<>();
            String subquery = subqueriesMap.get(fromString);
            Source source = new Source(SQLParser.parseQuery(subquery));
            sourceList.add(source);
            return sourceList;
        } else {
            List<Source> fromTables = Arrays.stream(fromString.split(","))
                    .map(String::trim)
                    .map(Source::new)
                    .collect(Collectors.toList());
            return fromTables;
        }

    }

    private static List<Join> parseJoin (String joinString) {
        Join join = new Join();
        int offset = 0;
        System.out.println(joinString);
        joinString = joinString.trim();
        String[] joinSplit = joinString.split(" ");

        if (Arrays.stream(JoinType.values()).anyMatch(enumValue -> enumValue.name().equals(joinSplit[0]))) {
            join.setJoinType(joinSplit[offset]);
            offset += 2;
        } else {
            offset++;
        }
        join.setJoinSource(new Source(joinSplit[offset]));
        offset += 2;
        String[] onCondition = Arrays.copyOfRange(joinSplit, offset, joinSplit.length);
        join.setOnCondition(String.join(" ", onCondition));
        List<Join> joinsList = new ArrayList<Join>();
        joinsList.add(join);
        return joinsList;
    }

    private static List<WhereClause> parseWhere (String whereString) {
        //если строка содержит between, то с помощью регулярки и группы, достать этот between и заменить на пробел
        //далее сплиттим получившуюся строку по слову AND или OR и получаем каждое условие по отдельности
        List<WhereClause> whereList = new ArrayList<>();
        if (whereString.contains("between") || whereString.contains("BETWEEN")) {
            Pattern betweenPattern = Pattern.compile("(\\S+)\\s+(BETWEEN)\\s+(\\S+\\s+AND\\s+\\S+)\\s+(AND)?");
            Matcher betweenMatch = betweenPattern.matcher(whereString);
            while (betweenMatch.find()) {
                WhereClause whereClause = new WhereClause(betweenMatch.group(1), betweenMatch.group(2), new Source(betweenMatch.group(3)));
                whereList.add(whereClause);
            }
            whereString = betweenMatch.replaceAll("").trim();
        }

        if (whereString.length() > 3) {
            String[] whereSplit = whereString.split(" AND | OR ", Pattern.CASE_INSENSITIVE);
            for (int i = 0; i < whereSplit.length; i++) {
                String whereItem = whereSplit[i];
                String[] whereItemToArray = whereItem.split(" ");
                //если whereItemToArray[2] содержит  if (fromString.contains("<Subquery_")) , то надо создать объект Query и засунуть его аргументом в конструктор
                if (whereItemToArray[2].contains("<Subquery_")) {
                    String subquery = subqueriesMap.get(whereItemToArray[2]);
                    Source source = new Source(SQLParser.parseQuery(subquery));
                    whereList.add(new WhereClause(whereItemToArray[0], whereItemToArray[1], source));
                } else
                    whereList.add(new WhereClause(whereItemToArray[0], whereItemToArray[1], new Source(whereItemToArray[2])));
            }
        }
        return whereList;
    }

    private static List<String> parseGroupBy (String groupByString) {
        List<String> groupTables = new ArrayList<>();
        String[] groupByTablesArray = groupByString.split(",");
        for (String table: groupByTablesArray) {
            groupTables.add(table.trim());
        }
        return groupTables;
    }

    private static List<WhereClause> parseHaving (String havingString) {
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
                //если whereItemToArray[2] содержит  if (fromString.contains("<Subquery_")) , то надо создать объект Query и засунуть его аргументом в конструктор
                havingList.add(new WhereClause(whereItemToArray[0], whereItemToArray[1], new Source(whereItemToArray[2])));
            }
        }
        return havingList;
    }

    private static List<Sort> parseSort (String sortString) {
        String[] sortItems = sortString.split(", ");
        Pattern sortPattern = Pattern.compile("\\s*(.*?)\\s+(ASC|DESC)?\\s*");
        Sort sortItemResult;
        List<Sort> parsedSortList = new ArrayList<>();
        for (String sortItem: sortItems) {
            Matcher sortMatcher = sortPattern.matcher(sortItem);
            while (sortMatcher.find()) {
                Source sortSource = new Source(sortMatcher.group(1));
                if (sortMatcher.group(2) != null) {
                    sortItemResult = new Sort(OrderDirectionType.valueOf(sortMatcher.group(2)), sortSource);
                } else {
                    sortItemResult = new Sort(sortSource);
                }
                parsedSortList.add(sortItemResult);
            }
        }
        return parsedSortList;
        //ORDER BY department ASC, salary DESC;
        //надо сначала сплитануть по запятой, а потом каждый айтем парсить регуляркой "//s*(.*?)//s+(ASC|DESC)?//s*"
        //первая группа - наш сурс – создаем объект сурс
        //вторая группа – если не null то создаем объект Sort расширенным конструктором
        //иначе создаем объект Sort с конструктором, который принимает только сурс

    }






}
