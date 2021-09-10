package ui;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {

//        String filePath = JOptionPane.showInputDialog(null,
//                "Enter the file path:",
//                "Load File",
//                JOptionPane.QUESTION_MESSAGE);
//        String filePath = "/Users/jonathanliteplo/Documents/stock app/save.json";
        String filePath = "./data/save.json";
        StockApp stockApp = new StockApp(filePath);
        stockApp.setVisible(true);

    }

}