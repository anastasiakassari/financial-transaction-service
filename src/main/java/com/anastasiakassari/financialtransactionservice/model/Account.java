package com.anastasiakassari.financialtransactionservice.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column
    private Double balance;
    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    private Timestamp createdAt;

    public Account() {

    }

    public Account(Currency currency) {
        this.currency = currency;
    }

    public Account(Currency currency, Double balance) {
        this.currency = currency;
        this.balance = balance;
    }

    public Account(Long id, Double balance, Currency currency, Timestamp createdAt) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public Account(Account account){
        this.id = account.id;
        this.currency = account.currency;
        this.balance = account.balance;
        this.createdAt = account.createdAt;
    }

    public Long getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", currency=" + currency +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return id.equals(account.id);
    }

}
