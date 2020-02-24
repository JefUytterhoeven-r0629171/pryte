package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Script;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GetScripts extends RequestHandler {
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        ArrayList<Script> scripts = new ArrayList<Script>();
        Script script = new Script();
        String inputvar ="", outputvar="";


        File[] files = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"/pryteapp/uploadedfiles").listFiles();
        File conffile;



        for (File file : files) {
            if (file.isDirectory()) {


            } else {
                script.setNaam(file.getName());

                script.setPath(file.getPath());
                script.setExtension( file.getName().substring(file.getName().lastIndexOf(".") + 1));


                //read config file
                try(BufferedReader br = new BufferedReader(new FileReader(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"/pryteapp/configfiles/" +script.getNaam()+".txt"))) {
                    String line = br.readLine();

                    while (line != null) {

                        if(line.equals(":::inputvar:::")){
                            inputvar = br.readLine();
                        }
                        if(line.equals(":::outputvar:::")){
                            outputvar = br.readLine();
                        }
                        line = br.readLine();
                    }
                    String[] splitvar = inputvar.split(",");
                    for(int i=0 ; i< splitvar.length;i++){
                        splitvar[i] = splitvar[i].replace(";","");
                        script.addInputType(splitvar[i]);

                    }

                    splitvar = outputvar.split(",");
                    for(int i=0 ; i< splitvar.length;i++){
                        splitvar[i] = splitvar[i].replace(";","");
                        script.addOutputTypes(splitvar[i]);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("de conf file voor " +script.getPath() + " kon niet worden gevonden");
                } catch (IOException e) {
                    e.printStackTrace();
                }



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
