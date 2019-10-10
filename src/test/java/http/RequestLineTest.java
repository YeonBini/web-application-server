package http;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestLineTest {

    @Test
    public void createMethod() {
        // given
        RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");

        // then
        assertEquals("GET", requestLine.getMethod());

    }

    @Test
    public void getPath() {
        // given
        RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");

        // then
        assertEquals("/index.html", requestLine.getPath());
    }

    @Test
    public void getParams() {
        // given
        RequestLine requestLineWithNoParam = new RequestLine("GET /index.html HTTP/1.1");
        RequestLine requestLineWithParam = new RequestLine("GET /index.html?userId=111 HTTP/1.1");

        // then
        assertTrue(requestLineWithNoParam.getParams().isEmpty());
        assertEquals(null, requestLineWithNoParam.getParams().get("userId"));
        assertEquals("111", requestLineWithParam.getParams().get("userId"));
    }
}