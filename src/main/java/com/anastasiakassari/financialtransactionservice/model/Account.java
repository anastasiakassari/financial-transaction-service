package com.anastasiakassari.financialtransactionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
