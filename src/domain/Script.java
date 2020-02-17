package domain;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Script {
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int ID;

    String path , extension, naam;
    ArrayList inputtypes = new ArrayList<String>();
    ArrayList outputtypes = new ArrayList<String>();

    public Script(String path, String extension, String naam) {
        this.path = path;
        this.extension = extension;
        this.naam = naam;
        ID = count.incrementAndGet();
    }


    public Script() {
        ID = count.incrementAndGet();
    }

    public int getId(){
        return ID;
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
