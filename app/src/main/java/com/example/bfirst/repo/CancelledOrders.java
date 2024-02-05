package com.example.bfirst.repo;

public class CancelledOrders {
    private boolean Order_cancelled;
    private String Ordered_Id;
    private String Product_Id;

    public CancelledOrders(boolean order_cancelled, String ordered_Id, String product_Id) {
        Order_cancelled = order_cancelled;
        Ordered_Id = ordered_Id;
        Product_Id = product_Id;
    }

    public boolean isOrder_cancelled() {
        return Order_cancelled;
    }

    public void setOrder_cancelled(boolean order_cancelled) {
        Order_cancelled = order_cancelled;
    }

    public String getOrdered_Id() {
        return Ordered_Id;
    }

    public void setOrdered_Id(String ordered_Id) {
        Ordered_Id = ordered_Id;
    }

    public String getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(String product_Id) {
        Product_Id = product_Id;
    }


    @Override
    public String toString() {
        return "CancelledOrders{" +
                "Order_cancelled=" + Order_cancelled +
                ", Ordered_Id='" + Ordered_Id + '\'' +
                ", Product_Id='" + Product_Id + '\'' +
                '}';
    }
}
