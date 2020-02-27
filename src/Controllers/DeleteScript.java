package Controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

public class DeleteScript extends RequestHandler{


    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {

        String naam = request.getParameter("naam");

        String FILE_TO = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"\\pryteapp\\uploadedfiles";

        FILE_TO = FILE_TO + "\\" + naam;
        System.out.println(FILE_TO);
        File file = new File(FILE_TO);
        file.delete();

        FILE_TO = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"\\pryteapp\\configfiles";
        FILE_TO = FILE_TO + "\\" + naam + ".txt";
        file = new File(FILE_TO);
        file.delete();

        return "index.jsp";
    }
}
