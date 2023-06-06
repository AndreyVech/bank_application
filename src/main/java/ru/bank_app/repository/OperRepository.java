package ru.bank_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bank_app.entity.EntityAccount;
import ru.bank_app.entity.EntityOperations;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface OperRepository extends JpaRepository<EntityOperations, BigInteger> {

    @Query(value = "select * " +
//            " case when a.is_credit = '1' THEN a.account_to ELSE a.account_client END as ACC_FROM" +
//            ", case when a.is_credit = '1' THEN a.account_client ELSE a.account_to END as ACC_TO" +
//            ", a.sum"+
//            ", a.nazn_pay" +
//            ", a.date_oper as DATE_OPER " +
//            ", a.account_client " +
            "from public.operations a " +
            "where 1=1 " +
            "and a.date_oper >= :dateStart " +
            "and a.date_oper <= :dateEnd " +
            "and a.account_client = :account " +
            "ORDER BY a.date_oper ASC",
            nativeQuery = true)
    List<EntityOperations> findAllInPeriod(
            @Param("dateStart") Date dateStart,
            @Param("dateEnd") Date dateEnd,
            @Param("account") String account
            );
}
