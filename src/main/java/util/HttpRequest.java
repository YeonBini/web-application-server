package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    public HttpRequest(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        try {
            // step 1. method와 url 세팅
            String[] token = br.readLine().split(" ");
            method = setMethod(token);
            path = setPath(token);
        } catch (IOException e) {
            log.debug("Errors on reading first request line : {}", e);
        }

        // step 2. headers 세팅
        try {
            headers = setHeaders(br);
        } catch (IOException e) {
            log.debug("Errors on setting headers values : {}", e);
        }

        // step 3. parameters 세팅
        if ("POST".equals(method.toUpperCase())) {
            try {
                String content_length = headers.get("Content-Length");
                parameters = setPostParameter(br, Integer.parseInt(content_length), method);
            } catch (Exception e) {
                log.debug("Errors on setting parameters values : {}", e);
            }
        }

        if ("GET".equals(method.toUpperCase())) {
            int queryStringIndex = path.indexOf("?");

            if (queryStringIndex > -1) {
                parameters = setGetParameter(path.substring(queryStringIndex + 1));
                path = path.substring(0, queryStringIndex);
            }
        }


    }

    private Map<String, String> setGetParameter(String substring) {
        return HttpRequestUtils.parseQueryString(substring);
    }

    private Map<String, String> setPostParameter(BufferedReader br, int content_length, String method) throws IOException {
        String readRequestBody = IOUtils.readData(br, content_length);
        return HttpRequestUtils.parseQueryString(readRequestBody);
    }

    private String setPath(String[] token) {
        return token[1].trim();
    }

    private String setMethod(String[] token) {
        return token[0].trim();
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

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
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
    /**
     * 원래는 method와 url을 동시에 세팅해주고 싶었으나,
     * 최소 단위로 나누는 것이 좋을 것 같아 주석 처리함.
     */
//    class RequestMethodAndUrl {
//        private String method;
//        private String url;
//
//        public RequestMethodAndUrl(String method, String url) {
//            this.method = method;
//            this.url = url;
//        }
//
//        public String getMethod() {
//            return method;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//    }

}
