package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonWriterStock;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private ArrayList<TradedStock> stocks;
    //inv == investments
    //invGainLoss = sum  of each position's net gain loss
    private double invGainLoss;
    private double cashBalance;
    //invMarketValue = sum of each position's marketValue
    private double invMarketValue;
    //invPrinciple = sum of each position's totalCost
    private double invPrinciple;

    private double invGainLossPercent;
    //netBalance = marketValue + cashBalance
    private double netBalance;

    private boolean profiting;

    private double dayChange;
    private double weekChange;
    private double hundredChange;

    private PriceHistory priceHistory;

    private String[][] stockTableInfo;

    JsonWriterStock jsonWriter;



    public Portfolio() {
        stocks = new ArrayList<>();
        cashBalance = 0.0;
        netBalance = 0.0;
        invGainLossPercent = 0.0;
        invGainLoss = 0.0;
        invPrinciple = 0.0;
        dayChange = 0.0;
        profiting = true;
    }

    public int getNumStocks() {
//        return stocks.size();
        return stocks.size();
    }


    public ArrayList<TradedStock> getStocks() {
        return stocks;
    }

    public Double getInvGainLoss() {
        return roundIt(invGainLoss);
    }

    public Double getCashBalance() {
        return cashBalance;
    }

    public Double getInvMarketValue() {
        return roundIt(invMarketValue);
    }

    public Double getInvGainLossPercent() {
        return roundIt(invGainLossPercent);
    }

    public Double getNetBalance() {
        return roundIt(netBalance);
    }

    public boolean isProfiting() {
        return invGainLoss >= 0;
    }

    public boolean isDayProfiting() {return dayChange > 0;}

    public Double getDayChange() {
        updateDayChange();
        dayChange = roundIt(dayChange);
        return dayChange;
    }

    public String[][] getStockTableInfo() {
        int numStocks = getNumStocks();
        stockTableInfo = new String[numStocks][10];
        initializeInfoStrings(stockTableInfo);
        return stockTableInfo;
    }

    private void initializeInfoStrings(String[][] stockTableInfo) {
        int i = 0;
        String currency = " U";
        //Description/symbol Portfolio% Quantity Average cost Total cost Current price Market value Unrealized gain/loss Unrealized gain/loss % 1-Day Change
        for (TradedStock ts : stocks) {
            stockTableInfo[i][0] = ts.getTicker() + " - " + ts.getCompName();
            stockTableInfo[i][1] = ts.getPortPercent() + "%";
            stockTableInfo[i][2] = ts.getQuantityHeld() + "";
            stockTableInfo[i][3] = "$" + ts.getAvgCost() + currency;
            stockTableInfo[i][4] = "$" + ts.getTotalCost() + currency;
            stockTableInfo[i][5] = "$" + ts.getCurrentPrice() + currency;
            stockTableInfo[i][6] = "$" + ts.getMarketValue() + currency;
            if (ts.isProfiting()) {
                stockTableInfo[i][7] = "$ +" + ts.getNetGainLoss() + currency;
                stockTableInfo[i][8] = "+" + ts.getGainLossPercent() + "%";
            } else {
                stockTableInfo[i][7] = "$" + ts.getNetGainLoss() + currency;
                stockTableInfo[i][8] = ts.getGainLossPercent() + "%";
            }
            if (ts.isDayProfiting()) {
                stockTableInfo[i][9] = "$ +" + ts.getDayChange() + currency +
                        "(+" + ts.getDayChangePercent() + "%)";
            } else {
                stockTableInfo[i][9] = "$ " + ts.getDayChange() + currency +
                        "(" + ts.getDayChangePercent() + "%)";
            }
            i++;
        }
    }


    public String[] getColumnHeaders() {
        String[] arr = {"Description/symbol", "Portfolio%", "Quantity",
                "Average cost", "Total cost",
                "Current price", "Market value",
                "Unrealized gain/loss", "Unrealized gain/loss %",
                "1-Day Change"};
        return arr;
    }


    public double getDayChangePercent() {
        double dc = getDayChange();
        if(invMarketValue > 0)
            return roundIt(dc * 100 / (invMarketValue - dc));
        else return 0.0;
    }

    public void addCash(double amount) {
        cashBalance += amount;
        netBalance += amount;
        updateStockPercents();
    }

    public void subtractCash(double amount) {
        cashBalance -= amount;
        netBalance -= amount;
        updateStockPercents();
    }

    public void buyStock(String ticker, String companyName,
                         double cost, double quantity, String mmddyyyy) {
        TradedStock ts = alreadyHas(ticker);
        if(ts == null) {
            ts = new TradedStock(ticker, companyName);
            stocks.add(ts);
        }

        ts.buy(mmddyyyy, cost, quantity);
        //todo make a user decision to have cashBalance auto update with each trade or manual update
        updateStats();
        updateStockPercents();
    }

    public TradedStock alreadyHas(String ticker) {
        for(TradedStock ts: stocks)
            if(ts.getTicker().equalsIgnoreCase(ticker)) {
                return ts;
            }
        return null;
    }

    public void sellStock(String ticker, double price, double quantity, String mmddyyy) {
        TradedStock ts = findStock(ticker);
        //remove stock from list if completely sold
        if(quantity == ts.getQuantityHeld())
            stocks.remove(findStock(ticker));
        ts.sell(mmddyyy, price, quantity);
        updateStats();
        updateStockPercents();
    }

    public void deleteStock(String ticker) {
        stocks.remove(ticker);
    }

    //To be linked to a button
    public void updateStats() {
        invGainLoss = 0.0;
        invMarketValue = 0.0;
        invPrinciple = 0.0;
        for (TradedStock ts : stocks) {
            invGainLoss += ts.getNetGainLoss();
            invMarketValue += ts.getMarketValue();
            invPrinciple += ts.getTotalCost();
        }
        netBalance = cashBalance + invMarketValue;
        profiting = invGainLoss > 0;
        invGainLossPercent = 0.0;
        if(invPrinciple > 0)
            invGainLossPercent = (invMarketValue - invPrinciple) / invPrinciple;
        invGainLossPercent *= 100;
    }

    public JSONObject portfolioToJSON() {
        JSONObject json = new JSONObject();
        json.put("stocks", stocksToJSON());
        json.put("cash", cashBalance);


        return json;
    }

    public void sortPriceHistoryOfStocks() {
        for (TradedStock ts : stocks) {
            ts.sortPriceHistory();
        }
    }

    public void updateDayChange() {
        dayChange = 0.0;
        for (TradedStock ts : stocks) {
            ts.updateDayChange();
            dayChange += ts.getDayChange();
        }
    }

    public void updateStockPrices() {
        for (TradedStock ts : stocks) {
            ts.updateCurrentPrice();
        }
        sortPriceHistoryOfStocks();
        updateStats();
    }

    public void updateStockHistories() {
        for (TradedStock ts : stocks) {
            ts.updatePriceHistory();
        }
    }

    public void updateStockPercents() {
        for (TradedStock ts : stocks) {
            double percent;
            percent = ts.getMarketValue() / netBalance;
            percent *= 100;
            ts.setPortPercent(percent);
        }
    }

    public void updateStockStats() {
        for (TradedStock ts : stocks) {
            ts.updateStats();
        }
    }

    public void addRecommendation(String ticker, String rec) {
        findStock(ticker).addRecommendation(rec);
    }


    private JSONArray stocksToJSON() {
        JSONArray arr = new JSONArray();
        for (TradedStock ts: stocks) {
            arr.put(ts.tradedStockToJSON());
        }
        return arr;
    }

    public void addTradedStockFromJSON(String ticker, String companyName, double totalCost, double avgCost, double gainLoss, double netGainLoss, double gainLossPercent, double marketValue, double quantityHeld, TransactionRecord transactions, List<String> recommendations, StockPriceHistory stockPriceHistory, double currentPrice, double portPercent, double dayChange) {
        TradedStock tradedStock = new TradedStock(
                ticker, companyName, totalCost, avgCost, gainLoss,
                netGainLoss, gainLossPercent, marketValue, quantityHeld,
                transactions, recommendations, stockPriceHistory, currentPrice, portPercent, dayChange);
        stocks.add(tradedStock);
    }

    public void savePortfolio() {
        try {
//            "C:\\Users\\jliteplo\\Documents\\JML HD DOCS\\JML Personal\\StockApp"
//            jsonWriter = new JsonWriterStock("./data/save.json");
            jsonWriter = new JsonWriterStock("./data/save.json");
            jsonWriter.open();
            jsonWriter.writePortfolio(this);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Saved.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file.");
        }
    }



    public String getInvString() {
        String s = "";
        s += getInvMarketValue();
        s = profiting ? s + " +" : s + " ";
        s = s + getInvGainLoss() + "(" + getInvGainLossPercent() + "%)";
        return s;
    }

    private double roundIt(double num) {
        return round(num, 2);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private TradedStock findStock(String ticker) {
        for(TradedStock ts: stocks) {
            if(ts.getTicker().equals(ticker))
                return ts;
        }
        return null;
    }

    public void addCashFromJson(double cash) {
        this.cashBalance = cash;
    }

    public void daySort() {
        stocks.sort(new dayStockComp());
    }

    public void propSort() {
        stocks.sort(new propStockComp());
    }

    public void marketSort() {
        stocks.sort(new marketStockComp());
    }

    public void gainLossSort() {
        stocks.sort(new gainLossStockComp());
    }

}
