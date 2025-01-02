package org.example.parsers;

import org.example.Join;
import org.example.JoinType;
import org.example.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinParser implements Parser<List<Join>> {
    public List<Join> parse(String joinString) {
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
}
