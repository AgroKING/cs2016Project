package com.simulator.ui;

import com.simulator.model.portfolio;
import com.simulator.model.transaction;
import com.simulator.observer.PriceObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class LedgerPanel extends JPanel implements PriceObserver {
    private portfolio portfolio;
    private JTable table;
    private DefaultTableModel tableModel;
    private int processedTxCount = 0;

    public LedgerPanel(portfolio p) {
        this.portfolio = p;
        setLayout(new BorderLayout());
        
        String[] cols = {"Ticker", "Side", "Qty", "Price", "Time"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        add(new JLabel("Transaction History", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setPreferredSize(new Dimension(400, 200));
        refreshTable();
    }
    
    private void refreshTable() {
        List<transaction> history = portfolio.getHistory();
        if (history.size() > processedTxCount) {
            for (int i = processedTxCount; i < history.size(); i++) {
                transaction t = history.get(i);
                Object[] row = {
                    t.getTicker(),
                    t.getSide(),
                    t.getQuantity(),
                    String.format("$%.2f", t.getPricePerUnit()),
                    t.getTimestamp().toString()
                };
                tableModel.addRow(row);
            }
            processedTxCount = history.size();
        }
    }

    @Override
    public void OnPriceUpdate(Map<String, Double> prices) {
        SwingUtilities.invokeLater(this::refreshTable);
    }
}
