package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunRscript {
    public void runScript(String path, String argumenten){
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            System.out.println(Runtime.getRuntime().exec("C:\\PROGRA~1\\R\\R-3.6.2\\bin\\Rscript --vanilla " + path).getOutputStream());


            InputStream instream = null;
            // Run this on Windows, cmd, /c = terminate after this run
            if(argumenten == null){
                instream = Runtime.getRuntime().exec("C:\\PROGRA~1\\R\\R-3.6.2\\bin\\Rscript --vanilla " + path).getInputStream();
            }else{
                instream = Runtime.getRuntime().exec("C:\\PROGRA~1\\R\\R-3.6.2\\bin\\Rscript --vanilla " + path).getInputStream();
            }


            // blocked :(
            BufferedReader nreader =
                    new BufferedReader(new InputStreamReader(instream));

            String nline;
            while ((nline = nreader.readLine()) != null) {
                System.out.println(nline);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
