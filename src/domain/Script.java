package domain;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Script {
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id;

    private String path , extension, naam /*output*/;
    private ArrayList inputtypes = new ArrayList<String>();
    private  ArrayList outputlijst = new ArrayList<String>();
    private  ArrayList inputlijst = new ArrayList<String>();
    private ArrayList outputtypes = new ArrayList<String>();

    public Script(String path, String extension, String naam) {
        this.path = path;
        this.extension = extension;
        this.naam = naam;
        id = count.incrementAndGet();
    }

    public ArrayList getOutputlijst() {
        return outputlijst;
    }

    public void setOutputlijst(ArrayList outputlijst) {
        this.outputlijst = outputlijst;
    }

    public void setOutputlijstString(String output){
        outputlijst = new ArrayList<String>();
        String[] out = output.split("_-_");

        for(int  i=0 ; i < out.length; i++){
            outputlijst.add(out[i]);
        }
    }

    public Script() {
        id = count.incrementAndGet();
    }

    public int getId(){
        return id;
    }

   /* public String getOutput() {
        String output = "";

        return output;
    }
/*
    public void setOutput(String output) {
        this.output = output;

        String[] out = output.split("_$_");

        for(int  i=0 ; i < out.length; i++){
            outputlijst.add(out[i]);
        }

    }*/

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

    public void addOutput(String line) {
        outputlijst.add(line);
    }

    public ArrayList<String> getInputlijst() {
        return inputlijst;
    }

    public void setInputlijst(ArrayList inputlijst) {
        this.inputlijst = inputlijst;
    }

    public void resetoutput() {
        outputlijst = new ArrayList<String>();
    }
    public void resetinput() {
        inputlijst = new ArrayList<String>();
    }

    public void addInput(String nieuw) {
        inputlijst.add(nieuw);
    }

}
