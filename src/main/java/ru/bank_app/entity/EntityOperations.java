package ru.bank_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "operations")
@Data
public class EntityOperations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    EntityAccount entityAccount;

    @NotNull
    @NotBlank
    private String accountTo;

    @NotNull
    @NotBlank
    private String accountClient;

    @NotNull
    @NotBlank
    private BigDecimal sum;

    @NotNull
    @NotBlank
    private Integer isCredit;

    @NotNull
    @NotBlank
    @DateTimeFormat
    private Date dateOper;

    @NotNull
    @NotBlank
    private String naznPay;
}
