package com.example.timeserver.utils;

import com.example.timeserver.model.Email;
import com.example.timeserver.model.UserPass;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class Users {
    public final static Map<UUID, UserPass> USERS = Stream.of(
            new AbstractMap.SimpleEntry<>(UUID.randomUUID(), UserPass.builder()
                    .password("pass")
                    .username("jeff")
                    .build()),
            new AbstractMap.SimpleEntry<>(UUID.randomUUID(), UserPass.builder()
                    .password("password")
                    .username("jeffrey")
                    .build())
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public final static List<Email> EMAILS = new ArrayList<>();
}
