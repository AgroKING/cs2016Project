package com.simulator;

import com.simulator.model.Stock;

public class TestRunner {
    public static void main(String[] args) {
        Stock apple = new Stock("AAPL", "Apple", 150.0);
        for(int i =0;i<100;i++){
            System.out.println(apple.simPrice(1.0/252));
        }

    }
}
