package model;

import java.util.Comparator;

public class marketStockComp implements Comparator<TradedStock> {
    public int compare(TradedStock o1, TradedStock o2) {
        if (o1.getMarketValue() == o2.getMarketValue()) return 0;
        else if (o1.getMarketValue() > o2.getMarketValue()) return 1;
        else return -1;
    }
}
