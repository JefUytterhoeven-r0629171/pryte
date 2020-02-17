package Controllers;

import domain.RunPythonScript;
import sun.misc.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;


@MultipartConfig(maxFileSize = 16177215)
public class UploadFile extends RequestHandler {
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = null; // input stream of the upload file
        Part filePart = null;
        String naam = request.getParameter("naam");
        String FILE_TO = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"\\pryteapp\\uploadedfiles";
        System.out.println(FILE_TO);

        try {
            // make the directory for the files if it doesn't yet exist
            File directory = new File(FILE_TO);
            if (! directory.exists()){
                directory.mkdirs();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }

            System.out.println("enter try block RunFIle Runfile" + request.getParameter("file")  );
            filePart = request.getPart("file");

            if (filePart != null) {
                FILE_TO = FILE_TO + "\\"  + filePart.getSubmittedFileName();
                System.out.println(filePart.getContentType() + "  " );
                System.out.println("first if block RunFIle Runfile");

                System.out.println("secund if block RunFIle Runfile");
                // get extension
                String extension = filePart.getContentType();
                String[] parts = extension.split("/");
                //extension = parts[parts.length - 1];
                System.out.println("extention gotten " + filePart.getSubmittedFileName());
                // obtains input stream of the upload file
                inputStream = filePart.getInputStream();
                File file = new File(FILE_TO);
                copyInputStreamToFile(inputStream, file);

                System.out.println("normaal is de file gesaved hier"+ file.getAbsolutePath());
                RunPythonScript runner = new RunPythonScript();
                runner.runScript(FILE_TO, null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        return "index.jsp";
    }

    // InputStream -> File
    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // commons-io
            //IOUtils.copy(inputStream, outputStream);

        }

    }

}
