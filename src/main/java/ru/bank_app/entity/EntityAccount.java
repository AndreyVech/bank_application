package ru.bank_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "accounts")
@Data
public class EntityAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal saldo;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String accNum;

    @NotNull
    private BigInteger userId;

}
