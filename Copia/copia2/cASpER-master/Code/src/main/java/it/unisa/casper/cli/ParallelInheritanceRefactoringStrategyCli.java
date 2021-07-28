package it.unisa.casper.cli;

import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.*;
import org.bouncycastle.util.Pack;

import java.util.ArrayList;

public class ParallelInheritanceRefactoringStrategyCli implements RefactoringStrategy {
    public ParallelInheritanceRefactoringStrategyCli(ClassBean super1, ClassBean super2, String project, ArrayList<PackageBean> packageBeans) {
        this.super1 = super1;
        this.super2 = super2;
        this.project = project;
        this.packageBeans = packageBeans;
    }

    @Override
    public void doRefactor() throws RefactorException {
        MethodBeanList methodBeanList = super2.methods;
        super1.methods = methodBeanList;

        InstanceVariableBeanList instanceVariableList = super2.instanceVariables;
        super1.instanceVariables = instanceVariableList;
    }

    private ClassBean super1;
    private ClassBean super2;
    private String project;
    private ArrayList<PackageBean> packageBeans;
}
