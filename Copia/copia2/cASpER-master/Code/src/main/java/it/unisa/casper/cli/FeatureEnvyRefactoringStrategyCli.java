package it.unisa.casper.cli;

import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;

public class FeatureEnvyRefactoringStrategyCli implements RefactoringStrategy {
    public FeatureEnvyRefactoringStrategyCli(MethodBean methodBeanToMove, String project) {
        this.methodBeanToMove = methodBeanToMove;
        this.project = project;
    }

    @Override
    public void doRefactor() throws RefactorException {
        ClassBean enviedClass = methodBeanToMove.getEnviedClass();
        enviedClass.addMethodBeanList(methodBeanToMove);
    }

    private MethodBean methodBeanToMove;
    private String project;
}
