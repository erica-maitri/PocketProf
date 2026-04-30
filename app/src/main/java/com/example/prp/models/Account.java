package com.example.prp.models;

public class Account {
    private double accountAmount;
    private String accountName;
    public Account(){}

    public Account(String accountName, double accountAmount) {
        this.accountName = accountName;
        this.accountAmount = accountAmount;
    }

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
