package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

//    private static boolean login_Yn;
    final private int LOGGED_IN = 1;
    final private int LOGGED_OUT = 2;

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
            if("".equals(line) || null == line) return;

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
                log.debug("[POST] params : {}", params);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;

            if("/user/create".equals(path)) {
//                UserUtils.addUser(requestMethod, path, params);
                DataBase.addUser(new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email")
                    )
                );

                log.debug("Find userby id : {}", DataBase.findUserById(params.get("userId")));
            }


            // userId, password를 통해 로그인
            int login_Yn = 0;
            if("/user/login".equals(path)) {
                User loginUser = DataBase.findUserById(params.get("userId"));
                path = "/user/list.html";
                login_Yn = LOGGED_IN;
                if(loginUser == null || !loginUser.getPassword().equals(params.get("password"))) {
                    login_Yn = 0;
                    path = "/user/login_failed.html";
                    log.debug("Login Failed!!!!!!!!");
                }
            }

            boolean logout_Yn = false;
            if("/user/logout".equals(path)) {
                login_Yn = LOGGED_OUT;
            }

            if("/user/list.html".equals(path)) {
                String cookie = headers.get("Cookie");
                if(cookie != null) {
                    Map<String, String> parseCookie = HttpRequestUtils.parseCookies(cookie);

                    if(true == Boolean.parseBoolean((parseCookie.get("logined")))) {
                        Collection<User> userList = DataBase.findAll();
                    }
                }

            }


            try {
                body = Files.readAllBytes(new File("./webapp" +path).toPath()); // file read util 로 빼기

                if(login_Yn > 0) {
                    response200HeaderWhenLoggedIn(dos, body.length, login_Yn);
                } else {
                    if(!path.endsWith(".css")) {
                        response200Header(dos, body.length, "html");
                    } else {
                        response200Header(dos, body.length, "css");
                    }

                }
                responseBody(dos, body);

            } catch (Exception e) {
                log.debug("Move to /index.html");
                response302Header(dos);
            }

            br.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
//            dos.writeBytes("Set-Cookie: logined="+ login_Yn + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String htmlOrCss) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + htmlOrCss+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderWhenLoggedIn(DataOutputStream dos, int lengthOfBodyContent, int login_Yn) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + (login_Yn==1) + " \r\n");
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
