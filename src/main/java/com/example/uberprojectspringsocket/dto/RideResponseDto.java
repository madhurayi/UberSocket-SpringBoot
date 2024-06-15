package com.example.uberprojectspringsocket.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideResponseDto {
    public Boolean response;
    public Long bookingId;

}
