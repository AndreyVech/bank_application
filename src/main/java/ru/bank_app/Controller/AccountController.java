package ru.bank_app.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank_app.dto.TransactionCash;
import ru.bank_app.entity.EntityAccount;
import ru.bank_app.repository.SessionRepository;
import ru.bank_app.repository.UserRepository;
import ru.bank_app.service.AccountService;
import ru.bank_app.service.SessionService;
import ru.bank_app.service.UserService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("${application.endpoint.root}")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("${application.endpoint.accountsByUser}")
    @ApiOperation("Получение счетов пользователя и их балансов")
    public ResponseEntity<?> getUserAccounts(@PathVariable BigInteger userId) {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(userId)) {
//если пользак авторизован - вернем его счета и балансы
            return ResponseEntity.ok().body(accountService.getAccountsByUserId(userId));
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("${application.endpoint.atm.takemoney}")
    @ApiOperation("Снятие наличных")
    public ResponseEntity<?> takeMoney(@RequestBody TransactionCash transactionCash) {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(transactionCash.getUserId())) {
//если пользак авторизован - совершим операцию
            if (accountService.updateAccountSaldo(transactionCash.getAccNum(), transactionCash.getSumma(), false)) {
                return ResponseEntity.ok().body(accountService.getAccountsByUserId(transactionCash.getUserId()));
            } else {
                return ResponseEntity.ok().body("not enough money");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("${application.endpoint.atm.putmoney}")
    @ApiOperation("Внесение наличных")
    public ResponseEntity<?> putMoney(@RequestBody TransactionCash transactionCash) {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(transactionCash.getUserId())) {
//если пользак авторизован - совершим операцию
            if (accountService.updateAccountSaldo(transactionCash.getAccNum(), transactionCash.getSumma(), true)) {
                return ResponseEntity.ok().body(accountService.getAccountsByUserId(transactionCash.getUserId()));
            } else {
                return ResponseEntity.ok().body("not enough money");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}