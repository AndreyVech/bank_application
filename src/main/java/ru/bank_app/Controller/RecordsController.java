package ru.bank_app.Controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank_app.dto.RecordsDto;
import ru.bank_app.repository.AcctRepository;
import ru.bank_app.repository.OperRepository;
import ru.bank_app.repository.SessionRepository;
import ru.bank_app.repository.UserRepository;
import ru.bank_app.service.AccountService;
import ru.bank_app.service.SessionService;
import ru.bank_app.service.UserService;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("${application.endpoint.root}")
@RequiredArgsConstructor
public class RecordsController {
    @Autowired
    private SessionService sessionService;
    private SessionRepository sessionRepository;
    private RecordsDto recordsDto;
    @Autowired
    private OperRepository operRepository;
    @Autowired
    private AcctRepository acctRepository;


    @PostMapping("${application.endpoint.app.getoperationlist}")
    @ApiOperation("Заказ выписки")
    public ResponseEntity<?> getOperationList(@RequestBody RecordsDto records
    ) throws ParseException {
//проверяем наличие сессии пользователя в базе.
        if (sessionService.getSessionByUserId(records.getUserId()))
//если пользак авторизован - запросим выписку
        {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd");
            Date dateStart = new Date();
            Date dateEnd = new Date();
            if (records.getDateStart().isEmpty()) {
                dateStart = format.parse("2000-01-01");
            } else {
                dateStart = format.parse(records.getDateStart());
            }
            System.out.println(dateStart);
            if (records.getDateEnd().isEmpty()) {
                dateEnd = format.parse("2099-01-01");
            } else {
                dateEnd = format.parse(records.getDateEnd());
            }
            if (!operRepository.findAllInPeriod(dateStart, dateEnd, records.getAccNum()).isEmpty()
            ) {
                return ResponseEntity.ok().body(operRepository.findAllInPeriod(
                        dateStart, dateEnd, records.getAccNum()));
            } else {
                return ResponseEntity.ok().body("Something went wrong");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
