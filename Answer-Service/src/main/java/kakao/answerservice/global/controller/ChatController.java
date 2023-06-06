package kakao.answerservice.global.controller;

import kakao.answerservice.global.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat.addUser")    // 유저 접속시
    @SendTo("/topic/public")
    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessage,
                                  SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("sender: "+ chatMessage.getSender());    // 유저가 보낸 sender값(userId)
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getContent());
        return chatMessage;
    }


    // http Get /test/testt 요청받았을 때, 연결된 소켓으로 "finished" 보냄.
    @GetMapping("/test/testt")
    @SendTo("/topic/public")
    public ResponseEntity<?> sendFinished() {
        System.out.println("Get Mapping 성공2");
        messagingTemplate.convertAndSend("/topic/public", "finished");
        return new ResponseEntity<>("finished", HttpStatus.OK);
    }

}
