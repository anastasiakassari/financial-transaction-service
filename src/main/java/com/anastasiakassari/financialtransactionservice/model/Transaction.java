package com.anastasiakassari.financialtransactionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
