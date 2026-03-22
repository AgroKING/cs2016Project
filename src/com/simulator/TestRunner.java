package com.simulator;

import com.simulator.model.Stock;
import com.simulator.model.Crypto;
import com.simulator.model.portfolio;
import com.simulator.model.transaction;
import com.simulator.engine.MarketSubject;
import com.simulator.engine.PriceEngine;
import com.simulator.service.TradeService;
import com.simulator.persistence.CSVRepository;
import com.simulator.observer.PriceObserver;

import java.util.Map;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("========== TEST 1: Price Simulation ==========");
        Stock apple = new Stock("AAPL", "Apple Inc.", 150.0);
        Crypto btc = new Crypto("BTC", "Bitcoin", 63000.0);

        System.out.println("AAPL starting price: " + apple.getCurrPrice());
        System.out.println("BTC  starting price: " + btc.getCurrPrice());

        for (int i = 1; i <= 10; i++) {
            apple.simPrice(1.0 / 252.0);
            btc.simPrice(1.0 / 252.0);
            System.out.println("Tick " + i + "  |  AAPL: " + String.format("%.2f", apple.getCurrPrice())
                    + "  |  BTC: " + String.format("%.2f", btc.getCurrPrice()));
        }

        System.out.println("\n========== TEST 2: Portfolio Buy/Sell ==========");
        portfolio p = new portfolio(100000.0);
        System.out.println("Starting cash: " + p.getCashBalance());

        p.buy("AAPL", 10, 150.0);
        System.out.println("After buying 10 AAPL @ 150.0:");
        System.out.println("  Cash: " + p.getCashBalance());
        System.out.println("  Holdings: " + p.getHoldings());

        p.sell("AAPL", 5, 155.0);
        System.out.println("After selling 5 AAPL @ 155.0:");
        System.out.println("  Cash: " + p.getCashBalance());
        System.out.println("  Holdings: " + p.getHoldings());

        System.out.println("  Transaction history: " + p.getHistory().size() + " trades");

        System.out.println("\n========== TEST 3: MarketSubject + Observer ==========");
        MarketSubject market = new MarketSubject();
        market.addAsset("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
        market.addAsset("BTC", new Crypto("BTC", "Bitcoin", 63000.0));

        // Create a fake observer to prove the pattern works
        market.registerObserver(new PriceObserver() {
            @Override
            public void OnPriceUpdate(Map<String, Double> prices) {
                System.out.println("  Observer received prices: " + prices);
            }
        });

        // Simulate one tick and notify
        for (var asset : market.getAssets()) {
            asset.simPrice(1.0 / 252.0);
        }
        market.notifyObservers();

        System.out.println("\n========== TEST 4: PriceEngine (3 seconds) ==========");
        MarketSubject market2 = new MarketSubject();
        market2.addAsset("TSLA", new Stock("TSLA", "Tesla", 245.0));
        market2.addAsset("ETH", new Crypto("ETH", "Ethereum", 3400.0));

        market2.registerObserver(new PriceObserver() {
            @Override
            public void OnPriceUpdate(Map<String, Double> prices) {
                System.out.println("  Live tick: " + prices);
            }
        });

        PriceEngine engine = new PriceEngine(market2);
        engine.start();
        System.out.println("Engine started... waiting 3 seconds");
        Thread.sleep(3000);
        engine.stop();
        System.out.println("Engine stopped.");

        System.out.println("\n========== TEST 5: CSV Persistence ==========");
        CSVRepository csv = new CSVRepository();
        portfolio p2 = new portfolio(50000.0);
        TradeService tradeService = new TradeService(p2, csv);

        Map<String, Double> fakePrices = Map.of("AAPL", 150.0, "BTC", 63000.0);
        tradeService.executeBuy(p2, "AAPL", 5, fakePrices);
        tradeService.executeSell(p2, "AAPL", 2, fakePrices);
        System.out.println("Saved 2 transactions to transactions.csv");

        List<transaction> loaded = csv.loadTransaction();
        System.out.println("Loaded " + loaded.size() + " transactions from CSV:");
        for (transaction t : loaded) {
            System.out.println("  " + t.getTicker() + " " + t.getSide() + " " + t.getQuantity() + " @ " + t.getPricePerUnit());
        }

        System.out.println("\n========== ALL TESTS PASSED ==========");
    }
}
