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
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        ) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            String line = br.readLine();
            log.debug("request line : {} ", line);

            if ("".equals(line) || null == line) return;

            String[] tokens = line.split(" ");
            boolean logined = false;
            int content_length = 0;
            while (!"".equals(line)) {
                log.debug("header : {} ", line);
                line = br.readLine().trim();

                if (line.contains("Cookie")) {
                    logined = isLogin(line);
                }

                if(line.contains("Content-Length")) {
                    content_length = getContentLength(line);
                }
            }

            String method = tokens[0].trim();
            String url = tokens[1].trim();

            if("POST".equals(method.toUpperCase())) {
                String body = IOUtils.readData(br, content_length);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);

                if ("/user/create".equals(url)) {
                    User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get(("email")));
                    DataBase.addUser(user);
                    log.debug("User :{}", user);

                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos, "/index.html");
                } else if ("/user/login".equals(url)) {
                    User user = DataBase.findUserById(params.get("userId"));

                    if(user == null) {
                        responseResource(out, "/user/login_failed.html");
                        return;
                    }

                    if(user.getPassword().equals(params.get("password"))) {
                        DataOutputStream dos = new DataOutputStream(out);
                        response302LoginSuccessHeader(dos);
                    } else {
                        responseResource(out, "/user/login_failed.html");
                    }
                }
            } else if ("/user/list".equals(url)) {
                if (!logined) {
                    responseResource(out, "/user/login.html");
                }
                Collection<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border='1'>");
                for (User user : users) {
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");

                byte[] body = sb.toString().getBytes();
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else if(url.endsWith(".css")) {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200CssHeader(dos, body.length);
                responseBody(dos, body);
            } else {
                responseResource(out, url);
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line) {
        String [] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    private void responseResource(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            byte [] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (Exception e) {
            response302Header(dos, "/index.html");
        }

    }

    private boolean isLogin(String line) {
        String[] headerToken = line.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerToken[1].trim());

        String value = cookies.get("logined");
        if (value == null) return false;
        return Boolean.parseBoolean(value);
    }

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: logined=true; path=/ \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + path + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
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
//            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
