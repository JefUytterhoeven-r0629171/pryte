package Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import domain.RunPythonScript;

import domain.RunScript;
import domain.RunnerRscript;
import domain.Script;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RunQueueScripts extends RequestHandler {

    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        // lijst maken om de scripts bij te houden en om de processen bij te houden. het is belangrijk dat de index van het script hetzelfde is als dit van zijn process
        Script[] scripts = null;
        boolean procesopen;
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<InputStream> inputStreams = new ArrayList<InputStream>();
        ArrayList<BufferedReader> readers = new ArrayList<>();
        ArrayList<OutputStream> outputStreams = new ArrayList<>();
        ArrayList<BufferedWriter> writers = new ArrayList<>();
        Multimap<String, String> inoutmap = ArrayListMultimap.create();

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
            Process process = null;
            for (int i = 0 ; i < scripts.length ; i++){
                // zorg dat de input en output's van deze scripts volledig leeg zijn. anders worden deze bijgehouden indien het script meerdere keren word gerunned.
                scripts[i].resetinput();
                scripts[i].resetoutput();
                // start de processen op. apparte manier voor Rscript en voor python
                switch (scripts[i].getExtension()){
                    case "py" :
                        System.out.println("er word een python script geladen");
                        runner = new RunPythonScript();
                        process = runner.runScript(scripts[i].getPath(), output);
                        processes.add(process);
                        //scripts[i].setOutputlijstString(output);
                        break;
                    case "R":
                        System.out.println("er word een R script geladen");
                        runner = new RunnerRscript();
                        process = runner.runScript(scripts[i].getPath(), output);
                        processes.add(process);
                        break;
                }
                if(process != null){
                    System.out.println("aanmaken van re readers en writers");
                    InputStream stdout = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                    inputStreams.add(stdout);
                    readers.add(reader);
                    OutputStream stdin = process.getOutputStream();
                    outputStreams.add(stdin);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
                    writers.add(writer);
                }


            }
            // configure inoutmap for quick acces to the needed inputs.
            System.out.println("configureren van de inoutmap");
            for (int i =0; i < scripts.length;i++){
                System.out.println("inoutmap config voor elk scipt");
                for(int j = 0; j < scripts[i].getInputIndex().size();j++){
                    String inputindex = scripts[i].getInputIndex().get(j);
                    int scriptindex = Integer.parseInt(inputindex.substring(inputindex.length() -3,inputindex.length()-2));
                    int varindex = Integer.parseInt(inputindex.substring(inputindex.length() -1,inputindex.length()));
                    String outputindex = Integer.toString(scriptindex -1) +'_' +Integer.toString(varindex -1);
                    inputindex = Integer.toString(i) + "_" + Integer.toString(j);
                    inoutmap.put(outputindex , inputindex);
                    System.out.println("schow inoutmap " + inoutmap);
                }
            }

            //zolang er processen open zijn blijven we gaan.
            while (checkProcesOpen(processes)){
                System.out.println("loopen zolang alle processen niet gesloten zijn" + processes.size());
                for (int i=0;i< processes.size();i++){
                    System.out.println("i = " + i);

                    //test of het script nog niet geschreven inputs heeft.

                    while (scripts[i].getInputlijst().size() > scripts[i].getLastreadinput()){
                        System.out.println("2");

                        writers.get(i).write(scripts[i].getInputlijst().get(scripts[i].getLastreadinput()));
                        writers.get(i).flush();
                        if(scripts[i].getInputlijst().size() == scripts[i].getLastreadinput() +1){
                            writers.get(i).close();
                        }

                        scripts[i].incrementLastReadinput();
                    }


                    //check if there are new inputs to read. keep reading untile there aren't
                    String lastline = readers.get(i).readLine();
                    System.out.println(lastline);
                    while (lastline != null){
                        if (lastline.equals("!!final var!!")){
                            // de laatste variabele is bereikt en het proces word gesloten.
                            processes.get(i).destroy();
                            writers.get(i).close();
                            readers.get(i).close();
                            System.out.println("het proces" + i +"word gelsoten (gelsoten if false) = " +processes.get(i).isAlive());
                            lastline = null;
                        }else {
                            scripts[i].addOutput(lastline);
                            System.out.println("de laatst gelezen lijn van het " +i+ "script is " + lastline);
                            lastline = readers.get(i).readLine();
                        }
                        System.out.println("lastline = " + lastline);
                    }
                    System.out.println("de outputs voor dit scipt zijn momenteel gelezen");
                    // alle ingeleze variabelen moeten nu naar de juiste scripts gestuurt worden
                    while (scripts[i].getLastreadoutput() <scripts[i].getOutputlijst().size()){
                        System.out.println("start redirecting outputputs to inputs getLastreadoutput=" + scripts[i].getLastreadoutput() + " outputlijstsize " + scripts[i].getOutputlijst().size());
                        String mapkey = Integer.toString(i) + "_" + Integer.toString(scripts[i].getLastreadoutput());
                        Collection<String> inputs = inoutmap.get(mapkey);
                        System.out.println(mapkey);
                        System.out.println(inputs);
                        Iterator<String> m = inputs.iterator();
                        //System.out.println(m.next());
                        while(m.hasNext()){

                            String[] target = m.next().split("_");
                            System.out.println(target[0] +" , " +target[1]);
                            ArrayList<String> inlijst = scripts[Integer.parseInt(target[0])].getInputlijst();

                            if(inlijst.size() < Integer.parseInt(target[1])){
                                while (inlijst.size() < Integer.parseInt(target[1])){
                                    System.out.println("1.2");
                                    inlijst.add(null);
                                }
                            }
                            inlijst.add(Integer.parseInt(target[1]), scripts[i].getOutputlijst().get(scripts[i].getLastreadoutput()));
                            scripts[Integer.parseInt(target[0])].setInputlijst(inlijst);
                        }
                        scripts[i].incrementLastReadOutpu();
                    }


                    System.out.println(scripts[i].getInputlijst());



                    System.out.println("dit script is voorlopig gedaan index = " + i);

                }
            }

            System.out.println("alle processen zijn gelsoten");

/*
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

                    //vul alle stdin's in.
                    this.writeAllInputVariables(processes.get(i), scripts[i]);
                }
                // read alles outputs van het script en slaag deze op.
                this.readAllOutputVariabels(processes.get(i) , scripts[i]);
                // sluit het script af nadat alles gebeurt is. dit om te zorgen dat er geen memory leaks zijn
                processes.get(i).destroyForcibly();
            }
*/

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
        System.out.println("just a test");

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
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        }


        return "index.jsp";
    }

    private boolean checkProcesOpen(ArrayList<Process> processes) {
        boolean open = false;
        for (int i=0 ; i< processes.size();i++){
            if(processes.get(i).isAlive()){
                open = true;
            }
        }
        return open;
    }

    //functie om stdout's van scripts te lezen
    public void readAllOutputVariabels(Process process , Script script){
        System.out.println("read de variabelen van het programma");
        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
        boolean tester = false;
        try {
            String lastline  = reader.readLine();
            System.out.println(lastline + process.isAlive());
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
