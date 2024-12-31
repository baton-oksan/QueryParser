package org.example;

public class Join {
    private Source joinSource;
    private JoinType joinType = JoinType.INNER;
    private String onCondition;

    //поля: название таблицы (по сути  source), тип джойна (енам?) , условие джойна (on)

    public void setJoinSource (Source joinSource) {
        this.joinSource = joinSource;
    }

    public void setJoinType (String joinType) {
        this.joinType = JoinType.valueOf(joinType);
    }

    public void setOnCondition (String onCondition) {
        this.onCondition = onCondition;
    }

    public void printJoin() {
        System.out.println(joinType);
        joinSource.printSource();
        System.out.println(onCondition);
    }
}
