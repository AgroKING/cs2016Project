package com.simulator.model;

import java.util.Random;

public class Stock extends Asset {
    private Random random = new Random();
    // constructor for super
    public Stock(String ticker,String name,double iniPrice) {
        super(ticker, name, iniPrice);

    }
    @Override
    public double simPrice(double dt) {
        double z = random.nextGaussian();
        double sigma = 0.02;
        double mu = 0.0007;

        double newPrice = getCurrPrice() * Math.exp((mu - 0.5 * sigma * sigma) * dt + sigma * Math.sqrt(dt) * z);
        currPrice = newPrice;
        return newPrice;

    }
}
