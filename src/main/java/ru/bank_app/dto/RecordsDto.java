package ru.bank_app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecordsDto {
    private BigInteger userId;
    private String accNum;
    private String dateStart;
    private String dateEnd;
}