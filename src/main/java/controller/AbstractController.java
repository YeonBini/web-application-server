package controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

public abstract class AbstractController implements Controller{

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if(request.getMethod() == HttpMethod.GET) {
            doGet(request, response);
        }

        if(request.getMethod() == HttpMethod.POST) {
            doPost(request, response);
        }
    }

    abstract void doPost(HttpRequest request, HttpResponse response);

    abstract void doGet(HttpRequest request, HttpResponse response);
}
