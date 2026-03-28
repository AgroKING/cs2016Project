package com.simulator.ui;

import com.simulator.model.portfolio;
import com.simulator.observer.PriceObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PortfolioSummaryPanel extends JPanel implements PriceObserver {
    private portfolio portfolio;
    private JLabel cashLabel;
    private JLabel holdingsValueLabel;
    private JLabel totalLabel;

    public PortfolioSummaryPanel(portfolio p) {
        this.portfolio = p;
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createTitledBorder("Portfolio Summary"));
        
        cashLabel = new JLabel("Cash: $0.00");
        holdingsValueLabel = new JLabel("Holdings: $0.00");
        totalLabel = new JLabel("Total: $0.00");
        
        add(cashLabel);
        add(holdingsValueLabel);
        add(totalLabel);
        setPreferredSize(new Dimension(200, 100));
    }

    @Override
    public void OnPriceUpdate(Map<String, Double> prices) {
        double cash = portfolio.getCashBalance();
        double hValue = portfolio.getTotalValue(prices);
        double total = cash + hValue;
        
        SwingUtilities.invokeLater(() -> {
            cashLabel.setText(String.format("Cash: $%,.2f", cash));
            holdingsValueLabel.setText(String.format("Holdings: $%,.2f", hValue));
            totalLabel.setText(String.format("Total: $%,.2f", total));
        });
    }
}
