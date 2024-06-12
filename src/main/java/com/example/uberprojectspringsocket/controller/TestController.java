package com.example.uberprojectspringsocket.controller;

import com.example.uberprojectspringsocket.dto.ChatRequest;
import com.example.uberprojectspringsocket.dto.ChatResponse;
import com.example.uberprojectspringsocket.dto.TestRequest;
import com.example.uberprojectspringsocket.dto.TestResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    private SimpMessagingTemplate messagingTemplate;

    public TestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest testRequest){
        System.out.println("recieved message from client: " + testRequest.getData());
        return TestResponse.builder()
                .data(testRequest.getData())
                .build();
    }

//    @SendTo("/topic/schedule")
//    @Scheduled(fixedDelay = 2000)
//    public void fixedMessage(){
//        messagingTemplate.convertAndSend("/topic/schedule", "Periodic message from server" + System.currentTimeMillis());
//    }

    @MessageMapping("/chat/{room}")
    @SendTo("/topic/message/{room}")
    public ChatResponse chatMessage(@DestinationVariable String room,  ChatRequest chatRequest){
        ChatResponse response = ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .timestamp(""+System.currentTimeMillis())
                .build();
        return response;
    }

    @MessageMapping("/privateChat/{room}/{userid}")
    public void privateChatMessage(@DestinationVariable String room, @DestinationVariable String userid, ChatRequest chatRequest){
        ChatResponse response = ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .timestamp(""+System.currentTimeMillis())
                .build();
        messagingTemplate.convertAndSendToUser(userid,"/queue/privateMessage/" +  room, response);
    }
}
