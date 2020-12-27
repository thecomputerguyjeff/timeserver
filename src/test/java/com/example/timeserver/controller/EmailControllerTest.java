package com.example.timeserver.controller;

import com.example.timeserver.config.ExternalMailConfiguration;
import com.example.timeserver.config.FeatureSwitchConfiguration;
import com.example.timeserver.model.Key;
import com.example.timeserver.model.UserPass;
import com.example.timeserver.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;


@RunWith(MockitoJUnitRunner.class)
public class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private FeatureSwitchConfiguration featureSwitchConfiguration;

    @InjectMocks
    private EmailController subject;

    @Test
    public void login_returnsSuccess_whenEmailIsTurnedOn_andLoginIsSuccessful() throws Exception {
        Key key = Key.builder()
                .primaryKey(UUID.randomUUID())
                .build();

        UserPass userPass = UserPass.builder().build();

        when(featureSwitchConfiguration.isEmailUp()).thenReturn(true);
        when(emailService.login(any(UserPass.class))).thenReturn(key);

        ResponseEntity actual = subject.login(userPass);

        verify(featureSwitchConfiguration).isEmailUp();
        verify(emailService).login(userPass);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(((Key)actual.getBody()).getPrimaryKey()).isEqualTo(key.getPrimaryKey());
    }

    @Test
    public void login_returnsServerDown_whenEmailIsTurnedOff() throws Exception {
        when(featureSwitchConfiguration.isEmailUp()).thenReturn(false);

        ResponseEntity actual = subject.login(UserPass.builder().build());

        verify(featureSwitchConfiguration).isEmailUp();

        assertThat(actual.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat((String)actual.getBody()).isEqualTo("Sorry, our server is down right now");
    }

    @Test
    public void login_returnsResponseWithException_whenEmailServiceThrowsHttpClientException() throws Exception {
        when(featureSwitchConfiguration.isEmailUp()).thenReturn(true);
        when(emailService.login(any(UserPass.class))).thenThrow(new HttpClientErrorException(BAD_REQUEST));

        UserPass userpass = UserPass.builder().build();
        ResponseEntity actual = subject.login(userpass);

        verify(featureSwitchConfiguration).isEmailUp();
        verify(emailService).login(userpass);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat((String)actual.getBody()).isEqualTo(BAD_REQUEST.toString());
    }

}