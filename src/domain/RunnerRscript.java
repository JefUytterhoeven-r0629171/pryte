package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunnerRscript implements RunScript {

    public Process runScript(String path, String argumenten){
        /* eerste versie van processen runnen. dit door de argmuneten in het path mee te geven. dit werkte maar had beperkingen.
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = null;
        String output = "";

        try {
            //System.out.println(Runtime.getRuntime().exec("C:\\PROGRA~1\\R\\R-3.6.2\\bin\\Rscript --vanilla " + path).getOutputStream());


            InputStream instream = null;
            // Run this on Windows, cmd, /c = terminate after this run
            if(argumenten == null){
                instream = Runtime.getRuntime().exec("C:\\PROGRA~1\\R\\R-3.6.2\\bin\\Rscript --vanilla " + path ).getInputStream();
            }else{
                instream = Runtime.getRuntime().exec("C:\\PROGRA~1\\R\\R-3.6.2\\bin\\Rscript --vanilla " + path +" "+ argumenten).getInputStream();
            }

/*
            // blocked :(
            BufferedReader nreader =
                    new BufferedReader(new InputStreamReader(instream));

            String nline;
            while ((nline = nreader.readLine()) != null) {
                output += nline;

            }

        System.out.println(output);

        } catch (IOException e) {
            e.printStackTrace();
        }

*/

        //hier word een process aangemaakt voor een Rscript.
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = null;
        String output ="";
        try {
            // Run this on Windows, cmd, /c = terminate after this run
            if(argumenten == null){
                processBuilder.command("Rscript", path );
            }else{ // deze met argumenten word noormaal niet meer gebruikt. maar het doet niets verkeert dus blijft momenteel staan
                processBuilder.command("Rscript", path, argumenten );
            }

            System.out.println("het proces word aangemaakt " + processBuilder.command());
            // het process opstarten
            processBuilder.start();
            process = processBuilder.start();

            /* eerste versien van prints lezen. dit is vergealgemeent en verschoven naar RunQueScript
            // blocked :(
            BufferedReader nreader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String nline;
            while ((nline = nreader.readLine()) != null) {
                System.out.println(nline);
                output+= nline;
            }
        */

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("het proces is gemaakt en word doorgegeven");
        //geef het process terug naar RunQueueScripts
        return process;
    }
}
