package com.simulator.ui;

import com.simulator.observer.PriceObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TickerPanel extends JPanel implements PriceObserver {
    private JLabel priceLabel;

    public TickerPanel() {
        this.setLayout(new FlowLayout());
        priceLabel = new JLabel("Waiting for prices...");
        priceLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        this.add(priceLabel);
    }

    @Override
    public void OnPriceUpdate(Map<String, Double> prices) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : prices.entrySet()) {
            sb.append(entry.getKey())
              .append(": $")
              .append(String.format("%.2f", entry.getValue()))
              .append("   ");
        }
        SwingUtilities.invokeLater(() -> priceLabel.setText(sb.toString()));
    }
}
