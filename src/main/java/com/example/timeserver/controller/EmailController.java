package com.example.timeserver.controller;

import com.example.timeserver.config.ExternalMailConfiguration;
import com.example.timeserver.config.FeatureSwitchConfiguration;
import com.example.timeserver.model.UserPass;
import com.example.timeserver.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {

    private final EmailService emailService;
    private final ExternalMailConfiguration externalMailConfiguration;
    private final FeatureSwitchConfiguration featureSwitchConfiguration;

    public EmailController(EmailService emailService, ExternalMailConfiguration externalMailConfiguration, FeatureSwitchConfiguration featureSwitchConfiguration) {
        this.emailService = emailService;
        this.externalMailConfiguration = externalMailConfiguration;
        this.featureSwitchConfiguration = featureSwitchConfiguration;
    }

    @PostMapping("/login")
    public Object login(@RequestBody UserPass userPass) {
        try {
            if(featureSwitchConfiguration.isEmailUp()) {
                return new ResponseEntity<>(emailService.login(userPass), HttpStatus.OK);
            }
            return new ResponseEntity<>("Sorry, our server is down right now", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }
    }
}
