package com.example.timeserver.service;

import com.example.timeserver.config.ExternalMailConfiguration;
import com.example.timeserver.model.*;
import com.example.timeserver.utils.Users;
import model.SharedModel;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private final RestTemplate restTemplate;
    private final ExternalMailConfiguration externalMailConfiguration;

    public EmailService(RestTemplate restTemplate, ExternalMailConfiguration externalMailConfiguration) {
        this.restTemplate = restTemplate;
        this.externalMailConfiguration = externalMailConfiguration;
    }

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

    public Object sendEmail(SendMailRequest sendMailRequest) throws HttpClientErrorException {
        String from = validateFrom(sendMailRequest.getFrom());
        try {
            UUID to = Users.USERS.entrySet().stream()
                    .filter(e -> sendMailRequest.getTo().equals(e.getValue().getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new NullPointerException("No User"))
                    .getKey();

            Users.EMAILS.add(
                    Email.builder()
                            .from(sendMailRequest.getFrom())
                            .to(to)
                            .message(sendMailRequest.getMessage())
                            .build());
            return ResponseEntity.ok();
        } catch (Exception e) {
            ExternalMailRequest body = ExternalMailRequest.builder()
                    .from(from)
                    .to(sendMailRequest.getTo())
                    .message(sendMailRequest.getMessage())
                    .build();

            String headerValue = new String(Base64.getEncoder().encode(externalMailConfiguration.getKey().getBytes()));
            HttpHeaders headers = new HttpHeaders();
            headers.add("api-key", headerValue);

            HttpEntity<ExternalMailRequest> httpEntity = new HttpEntity<>(body, headers);

            try {
                SharedModel sharedModel = SharedModel.builder().build();
                ResponseEntity<SendResponse> x =
                        restTemplate.exchange("http://" + externalMailConfiguration.getUrl() + "/api/v1/email/receiveExternalMail", HttpMethod.POST, httpEntity, SendResponse.class);

                Optional<SendResponse> s = Optional.ofNullable(x.getBody());
                Optional<String> a = Optional.empty();

                return s.orElseThrow(() -> new Exception("")).getRecipient();

//                return  sR.getRecipient();


            } catch (Exception a) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
//            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public String validateFrom(UUID from) {
        String user = "";
        try {
            user = Users.USERS.get(from).getUsername();
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        if (user.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
