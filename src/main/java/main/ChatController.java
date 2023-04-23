package main;

import main.model.Message;
import main.model.MessageRepository;
import main.model.User;
import main.model.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController

public class ChatController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/init")
    public HashMap<String, Boolean> init(){
        HashMap<String, Boolean> response = new HashMap<>();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        Optional<User> user = userRepository.findBySessionId(sessionId);

        response.put("result", user.isPresent());
        return response;
    }

    @PostMapping("/auth")
    public HashMap<String, Boolean> auth(@RequestParam String name){
        HashMap<String, Boolean> response = new HashMap<>();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        if (!Strings.isNotEmpty(name)){
            response.put("result", false);
        }

        User user = new User();
        user.setName(name);
        user.setSessionId(sessionId);

        userRepository.save(user);

        response.put("result", true);
        return response;
    }
    @GetMapping("/message")
    public List<String> getMessagesList(){
        return new ArrayList<>();
    }
    @PostMapping("/message")
    public HashMap<String, Boolean> sendMessage (@RequestParam String message){
        HashMap<String, Boolean> response = new HashMap<>();
        if (Strings.isEmpty(message)){
            response.put("result", false);
            return response;
        }

        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = userRepository.findBySessionId(sessionId).get();

        Message newMessage = new Message();
        newMessage.setMessage(message);
        newMessage.setLocalDateTime(LocalDateTime.now());
        newMessage.setUser(user);

        messageRepository.save(newMessage);

        response.put("result", true);
        return response;
    }
    @GetMapping("/user")
    public HashMap<Integer, String> getUsersList(){
        return new HashMap<>();
    }
}
