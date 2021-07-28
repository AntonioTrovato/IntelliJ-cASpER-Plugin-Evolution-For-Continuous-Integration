package it.unisa.casper.cli;

import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MisplacedClassRefactoringStrategyCli implements RefactoringStrategy {
    public MisplacedClassRefactoringStrategyCli(ClassBean classBean, PackageBean targetPackage, String project) {
        this.classBean = classBean;
        this.targetBean = targetPackage;
        this.project = project;
    }

    public void doRefactor() throws RefactorException{
        try {
            String nome = classBean.getFullQualifiedName().substring(classBean.getFullQualifiedName().lastIndexOf(".")+1)+".java";
            Files.move(Paths.get(classBean.getPathToFile()+"/"+nome), Paths.get(targetBean.getFullQualifiedName()+"/"+nome));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ClassBean classBean;
    private PackageBean targetBean;
    private String project;
}
