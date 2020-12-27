package com.example.timeserver.controller;

import com.example.timeserver.config.ExternalMailConfiguration;
import com.example.timeserver.config.FeatureSwitchConfiguration;
import com.example.timeserver.model.ExternalMailRequest;
import com.example.timeserver.model.Key;
import com.example.timeserver.model.SendMailRequest;
import com.example.timeserver.model.UserPass;
import com.example.timeserver.service.EmailService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.xml.ws.Response;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {

    private final EmailService emailService;
    private final FeatureSwitchConfiguration featureSwitchConfiguration;

    public EmailController(EmailService emailService, FeatureSwitchConfiguration featureSwitchConfiguration) {
        this.emailService = emailService;
        this.featureSwitchConfiguration = featureSwitchConfiguration;
    }

    @ApiOperation(notes = "test notes", value = "this is a value")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "ok",
//                    response = String.class,
//                    examples = {@Example(value = @ExampleProperty(mediaType = "String", value = "1234-1234-12345678"))}
//            ),
//            @ApiResponse(
//                    code = 401,
//                    message = "unauthorized",
//                    response = String.class
//            )}
//    )
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserPass userPass) {
        try {
            if (featureSwitchConfiguration.isEmailUp()) {
                Key key = emailService.login(userPass);
                return new ResponseEntity<>(key, HttpStatus.OK);
            }
            return new ResponseEntity<>("Sorry, our server is down right now", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }
    }

    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalMailRequest externalMailRequest,
                                      @ApiParam()
                                      @RequestHeader("api-key")
                                              String key) {
        if (!"letMeIn".equals(new String(Base64.getDecoder().decode(key)))) {
            return new ResponseEntity<>("Incorrect Api Key", HttpStatus.UNAUTHORIZED);
        }
        if ("NonUser".equals(externalMailRequest.getTo())) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
        if (!externalMailRequest.getMessage().isEmpty() && !externalMailRequest.getFrom().isEmpty()) {
            return new ResponseEntity<>("Message Received and Saved", HttpStatus.OK);
        }
        return new ResponseEntity<>("Missing fields", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/send")
    public Object send(@RequestBody SendMailRequest sendMailRequest) {
        return emailService.sendEmail(sendMailRequest);
    }
}
