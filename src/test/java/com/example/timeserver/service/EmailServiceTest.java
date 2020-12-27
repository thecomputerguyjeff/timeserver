package com.example.timeserver.service;

import com.example.timeserver.config.ExternalMailConfiguration;
import com.example.timeserver.model.Email;
import com.example.timeserver.model.SendResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalMailConfiguration externalMailConfiguration;

    @Mock
    ResponseEntity mockResponse;

    @InjectMocks
    private EmailService subject;

    @Test
    public void callExternal() throws Exception {

//        ResponseEntity mockResponse = mock(ResponseEntity.class);

        when(restTemplate.exchange(anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(SendResponse.class))
        ).thenReturn(mockResponse);

        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);

    }

}