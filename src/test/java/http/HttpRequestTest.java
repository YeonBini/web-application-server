package http;

import http.HttpRequest;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    final private String testDirectory = "./src/test/resources/";

    @Test
    public void request_Get() throws FileNotFoundException {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_Get.txt"));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals(HttpMethod.GET, httpRequest.getMethod());
        assertEquals("/user/create", httpRequest.getPath());
        assertEquals("keep-alive", httpRequest.getHeaders().get("Connection"));
        assertEquals("logined=true", httpRequest.getHeader("Cookie"));
        assertEquals("111", httpRequest.getParameters().get("userId"));
    }
    @Test
    public void request_Get_Cookie() throws FileNotFoundException {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_Get.txt"));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals(HttpMethod.GET, httpRequest.getMethod());
        assertEquals("true", httpRequest.getCookie("logined"));
    }



    @Test
    public void request_Post() throws FileNotFoundException {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_Post.txt"));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals(HttpMethod.POST, httpRequest.getMethod());
        assertEquals("/user/create", httpRequest.getPath());
        assertEquals("keep-alive", httpRequest.getHeader("Connection"));
        assertEquals("111", httpRequest.getParameter("userId"));
    }

    @Test
    public void LengthTest() {
        System.out.println("userId=111&password=111&name=YeonBin".length());
    }

}
