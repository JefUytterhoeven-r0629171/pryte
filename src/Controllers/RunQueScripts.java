package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.RunPythonScript;

import domain.RunScript;
import domain.RunnerRscript;
import domain.Script;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RunQueScripts extends RequestHandler {

    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("hij zit in de controller om scripts te runnen.");
        Script[] scripts = null;

        try {
            // 1. get received JSON data from request
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(request.getInputStream()));

            String json = "";
            if (br != null) {

                json = br.readLine();

                System.out.println(json);
            }
            scripts = new ObjectMapper().readValue(json, Script[].class);

            String output = null;
            RunScript runner;
            for (int i = 0 ; i < scripts.length ; i++){
                switch (scripts[i].getExtension()){
                    case "py" :
                        runner = new RunPythonScript();
                        output = runner.runScript(scripts[i].getPath(), "");
                        scripts[i].addOutputTypes(output);
                        break;
                    case "R":
                        runner = new RunnerRscript();
                        output = runner.runScript(scripts[i].getPath(), "");
                        scripts[i].addOutputTypes(output);
                        break;
                }
            }

            System.out.println(output);

        }catch (IOException e) {
                e.printStackTrace();
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


        return "index.jsp";
    }
}
