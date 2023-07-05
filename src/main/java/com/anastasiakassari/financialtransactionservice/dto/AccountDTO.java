package com.anastasiakassari.financialtransactionservice.dto;

import com.anastasiakassari.financialtransactionservice.model.Currency;

public class AccountDTO {
    private Currency currency;
    private Double balance;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "currency=" + currency +
                ", balance=" + balance +
                '}';
    }
}
