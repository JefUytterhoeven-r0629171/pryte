package domain;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateScriptConfigFile {

    public void CreateConf(ArrayList<String> inputvar , ArrayList<String> outputvar, String name){

        try {
            List<String> lines = new ArrayList<String>();
            lines.add(":::inputvar:::");
            String types = "";
            for (int i=0; i <inputvar.size() ; i++){
                types += inputvar.get(i) +",";
            }
            if(!types.equals("")) {
                types = types.substring(0, types.length() - 1);
            }
            types +=';';
            lines.add(types);
            lines.add(":::outputvar:::");
            types = "";
            for (int i=0; i <outputvar.size() ; i++){
                types += outputvar.get(i) +",";
            }
            if(!types.equals("")) {
                types = types.substring(0, types.length() - 1);
            }
            types +=';';
            lines.add(types);

            File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"/pryteapp/configfiles/");
            if (! directory.exists()){
                directory.mkdirs();
            }

            Path file = Paths.get(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +"/pryteapp/configfiles/" + name +".txt" );
            Files.write(file, lines, StandardCharsets.UTF_8);
            //Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
