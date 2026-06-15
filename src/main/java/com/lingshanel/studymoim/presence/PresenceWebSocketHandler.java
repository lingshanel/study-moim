package com.lingshanel.studymoim.presence;

import tools.jackson.databind.ObjectMapper;
import com.lingshanel.studymoim.user.domain.User;
import com.lingshanel.studymoim.user.repository.UserRepository;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class PresenceWebSocketHandler extends TextWebSocketHandler {

    private final UserRepository userRepository;
    private final PresenceService presenceService;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public PresenceWebSocketHandler(
            UserRepository userRepository,
            PresenceService presenceService,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.presenceService = presenceService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object userIdValue = session.getAttributes().get(PresenceHandshakeInterceptor.USER_ID_ATTRIBUTE);
        if (!(userIdValue instanceof Long userId)) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !user.isActive()) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return;
        }

        sessions.put(session.getId(), session);
        presenceService.connect(session.getId(), user);
        broadcastPresence();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        presenceService.disconnect(session.getId());
        broadcastPresence();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sessions.remove(session.getId());
        presenceService.disconnect(session.getId());
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
        broadcastPresence();
    }

    private void broadcastPresence() {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                    "type", "presence",
                    "members", presenceService.getOnlineMembers()
            ));
            TextMessage message = new TextMessage(payload);
            sessions.forEach((id, session) -> sendSafely(id, session, message));
        } catch (Exception ignored) {
            // Serialization errors leave the current presence state unchanged.
        }
    }

    private void sendSafely(String id, WebSocketSession session, TextMessage message) {
        if (!session.isOpen()) {
            sessions.remove(id);
            presenceService.disconnect(id);
            return;
        }
        try {
            synchronized (session) {
                session.sendMessage(message);
            }
        } catch (IOException exception) {
            sessions.remove(id);
            presenceService.disconnect(id);
        }
    }
}
