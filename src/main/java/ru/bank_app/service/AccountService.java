package ru.bank_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank_app.entity.EntityAccount;
import ru.bank_app.entity.EntityOperations;
import ru.bank_app.exception.AccNotFoundException;
import ru.bank_app.repository.AcctRepository;
import ru.bank_app.repository.OperRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AcctRepository accountRepository;

    @Autowired
    private OperRepository operRepository;

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

    public boolean moneyTransferAtm(String accClient, String accAtm, BigDecimal summa, int isCredit) {
        EntityAccount client = accountRepository.findByAccNum(accClient);
        EntityAccount atm = accountRepository.findByAccNum(accAtm);
        //получим результат дебетования счета клиента и банкомата
        BigDecimal resultClient = client.getSaldo().subtract(summa);
        BigDecimal resultAtm = atm.getSaldo().subtract(summa);
        if (isCredit == 1) {
            //если пополнение через банкомат
            //сделаем назначение платежа для выписки
            String naznPayAtm = "ATM.Пополнение счета "+ accClient;
            String naznPayClient = "Пополнение счета через банкомат "+ accountRepository.findByAccNum(accAtm).getId();
            if (createOperationCreditAtm(client, summa, isCredit, naznPayClient)
                && createOperationCreditAtm(atm, summa, isCredit, naznPayAtm)
            ) {
                client.setSaldo(client.getSaldo().add(summa));
                atm.setSaldo(atm.getSaldo().add(summa));
                accountRepository.save(client);
                accountRepository.save(atm);
            }
        } else {
            //если снятие в банкомате, то оба счета уменьшаем
            //сделаем назначение платежа для выписки
            String naznPayAtm = "ATM.Снятие наличных по счету "+ accClient;
            String naznPayClient = "Снятие наличных в банкомате "+ accountRepository.findByAccNum(accAtm).getId();
            if (resultClient.compareTo(BigDecimal.valueOf(0)) > 0
                    && resultAtm.compareTo(BigDecimal.valueOf(0)) > 0 //денег на обоих счетах достаточно
                    && createOperationDebitAtm(client, summa, isCredit, naznPayClient)
                    && createOperationDebitAtm(atm, summa, isCredit, naznPayAtm) // запишем операции по обоим счетам
            ) {
                client.setSaldo(client.getSaldo().subtract(summa));
                atm.setSaldo(atm.getSaldo().subtract(summa));
                accountRepository.save(atm);
                accountRepository.save(client);
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean createOperationCreditAtm(EntityAccount entityAccountTo, BigDecimal sum, Integer isCredit, String naznPay) {
        //простой вариант без внебалансовых счетов
        EntityOperations entityOperations = new EntityOperations();
        entityOperations.setEntityAccount(entityAccountTo);
        entityOperations.setSum(sum);
        entityOperations.setAccountClient(entityAccountTo.getAccNum());
        entityOperations.setIsCredit(isCredit);
        Date nowDate = new Date(System.currentTimeMillis());
        entityOperations.setDateOper(nowDate);
        entityOperations.setNaznPay(naznPay);
        entityOperations.setAccountTo("ATM");
        operRepository.save(entityOperations);
        return true;
    }

    private boolean createOperationDebitAtm(EntityAccount entityAccountFrom, BigDecimal sum, Integer isCredit, String naznPay) {
        //простой вариант без внебалансовых счетов. Счет пополнения оставим пустым
        EntityOperations entityOperations = new EntityOperations();
        entityOperations.setEntityAccount(entityAccountFrom);
        entityOperations.setSum(sum);
        entityOperations.setAccountClient(entityAccountFrom.getAccNum());
        entityOperations.setIsCredit(isCredit);
        Date nowDate = new Date(System.currentTimeMillis());
        entityOperations.setDateOper(nowDate);
        entityOperations.setNaznPay(naznPay);
        entityOperations.setAccountTo("ATM");
        operRepository.save(entityOperations);
        return true;
    }

    public boolean moneyTransfer(String accDt, String accKt, BigDecimal summa) {
        EntityAccount clientDt = accountRepository.findByAccNum(accDt);
        EntityAccount clientKt = accountRepository.findByAccNum(accKt);
        //получим результат дебетования счета клиентаДт
        BigDecimal resultClientDt = clientDt.getSaldo().subtract(summa);
        //Уменьшаем счет Дт и пополняем счет Кт
        //сделаем назначение платежа для выписки
        String naznPayDt = "Перевод на счет "+ accKt;
        String naznPayKt = "Перевод со счета "+ accDt;
        if (resultClientDt.compareTo(BigDecimal.valueOf(0)) > 0 //денег на счете достаточно
                && createOperation(clientDt, accKt, summa, 0, naznPayDt)
                && createOperation(clientKt, accDt, summa, 1, naznPayKt) // запишем операции по обоим счетам
        ) {
            clientDt.setSaldo(clientDt.getSaldo().subtract(summa));
            clientKt.setSaldo(clientKt.getSaldo().add(summa));
            accountRepository.save(clientDt);
            accountRepository.save(clientKt);
        } else {
            return false;
        }
        return true;
    }

    private boolean createOperation(EntityAccount entityAccountFrom, String accTo, BigDecimal sum, Integer isCredit, String naznPay) {
        EntityOperations entityOperations = new EntityOperations();
        entityOperations.setEntityAccount(entityAccountFrom);
        entityOperations.setSum(sum);
        entityOperations.setIsCredit(isCredit);
        entityOperations.setAccountClient(entityAccountFrom.getAccNum());
        Date nowDate = new Date(System.currentTimeMillis());
        entityOperations.setDateOper(nowDate);
        entityOperations.setNaznPay(naznPay);
        entityOperations.setAccountTo(accTo);
        operRepository.save(entityOperations);
        return true;
    }



    public void deleteAccount(BigInteger id) {
        EntityAccount account = accountRepository.findById(id).orElseThrow(() -> new AccNotFoundException(id.longValue()));
        accountRepository.delete(account);
    }
}
