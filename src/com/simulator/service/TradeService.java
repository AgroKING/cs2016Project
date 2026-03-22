package com.simulator.service;

import com.simulator.model.Side;
import com.simulator.model.portfolio;
import com.simulator.model.transaction;
import com.simulator.persistence.CSVRepository;

import java.time.LocalDateTime;
import java.util.Map;

public class TradeService {
    private portfolio portfolio;
    private CSVRepository csvRepository;
    public TradeService(portfolio portfolio, CSVRepository csvRepository) {
        this.portfolio = portfolio;
        this.csvRepository = csvRepository;

    }
    public void executeBuy(portfolio portfolio,String ticker , int quantity, Map<String,Double > livePrices ) {
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity should be greater than 0");
        }
        if(!livePrices.containsKey(ticker)){
            throw new IllegalArgumentException("Ticker not found");
        }
        double currprice = livePrices.get(ticker);

        portfolio.buy(ticker,quantity,currprice);
        transaction t = new transaction(ticker, Side.BUY,quantity,currprice, LocalDateTime.now());
        csvRepository.saveTransaction(t);

    }

    public void executeSell(portfolio portfolio,String ticker , int quantity, Map<String,Double> livePrices ) {
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity should be greater than 0");
        }
        if(!livePrices.containsKey(ticker)){
            throw new IllegalArgumentException("Ticker not found");
        }
        double currprice = livePrices.get(ticker);
        portfolio.sell(ticker,quantity,currprice);
        transaction t = new transaction(ticker, Side.SELL,quantity,currprice, LocalDateTime.now());
        csvRepository.saveTransaction(t);
    }
}
