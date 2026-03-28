package com.simulator.ui;

import com.simulator.model.portfolio;
import com.simulator.service.TradeService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private TickerPanel tickerPanel;
    private ChartPanel chartPanel;
    private TradePanel tradePanel;
    private LedgerPanel ledgerPanel;
    private PortfolioSummaryPanel portfolioSummaryPanel;

    public MainFrame(portfolio p, TradeService ts) {
        setTitle("Market Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tickerPanel = new TickerPanel();
        chartPanel = new ChartPanel();
        tradePanel = new TradePanel(p, ts);
        ledgerPanel = new LedgerPanel(p);
        portfolioSummaryPanel = new PortfolioSummaryPanel(p);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(tickerPanel, BorderLayout.NORTH);
        topPanel.add(tradePanel, BorderLayout.SOUTH);

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(portfolioSummaryPanel, BorderLayout.NORTH);
        eastPanel.add(ledgerPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    public TickerPanel getTickerPanel() { return tickerPanel; }
    public ChartPanel getChartPanel() { return chartPanel; }
    public TradePanel getTradePanel() { return tradePanel; }
    public LedgerPanel getLedgerPanel() { return ledgerPanel; }
    public PortfolioSummaryPanel getPortfolioSummaryPanel() { return portfolioSummaryPanel; }
}
