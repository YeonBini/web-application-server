package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import util.HttpRequestUtils.Pair;

public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is("password2"));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined"), is("true"));
        assertThat(parameters.get("JSessionId"), is("1234"));
        assertThat(parameters.get("session"), is(nullValue()));
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair, is(new Pair("userId", "javajigi")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }

    @Test
    public void getRequestMethod() {
    }

    @Test
    public void getUrlPath() {
        // given
        String url = "GET /user/create?userId=&password=7537695aA%21&name=&email=yeonbn88%40gmail.com HTTP/1.1";

        // then
        assertEquals(HttpRequestUtils.getUrlPath(url), "/user/create");
    }

    @Test
    public void getParams() {
        // given
        String url = "GET /user/create?userId=%EC%A0%95%EC%97%B0%EB%B9%88&password=1234&name=%EC%A0%95%EC%97%B0%EB%B9%88&email=yeonbn%40hotmail.com HTTP/1.1";
//        String url = "GET /user/create HTTP/1.1";
        String [] line = url.split(" ");
        // then
        System.out.println(line[1].indexOf("?"));
        System.out.println(line[1].substring(line[1].indexOf("?") + 1));
        System.out.println(HttpRequestUtils.getParams(url));
        System.out.println(HttpRequestUtils.getParams(url).get("userId"));
    }

    @Test
    public void predicateTest() {
        boolean test = false;
        System.out.println("abc = " +test);
    }
}
