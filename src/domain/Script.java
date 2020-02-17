package domain;

import java.util.ArrayList;

public class Script {
    String path , extension, naam;
    ArrayList inputtypes = new ArrayList<String>();
    ArrayList outputtypes = new ArrayList<String>();

    public Script(String path, String extension, String naam) {
        this.path = path;
        this.extension = extension;
        this.naam = naam;
    }

    public Script() {

    }

    public void addInputType(String type){
        inputtypes.add(type);
    }

    public void addOutputTypes(String type){
        outputtypes.add(type);
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public ArrayList getInputtypes() {
        return inputtypes;
    }

    public void setInputtypes(ArrayList inputtypes) {
        this.inputtypes = inputtypes;
    }

    public ArrayList getOutputtypes() {
        return outputtypes;
    }

    public void setOutputtypes(ArrayList outputtypes) {
        this.outputtypes = outputtypes;
    }
}
