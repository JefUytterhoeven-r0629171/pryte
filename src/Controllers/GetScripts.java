package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Script;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GetScripts extends RequestHandler {
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        ArrayList<Script> scripts = new ArrayList<Script>();
        Script script = new Script();

        System.out.println("scripts are being loaded");
        File[] files = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"/pryteapp/uploadedfiles").listFiles();

        for (File file : files) {
            if (file.isDirectory()) {

                System.out.println("dir: " + file.getName());
            } else {
                script.setNaam(file.getName());
                script.setPath(file.getPath());
                script.setExtension( file.getName().substring(file.getName().lastIndexOf(".") + 1));

                scripts.add(script);
                script = new Script();
            }

        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            String personenJSON = mapper.writeValueAsString(scripts);
            System.out.println(personenJSON);
            response.setContentType("application/json");
            response.getWriter().write(personenJSON);
        } catch (JsonProcessingException e) {

        } catch (IOException e) {
            e.getMessage();
        }


        //request.setAttribute("scripts" , scripts);
        return "index.jsp";
    }
}
