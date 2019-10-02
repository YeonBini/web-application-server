package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.UserUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // step 1. BufferedReader를 통해 InputStream data를 읽을 수 있도록 선언
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // step 2. http request 방식, 경로 및 header 정보를 미리 정리해둔다.
            String line = br.readLine();
            String requestMethod = HttpRequestUtils.getRequestMethod(line);
            String path = HttpRequestUtils.getUrlPath(line);
            Map<String, String> headers = HttpRequestUtils.makeHeaders(br);

            // step 3. request 방식에 따라서 param을 다르게 처리해준다.
            Map<String, String> params = null;
            if("GET".equals(requestMethod)) {
               params = HttpRequestUtils.getParams(line);
            }


            if("POST".equals(requestMethod)) {
                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                params = HttpRequestUtils.parseQueryString(requestBody);
            }

            UserUtils.addUser(requestMethod, path, params);



            byte[] body;
            try {
                body = Files.readAllBytes(new File("./webapp" +path).toPath()); // file read util 로 빼기
            } catch (Exception e) {
                log.debug("Cannot find a file given path ");
                body = Files.readAllBytes(new File("./webapp/index.html").toPath()); // file read util 로 빼기
            }
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length);
            responseBody(dos, body);

            br.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
