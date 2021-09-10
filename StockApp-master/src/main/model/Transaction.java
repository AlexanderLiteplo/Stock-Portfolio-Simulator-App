package model;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
    private String type;
    private double price;
    private LocalDate date;
    private double quantity;
    private double avgPurchasePrice;

    public Transaction(LocalDate date, double price, double avgPrice, double quantity, boolean buy) {
        this.date = date;
        this.price = price;
        this.quantity = quantity;
        type = buy ? "BUY" : "SELL";
        this.avgPurchasePrice = avgPrice;
    }

    public Transaction(LocalDate ld, double price, long quantity, String type, double avgPurchasePrice) {
        this.date = ld;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.avgPurchasePrice = avgPurchasePrice;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isBuy() {
        return type.equals("BUY");
    }

    public JSONObject transactionToJSON(){
        JSONObject j = new JSONObject();
        j.put("date",date.toString());
        j.put("price",price);
        j.put("quantity",quantity);
        j.put("type",type);
        j.put("avgPurchasePrice", avgPurchasePrice);
        return j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }



    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", quantity=" + quantity +
                ", avgPurchasePrice=" + avgPurchasePrice +
                '}';
    }

    @Override
    public int compareTo(Transaction t) {
        return date.compareTo(t.date);
    }
}
