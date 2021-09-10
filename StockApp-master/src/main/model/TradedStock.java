package model;


import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;


public class TradedStock extends Stock {

    private double totalCost;
    private double avgCost;

    //unrealised
    private double stockGL;
    private double netGainLoss;
    private double gainLossPercent;
    private double marketValue;

    //realised
//    private double realNetGainLoss;
//    //total net gain/loss = realised netGL + unrealised netGL
//    private double totNetGainLoss;

    private double portPercent;
    private double dayChange;
//    private List<Double> dayChangeHist;

    private double quantityHeld;
    private TransactionRecord transactions;

    private boolean profiting;
    private boolean dayProfiting;

    //JSON Constructor
    public TradedStock(String ticker, String companyName, double totalCost,
                       double avgCost, double stockGL, double netGainLoss,
                       double gainLossPercent, double marketValue,
                       double quantityHeld, TransactionRecord transactions,
                       List<String> recommendations, StockPriceHistory priceHistory, double currentPrice, double portPercent, double dayChange) {
        super(ticker, companyName);
        this.totalCost = totalCost;
        this.avgCost = avgCost;
        this.stockGL = stockGL;
        this.netGainLoss = netGainLoss;
        this.gainLossPercent = gainLossPercent;
        this.marketValue = marketValue;
        this.quantityHeld = quantityHeld;
        this.transactions = transactions;
        this.currentPrice = currentPrice;
        this.recommendations = recommendations;
        this.priceHistory = priceHistory;
        this.portPercent = portPercent;
        this.dayChange = dayChange;
        profiting = netGainLoss > 0;
    }

    /*
        REQUIRES:
        MODIFIES:
        EFFECTS:
         */
    public TradedStock(String ticker, String companyName) {
        super(ticker, companyName);
        totalCost = 0.0;
        avgCost = 0.0;
        stockGL = 0.0;
        netGainLoss = 0.0;
        gainLossPercent = 0.0;
        marketValue = 0.0;
        quantityHeld = 0.0;
        transactions = new TransactionRecord();
    }

    public double getMarketValue() {
        return roundIt(marketValue);
    }

    public Double getTotalCost() {
        return roundIt(totalCost);
    }

    public Double getAvgCost() {
        return roundIt(avgCost);
    }

    public Double getStockGL() {
        return roundIt(stockGL);
    }

    public Double getNetGainLoss() {
        return roundIt(netGainLoss);
    }

    public Double getGainLossPercent() {
        return roundIt(gainLossPercent);
    }

    public double getQuantityHeld() {
        return quantityHeld;
    }

    public TransactionRecord getTransactions() {
        return transactions;
    }

    public double getDayChange() {
        return roundIt(dayChange);
    }


    public double getDayChangePercent() {
        return roundIt(dayChange * 100 / (marketValue - dayChange));
    }

    public String getCompName() {
        return companyName;
    }

    public boolean isProfiting() {
        return profiting;
    }

    public boolean isDayProfiting() {
        return dayProfiting;
    }

    public String getTicker() {
        return ticker;
    }

    public void setPortPercent(double portPercent) {
        this.portPercent = portPercent;
    }

    public double getPortPercent() {
        return roundIt(portPercent);
    }

    public void buy(String mmddyyyy, Double price, double quantity) {
        LocalDate date = mmddyyyy.equalsIgnoreCase("today") ? LocalDate.now() : localDateConverter(mmddyyyy);
        transactions.addTransaction(date, price, price, quantity, true);
        updateBuyDetails(price, quantity);
    }

    private void updateBuyDetails(Double price, double quantity) {
        totalCost += price * quantity;
        avgCost = (totalCost) / ((quantity + quantityHeld) * 1.0);
        quantityHeld += quantity;
        stockGL = currentPrice - avgCost;
        updateStats();
    }

    //User can enter "today" instead of date
    public void sell(String mmddyyyy, Double price, double quantity) {
        if (quantityHeld < quantity) {
            //todo make this do something
            return;
        }
        LocalDate date = mmddyyyy.equalsIgnoreCase("today") ? LocalDate.now() : localDateConverter(mmddyyyy);
        transactions.addTransaction(date, price, avgCost, quantity, false);
        updateSellDetails(price, quantity);
    }

