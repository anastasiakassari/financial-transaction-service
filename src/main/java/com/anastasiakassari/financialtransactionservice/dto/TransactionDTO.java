package com.anastasiakassari.financialtransactionservice.dto;

import com.anastasiakassari.financialtransactionservice.model.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TransactionDTO {
    private Long sourceAccountId;
    private Long targetAccountId;
    private Double amount;
    private Currency currency;
}
