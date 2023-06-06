package ru.bank_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bank_app.entity.EntityAccount;
import ru.bank_app.entity.EntitySession;
import ru.bank_app.repository.SessionRepository;
import ru.bank_app.repository.UserRepository;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 111180000)
    public void clearAllSessions() {
        sessionRepository.deleteAll();
    }

    public boolean getSessionByUserId(BigInteger id) {
        if (sessionRepository.findByUserName(userRepository.findById(id.longValue()).getName()) == null) {
            return false;
        } else return true;
    }
}
