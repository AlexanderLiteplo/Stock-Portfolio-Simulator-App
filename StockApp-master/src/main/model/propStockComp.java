package model;

import java.util.Comparator;

public class propStockComp implements Comparator<TradedStock> {
    public int compare(TradedStock o1, TradedStock o2) {
        if (o1.getPortPercent() == o2.getPortPercent()) return 0;
        else if (o1.getPortPercent() > o2.getPortPercent()) return 1;
        else return -1;
    }
}
