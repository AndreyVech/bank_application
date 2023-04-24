package ru.bank_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionCash {
    private BigInteger userId;
    private String accNum;
    private BigDecimal summa;
}
