package it.unisa.casper.adapters;

import java.util.ArrayList;

public class GeneralCompilationUnit {
    public GeneralCompilationUnit(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void addGeneralImportDeclaration(GeneralImportDeclaration generalImportDeclaration) {
        this.generalImportDeclarationsArrayList.add(generalImportDeclaration);
    }

    public ArrayList<GeneralImportDeclaration> getGeneralImportDeclarationsArrayList() {
        return this.generalImportDeclarationsArrayList;
    }

    private String source;
    private ArrayList<GeneralImportDeclaration> generalImportDeclarationsArrayList = new ArrayList<GeneralImportDeclaration>();
}
