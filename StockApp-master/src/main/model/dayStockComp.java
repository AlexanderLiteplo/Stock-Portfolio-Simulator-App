package model;

import java.util.Comparator;

public class dayStockComp implements Comparator<TradedStock> {
    public int compare(TradedStock o1, TradedStock o2) {
        if (o1.getDayChange() == o2.getDayChange()) return 0;
        else if (o1.getDayChange() > o2.getDayChange()) return 1;
        else return -1;
    }
}
