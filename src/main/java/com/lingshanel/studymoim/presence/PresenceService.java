package com.lingshanel.studymoim.presence;

import com.lingshanel.studymoim.user.domain.User;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PresenceService {

    private final ConcurrentHashMap<String, PresenceMemberResponse> connections = new ConcurrentHashMap<>();

    public void connect(String connectionId, User user) {
        connections.put(connectionId, new PresenceMemberResponse(user.getId(), user.getNickname()));
    }

    public void disconnect(String connectionId) {
        connections.remove(connectionId);
    }

    public List<PresenceMemberResponse> getOnlineMembers() {
        return connections.values().stream()
                .distinct()
                .sorted(Comparator.comparing(PresenceMemberResponse::nickname, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
