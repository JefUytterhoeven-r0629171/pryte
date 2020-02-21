package domain;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RunPythonScript implements RunScript {
    public Process runScript(String path, String argumenten){
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = null;
        String output ="";
        try {
            // Run this on Windows, cmd, /c = terminate after this run
            if(argumenten == null){
                processBuilder.command("python", path );
            }else{
                processBuilder.command("python", path, argumenten );
            }

            System.out.println("het proces word aangemaakt");
            processBuilder.start();
            process = processBuilder.start();

            /*
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
        return process;
    }
}
