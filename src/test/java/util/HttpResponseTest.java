package util;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HttpResponseTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws FileNotFoundException {
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Forward.txt"));
        httpResponse.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws FileNotFoundException {
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        httpResponse.sendRedirect("/index.html");

    }

    @Test
    public void responseCookies() throws FileNotFoundException {
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Cookies.txt"));
        httpResponse.addHeaders("Set-Cookie", "logined=true");
        httpResponse.sendRedirect("/index.html");

    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
