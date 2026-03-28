package com.simulator.ui;

import com.simulator.observer.PriceObserver;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel implements PriceObserver {
    private Map<String, List<Double>> history = new HashMap<>();
    private static final int MAX_HISTORY = 100;
    
    public ChartPanel() {
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(800, 400));
    }

    @Override
    public void OnPriceUpdate(Map<String, Double> prices) {
        for (Map.Entry<String, Double> entry : prices.entrySet()) {
            history.putIfAbsent(entry.getKey(), new ArrayList<>());
            List<Double> assetHistory = history.get(entry.getKey());
            assetHistory.add(entry.getValue());
            if (assetHistory.size() > MAX_HISTORY) {
                assetHistory.remove(0); // keep it fixed size
            }
        }
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();

        Color[] colors = {Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.RED};
        int colorIdx = 0;

        for (Map.Entry<String, List<Double>> entry : history.entrySet()) {
            g2d.setColor(colors[colorIdx % colors.length]);
            
            List<Double> data = entry.getValue();
            if (data.size() < 2) {
                colorIdx++;
                continue;
            }

            double max = Collections.max(data);
            double min = Collections.min(data);
            if (max == min) { max += 1; min -= 1; }

            int xStep = width / MAX_HISTORY;
            
            for (int i = 0; i < data.size() - 1; i++) {
                int x1 = i * xStep;
                int y1 = height - (int) ((data.get(i) - min) / (max - min) * (height - 40)) - 20;
                int x2 = (i + 1) * xStep;
                int y2 = height - (int) ((data.get(i+1) - min) / (max - min) * (height - 40)) - 20;

                g2d.drawLine(x1, y1, x2, y2);
            }
            
            // Draw legend
            g2d.drawString(entry.getKey() + ": $" + String.format("%.2f", data.get(data.size()-1)), 10, 20 + colorIdx * 15);
            colorIdx++;
        }
    }
}
