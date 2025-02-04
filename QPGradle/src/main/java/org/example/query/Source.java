package org.example.query;

public class Source {
    private String sourceTable;
    private Query sourceQuery;

    public Source(Query sourceQuery) {
        this.sourceQuery = sourceQuery;
    }

    public Source (String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public Query getSourceQuery() {
        return sourceQuery;
    }

    public void printSource() {
        if (sourceTable != null) {
            System.out.println(sourceTable);
        }

        if (sourceQuery != null) {
            System.out.println();
            System.out.println("----THIS IS SUBQUERY START----");
            sourceQuery.printQuery();
            System.out.println("----THIS IS SUBQUERY END----");
        }
    }

    //сурс - не обязательно название таблицы. Это может быть и query
    //дженерики?

}
