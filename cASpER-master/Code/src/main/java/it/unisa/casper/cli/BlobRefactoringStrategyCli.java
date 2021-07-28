package it.unisa.casper.cli;

import com.intellij.openapi.project.Project;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlobRefactoringStrategyCli implements RefactoringStrategy {
    public BlobRefactoringStrategyCli(ClassBean originalClass, ArrayList<ClassBean> splittedList, String project) {
        this.originalClass = originalClass;
        this.splittedList = splittedList;
        this.project = project;
    }

    @Override
    public void doRefactor() throws RefactorException {
        for (ClassBean classBean : splittedList) {
            File classFile = new File(classBean.getFullQualifiedName());
            try {
                FileWriter fileWriter = new FileWriter(classFile);
                fileWriter.write(classBean.getTextContent());
            }catch (IOException e) {
                e.printStackTrace();
            }
            (new File(originalClass.getFullQualifiedName())).delete();
        }
    }

    private ClassBean originalClass;
    private ArrayList<ClassBean> splittedList;
    private String project;
}
