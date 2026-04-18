package com.simulator.ui;

import com.simulator.model.portfolio;
import com.simulator.observer.PriceObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

// 2A: extends JPanel, implements PriceObserver
public class HoldingsPanel extends JPanel implements PriceObserver {

    // 2B: Fields
    private final portfolio portfolio;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel cashLabel;
    private final JLabel totalLabel;

    // 2C: Constructor
    public HoldingsPanel(portfolio portfolio) {
        this.portfolio = portfolio;

        // 2C-2: DefaultTableModel with columns, 2C-3: cells non-editable
        tableModel = new DefaultTableModel(
                new String[]{"Ticker", "Qty", "Avg Cost", "Current", "Value", "P&L"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 2C-4: JTable with the model
        table = new JTable(tableModel);

        // 2E: Custom renderer for P&L column (index 5)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (value instanceof Double pnl) {
                    setForeground(pnl >= 0 ? new Color(0, 153, 0) : new Color(204, 0, 0));
                } else {
                    setForeground(table.getForeground());
                }
                return cell;
            }
        });

        // 2B / 2C-5: Summary bar labels
        cashLabel  = new JLabel("Cash: $0.00");
        totalLabel = new JLabel("Total: $0.00");

        // 2C-5: Layout
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel summaryBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryBar.add(cashLabel);
        summaryBar.add(Box.createHorizontalStrut(20));
        summaryBar.add(totalLabel);
        add(summaryBar, BorderLayout.SOUTH);
    }

    // 2D: Called every second with live prices
    @Override
    public void OnPriceUpdate(Map<String, Double> prices) {
        SwingUtilities.invokeLater(() -> {

            // 2D-1: Clear existing rows
            tableModel.setRowCount(0);

            // 2D-2: Snapshot holdings and avgCost from portfolio
            Map<String, Integer> holdings = portfolio.getHoldings();
            Map<String, Double>  avgCost  = portfolio.getAvgCost();

            // 2D-3: One row per ticker
            for (String ticker : holdings.keySet()) {
                int    qty     = holdings.get(ticker);
                double avg     = avgCost.getOrDefault(ticker, 0.0);
                double current = prices.getOrDefault(ticker, 0.0);
                double value   = qty * current;
                double pnl     = qty * (current - avg);

                tableModel.addRow(new Object[]{ticker, qty, avg, current, value, pnl});
            }

            // 2D-4: Update summary labels
            double cash  = portfolio.getCashBalance();
            double total = portfolio.getTotalValue(prices);
            cashLabel.setText(String.format("Cash: $%.2f", cash));
            totalLabel.setText(String.format("Total: $%.2f", total));
        });
    }
}