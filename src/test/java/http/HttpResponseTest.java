package http;

import http.HttpResponse;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Test
    public void findPath() throws IOException {
        System.out.println(Paths.get("./webapp").toAbsolutePath().toString());
        System.out.println(Paths.get("../webapp").toAbsolutePath().toString());
        System.out.println(Paths.get("/webapp").toAbsolutePath().toString());

        byte[] body = Files.readAllBytes(new File("./webapp/" + "index.html").toPath());
        System.out.println(new File("./webapp/" + "index.html").toURI());
        for(byte b :body) {
            System.out.print((char)b);
        }

    }


    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
