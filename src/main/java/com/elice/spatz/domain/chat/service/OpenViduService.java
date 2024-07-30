package com.elice.spatz.domain.chat.service;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.TokenOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenViduService {

    private OpenVidu openVidu;

    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    private Map<String, Session> sessions = new HashMap<>();

    @PostConstruct
    public void init() {
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    public String createSession(String sessionId) throws Exception {
        Session session = openVidu.createSession();
        sessions.put(sessionId, session);
        return session.getSessionId();
    }

    public String createToken(String sessionId) throws Exception {
        Session session = sessions.get(sessionId);
        if (session == null) {
            session = openVidu.createSession();
            sessions.put(sessionId, session);
        }
        TokenOptions tokenOptions = new TokenOptions.Builder()
            .build();
        return session.generateToken(tokenOptions);
    }

    public List<String> getActiveSessions() {
        return sessions.keySet().stream().collect(Collectors.toList());
    }
}
