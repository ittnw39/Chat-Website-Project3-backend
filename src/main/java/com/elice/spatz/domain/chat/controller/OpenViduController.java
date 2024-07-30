package com.elice.spatz.domain.chat.controller;

import com.elice.spatz.domain.chat.service.OpenViduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class OpenViduController {

    @Autowired
    private OpenViduService openViduService;

    @PostMapping("/create-session")
    public String createSession(@RequestParam String sessionId) {
        try {
            return openViduService.createSession(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating session";
        }
    }

    @PostMapping("/get-token")
    public String getToken(@RequestParam String sessionId) {
        try {
            return openViduService.createToken(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating token";
        }
    }

    @GetMapping("/active-sessions")
    public List<String> getActiveSessions() {
        return openViduService.getActiveSessions();
    }
}

