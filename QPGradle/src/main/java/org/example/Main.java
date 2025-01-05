package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String inputQuery = "SELECT product_id, product_name, price " +
                "FROM products " +
                "WHERE category = 'electronics' ORDER BY price DESC LIMIT 5;";
        String inputQuery2 = "SELECT id, name FROM employees WHERE department_id IN (SELECT id FROM departments WHERE location = 'New York');";
        String inputQuery3 = "SELECT author.name, count(book.id), sum(book.cost) FROM author LEFT JOIN book ON (author.id = book.author_id) GROUP BY author.name HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 LIMIT 10;";
        //String inputQuery4 = "SELECT * FROM employees WHERE salary BETWEEN 50000 AND 100000 AND department_id IN (SELECT id FROM departments WHERE location = 'New York');";

        Query query = SQLParser.parseQuery(inputQuery3);
        query.printQuery();

    }

}


