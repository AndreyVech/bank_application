package ru.bank_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bank_app.entity.EntityAccount;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;


@NoArgsConstructor
@AllArgsConstructor
@Data
@NotNull
public class OperationDto {
    private BigInteger userId;
    private String accDt;
    private BigDecimal summa;
    private int isCredit;
    private String accKt;
}


