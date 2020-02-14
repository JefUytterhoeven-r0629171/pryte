package service;

import domain.Script;

import java.util.ArrayList;

public class Service {
    private ArrayList scriptList = new ArrayList<Script>();

    public ArrayList getScriptList() {
        return scriptList;
    }

    public void setScriptList(ArrayList scriptList) {
        this.scriptList = scriptList;
    }

    public void addScript(Script s){
        scriptList.add(s);
    }
}
