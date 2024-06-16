package com.example.uberprojectspringsocket.controller;

import com.example.uberprojectspringsocket.dto.RideRequestDto;
import com.example.uberprojectspringsocket.dto.RideResponseDto;
import com.example.uberprojectspringsocket.dto.UpdateBookingRequestDto;
import com.example.uberprojectspringsocket.dto.UpdateBookingResponseDto;
import com.example.uberprojectspringsocket.producers.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/socket")
public class DriverRequestController {
    private SimpMessagingTemplate messagingTemplate;
    private RestTemplate restTemplate;
    private KafkaProducerService kafkaProducerService;

    public DriverRequestController(SimpMessagingTemplate messagingTemplate,KafkaProducerService kafkaProducerService) {
        this.messagingTemplate = messagingTemplate;
        this.restTemplate = new RestTemplate();
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping
    public Boolean help(){
        kafkaProducerService.publishMessage("sample-topic","Hello");
        return true;
    }

    @PostMapping("/newride")
    @CrossOrigin(originPatterns = "*")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        System.out.println("Request for rides recieved");
        sendDdriversNewRideRequest(rideRequestDto);
        System.out.println("Req Completed");
        return ResponseEntity.ok(true);
    }

    public void sendDdriversNewRideRequest(RideRequestDto rideRequestDto) {
        //Ideally the request should only go to nearby drivers, but for simplicity we send it to everyone
        messagingTemplate.convertAndSend("/topic/rideRequest", rideRequestDto);

    }

    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId, RideResponseDto rideResponseDto){
        System.out.println(rideResponseDto.getResponse()+" "+userId);
        UpdateBookingRequestDto updateBookingRequestDto = UpdateBookingRequestDto.builder()
                .driverId(Optional.of(Long.parseLong(userId)))
                .status("SCHEDULED")
                .build();
        ResponseEntity<UpdateBookingResponseDto> result=restTemplate.postForEntity("http://localhost:7000/api/v1/booking/"+rideResponseDto.bookingId, updateBookingRequestDto, UpdateBookingResponseDto.class);
        kafkaProducerService.publishMessage("sample-topic","Hello");
        System.out.println(result.getStatusCode());
    }

}
