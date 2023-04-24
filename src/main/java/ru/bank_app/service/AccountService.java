package ru.bank_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank_app.entity.EntityAccount;
import ru.bank_app.exception.AccNotFoundException;
import ru.bank_app.repository.AcctRepository;
import ru.bank_app.repository.UserRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AcctRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EntityAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<EntityAccount> getAccountsByUserId(BigInteger userId) {
        return accountRepository.findByUserId(userId);
    }

    public EntityAccount getAccountsByNum(String accNum) {
        return accountRepository.findByAccNum(accNum);
    }

    public EntityAccount createAccount(EntityAccount entityAccount) {
        return accountRepository.save(entityAccount);
    }

    public boolean updateAccountSaldo(String accNum, BigDecimal summa, boolean credit) {
        System.out.println("updateAccountSaldo 0");
        EntityAccount account = accountRepository.findByAccNum(accNum);
        if (credit) {
            account.setSaldo(account.getSaldo().add(summa));
            accountRepository.save(account);
        } else {
            BigDecimal result = account.getSaldo().subtract(summa);
            if (result.compareTo(BigDecimal.valueOf(0)) > 0) {
                account.setSaldo(account.getSaldo().subtract(summa));
                accountRepository.save(account);
            } else {
                return false;
            }
        }
        return true;
    }

    public void deleteAccount(BigInteger id) {
        EntityAccount account = accountRepository.findById(id).orElseThrow(() -> new AccNotFoundException(id.longValue()));
        accountRepository.delete(account);
    }
}
