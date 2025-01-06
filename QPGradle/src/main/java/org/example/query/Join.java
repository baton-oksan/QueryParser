package org.example.query;

import org.example.enums.JoinType;

public class Join {
    private Source joinSource;
    private JoinType joinType = JoinType.INNER;
    private String onCondition;

    //поля: название таблицы (по сути  source), тип джойна (енам?) , условие джойна (on)

    public Join() {}

    public Join(Source joinSource, String joinType, String onCondition) {
        this.joinSource = joinSource;
        this.joinType = JoinType.valueOf(joinType);
        this.onCondition = onCondition;
    }

    public Join(Source joinSource, String onCondition) {
        this.joinSource = joinSource;
        this.onCondition = onCondition;
    }

    public void setJoinSource (Source joinSource) {
        this.joinSource = joinSource;
    }

    public void setJoinType (String joinType) {
        this.joinType = JoinType.valueOf(joinType);
    }

    public void setOnCondition (String onCondition) {
        this.onCondition = onCondition;
    }

    public Source getJoinSource() {
        return this.joinSource;
    }

    public JoinType getJoinType() {
        return this.joinType;
    }

    public String getOnCondition() {
        return this.onCondition;
    }

    public void printJoin() {
        System.out.println("   Join type: " + joinType);
        System.out.print("   Join source: ");
        joinSource.printSource();
        System.out.println("   Join condition: " + onCondition);
    }
}
