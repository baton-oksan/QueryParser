package org.example;

import org.example.parsers.SQLParser;
import org.example.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> testQueries = new ArrayList<>();
        testQueries.add("SELECT product_id, product_name, price " +
                "FROM products " +
                "WHERE category = 'electronics' ORDER BY price DESC LIMIT 5;");
        testQueries.add("SELECT id, name FROM employees " +
                "WHERE department_id IN (SELECT id FROM departments WHERE location = 'New York');");
        testQueries.add("SELECT author.name, count(book.id), sum(book.cost) " +
                "FROM author LEFT JOIN book ON (author.id = book.author_id) " +
                "GROUP BY author.name HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 LIMIT 10;");
        testQueries.add("SELECT * FROM employees WHERE salary BETWEEN 50000 AND 100000 AND department_id IN " +
                "(SELECT id FROM departments WHERE location = 'New York');");

        int counter = 1;
        for (String testQuery : testQueries) {
            Query query = SQLParser.parseQuery(testQuery);

            System.out.println("___________________");
            System.out.println("TEST QUERY NUMBER " + counter);
            query.printQuery();
            System.out.println("___________________");

            counter++;
        }
    }
}


