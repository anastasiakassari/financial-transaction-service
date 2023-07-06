package com.anastasiakassari.financialtransactionservice.dto;

import com.anastasiakassari.financialtransactionservice.model.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AccountDTO {
    private Currency currency;
    private Double balance;
}
