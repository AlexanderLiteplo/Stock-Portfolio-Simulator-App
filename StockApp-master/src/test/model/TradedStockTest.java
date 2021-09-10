package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TradedStockTest {

    TradedStock ts0;
    TradedStock ts1;
    TradedStock ts2;


    @BeforeEach
    void doBefore() {
        ts0 = new TradedStock("A","ca");
        ts1 = new TradedStock("B","cb");
        ts2 = new TradedStock("C","cc");
    }

    @Test
    void buyTest(){

    }
}
