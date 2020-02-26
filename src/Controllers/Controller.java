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
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(maxFileSize = 16177215)
@WebServlet("/Controller")
public class Controller extends javax.servlet.http.HttpServlet {
    private Service model = new Service();
    private ControllerFactory factory = new ControllerFactory();
    HttpSession session;

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
        this.session = request.getSession();
        String login = (String) this.session.getAttribute("login");
        System.out.println(login);

        //UNCOMMENT CODE BELOW TO ACTIVATE THE LOGIN
        //if(login != null && login.equals("true")){
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
       /*}else {
            // use this code to add the login
            if(action != null && action.equals("Login")){
                this.login(request, response);
                destination = "index.jsp";
                RequestDispatcher view = request.getRequestDispatcher(destination);
                view.forward(request, response);
            }else {
                destination = "login.jsp";
                RequestDispatcher view = request.getRequestDispatcher(destination);
                view.forward(request, response);
            }
            }*/



        if(type == null) {
            if (action == null) {
                System.out.println("de volgende pagina word geladen :" + destination);
                RequestDispatcher view = request.getRequestDispatcher(destination);
                view.forward(request, response);
            }
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String pas = request.getParameter("pass");
        String user = request.getParameter("usr");
        String seed = "S<4t56!sd@(n";
        int passlength = pas.length();
        String passseed  ="";

        passseed += pas.substring(0 , passlength/5);
        passseed += seed.substring(0,3);
        passseed += pas.substring(passlength/5 , passlength/5*2);
        passseed += seed.substring(3,6);
        passseed += pas.substring(passlength/5*2 , passlength/5*3);
        passseed += seed.substring(6,9);
        passseed += pas.substring(passlength/5*3,passlength/5*4);
        passseed+= seed.substring(6,12);
        passseed += pas.substring(passlength/5*4 , passlength );

        try {
            String hashed = this.hash(passseed);

            System.out.println(hashed);
            if (hashed.equals("66f7385ddabade525f675a9d85929f91f60b8985636e4392b8a3526317180b08504d00aef07e1297a6fc567703d31f0a57719c4478f89c48b78dfc6762e57e81")) {
                this.session.setAttribute("login", "true");
                System.out.println("user has loged in");
            }else {
                this.session.setAttribute("login", "false");
                System.out.println("wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

    public String hash(String toHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-512");
        crypt.reset();
        byte[] passwordBytes = toHash.getBytes("UTF-8");
        crypt.update(passwordBytes);
        byte[] digest = crypt.digest();
        return (new BigInteger(1, digest)).toString(16);
    }
}
