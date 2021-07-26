package it.unisa.casper.cli;

import it.unisa.casper.adapters.GeneralCompilationUnit;
import it.unisa.casper.adapters.GeneralImportDeclaration;
import it.unisa.casper.adapters.GeneralPackage;
import it.unisa.casper.parser.ParsingException;
import it.unisa.casper.parser.PsiParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CommandLineCasper{

    public static void main(String[] args) throws IOException, ParsingException {
        Set<String> files = new HashSet<>();
        ArrayList<GeneralPackage> generalPackages = listOfPackage(args[0],files,null);
        PsiParser psiParser = new PsiParser(args[0],generalPackages);
        psiParser.parse();
        //System.out.println(project.getBasePath());
        /*Project project = null;
        final List<PackageBean>[] packageBeanList = new List[]{new ArrayList<>()};
        System.out.println("Comincio l'analisi tramite cASpER");
        PsiParser psiParser = new PsiParser(project);
        System.out.println("Effettuo il parsing");
        try {
            packageBeanList[0] = psiParser.parse();
        } catch (ParsingException e) {
            System.out.println("Errore durante il parsing");
            e.printStackTrace();
        }*/
    }

    public static ArrayList<GeneralPackage> listOfPackage(String directoryName, Set<String> pack, GeneralPackage generalPackage) throws IOException {
        File directory = new File(directoryName);
        ArrayList<GeneralPackage> generalPackages = new ArrayList<>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if (file.getAbsolutePath().endsWith("java")) {
                    String source = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    System.out.println(source);
                    GeneralCompilationUnit generalCompilationUnit = new GeneralCompilationUnit(source);
                    FileReader fr = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fr);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if(line.trim().startsWith("import")) {
                            generalCompilationUnit.addGeneralImportDeclaration(new GeneralImportDeclaration(line.substring(7)));
                        }
                    }
                    generalPackage.addGeneralCompilationUnit(generalCompilationUnit);
                }
                String path=file.getPath();
                String packName=path.substring(path.indexOf("src")+4, path.lastIndexOf('\\'));
                pack.add(packName.replace('\\', '.'));
            } else if (file.isDirectory()) {
                GeneralPackage generalPackage1 = new GeneralPackage(file.getName());
                generalPackages.add(generalPackage1);
                listOfPackage(file.getAbsolutePath(), pack, generalPackage1);
            }
        }
        return generalPackages;
    }

}
