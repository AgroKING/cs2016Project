package com.simulator.persistence;

import com.simulator.model.Side;

import com.simulator.model.transaction;

import java.io.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVRepository {
    private transaction transaction;


    public void saveTransaction(transaction t) {
       String line = t.getTicker() + "," + t.getSide() + "," + t.getQuantity() + "," + t.getPricePerUnit() + "," + t.getTimestamp() + "," ;
       try(BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv",true))){
           bw.write(line);
           bw.newLine();
       }
       catch(IOException e) {
           System.out.println("Error writing transactions.csv "+e.getMessage());

       }

    }
    public List<transaction> loadTransaction() {
        List<transaction> list = new ArrayList<>();
        File file = new File("transactions.csv");
        if (!file.exists()) {
            return new ArrayList<>();   // no history yet, return empty list
        }
        try(BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            String line ;
            String [] tokens;
            while((line = br.readLine() )!=null){
                tokens = line.split(",");
                String ticker =  tokens[0];
                Side side = Side.valueOf(tokens[1]);
                int quantity = Integer.parseInt(tokens[2]);
                double pricePerUnit = Double.parseDouble(tokens[3]);
                LocalDateTime timestamp = LocalDateTime.parse(tokens[4]);
                list.add( new transaction(ticker,side,quantity,pricePerUnit,timestamp));
            }


        }
        catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return list;
    }
}
