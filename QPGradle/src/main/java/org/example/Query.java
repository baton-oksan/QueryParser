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

    private Query(QueryBuilder builder) {
        this.columns = builder.columns;
        this.fromSources = builder.fromSources;
        this.joins = builder.joins;
        this.whereClauses = builder.whereClauses;
        this.groupByColumns = builder.groupByColumns;
        this.havingClauses = builder.havingClauses;
        this.sortColumns = builder.sortColumns;
        this.limit = builder.limit;
        this.offset = builder.offset;
    }

    public List<String> getColumns() {return columns;}
    public List<Source> getFromSources() {return fromSources;}
    public List<Join> getJoins() {return joins;}
    public List<Condition> getWhereClauses() {return whereClauses;}
    public List<String> getGroupByColumns() {return groupByColumns;}
    public List<Condition> getHavingClauses() {return havingClauses;}
    public List<Sort> getSortColumns() {return sortColumns;}
    public Integer getLimit() {return limit;}
    public Integer getOffset() {return offset;}

    public void printQuery() {
        System.out.println("SELECT: ");
        System.out.println("   " + columns.stream()
                    .collect(Collectors.joining(", ")));

        System.out.println("FROM: ");
        for (Source fromSource : fromSources) {
            fromSource.printSource();
        }



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

    public static class QueryBuilder {
        private List<String> columns;
        private List<Source> fromSources;
        private List<Join> joins;
        private List<Condition> whereClauses;
        private List<String> groupByColumns;
        private List<Condition> havingClauses;
        private List<Sort> sortColumns;
        private Integer limit;
        private Integer offset;

        public QueryBuilder columns(List<String> columns) {
            this.columns = columns;
            return this;
        }

        public QueryBuilder fromSources(List<Source> fromSources) {
            this.fromSources = fromSources;
            return this;
        }

        public QueryBuilder joins(List<Join> joins) {
            this.joins = joins;
            return this;
        }

        public QueryBuilder whereClauses(List<Condition> whereClauses) {
            this.whereClauses = whereClauses;
            return this;
        }

        public QueryBuilder groupByColumns(List<String> groupByColumns) {
            this.groupByColumns = groupByColumns;
            return this;
        }

        public QueryBuilder havingClauses(List<Condition> havingClauses) {
            this.havingClauses = havingClauses;
            return this;
        }

        public QueryBuilder sortColumns(List<Sort> sortColumns) {
            this.sortColumns = sortColumns;
            return this;
        }

        public QueryBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public QueryBuilder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Query build() {
            return new Query(this);
        }

    }
}
