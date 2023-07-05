package com.anastasiakassari.financialtransactionservice.repository;

import com.anastasiakassari.financialtransactionservice.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
}
