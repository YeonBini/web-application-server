package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private Logger log = LoggerFactory.getLogger(HttpSession.class);

    private String sessionId;
    private Map<String, Object> sessionAttribute = new HashMap<>();

    public HttpSession(String id) {
        this.sessionId = id;
        // HttpSession을 만들어줄 때 HttpSessions에 추가해준다.
        HttpSessions.addHttpSession(sessionId, this);
    }

    public String getId() {
        return this.sessionId;
    }

    public void setAttribute(String name, Object value) {
        sessionAttribute.put(name, value);
    }

    public Object getAttribute(String name) {
        return sessionAttribute.get(name);
    }

    public void removeAttribute(String name) {
        sessionAttribute.remove(name);
    }

    public void invalidate() {
        HttpSessions.remove(this.sessionId);
    }

}
