package it.unisa.casper.cli;

import com.intellij.openapi.project.Project;
import it.unisa.casper.refactor.exceptions.PromiscuousPackageException;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class PromiscuousPackageRefactoringStrategyCli implements RefactoringStrategy {
    /**
     * PromiscuousPackageRefactoringStrategy è il costruttore dell'omonima classe
     *
     * @param packageBeanSource Package dal quale verranno estratte le classi
     * @param newPackages       Lista dei package da estrarre
     * @param project           Path del progetto nel quale sono presenti i PackageBean
     */
    public PromiscuousPackageRefactoringStrategyCli(PackageBean packageBeanSource, Collection<PackageBean> newPackages, String project) {
        this.packageBeanSource = packageBeanSource;
        this.newPackages = (List<PackageBean>) newPackages;
        this.project = project;
    }

    /**
     * Metodo che permette il refactoring di codeSmell di tipo Promiscuous Package
     *
     * @throws PromiscuousPackageException
     */
    @Override
    public void doRefactor() throws RefactorException {
        List<ClassBean> classBeanList;
        String pathPackage = packageBeanSource.getClassList().get(0).getPathToFile().replace("\\","/");
        String incopletePath = pathPackage.substring(0, pathPackage.lastIndexOf('/'));
        String path, fqn;
        int inizio = 1, i;

        //fqn = caspertrial.
        fqn = packageBeanSource.getFullQualifiedName().replace("\\","/");
        fqn = fqn.substring(0, fqn.lastIndexOf(".") + 1);
        if (fqn.equalsIgnoreCase("")) {
            fqn += ".";
        }

        while (findPackage(project,fqn + "Package" + inizio)==1) {
            inizio++;
        }
        i = inizio;

        for (PackageBean toPackage : newPackages) {
            path = fqn + "Package" + i++;
            toPackage.setFullQualifiedName(path.replace("\\","/"));
            classBeanList = toPackage.getClassList();

            path = incopletePath + "/" + toPackage.getFullQualifiedName().replace("\\","/").substring(toPackage.getFullQualifiedName().lastIndexOf(".") + 1);
            new File(path.replace("\\","/")).mkdir();

            for (ClassBean classBean : classBeanList) {
                try {
                    String nome = classBean.getFullQualifiedName().replace("\\","/").substring(classBean.getFullQualifiedName().replace("\\","/").lastIndexOf(".")+1)+".java";
                    Files.move(Paths.get(classBean.getPathToFile().replace("\\","/").replace("\\","/")+"/"+nome), Paths.get(path+"/"+nome));
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        (new File(pathPackage)).delete();

    }



    public static int findPackage(String directoryName, String packageName) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                if(file.getName().equals(packageName))
                    return 1;
                findPackage(file.getAbsolutePath(), packageName);
            }
        }
        return 0;
    }

    private PackageBean packageBeanSource;
    private List<PackageBean> newPackages;
    private String project;
}
