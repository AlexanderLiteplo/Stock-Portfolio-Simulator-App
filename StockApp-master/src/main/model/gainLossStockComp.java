package model;

import java.util.Comparator;

public class gainLossStockComp implements Comparator<TradedStock> {
    public int compare(TradedStock o1, TradedStock o2) {
        if (o1.getNetGainLoss() == o2.getNetGainLoss()) return 0;
        else if (o1.getNetGainLoss() > o2.getNetGainLoss()) return 1;
        else return -1;
    }
}
