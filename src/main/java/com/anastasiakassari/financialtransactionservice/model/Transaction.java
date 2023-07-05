package com.anastasiakassari.financialtransactionservice.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column
    private Long sourceAccountId;
    @Column
    private Long targetAccountId;
    @Column
    private Double amount;
    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Transaction(){

    }

    public Transaction(Long sourceAccountId, Long targetAccountId, Double amount, Currency currency){
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.currency = currency;
    }

    public Transaction(Long id, Long sourceAccountId, Long targetAccountId, Double amount, Currency currency){
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.currency = currency;
    }

    public Transaction(Transaction transaction){
        this.id = transaction.id;
        this.sourceAccountId = transaction.sourceAccountId;
        this.targetAccountId = transaction.targetAccountId;
        this.amount = transaction.amount;
        this.currency = transaction.currency;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sourceAccountId=" + sourceAccountId +
                ", targetAccountId=" + targetAccountId +
                ", amount=" + amount +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction transaction = (Transaction) obj;
        return id.equals(transaction.id);
    }
}
