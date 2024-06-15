package com.example.uberprojectspringsocket.dto;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingRequestDto {
    private String status;
    private Optional<Long> driverId;
}
