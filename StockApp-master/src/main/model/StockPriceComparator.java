package model;

import java.util.Comparator;

public class StockPriceComparator implements Comparator<Price> {

    @Override
    public int compare(Price o1, Price o2) {
        return o1.compareTo(o2);
    }
}
