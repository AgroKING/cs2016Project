package com.simulator.model;

public abstract class Asset {
    protected   String ticker;
    protected   String name;
    protected   double currPrice;
    protected   double prevPrice;

    public Asset(String ticker , String name , double iniPrice){
        this.ticker = ticker;
        this.name = name ;
        this.currPrice = iniPrice;
        this.prevPrice = iniPrice;

    }
    // getters
    String getTicker() {
        return ticker;
    }
    String getName() {
        return name;
    }
    public double getCurrPrice() {
        return currPrice;
    }
    double getPrevPrice() {
        return prevPrice;
    }
    double getPercentChange(){
        return ((currPrice - prevPrice)/prevPrice)*100;
    }
    public abstract double simPrice(double dt);
}
