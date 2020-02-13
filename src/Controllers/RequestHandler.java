package Controllers;

import service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class RequestHandler {
    private Service service;

    public abstract String handleRequest (HttpServletRequest request, HttpServletResponse response);

    public void setModel (Service service) {
        this.service = service;
    }


}
