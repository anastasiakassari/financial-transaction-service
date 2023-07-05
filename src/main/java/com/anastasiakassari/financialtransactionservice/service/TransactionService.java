package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.model.Transaction;

import java.util.List;

public interface TransactionService {

    public List<Transaction> getTransactions();
    public Transaction getTransactionById(Long id);
    public Transaction createTransaction(TransactionDTO transaction);
}
