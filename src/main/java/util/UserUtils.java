package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *  http 통신을 하며 User Model의 생성, 조회, 수정, 삭제 등에 필요한 기능들을 이 곳에서 처리한다.
 */
public class UserUtils {

    private static Logger log = LoggerFactory.getLogger(UserUtils.class);

    public static void addUser(String requestMethod, String path, Map<String, String> params) {
        if(!params.isEmpty()) {
            if("/user/create".equals(path)) {
                User user = new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email"));

                log.debug("[{}] User : {}", requestMethod, user);
            }
        }
    }
}
