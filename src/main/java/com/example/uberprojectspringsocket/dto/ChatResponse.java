package com.example.uberprojectspringsocket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String name;
    private String message;
    private String timestamp;

}
