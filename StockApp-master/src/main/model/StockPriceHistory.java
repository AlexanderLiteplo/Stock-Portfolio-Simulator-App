package model;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class StockPriceHistory implements Iterable<Price> {
    List<Price> priceHistory;
    String ticker;
    Double dayChange;

    public StockPriceHistory(String ticker) {
        priceHistory = new ArrayList<>();
        this.ticker = ticker;
        dayChange = 0.0;
    }

    public void addStockPrice(Price price) {
        if (!priceHistory.contains(price)) {
            priceHistory.add(price);
        }
    }

    public void updateHistory() {
        PriceHistoryScraper phs = new PriceHistoryScraper();
    }

    public void sort() {
        priceHistory.sort(new StockPriceComparator());
    }

    @Override
    public String toString() {
        return "StockPriceHistory{" +
                "priceHistory=" + priceHistory.toString() +
                ", ticker='" + ticker + '\'' +
                '}';
    }

    @Override
    public Iterator<Price> iterator() {
        return priceHistory.iterator();
    }

    public JSONArray priceHistoryToJSON() {
        JSONArray arr = new JSONArray();
        for (Price price : this) {
            arr.put(price.priceToJSON());
        }
        return arr;
    }

    public double getDayChange() {
        updateDayChange();
        return dayChange;
    }

    private void updateDayChange() {
        //todo get rid of this
        if (priceHistory.size() < 2) {
            updateHistory();
        }
        //gets second and last items booiiiii
        dayChange = priceHistory.get(priceHistory.size() - 1).getPrice() - priceHistory.get(priceHistory.size() - 2).getPrice();
        //todo find out why this gets called 3 times every time i do something on neverminds cuz there is three stocks hmmmmmmmm
        System.out.println(ticker + " Day Change:" + dayChange);
        System.out.println(priceHistory.get(priceHistory.size() -1).getPrice() + " today's close?");
        System.out.println(priceHistory.get(priceHistory.size() -2).getPrice() + " yesterday's close");


    }

    private class PriceHistoryScraper {

        private PriceHistoryScraper() {
            try {
                //todo clean up into methods
                String url = "https://finance.yahoo.com/quote/" + ticker + "/history?p=" + ticker;
                Document doc = Jsoup.connect(url).get();
                Elements ele = doc.select("tbody");

                Elements rows = ele.select("tr");
                Iterator<Element> rowIterator = rows.iterator();
                while (rowIterator.hasNext()) {
                    Element row = rowIterator.next();
                    Elements cols = row.select("td");
                    String date = cols.get(0).text();
                    if (cols.size() < 5)
                        continue;
                    String close = (cols.get(4).text());
                    close = close.replaceAll(",", "");
                    Double closingPrice = Double.parseDouble(close);

                    Price sp = new Price(closingPrice, yahooDateToLocal(date));
                    addStockPrice(sp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //yahoo date string format: MMM DD, YYYY
        private LocalDate yahooDateToLocal(String string) throws ParseException {
            String monthString = string.substring(0, 3);
            String day = string.substring(4, 6);
            String year = string.substring(8, 12);
            Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return LocalDate.of(Integer.parseInt(year), cal.get(Calendar.MONTH) + 1, Integer.parseInt(day));
        }
    }
}
