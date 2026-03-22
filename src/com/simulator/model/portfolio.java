package com.simulator.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simulator.model.Side.BUY;

public class portfolio {
    private double cashBalance;
    private Map<String,Integer> holdings = new HashMap<>();
    private List<transaction> history = new ArrayList<>();

    portfolio(double startingCash) {
        this.cashBalance = startingCash;
    }

    public void buy(String ticker, int quantity, double price) {
        double cost = quantity * price;
        if(cost <= cashBalance) {
            cashBalance -= cost;
            int currentQty = holdings.getOrDefault(ticker, 0);
            holdings.put(ticker, currentQty + quantity);
            history.add(new transaction(ticker,Side.BUY,quantity,price, LocalDateTime.now()));

        }
        else{
            throw new  IllegalArgumentException("Invalid cash balance value");
        }

    }
    public void sell(String ticker, int quantity, double price) {
        if(holdings.getOrDefault(ticker,0) >= quantity) {
            int currentQty = holdings.getOrDefault(ticker, 0);
            cashBalance += quantity * price;
            holdings.put(ticker, currentQty - quantity);
            history.add(new transaction(ticker,Side.SELL,quantity,price, LocalDateTime.now()));

        }
        else{
            throw new  IllegalArgumentException("Invalid Holding value");
        }

    }
    double getCashBalance() {
        return cashBalance;
    }
    List<transaction> getHistory() {
        return new ArrayList<>(history) ;
    }
    Map<String, Integer> getHoldings() {
        return new HashMap<>(holdings);
    }
    double getTotalValue(Map<String, Double> livePrices) {
        double sum = 0;
        for(String ticker: holdings.keySet()){
            int quantity = holdings.get(ticker);
            sum += quantity * livePrices.get(ticker);

        }
        return sum;
    }

}
