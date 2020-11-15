package com.example.timeserver.service;

import com.example.timeserver.model.Email;
import com.example.timeserver.model.Key;
import com.example.timeserver.model.UserPass;
import com.example.timeserver.utils.Users;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class EmailService {


    public Key login(UserPass userPass) throws HttpClientErrorException {

        return Key.builder()
                .primaryKey(Users.USERS.entrySet().stream()
                        .filter(e -> userPass.getUsername().equals(e.getValue().getUsername()) && userPass.getPassword().equals(e.getValue().getPassword()))
                        .findFirst()
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "I'm sorry, we could not find your account"))
                        .getKey())
                .build();
    }

    public boolean send() {
        Users.EMAILS.add(Email.builder().build());
        return true;
    }
}