    //todo review method -- why is price not used?
    private void updateSellDetails(Double price, double quantity) {
        quantityHeld -= quantity;
        totalCost -= avgCost * quantity;
        if (0 == quantityHeld) {
            avgCost = 0.0;
            stockGL = 0.0;
        } else {
            stockGL = currentPrice - avgCost;
        }
        updateStats();
    }

    /*
    REQUIRES:
    MODIFIES: this
    EFFECTS: updates stock information
     */
    public void updateStats() {
        updateDayChange();
        updateCurrentPrice();
        stockGL = currentPrice - avgCost;
        marketValue = currentPrice * quantityHeld;
        netGainLoss = stockGL * quantityHeld;
        gainLossPercent = (currentPrice - avgCost) / avgCost;
        gainLossPercent *= 100;
        profiting = stockGL > 0;
    }

    public void updateDayChange() {
        dayChange = priceHistory.getDayChange();
        dayChange = dayChange * quantityHeld;
        if (dayChange > 0)
            dayProfiting = true;
    }

    public void sortPriceHistory() {
        priceHistory.sort();
    }



    private double roundIt(double num) {
        return round(num, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    @SuppressWarnings({"checkstyle:OperatorWrap", "CheckStyle"})

//    @Override
//    public String toString() {
//        return "TradedStock{" +
//                "totalCost=" + totalCost +
//                ", avgCost=" + avgCost +
//                ", gainLoss=" + stockGL +
//                ", netGainLoss=" + netGainLoss +
//                ", gainLossPercent=" + gainLossPercent +
//                ", marketValue=" + marketValue +
//                ", realNetGainLoss=" + realNetGainLoss +
//                ", totNetGainLoss=" + totNetGainLoss +
//                ", quantityHeld=" + quantityHeld +
//                ", transactions=" + transactions +
//                ", info='" + info + '\'' +
//                ", ticker='" + ticker + '\'' +
//                ", companyName='" + companyName + '\'' +
//                ", recommendations=" + recommendations +
//                ", currentPrice=" + currentPrice +
//                '}';
//    }

    public String toString() {
        return "TradedStock{" +
                "totalCost=" + totalCost +
                ", avgCost=" + avgCost +
                ", gainLoss=" + stockGL +
                ", netGainLoss=" + netGainLoss +
                ", gainLossPercent=" + gainLossPercent +
                ", marketValue=" + marketValue +
                ", quantityHeld=" + quantityHeld +
                ", transactions=" + transactions +
                ", info='" + info + '\'' +
                ", ticker='" + ticker + '\'' +
                ", companyName='" + companyName + '\'' +
                ", recommendations=" + recommendations +
                ", currentPrice=" + currentPrice +
                '}';
    }


    public String getStockString() {
        return companyName + " " + ticker + " | "
                + quantityHeld + " | " + avgCost + " | "
                + currentPrice +
                " | " + marketValue +
                "  " + stockGL + "(" + gainLossPercent + "%)" +
                " | " + portPercent + "%";
    }

    public JSONObject tradedStockToJSON() {
        JSONObject jstock = new JSONObject();
        jstock.put("totalCost", totalCost);
        jstock.put("avgCost", avgCost);
        jstock.put("gainLoss", stockGL);
        jstock.put("netGainLoss", netGainLoss);
        jstock.put("gainLossPercent", gainLossPercent);
        jstock.put("marketValue", marketValue);
//        jstock.put("realNetGainLoss", realNetGainLoss);
//        jstock.put("totNetGainLoss", totNetGainLoss);
        jstock.put("quantityHeld", quantityHeld);
        jstock.put("transactions", transactions.transactionRecordToJSON());
        jstock.put("ticker", ticker);
        jstock.put("companyName", companyName);
        jstock.put("recommendations", recommendationsToJSON());
        jstock.put("currentPrice", currentPrice);
        //todo change to stockPriceHistory
        jstock.put("priceHistory", priceHistory.priceHistoryToJSON());
        jstock.put("portPercent", portPercent);
        jstock.put("dayChange", dayChange);
        return jstock;
    }

    private JSONArray recommendationsToJSON() {
        JSONArray arr = new JSONArray();
        for (String s : recommendations) {
            arr.put(s);
        }
        return arr;
    }

}
