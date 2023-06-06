package ru.bank_app.Controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank_app.dto.OperationDto;
import ru.bank_app.dto.TransactionCashDto;
import ru.bank_app.repository.AcctRepository;
import ru.bank_app.repository.SessionRepository;
import ru.bank_app.repository.UserRepository;
import ru.bank_app.service.AccountService;
import ru.bank_app.service.SessionService;
import ru.bank_app.service.UserService;

import java.math.BigInteger;

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
    private AcctRepository acctRepository;

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
    public ResponseEntity<?> takeMoney(@RequestBody TransactionCashDto transactionCash) {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(transactionCash.getUserId())) {
//если пользак авторизован - совершим операцию
            if (accountService.moneyTransferAtm(transactionCash.getAccNum(),
                    transactionCash.getAtmNum(),
                    transactionCash.getSumma(),
                    0
            )
            ) {
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
    public ResponseEntity<?> putMoney(@RequestBody TransactionCashDto transactionCash) {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(transactionCash.getUserId())) {
//если пользак авторизован - совершим операцию
//            System.out.println("startC");
            if (accountService.moneyTransferAtm(transactionCash.getAccNum(),
                    transactionCash.getAtmNum(),
                    transactionCash.getSumma(),
                    1
            )
            ) {
                return ResponseEntity.ok().body(accountService.getAccountsByUserId(transactionCash.getUserId()));
            } else {
                return ResponseEntity.ok().body("Something went wrong");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("${application.endpoint.app.transfermoney}")
    @ApiOperation("Электронный перевод")
//    Здесь и далее - это внутренний перевод, мы знаем счет Дт и Кт
    public ResponseEntity<?> transferMoney(@RequestBody OperationDto OperationDto) {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(OperationDto.getUserId())) {
//если пользак авторизован - совершим операцию
//            System.out.println("startE");
            if (accountService.moneyTransfer(OperationDto.getAccDt()
                    , OperationDto.getAccKt()
                    , OperationDto.getSumma()

                )
            ) {
                return ResponseEntity.ok().body(accountService.getAccountsByUserId(OperationDto.getUserId()));
            } else {
                return ResponseEntity.ok().body("Something went wrong");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}