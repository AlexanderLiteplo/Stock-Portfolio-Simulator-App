package persistence;

import model.Portfolio;
import model.Price;
import model.StockPriceHistory;
import model.TransactionRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//Represents a Json reader object

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Portfolio from file and returns it
    // throws IOException if an error occurs reading data from file
    public Portfolio read() throws IOException {
        String jsonData = readFile(source);
        //check if json save file is empty and return new portfolio if so
        if (jsonData.length() < 100 || jsonData == null) return new Portfolio();
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses schedule from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        double cash = jsonObject.getDouble("cash");
        Portfolio newPortfolio = new Portfolio();
        parseTradedStocks(newPortfolio, jsonObject);
//        newPortfolio.updateStockHistories();
        newPortfolio.addCashFromJson(cash);
        newPortfolio.updateStats();
        return newPortfolio;
    }

    // MODIFIES: Schedule
    // EFFECTS: parses TradedStocks from JSON object and adds them to portfolio
    private void parseTradedStocks(Portfolio portfolio, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("stocks");
        for (Object json : jsonArray) {
            JSONObject nextStock = (JSONObject) json;
            parseTradedStock(portfolio, nextStock);
        }
    }


    // MODIFIES: portfolio
    // EFFECTS: parses ScheduledBlock from JSON object and adds it to portfolio
    private void parseTradedStock(Portfolio portfolio, JSONObject jsonObject) {
        double totalCost = jsonObject.getDouble("totalCost");
        double avgCost = jsonObject.getDouble("avgCost");
        double gainLoss = jsonObject.getDouble("gainLoss");
        double netGainLoss = jsonObject.getDouble("netGainLoss");
        double gainLossPercent = jsonObject.getDouble("gainLossPercent");
        double marketValue = jsonObject.getDouble("marketValue");
//        double realNetGainLoss = jsonObject.getDouble("realNetGainLoss");
//        double totNetGainLoss = jsonObject.getDouble("totNetGainLoss");
        double quantityHeld = jsonObject.getDouble("quantityHeld");
        String ticker = jsonObject.getString("ticker");
        String companyName = jsonObject.getString("companyName");
        double currentPrice = jsonObject.getDouble("currentPrice");
        double portPercent = jsonObject.getDouble("portPercent");
        double dayChange = jsonObject.getDouble("dayChange");
        StockPriceHistory stockPriceHistory = parseStockPriceHistory(ticker, jsonObject);
        TransactionRecord transactions = parseTransactionRecord(jsonObject);
        List<String> recommendations = parseRecommendations(jsonObject);

        portfolio.addTradedStockFromJSON(
                ticker, companyName, totalCost, avgCost, gainLoss,
                netGainLoss, gainLossPercent, marketValue, quantityHeld,
                transactions, recommendations, stockPriceHistory, currentPrice, portPercent, dayChange);
    }

    private List<String> parseRecommendations(JSONObject jsonObject) {
        List<String> arr = new ArrayList<>();
        JSONArray jarr = jsonObject.getJSONArray("recommendations");
        for (Object s : jarr) {
            arr.add((String) s);
        }
        return arr;
    }

    private TransactionRecord parseTransactionRecord(JSONObject jsonObject) {
        TransactionRecord tr = new TransactionRecord();
        JSONArray jarr = jsonObject.getJSONArray("transactions");
        for (Object o : jarr) {
            JSONObject j = (JSONObject) o;
            LocalDate ld = dateStringToLocal(j.getString("date"));
            double price = j.getDouble("price");
            long quantity = j.getLong("quantity");
            String type = j.getString("type");
            double avgPurchasePrice = j.getDouble("avgPurchasePrice");
            tr.addTransactionFromJSONStore(ld, price, quantity, type, avgPurchasePrice);
        }
        return tr;
    }

    //string in form ex: 2020-12-31
    private LocalDate dateStringToLocal(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        LocalDate ld = LocalDate.of(year, month, day);
        return ld;
    }

    private StockPriceHistory parseStockPriceHistory(String ticker, JSONObject jsonObject) {
        StockPriceHistory sph = new StockPriceHistory(ticker);
        JSONArray jarr = jsonObject.getJSONArray("priceHistory");
        for (Object o : jarr) {
            JSONObject j = (JSONObject) o;
            LocalDate ld = dateStringToLocal(j.getString("date"));
            Price sprice = new Price(j.getDouble("price"), ld);
            sph.addStockPrice(sprice);
        }
        return sph;
    }
}
