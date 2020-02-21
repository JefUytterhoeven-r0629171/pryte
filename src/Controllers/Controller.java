package Controllers;

import domain.Script;
import service.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(maxFileSize = 16177215)
@WebServlet("/Controller")
public class Controller extends javax.servlet.http.HttpServlet {
    private Service model = new Service();
    private ControllerFactory factory = new ControllerFactory();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request,response);
    }

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String destination = "index.jsp";
        String type = request.getParameter("type");
        loadIndexPage(request,response);
        RequestHandler handler;

        if (action != null) {
            try {
                handler = factory.getController(action, model);
                destination = handler.handleRequest(request, response);
            } catch (NotAuthorizedException exc) {
                List<String> errors = new ArrayList<String>();
                errors.add(exc.getMessage());
                request.setAttribute("errors", errors);
                destination = "index.jsp";
            }
            if(type == null) {
                RequestDispatcher view = request.getRequestDispatcher(destination);
                view.forward(request, response);
            }
        }

        if(type == null) {
            if (action == null) {
                RequestDispatcher view = request.getRequestDispatcher(destination);
                view.forward(request, response);
            }
        }
    }

    public void loadIndexPage(HttpServletRequest request, HttpServletResponse response) {
        ArrayList scripts = new ArrayList<Script>();
        Script script = new Script();
        File[] files = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"/pryteapp/uploadedfiles").listFiles();

        for (File file : files) {
            if (file.isDirectory()) {


            } else {
                script.setNaam(file.getName());
                script.setPath(file.getPath());
                script.setExtension( file.getName().substring(file.getName().lastIndexOf(".") + 1));

                scripts.add(script);
                script = new Script();
            }

        }

        request.setAttribute("scripts" , scripts);

    }
}
