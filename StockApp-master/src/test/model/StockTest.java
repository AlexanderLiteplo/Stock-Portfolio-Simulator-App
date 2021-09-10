package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockTest {
    TradedStock s0;
    TradedStock s1;
    TradedStock s2;
    TradedStock stock;
    LocalDate today;


    @BeforeEach
    void before() {
        s0 = new TradedStock("TSLA", "tesla");
        s1 = new TradedStock("AAPL", "Apple");
        s2 = new TradedStock("FVRR", "Fiverr");
        stock = new TradedStock("ABC","test");
        today = LocalDate.now();
    }

    @Test
    void buyTest() {
        s0.buy("02202020", 69.69, 5);
        assertEquals(69.69, s0.getAvgCost());
        assertEquals(69.69 * 5, s0.getTotalCost());
//        System.out.println(s0.getCurrentPrice());
//        System.out.println(s0.getGainLoss());
        System.out.println(s0.toString());

    }

    @Test
    void sellTest() {
        s0.buy("02202020", 420.00, 5);
        s0.sell("today", 500.00, 3);
        assertEquals(2, s0.getQuantityHeld());
        assertEquals(420 * 2.0, s0.getTotalCost());
        System.out.println(s0.toString());
    }

    @Test
    void multipleBuysTest() {
        s0.buy("02202020", 100.0, 5);
        s0.buy("03202020", 500.0, 5);

        assertEquals((100.0 * 5 + 500.0 * 5) / 10.0, s0.getAvgCost());
        assertEquals(100.0 * 5 + 500.0 * 5, s0.getTotalCost());
        assertEquals(2,s0.getTransactions().size());

        //test selling all stock
        s0.sell("today",700.0,10);

//        System.out.println(s0.getCurrentPrice());
//        System.out.println(s0.getGainLoss());
        System.out.println(s0.toString());
    }


}
