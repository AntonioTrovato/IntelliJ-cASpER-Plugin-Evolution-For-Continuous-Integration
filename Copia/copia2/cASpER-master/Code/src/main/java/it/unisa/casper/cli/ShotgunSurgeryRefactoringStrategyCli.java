package it.unisa.casper.cli;

import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.MethodBeanList;

import java.util.ArrayList;
import java.util.List;

public class ShotgunSurgeryRefactoringStrategyCli implements RefactoringStrategy {
    public ShotgunSurgeryRefactoringStrategyCli(ClassBean classBean, String project) {
        this.classBean = classBean;
        this.project = project;
    }

    @Override
    public void doRefactor() throws RefactorException {
        List<ClassBean> affectedClass = classBean.getShotgunSurgeryHittedClasses();

        for (ClassBean classBean : affectedClass) {
            for (MethodBean methodBean : this.classBean.getMethodList()) {
                classBean.addMethodBeanList(methodBean);
            }
        }
    }

    private ClassBean classBean;
    private String project;
}
