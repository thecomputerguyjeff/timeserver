package com.example.timeserver.controller;

import com.example.timeserver.config.ExternalMailConfiguration;
import com.example.timeserver.config.FeatureSwitchConfiguration;
import com.example.timeserver.model.ExternalMailRequest;
import com.example.timeserver.model.UserPass;
import com.example.timeserver.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;

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

    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalMailRequest externalMailRequest,
                                      @RequestHeader("api-key") String key) {
        if(!"letMeIn".equals(new String(Base64.getDecoder().decode(key)))) {
            return new ResponseEntity<>("Incorrect Api Key", HttpStatus.UNAUTHORIZED);
        }
        if("NonUser".equals(externalMailRequest.getTo())) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
        if(!externalMailRequest.getMessage().isEmpty() && !externalMailRequest.getFrom().isEmpty()) {
            return new ResponseEntity<>("Message Received and Saved", HttpStatus.OK);
        }
        return new ResponseEntity<>("Missing fields", HttpStatus.BAD_REQUEST);
    }
}
