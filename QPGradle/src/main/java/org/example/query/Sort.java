package org.example.query;

import org.example.enums.OrderDirectionType;

public class Sort {
    private OrderDirectionType orderDirectionType = OrderDirectionType.ASC;
    private Source orderSource;

    public OrderDirectionType getOrderDirectionType() {
        return orderDirectionType;
    }
    public Source getOrderSource() {
        return orderSource;
    }

    public Sort(OrderDirectionType orderDirectionType, Source orderSource) {
        this.orderDirectionType = orderDirectionType;
        this.orderSource = orderSource;
    }

    public Sort(Source orderSource) {
        this.orderSource = orderSource;
    }

    public void printSort() {
        System.out.print("   " + "Order column: ");
        orderSource.printSource();
        System.out.println("   " + "Order direction: " + orderDirectionType);
    }
}
