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
        // lijst maken om de scripts bij te houden en om de processen bij te houden. het is belangrijk dat de index van het script hetzelfde is als dit van zijn process
        Script[] scripts = null;
        ArrayList<Process> processes = new ArrayList<Process>();

        try { // get het json object met scripts dat door de webpagina naar de server word gestuurt. hierien staan al de te runnen scripts.
            // 1. get received JSON data from request
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(request.getInputStream()));

            String json = "";
            if (br != null) {
                json = br.readLine();

            }

            // maak van deze json scripts echte scripts.
            scripts = new ObjectMapper().readValue(json, Script[].class);

            String output = null;
            // runner is een interface om processen op te starten. en splitst op voor python en R
            RunScript runner;
            for (int i = 0 ; i < scripts.length ; i++){
                // zorg dat de input en output's van deze scripts volledig leeg zijn. anders worden deze bijgehouden indien het script meerdere keren word gerunned.
                scripts[i].resetinput();
                scripts[i].resetoutput();
                // start de processen op. apparte manier voor Rscript en voor python
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


            // de processen zijn opgestart nu gaan we voor elk process achter elkaar de outputs en inputs lezen.
            for (int i=0; i < processes.size();i++){
                System.out.println("het zooveelste proces word geladen : " +i);
                if(i >0){// enkel wanneer het het 2de script is kunnen er input waardes zijn. zou eens moeten testen zonder deze if
                    // zorg dat de output van het vorige script word doorgegeven aan de input van dit script. dit zal veranderen wanneer men zelf kan kiezen van waar naar waar we gaan
                    for (int j=0;j < scripts[i].getInputIndex().size(); j++){
                        String inputindex = scripts[i].getInputIndex().get(j);
                        System.out.println(inputindex);
                        int scriptindex = Integer.parseInt(inputindex.substring(inputindex.length() -3,inputindex.length()-2));
                        int varindex = Integer.parseInt(inputindex.substring(inputindex.length() -1,inputindex.length()));
                        scriptindex--;
                        varindex--;
                        System.out.println("de scriptindex = " + scriptindex + "de varindex = " + varindex);
                        scripts[i].addInput(scripts[scriptindex].getOutputlijst().get(varindex));
                        System.out.println(scripts[scriptindex].getOutputlijst().get(varindex));
                    }
                    System.out.println(scripts[i].getInputlijst());

                   /*
                    System.out.println(scripts[1].getInputIndex());
                    String inputindex = scripts[i].getInputIndex().get(0);

                    int scriptindex = Integer.parseInt(inputindex.substring(inputindex.length() -3,inputindex.length()-2));
                    int varindex = Integer.parseInt(inputindex.substring(inputindex.length() -1,inputindex.length()));
                    System.out.println(scriptindex +" " +varindex);
                    scripts[1].addInput(scripts[0].getOutputlijst().get(0));
                    */
                    //vul alle stdin's in.
                    this.writeAllInputVariables(processes.get(i), scripts[i]);
                }
                // read alles outputs van het script en slaag deze op.
                this.readAllOutputVariabels(processes.get(i) , scripts[i]);
                // sluit het script af nadat alles gebeurt is. dit om te zorgen dat er geen memory leaks zijn
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

            // geef de bewerkte scripts terug naar de webpagina als een json.
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

    //functie om stdout's van scripts te lezen
    public void readAllOutputVariabels(Process process , Script script){
        System.out.println("read de variabelen van het programma");
        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        boolean tester = false;
        try {
            String lastline  = reader.readLine();;
            while (lastline != null){

                System.out.println(lastline + process.isAlive());
                if (lastline.equals("!!final var!!")){
                    tester = true;
                }else {
                    script.addOutput(lastline);
                }
                lastline = reader.readLine();
            }
                //lastline = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
// functie om stdin's van script te schrijven
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
