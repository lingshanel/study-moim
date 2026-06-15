package com.lingshanel.studymoim.presence;

import static org.assertj.core.api.Assertions.assertThat;

import com.lingshanel.studymoim.user.domain.User;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class PresenceServiceTest {

    private final PresenceService presenceService = new PresenceService();

    @Test
    void multipleConnectionsFromSameUserAreShownOnce() throws Exception {
        User user = new User("member@example.com", "encoded", "스터디원");
        setId(user, 1L);

        presenceService.connect("connection-1", user);
        presenceService.connect("connection-2", user);

        assertThat(presenceService.getOnlineMembers()).containsExactly(
                new PresenceMemberResponse(1L, "스터디원")
        );

        presenceService.disconnect("connection-1");
        assertThat(presenceService.getOnlineMembers()).hasSize(1);

        presenceService.disconnect("connection-2");
        assertThat(presenceService.getOnlineMembers()).isEmpty();
    }

    private void setId(User user, Long id) throws Exception {
        Field field = User.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(user, id);
    }
}
