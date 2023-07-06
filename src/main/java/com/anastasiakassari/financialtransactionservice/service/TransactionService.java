package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.model.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions();

    Transaction getTransactionById(Long id);

    Transaction createTransaction(TransactionDTO transaction);
}
