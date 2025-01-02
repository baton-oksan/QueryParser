package org.example.parsers;

import org.example.OrderDirectionType;
import org.example.Sort;
import org.example.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortParser implements Parser<List<Sort>> {
    public List<Sort> parse (String sortString) {
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
        //вторая группа – если не null то создаем объект org.example.Sort расширенным конструктором
        //иначе создаем объект org.example.Sort с конструктором, который принимает только сурс
    }
}
