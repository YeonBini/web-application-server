package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> parameters;
    private RequestLine requestLine;

    public HttpRequest(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // step 1. method, path 설정
        try {
            String line = br.readLine();
            if(line == null) return;
            requestLine = new RequestLine(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // step 2. headers, cookies 세팅
        try {
            headers = setHeaders(br);
            cookies = setCookies();
        } catch (IOException e) {
            log.debug("Errors on setting headers values : {}", e);
        }

        // step 3. parameters 세팅
        if (requestLine.getMethod().isPost()) {
            try {
                String content_length = headers.get("Content-Length");
                parameters = setPostParameter(br, Integer.parseInt(content_length));
            } catch (Exception e) {
                log.debug("Errors on setting parameters values : {}", e);
            }
        } else {
            parameters = requestLine.getParams();
        }

    }

    private Map<String, String> setCookies() {
        return HttpRequestUtils.parseCookies(getHeader("Cookie"));
    }

    private Map<String, String> setPostParameter(BufferedReader br, int content_length) throws IOException {
        String readRequestBody = IOUtils.readData(br, content_length);
        return HttpRequestUtils.parseQueryString(readRequestBody);
    }

    private Map<String, String> setHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line;
        while ((line = br.readLine()) != null && !"".equals(line)) {
            String[] tokens = line.split(":");
            headers.put(tokens[0].trim(), tokens[1].trim());
        }

        return headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
}
