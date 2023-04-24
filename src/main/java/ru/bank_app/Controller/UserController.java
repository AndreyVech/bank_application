package ru.bank_app.Controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank_app.entity.EntitySession;
import ru.bank_app.entity.EntityUser;
import ru.bank_app.repository.SessionRepository;
import ru.bank_app.repository.UserRepository;
import ru.bank_app.service.UserService;

import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("${application.endpoint.root}")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping("${application.endpoint.login}")
    @ApiOperation("Вход пользователя, получение сессии")
    public ResponseEntity<String> atmLogin(@RequestBody EntityUser user) {
        EntityUser dbUser = userService.findByName(user.getName());
        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
            EntitySession entitySession = new EntitySession();
            entitySession.setUserName(user.getName());
            sessionRepository.save(entitySession);
            return ResponseEntity.ok("Logged in successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}