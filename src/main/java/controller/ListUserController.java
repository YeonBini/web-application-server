package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Collection;

public class ListUserController extends AbstractController {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        super.service(request, response);
    }

    @Override
    void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogin(request.getCookie("logined"))) {
            response.sendRedirect("/user/login.html");
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

        response.forwardBody(sb.toString());
    }

    private boolean isLogin(String cookieValue) {
        if(cookieValue == null) return false;
        return Boolean.parseBoolean(cookieValue);
    }
}
