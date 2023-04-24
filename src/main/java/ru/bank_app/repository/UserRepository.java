package ru.bank_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank_app.entity.EntityUser;

import java.math.BigInteger;

@Repository
public interface UserRepository extends JpaRepository<EntityUser, BigInteger> {
    EntityUser findByName(String Name);
    EntityUser findById(Long id);
}

