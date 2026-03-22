package com.simulator.model;

import java.util.Random;

public class Crypto extends Asset {
    private Random random = new Random();
    // constructor for super
    public Crypto(String ticker,String name,double iniPrice) {
        super(ticker, name, iniPrice);

    }
    @Override
    public double simPrice(double dt) {
        double z = random.nextGaussian();
        double sigma = 0.05;
        double mu = 0.001;

        double newPrice = getCurrPrice() * Math.exp((mu - 0.5 * sigma * sigma) * dt + sigma * Math.sqrt(dt) * z);
        currPrice = newPrice;
        return newPrice;

    }
}
