package com.simulator.observer;
import java.util.Map;
public interface PriceObserver {
    void OnPriceUpdate(Map<String, Double> price);
}
