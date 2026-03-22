package com.simulator.engine;

import com.simulator.model.Asset;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Implementation of threading and thread safety
public class PriceEngine {
    private ScheduledExecutorService scheduler;
    private MarketSubject marketSubject;
    public PriceEngine(MarketSubject marketSubject) {
        this.marketSubject = marketSubject;
    }
    public void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            // 1. Simulate new prices for every asset
            for (Asset asset : marketSubject.getAssets()) {
                asset.simPrice(1.0 / 252.0);
            }
            // 2. Tell all observers about the new prices
            marketSubject.notifyObservers();
        }, 0, 1, TimeUnit.SECONDS);

    }
    public void stop() {
        scheduler.shutdown();
    }

}
