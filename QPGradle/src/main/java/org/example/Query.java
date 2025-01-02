package org.example;

import java.util.List;
import java.util.stream.Collectors;

public class Query {
    private List<String> columns;
    private List<Source> fromSources;
    private List<Join> joins;
    private List<Condition> whereClauses;
    private List<String> groupByColumns;
    private List<Condition> havingClauses;
    private List<Sort> sortColumns;
    private Integer limit;
    private Integer offset;

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setFromSources(List<Source> fromSources) {
        this.fromSources = fromSources;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public void setWheres(List<Condition> whereClauses) {
        this.whereClauses = whereClauses;
    }

    public void setGroupByColumns (List<String> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public void setHavingClauses (List<Condition> havingClauses) {
        this.havingClauses = havingClauses;
    }

    public void setSortColumns (List<Sort> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public void setLimit (int limit) {
        this.limit = limit;
    }
    public void setOffset (int offset) {
        this.offset = offset;
    }

    public void printQuery() {
        System.out.print("SELECT: ");
        System.out.println(columns.stream()
                    .collect(Collectors.joining(", ")));

        //почему-то не печатаю тут fromSources



        if (joins != null) {
            System.out.print("JOIN: ");
            for (Join join: joins) {
                join.printJoin();
            }
        }

        if (whereClauses != null) {
            System.out.print("WHERE: ");
            for (Condition whereClause: whereClauses) {
                whereClause.printWhere();
            }
        }

        if (groupByColumns != null) {
            System.out.print("GROUP BY: ");
            System.out.println(groupByColumns.stream()
                    .collect(Collectors.joining(", ")));
        }

        if (havingClauses != null) {
            System.out.print("HAVING: ");
            for (Condition whereClause: havingClauses) {
                whereClause.printWhere();
            }
        }

        if (sortColumns != null) {
            System.out.print("ORDER BY: ");
            for (Sort sort : sortColumns) {
                sort.printSort();
            }
        }

        if (limit != null) {
            System.out.print("LIMIT: ");
            System.out.println(limit);
        }

        if (offset != null) {
            System.out.print("OFFSET: ");
            System.out.println(offset);
        }
    }
}
