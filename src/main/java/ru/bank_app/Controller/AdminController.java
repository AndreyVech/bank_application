package ru.bank_app.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank_app.entity.EntityAccount;
import ru.bank_app.entity.EntityUser;
import ru.bank_app.repository.SessionRepository;
import ru.bank_app.repository.UserRepository;
import ru.bank_app.service.AccountService;
import ru.bank_app.service.UserService;

import javax.validation.ValidationException;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("${application.endpoint.root}")
@RequiredArgsConstructor
@Api("sadasd")
public class AdminController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("${application.endpoint.admin.allAccounts}")
    @ApiOperation("Получение всех счетов и балансов. Админка.")
    public List<EntityAccount> getAllAccs() {
        return accountService.getAllAccounts();
    }

    @PostMapping("${application.endpoint.admin.allAccounts}")
    @ApiOperation("Создание счетов. Админка.")
    public ResponseEntity<EntityAccount> createAcc(@RequestBody EntityAccount entityAccount) {
        EntityAccount account = accountService.createAccount(entityAccount);
        return ResponseEntity.created(URI.create("/accounts/" + account.getId())).body(account);
    }

    @GetMapping("${application.endpoint.admin.accountsByUser}")
    @ApiOperation("Получение счетов пользователя и их балансов. Админка.")
    public ResponseEntity<?> getUserAccountsByAdmin(@PathVariable BigInteger userId) {
        if (userId != null) {
            return ResponseEntity.ok().body(accountService.getAccountsByUserId(userId));
        } else {
            return ResponseEntity.status(400).body("Not autorized");
        }
    }


    @GetMapping("${application.endpoint.admin.users}")
    @ApiOperation("Получение всех пользователей и их счетов. Админка.")
    public List<EntityUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("${application.endpoint.admin.users}")
    @ApiOperation("Создание всех счетов и балансов. Админка.")
    public ResponseEntity<?> createUser(@RequestBody EntityUser entityUser) {
        try {
            if (userRepository.findByName(entityUser.getName()) == null) {
                userService.createUser(entityUser);
                return new ResponseEntity<>(entityUser, HttpStatus.OK);
            }
        } catch (ValidationException e) {
            return new ResponseEntity<>("Not correct request",HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>("Not correct request",HttpStatus.BAD_REQUEST);
    }

}
