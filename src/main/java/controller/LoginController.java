package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends AbstractController{
    @Override
    public void service(HttpRequest request, HttpResponse response) {
       super.service(request, response);
    }

    @Override
    void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));

        if (user == null) {
            response.sendRedirect("/user/login_failed.html");
            return;
        }

        if (user.getPassword().equals(request.getParameter("password"))) {
//            response.addHeaders("Set-Cookie", "logined=true;path=/");

            // ADD SESSION
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }

    @Override
    void doGet(HttpRequest request, HttpResponse response) {

    }
}
