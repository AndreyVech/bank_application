package ru.bank_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank_app.entity.EntitySession;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<EntitySession, BigInteger> {
    EntitySession findByUserName(String name);
}
