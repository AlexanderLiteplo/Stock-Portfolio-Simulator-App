package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionRecord implements Iterable<Transaction> {
    List<Transaction> transactions;


    public TransactionRecord() {
        transactions = new ArrayList<>();
    }

    public JSONArray transactionRecordToJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Transaction t : this) {
            jsonArray.put(t.transactionToJSON());
        }
        return jsonArray;
    }

    public void addTransaction(LocalDate date, Double price, Double avgPrice, double quantity, boolean purchase) {
        transactions.add(new Transaction(date, price, avgPrice, quantity, purchase));
    }

    public void addTransactionFromJSONStore(LocalDate ld, double price, long quantity, String type, double avgPurchasePrice) {
        transactions.add(new Transaction(ld, price, quantity, type, avgPurchasePrice));
    }

    public long size() {
        return transactions.size();
    }

    public Iterator<Transaction> iterator() {
        return transactions.iterator();
    }

}
