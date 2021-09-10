package model;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Objects;

public class Price implements Comparable<Price> {
    private double price;
    private LocalDate localDate;

    public Price(double price, LocalDate localDate) {
        this.price = price;
        this.localDate = localDate;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "price=" + price +
                ", localDate=" + localDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price that = (Price) o;
        return Objects.equals(localDate, that.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate);
    }

    @Override
    public int compareTo(Price o) {
        return localDate.compareTo(o.localDate);
    }

    public JSONObject priceToJSON() {
        JSONObject jprice = new JSONObject();
        jprice.put("price",price);
        jprice.put("date",localDate.toString());
        return jprice;
    }
}
