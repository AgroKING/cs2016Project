package com.simulator.engine;

import com.simulator.model.Asset;
import com.simulator.observer.PriceObserver;

import java.util.*;

public class MarketSubject {
    private List<PriceObserver> observers =  new ArrayList<>();
    private Map<String, Asset> assets = new HashMap<>();


   public void addAsset(String ticker, Asset asset) {
        assets.put(ticker, asset);
    }
    public void removeAsset(String ticker) {
        assets.remove(ticker);
    }
    public Asset getAsset(String ticker) {
        return assets.get(ticker);
    }
    public void registerObserver(PriceObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(PriceObserver observer) {
        observers.remove(observer);
    }
    public Map<String, Double> getAllPrices() {
        Map<String, Double> prices = new HashMap<>();
        for (String ticker : assets.keySet()) {
            prices.put(ticker, assets.get(ticker).getCurrPrice());
        }
        return prices;
    }
    public void notifyObservers() {
        Map<String, Double> prices = getAllPrices();
        for (PriceObserver observer : observers) {
            observer.OnPriceUpdate(prices);
        }
    }
    public Collection<Asset> getAssets() {
        return assets.values();
    }

}


