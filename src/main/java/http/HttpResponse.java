package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    final private Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream os) {
        dos = new DataOutputStream(os);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp/" + url).toPath());

            if (url.endsWith(".css")) {
                headers.put("Content-Type", "text/css");
            } else if (url.endsWith(".js")) {
                headers.put("Content-Type", "application/javascript");
            } else {
                headers.put("Content-Type", "text/html");
            }
            headers.put("Content-Length", body.length + "");
            response200Header(body.length);
            responseBody(body);
        } catch (IOException e) {
            log.error("Errors on writing request body");
        }
    }

    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    public void forwardBody(String body) {
        byte[] content = body.getBytes();
        headers.put("Content_Type", "text/html;charset=utf-8 \r\n");
        headers.put("Content_Length", body.length() + "");
        response200Header(content.length);
        responseBody(content);
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("Location: "+ redirectUrl +" \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error("Errors when redirection : {}", e);
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void response200Header(int length) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error("Errors on writing response 200 header :{}", e);
        }
    }

    private void processHeaders() {
        try {
            for (String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) +"\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
