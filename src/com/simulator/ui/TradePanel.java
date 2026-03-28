package com.simulator.ui;

import com.simulator.observer.PriceObserver;
import com.simulator.service.TradeService;
import com.simulator.model.portfolio;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TradePanel extends JPanel implements PriceObserver {
    private portfolio portfolio;
    private TradeService tradeService;
    private Map<String, Double> lastPrices;
    
    private JTextField tickerField;
    private JTextField qtyField;
    private JButton buyBtn;
    private JButton sellBtn;

    public TradePanel(portfolio p, TradeService ts) {
        this.portfolio = p;
        this.tradeService = ts;
        
        setLayout(new FlowLayout());
        
        add(new JLabel("Ticker:"));
        tickerField = new JTextField(5);
        add(tickerField);
        
        add(new JLabel("Qty:"));
        qtyField = new JTextField(5);
        add(qtyField);
        
        buyBtn = new JButton("BUY");
        sellBtn = new JButton("SELL");
        add(buyBtn);
        add(sellBtn);
        
        buyBtn.addActionListener(e -> handleTrade(true));
        sellBtn.addActionListener(e -> handleTrade(false));
    }

    private void handleTrade(boolean isBuy) {
        if (lastPrices == null) {
            JOptionPane.showMessageDialog(this, "Wait for live prices first.");
            return;
        }
        try {
            String ticker = tickerField.getText().trim().toUpperCase();
            int qty = Integer.parseInt(qtyField.getText().trim());
            
            if (isBuy) {
                tradeService.executeBuy(portfolio, ticker, qty, lastPrices);
            } else {
                tradeService.executeSell(portfolio, ticker, qty, lastPrices);
            }
            JOptionPane.showMessageDialog(this, "Trade Executed!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Trade failed: " + ex.getMessage());
        }
    }

    @Override
    public void OnPriceUpdate(Map<String, Double> prices) {
        this.lastPrices = prices;
    }
}
