package http;

import model.User;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class HttpSessionTest {

    @Test
    public void getAttribute() {
        // given
        User user = new User("111", "111", "111", "111");
        HttpSession session = new HttpSession(UUID.randomUUID().toString());

        // when
        session.setAttribute("user", user);
        System.out.println(session.getAttribute("user").toString());
        System.out.println(HttpSessions.getSessions(session.getId()));

    }
}