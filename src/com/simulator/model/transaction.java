package com.simulator.model;

import java.time.LocalDateTime;

public class transaction {
    private final String ticker;
    private final Side side;
    private final int quantity;
    private final double pricePerUnit;
    private final LocalDateTime timestamp;
    //constructor

    public transaction(String ticker, Side side, int quantity, double pricePerUnit, LocalDateTime timestamp) {
        this.ticker = ticker;
        this.side = side;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.timestamp = timestamp;


    }
    // getter for the immutables
    public String getTicker() {
        return ticker;
    }
    public Side getSide() {
        return side;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
