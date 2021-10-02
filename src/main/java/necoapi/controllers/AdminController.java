package necoapi.controllers;

import necoapi.domain.TopUp;
import necoapi.domain.TopUpResponse;
import necoapi.domain.UserRequest;
import necoapi.domain.UserResponse;
import necoapi.models.User;
import necoapi.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class AdminController {

    public static final String REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    Service service;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRequest user) {
        String password = user.getPassword();
        if (password.length() < 3) {
            throw new InvalidParameterException("Password must be at least 3 characters long");
        }
        if (user.getUsername().length() < 3) {
            throw new InvalidParameterException("Username must be at least 3 characters long");
        }
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(user.getEmail());
        if (!matcher.matches()) {
            throw new InvalidParameterException("Email address is invalid");
        }
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        User userSaved = service.createUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PostMapping("/account/{accountId}/topup")
    public ResponseEntity<Object> topUpAccountBalance(
            @PathVariable(name = "accountId") Long accountId,
            @RequestBody TopUp topUp) {
        TopUp topUpResponse = service.topUpAccount(topUp, accountId);
        return new ResponseEntity<>(topUpResponse, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}/topups")
    public ResponseEntity<Object> getTopUps(
            @PathVariable(name = "accountId") Long accountId) {
        List<TopUpResponse> topUpResponse = service.getTopUp(accountId);
        return new ResponseEntity<>(topUpResponse, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        List<UserResponse> userResponses = service.getUsers();
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

}
