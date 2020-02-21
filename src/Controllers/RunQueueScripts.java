package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.RunPythonScript;

import domain.RunScript;
import domain.RunnerRscript;
import domain.Script;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

public class RunQueueScripts extends RequestHandler {

    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        Script[] scripts = null;
        System.out.println("test");
        ArrayList<Process> processes = new ArrayList<Process>();

        try {
            // 1. get received JSON data from request
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(request.getInputStream()));

            String json = "";
            if (br != null) {
                json = br.readLine();

            }


            scripts = new ObjectMapper().readValue(json, Script[].class);
            System.out.println("hij is de que aan het laden");
            String output = null;
            RunScript runner;
            for (int i = 0 ; i < scripts.length ; i++){
                switch (scripts[i].getExtension()){
                    case "py" :
                        System.out.println("er word een python script geladen");
                        runner = new RunPythonScript();
                        processes.add(runner.runScript(scripts[i].getPath(), output));
                        //scripts[i].setOutputlijstString(output);
                        break;
                    case "R":
                        System.out.println("er word een R script geladen");
                        runner = new RunnerRscript();
                        processes.add(runner.runScript(scripts[i].getPath(), output));
                       // scripts[i].setOutputlijstString(output);
                        break;
                }
            }



            for (int i=0; i < processes.size();i++){
                System.out.println("het zooveelste proces word geladen : " +i);
                if(i >0){
                    scripts[i].setInputlijst(scripts[i-1].getOutputlijst());
                    System.out.println(scripts[i].getInputlijst());
                    this.writeAllInputVariables(processes.get(i), scripts[i]);
                }

                this.readAllOutputVariabels(processes.get(i) , scripts[i]);
                processes.get(i).destroyForcibly();
            }


         //hardcoded versien van stdin stdout voor 1 variabele door te geven van stdout naar stdin
/*
            System.out.println("de output en input streams worden gemaakt");
            OutputStream stdin = processes.get(1).getOutputStream();
            InputStream stdout = processes.get(0).getInputStream();
            InputStream stdout2 = processes.get(1).getInputStream();
            System.out.println("bufferreader's en writers worden gemaakt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(stdout2));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

            String lastline = reader.readLine();
            System.out.println("normaal word deze string gelezen" + lastline);

            writer.write(lastline);
            writer.flush();
            writer.close();
            lastline = reader2.readLine();
            System.out.println("please be turned" + lastline);
*/


        }catch (IOException e) {
                e.printStackTrace();
            }
        ObjectMapper mapper = new ObjectMapper();

        try {
            String personenJSON = mapper.writeValueAsString(scripts);
            response.setContentType("application/json");
            response.getWriter().write(personenJSON);
        } catch (JsonProcessingException e) {

        } catch (IOException e) {
            e.getMessage();
        }


        return "index.jsp";
    }

    public void readAllOutputVariabels(Process process , Script script){
        System.out.println("read de variabelen van het programma");
        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        boolean tester = false;
        try {
            String lastline;
            while (tester == false){
                lastline = reader.readLine();
                System.out.println(lastline + process.isAlive());
                if (lastline.equals("!!final var!!")){
                    tester = true;
                }else {
                    script.addOutput(lastline);
                }
            }
                //lastline = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeAllInputVariables(Process process , Script script){
        System.out.println("write de variablen naar het programma");
        OutputStream stdin = process.getOutputStream();
        //script.addInput("test dit als revers");
        System.out.println(script.getInputlijst().toString());

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
        String input = "";
        try {

            for (int i = 0 ; i < script.getInputlijst().size();i++){
                System.out.println("schrijf " +script.getInputlijst().get(i));
                if(i +1 < script.getInputlijst().size()){
                    input+= script.getInputlijst().get(i) + "\n";
                }else{
                    input+= script.getInputlijst().get(i);
                }


            }
            System.out.println(input);
            writer.write(input);

            writer.flush();
            writer.close();
            System.out.println(process.isAlive());
            System.out.println("exit the writer");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
