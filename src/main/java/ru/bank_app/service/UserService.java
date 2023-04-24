package ru.bank_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank_app.entity.EntityUser;
import ru.bank_app.exception.UserNotFoundException;
import ru.bank_app.repository.UserRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<EntityUser> getAllUsers() {
        return userRepository.findAll();
    }

    public EntityUser getUserById(BigInteger id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.longValue()));
    }

    public EntityUser createUser(EntityUser entityUser) {
        return userRepository.save(entityUser);
    }

    public EntityUser updateUser(BigInteger id, EntityUser userDetails) {
        EntityUser user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.longValue()));
        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(BigInteger id) {
        EntityUser user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.longValue()));
        userRepository.delete(user);
    }

    public EntityUser findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<EntityUser> findById(BigInteger userId) {
        return userRepository.findById(userId);
    }
}
