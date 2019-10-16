package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpRequest에서 반환해야하는 값이 여러 개 있을 때
 * java의 경우 클래스를 만들어서 반환하는 것도 하나의 방법
 */
public class RequestLine {

    final private Logger log = LoggerFactory.getLogger(RequestLine.class);

    private HttpMethod method;
    private String path;
    private Map<String, String> params = new HashMap<>();

    public RequestLine(String requestLine) {
        log.debug("Request Line is {}", requestLine);
        String [] tokens = requestLine.split(" ");

        if(tokens.length != 3) {
            throw new IllegalArgumentException(requestLine + "이 형식에 맞지 않습니다.");
        }

        method = HttpMethod.valueOf(tokens[0]);

        if(method.isPost()) {
            path = tokens[1].trim();
            return;
        }

        if(method.isGet()) {
            int index = tokens[1].indexOf("?");

            if(index > -1) {
                path = tokens[1].substring(0, index);
                params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
            } else {
                path = tokens[1].trim();
            }
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
