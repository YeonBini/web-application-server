package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController extends AbstractController {
    final private Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        super.service(request, response);
    }

    @Override
    void doPost(HttpRequest request, HttpResponse response) {
        if("/user/create".equals(request.getPath())) {
//            CreateUsers(request, response);
        }
    }


    @Override
    void doGet(HttpRequest request, HttpResponse response) {

    }


//    private void CreateUsers(HttpRequest request, HttpResponse response) {
//            User user = new User(
//                    request.getParameter("userId"),
//                    request.getParameter("password"),
//                    request.getParameter("name"),
//                    request.getParameter("email"));
//            DataBase.addUser(user);
//            log.debug("User :{}", user);
//
//            response.sendRedirect("/index.html");
//    }
}
