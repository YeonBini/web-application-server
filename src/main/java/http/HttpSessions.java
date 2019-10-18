package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static Map<String, HttpSession> sessions = new HashMap<>();

    public static void addHttpSession(String uuid, HttpSession session) {
        sessions.put(uuid, session);
    }

    public static HttpSession getSessions(String uuid) {
        HttpSession session = sessions.get(uuid);

        if(session == null) {
            session = new HttpSession(uuid);
            addHttpSession(uuid, session);
        }

        return session;
    }

    static void remove(String id) {
        sessions.remove(id);
    }
}
