package com.example.timeserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class SendResponse {
    private UUID recipient;

    public SendResponse (UUID recipient) {
        this.recipient = Objects.requireNonNull(recipient);
    }
}
