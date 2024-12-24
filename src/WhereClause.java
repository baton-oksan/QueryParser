public class WhereClause {
    private String columnName;
    private String operator;
    private Source source;

    public WhereClause(String columnName, String operator, Source source) {
        this.columnName = columnName;
        this.operator = operator;
        this.source = source;
    }

    public void printWhere() {
        System.out.println(columnName);
        System.out.println(operator);
        source.printSource();
    }


}

//salary > 50000
//department IN ('HR', 'Finance', 'IT')
//manager_id IS NULL
//name LIKE 'A%'
//price BETWEEN 50 AND 100
//вот тут видно у нас есть поле, тип предиката (операторы сравнения, IN, проверка на null, LIKE , BETWEEN), второй операнд

//AND and OR делят условия, но AND может быть и частью between условия

//допустим у нас есть кусок текста между словом where и or
//типы условий?

//надо определять тип условия и уже в зависимости от типа парсить дальше
//делим where условия по AND/OR, но первый AND после BETWEEN надо игнорировать
//если строка содержит between, то с помощью регулярки и группы, достать этот between и заменить на пробел
//далее парсим получившуюся строку по слову AND и получаем каждое условие по отдельности

//создать класс для каждого типа-условия?
