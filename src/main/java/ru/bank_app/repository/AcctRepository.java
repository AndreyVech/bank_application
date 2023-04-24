package ru.bank_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank_app.entity.EntityAccount;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AcctRepository extends JpaRepository<EntityAccount, BigInteger> {
    List<EntityAccount> findByUserId(BigInteger userId);
    EntityAccount findByAccNum(String accNum);
}
