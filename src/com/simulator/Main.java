package com.simulator;

import com.simulator.engine.MarketSubject;
import com.simulator.engine.PriceEngine;
import com.simulator.model.Crypto;
import com.simulator.model.Stock;
import com.simulator.model.portfolio;
import com.simulator.model.transaction;
import com.simulator.model.Side;
import com.simulator.persistence.CSVRepository;
import com.simulator.service.TradeService;
import com.simulator.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            portfolio p = new portfolio(100000.0); // 1. Create Portfolio

            MarketSubject market = new MarketSubject();
            market.addAsset("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
            market.addAsset("BTC", new Crypto("BTC", "Bitcoin", 63000.0));
            market.addAsset("TSLA", new Stock("TSLA", "Tesla", 245.0));
            market.addAsset("GOOG", new Stock("GOOG", "Google", 339.40));

            CSVRepository csvRepo = new CSVRepository();

            // Restore portfolio state from saved transactions
            for (transaction t : csvRepo.loadTransaction()) {
                if (t.getSide() == Side.BUY) {
                    p.buy(t.getTicker(), t.getQuantity(), t.getPricePerUnit());
                } else {
                    p.sell(t.getTicker(), t.getQuantity(), t.getPricePerUnit());
                }
            }
            System.out.println("Restored " + p.getHistory().size() + " trades from transactions.csv");

            TradeService tradeService = new TradeService(p, csvRepo);

            MainFrame mainFrame = new MainFrame(p, tradeService);

            market.registerObserver(mainFrame.getTickerPanel());
            market.registerObserver(mainFrame.getChartPanel());
            market.registerObserver(mainFrame.getTradePanel());
            market.registerObserver(mainFrame.getLedgerPanel());
            market.registerObserver(mainFrame.getHoldingsPanel()); // 3B: swapped to HoldingsPanel

            mainFrame.setVisible(true);

            PriceEngine engine = new PriceEngine(market);

            mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    engine.stop();
                }
            });

            engine.start();
        });
    }
}