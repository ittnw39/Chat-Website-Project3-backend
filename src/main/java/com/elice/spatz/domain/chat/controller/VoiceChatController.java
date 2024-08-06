package com.elice.spatz.domain.chat.controller;

import com.elice.spatz.domain.chat.entity.VoiceChat;
import com.elice.spatz.domain.chat.service.VoiceChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/voiceChats")
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceChatController {

    @Autowired
    private VoiceChatService voiceChatService;

    @PostMapping
    public ResponseEntity<VoiceChat> createVoiceChat(@RequestBody VoiceChat voiceChat) {
        VoiceChat createdVoiceChat = voiceChatService.createVoiceChat(voiceChat);
        return ResponseEntity.ok(createdVoiceChat);
    }

    @GetMapping
    public ResponseEntity<List<VoiceChat>> getAllVoiceChats() {
        List<VoiceChat> voiceChats = voiceChatService.getAllVoiceChats();
        return ResponseEntity.ok(voiceChats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoiceChat> getVoiceChatById(@PathVariable Long id) {
        VoiceChat voiceChat = voiceChatService.getVoiceChatById(id);
        if (voiceChat != null) {
            return ResponseEntity.ok(voiceChat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}