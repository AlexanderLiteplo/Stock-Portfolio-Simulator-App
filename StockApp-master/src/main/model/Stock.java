package model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Stock {
    String info;
    protected String ticker;
    protected String companyName;
    protected List<String> recommendations;
    protected Double currentPrice;
    protected StockPriceHistory priceHistory;

    public Stock(String ticker, String companyName) {
        priceHistory = new StockPriceHistory(ticker);
        this.ticker = ticker;
        this.companyName = companyName;
        recommendations = new ArrayList<>();
        currentPrice = findCurrentPrice();
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double manualEnteredCurrentPrice) {
        LocalDate today = LocalDate.now();
        priceHistory.addStockPrice(new Price(manualEnteredCurrentPrice, today));
        this.currentPrice = manualEnteredCurrentPrice;
    }

    public StockPriceHistory getPriceHistory() {
        return priceHistory;
    }

    public void addRecommendation(String s) {
        if (!recommendations.contains(s))
            recommendations.add(s);
    }

    public void updateCurrentPrice() {
        setCurrentPrice(findCurrentPrice());
    }

    private Double findCurrentPrice() {
        CurrentPriceScraper s = new CurrentPriceScraper();
        Double scrapedCurrentPrice = s.scrapeCurrentPrice();
        return scrapedCurrentPrice;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + ticker + '\'' +
                ", companyName='" + companyName + '\'' +
                ", recommendations=" + recommendations +
                ", currentPrice=" + currentPrice +
                '}';
    }


    /*
    REQUIRES:
    MODIFIES: this.priceHistory
    EFFECTS: adds yahoo data of closing prices of last 100 days to the map
     */
    public void updatePriceHistory() {
        priceHistory.updateHistory();
    }

    public static LocalDate localDateConverter(String mmddyyyy) {
        int year = Integer.parseInt(mmddyyyy.substring(4));
        int month = Integer.parseInt(mmddyyyy.substring(0, 2));
        int day = Integer.parseInt(mmddyyyy.substring(2, 4));
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate;
    }



    private class CurrentPriceScraper {

        private CurrentPriceScraper() {
            try {
                String url = "https://finance.yahoo.com/quote/" + ticker + "/";
                Document doc = Jsoup.connect(url).get();
                Elements ele = doc.select("div#quote-header-info");
                info = "";
                for (Element e : ele) {
                    info = e.text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private double scrapeCurrentPrice() {
            String price = formatPriceString();
            return Double.parseDouble(price);
        }

        private String formatPriceString() {
            int start = info.indexOf("watchlist");
            info = info.substring(start);
            int end = getEnd();
            String price = info.substring(10, end);
            price = price.replaceAll(",", "");
            return price;
        }

        private int getEnd() {
            int end;
            int minusIndex = info.indexOf("-");
            int plusIndex = info.indexOf("+");
            if (minusIndex == -1) {
                end = plusIndex;
            } else if (plusIndex == -1) {
                end = minusIndex;
            } else {
                end = minusIndex > plusIndex ? plusIndex : minusIndex;
            }
            return end;
        }
    }


}
