package com.anastasiakassari.financialtransactionservice.repository;

import com.anastasiakassari.financialtransactionservice.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
