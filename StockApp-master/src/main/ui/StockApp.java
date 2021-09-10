package ui;

import model.Portfolio;
import persistence.JsonReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StockApp extends JFrame {
    private JPanel mainPanel;
    private JPanel topMenu;
    private JComboBox sort;
    private JLabel tickerLabel;
    private JTextField tickerField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField dateField;
    private JLabel priceLabel;
    private JButton buyStockButton;
    private JButton sellStockButton;
    private JLabel cashLabel;
    private JLabel invLabel;
    private JLabel totValLabel;
    private JButton updatePricesButton;
    private JButton updateHistoryButton;
    private JTextField cashField;
    private JButton addCashButton;
    private JButton subtractCashButton;
    private JLabel profitLossLabel;
    private JTextField recTickerField;
    private JTextField recField;
    private JButton addRecButton;
    private JLabel dateLabel;
    private JLabel quantityLabel;
    private JLabel dayPL;
    private JLabel weekPL;
    private JLabel compName;
    private JTextField compNameField;
    private JList stockList;
    private JButton saveButton;
    private JLabel hunDayLabel;
    private JTable table;
    private JButton proportionSortButton;
    private JButton gainLossSortButton;
    private JButton markValSortButton;
    private JButton dayChangeSortButton;



    private DefaultListModel listModel;

    private boolean firstOpen;

    private String filePath;

    private Portfolio portfolio;
//    private JFrame mainFrame;

    public StockApp(String filePath) {
        super("Stock Tracker");
        this.filePath = filePath;
        initializePortfolio();
        initializeFrame();
        addButtonListeners();
        initializeTable();
        initializeInfoPanelContents();
        this.setVisible(true);


    }

    private void initializeFrame() {
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void initializePortfolio() {
        //"./data/save.json"
        portfolio = loadPortfolio("./data/save.json");
        portfolio.updateStockPercents();
        updatePortfolio();
    }

    private void initializeInfoPanelContents() {
        cashLabel.setText("Cash: " + portfolio.getCashBalance());
        invLabel.setText("Investments: " + portfolio.getInvString());
        if (portfolio.isProfiting()) {
//            invLabel.setForeground(Color.green);
        } else {
            invLabel.setForeground(Color.red);
        }

        totValLabel.setText("Total Porfolio Value: " + portfolio.getNetBalance());

        String dayStr = "1-day: ";
        dayStr = portfolio.isDayProfiting() ? dayStr + "+" : dayStr;
        dayStr = dayStr + portfolio.getDayChange();
        dayStr = dayStr + "(";
        dayStr = portfolio.isDayProfiting() ? dayStr + "+" : dayStr;
        dayStr += portfolio.getDayChangePercent();
        dayStr = dayStr + "%)";
        dayPL.setText(dayStr);
        if(portfolio.isDayProfiting()) {
//            dayPL.setForeground(Color.);
        } else {
            dayPL.setForeground(Color.red);
        }

        //todo build out week PL and 100 day PL
//        private JLabel dayPL;
//        private JLabel weekPL;
//        hunDayLabel = new JLabel("" + );
    }



    private void initializeTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        table.setModel(tableModel);
        tableModel.setDataVector(portfolio.getStockTableInfo(),portfolio.getColumnHeaders());
    }



    // MODIFIES: this
    // EFFECTS: loads portfolio
    //      if portfolio not found, beep and do nothing
    public static Portfolio loadPortfolio(String fileName) {
        //./data/save.json
        try {
            JsonReader jsonReader = new JsonReader(fileName);
            Portfolio p = jsonReader.read();
            return p;
        } catch (IOException e) {
            Toolkit.getDefaultToolkit().beep();
        }
        return null;
    }

    private void refreshApp() {
        updatePortfolio();
        initializeInfoPanelContents();
        initializeTable();
    }

    private void updatePortfolio() {
        portfolio.updateDayChange();
        portfolio.updateStats();
        portfolio.updateStockStats();
    }

    private void addButtonListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.savePortfolio();
            }
        });
        updatePricesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.updateStockPrices();
                refreshApp();
            }
        });

        updateHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.updateStockHistories();
                refreshApp();
            }
        });
        buyStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticker = tickerField.getText();
                String compName = compNameField.getText();
                double price = Double.parseDouble(priceField.getText());
                double quantity = Double.parseDouble(quantityField.getText());
                String date = dateField.getText();
                tickerField.setText("");
                compNameField.setText("");
                priceField.setText("");
                quantityField.setText("");
                dateField.setText("");
                portfolio.buyStock(ticker, compName, price, quantity, date);
                refreshApp();
            }
        });
        sellStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticker = tickerField.getText();
                double price = Double.parseDouble(priceField.getText());
                double quantity = Double.parseDouble(quantityField.getText());
                String date = dateField.getText();
                tickerField.setText("");
                compNameField.setText("");
                priceField.setText("");
                quantityField.setText("");
                dateField.setText("");
                portfolio.sellStock(ticker, price, quantity, date);
                refreshApp();
            }
        });
        addCashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.addCash(Double.parseDouble(cashField.getText()));
                cashField.setText("");
                refreshApp();
            }
        });
        subtractCashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.subtractCash(Double.parseDouble(cashField.getText()));
                cashField.setText("");
                refreshApp();
            }
        });
        addRecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.addRecommendation(recTickerField.getText(), recField.getText());
            }
        });
        //todo make a sort by unrealised GL percent button
        //todo make a sort by total cost (principle investment)
        //
        proportionSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.propSort();
                initializeTable();
            }
        });
        dayChangeSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.daySort();
                initializeTable();
            }
        });
        markValSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.marketSort();
                initializeTable();
            }
        });
        gainLossSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portfolio.gainLossSort();
                initializeTable();
            }
        });

    }

}
