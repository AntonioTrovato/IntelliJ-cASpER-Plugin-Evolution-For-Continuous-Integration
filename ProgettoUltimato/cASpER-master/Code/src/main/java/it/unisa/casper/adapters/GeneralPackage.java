package it.unisa.casper.adapters;

import java.util.ArrayList;

public class GeneralPackage {
    public GeneralPackage(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void addGeneralCompilationUnit(GeneralCompilationUnit generalCompilationUnit) {
        this.generalCompilationUnitArrayList.add(generalCompilationUnit);
    }

    public ArrayList<GeneralCompilationUnit> getGeneralCompilationUnits() {
        return this.generalCompilationUnitArrayList;
    }

    private String elementName;
    private ArrayList<GeneralCompilationUnit> generalCompilationUnitArrayList = new ArrayList<GeneralCompilationUnit>();
}
