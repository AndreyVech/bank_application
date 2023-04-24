package ru.bank_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class EntityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String name;

    @NotBlank
    @NotNull
    private String password;

    @OneToMany
    @JoinColumn(name = "userId")
    List<EntityAccount> accountList = new ArrayList<>();
}
