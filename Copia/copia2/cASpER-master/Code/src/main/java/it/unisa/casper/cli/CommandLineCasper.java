package it.unisa.casper.cli;

import it.unisa.casper.adapters.GeneralCompilationUnit;
import it.unisa.casper.adapters.GeneralImportDeclaration;
import it.unisa.casper.adapters.GeneralPackage;
import it.unisa.casper.analysis.code_smell.*;
import it.unisa.casper.parser.PsiParser;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.splitting_algorithm.SplitClasses;
import it.unisa.casper.refactor.splitting_algorithm.SplitPackages;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CommandLineCasper{
    public static void main(String[] args) throws Exception {
        System.out.println("Imposto i dati necessari");
        setSystem();
        //inizializzo un lettore per l'input
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //Avvio la raccolta dei package da analizzare con il parser
        Set<String> files = new HashSet<>();
        ArrayList<GeneralPackage> generalPackages = listOfPackage(args[0]+"/src",files,null);

        //Effettuo l'analisi dei package rilevati
        final List<PackageBean>[] packageList = new List[]{new ArrayList<>()};
        PsiParser psiParser = new PsiParser(args[0],generalPackages);
        packageList[0] = psiParser.parse();
        for(PackageBean packageBean : packageList[0]) {
            packageBeans.add(packageBean);
        }

        int i = 1;
        System.out.println("Ecco i code smell rilevati, scegline uno o digita un qualsiasi altro numero \nper non effettuare il refactoring: ");
        String line = "";
        HashMap<PackageBean,CodeSmell> packageCodeSmell = new HashMap<>();
        HashMap<ClassBean,CodeSmell> classCodeSmell = new HashMap<>();
        HashMap<MethodBean,CodeSmell> methodCodeSmell = new HashMap<>();
        for(PackageBean packageBean : packageList[0]) {
            for(CodeSmell codeSmell : packageBean.getAffectedSmell()) {
                line = String.valueOf(i) + " -> Member name: " + packageBean.getFullQualifiedName() + " | ";
                line += "Smell detected: " + codeSmell.getSmellName() + ".\n";
                packageCodeSmell.put(packageBean,codeSmell);
                i++;
            }
            for(ClassBean classBean : packageBean.getClassList()) {
                for(CodeSmell codeSmell : classBean.getAffectedSmell()) {
                    line += String.valueOf(i) + " -> Member name: " + classBean.getFullQualifiedName() + " | ";
                    line += "Smell detected: " + codeSmell.getSmellName() + ".\n";
                    classCodeSmell.put(classBean,codeSmell);
                    i++;
                }
                for(MethodBean methodBean : classBean.getMethodList()) {
                    for(CodeSmell codeSmell : methodBean.getAffectedSmell()) {
                        line += String.valueOf(i) + " -> Member name: " + methodBean.getFullQualifiedName() + " | ";
                        line += "Smell detected: " + codeSmell.getSmellName() + ".\n";
                        methodCodeSmell.put(methodBean,codeSmell);
                        i++;
                    }
                }
            }
        }

        System.out.println(line);

        int index = Integer.valueOf(bufferedReader.readLine()) - 1;
        if (index < 0) {
            System.out.println("Hai scelto di  annullare");
            return;
        }
        CodeSmell codeSmellScelto = null;
        PackageBean packageScelto = null;
        ClassBean classScelta = null;
        MethodBean methodScelto = null;
        if(index < packageCodeSmell.size()) {
            int valueIndex = 0;
            for(CodeSmell codeSmell : packageCodeSmell.values()) {
                if(valueIndex == index) {
                    codeSmellScelto = codeSmell;
                    break;
                }
                valueIndex++;
            }
            int keyIndex = 0;
            for(PackageBean packageBean : packageCodeSmell.keySet()) {
                if(keyIndex == index) {
                    packageScelto = packageBean;
                    break;
                }
                valueIndex++;
            }
            try {
                packageLevelRefactoring(packageScelto, codeSmellScelto, args[0]);
            }catch (RefactorException e) {
                System.out.println("Impossibile effettuare il refactoring\n");
            }
        }else if(index < classCodeSmell.size()) {
            int valueIndex = 0;
            for(CodeSmell codeSmell : classCodeSmell.values()) {
                if(valueIndex == index - packageCodeSmell.size()) {
                    codeSmellScelto = codeSmell;
                    break;
                }
                valueIndex++;
            }
            int keyIndex = 0;
            for(ClassBean classBean : classCodeSmell.keySet()) {
                if(keyIndex == index - packageCodeSmell.size()) {
                    classScelta = classBean;
                    break;
                }
                valueIndex++;
            }
            try {
                classLevelRefactoring(classScelta,codeSmellScelto,args[0]);
            }catch (Exception e) {
                System.out.println("Impossinile effettuare il refactoring");
            }
        }else if(index < methodCodeSmell.size()) {
            int valueIndex = 0;
            for(CodeSmell codeSmell : methodCodeSmell.values()) {
                if(valueIndex == (index - packageCodeSmell.size() - classCodeSmell.size())) {
                    codeSmellScelto = codeSmell;
                    break;
                }
                valueIndex++;
            }
            int keyIndex = 0;
            for(MethodBean methodBean : methodCodeSmell.keySet()) {
                if(keyIndex == (index - packageCodeSmell.size() - classCodeSmell.size())) {
                    methodScelto = methodBean;
                    break;
                }
                valueIndex++;
            }
            try {
                methodLevelRefactoring(methodScelto, codeSmellScelto, args[0]);
            }catch (Exception e) {
                System.out.println("Impossibile effettuare il refactoring");
            }
        }else {
            System.out.println("Hai scelto di annullare");
            return;
        }
    }

    public static void packageLevelRefactoring(PackageBean packageBean, CodeSmell codeSmell, String project) throws Exception{
        //tale smell sarà un Promicuous Package
        //splittiamo il package
        System.out.println("Package che si formeranno dopo il refactoring: ");
        ArrayList<PackageBean> obtainedPackageBeans = new ArrayList<>();
        for(PackageBean packageBean1 : new SplitPackages().split(packageBean,0)) {
            obtainedPackageBeans.add(packageBean1);
            System.out.println(packageBean1.getFullQualifiedName());
        }

        String risposta = "";
        while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
            System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
            risposta = bufferedReader.readLine();
            if(risposta.equals("n")) {
                return;
            }
        }

        PromiscuousPackageRefactoringStrategyCli refactoringStrategy = new PromiscuousPackageRefactoringStrategyCli(packageBean,obtainedPackageBeans,project);
        refactoringStrategy.doRefactor();

    }

    public static void classLevelRefactoring(ClassBean classBean, CodeSmell codeSmell, String project) throws Exception{
        if(codeSmell instanceof MisplacedClassCodeSmell) {
            System.out.println("Envied package: " + classBean.getEnviedPackage().getFullQualifiedName());

            String risposta = "";
            while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
                System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
                risposta = bufferedReader.readLine();
                if(risposta.equals("n")) {
                    return;
                }
            }

            MisplacedClassRefactoringStrategyCli refactoringStrategy = new MisplacedClassRefactoringStrategyCli(classBean,classBean.getEnviedPackage(),project);
            refactoringStrategy.doRefactor();

        }else if(codeSmell instanceof BlobCodeSmell) {
            //dovrà essere splittata la classe
            System.out.println("Classi che si formeranno dopo il refactoring: ");
            ArrayList<ClassBean> obtainedClassBeans = new ArrayList<>();
            for(ClassBean classBean1 : new SplitClasses().split(classBean,0)) {
                obtainedClassBeans.add(classBean1);
                System.out.println(classBean1.getFullQualifiedName());
            }

            String risposta = "";
            while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
                System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
                risposta = bufferedReader.readLine();
                if(risposta.equals("n")) {
                    return;
                }
            }

            BlobRefactoringStrategyCli refactoringStrategy = new BlobRefactoringStrategyCli(classBean,obtainedClassBeans,project);
            refactoringStrategy.doRefactor();

        }else if(codeSmell instanceof DivergentChangeCodeSmell) {
            //la classe sarà suddivisa
            System.out.println("Classi che si formeranno dopo il refactoring: ");
            ArrayList<ClassBean> obtainedClassBeans = new ArrayList<>();
            for(ClassBean classBean1 : new SplitClasses().splitClassHistory(classBean)) {
                obtainedClassBeans.add(classBean1);
                System.out.println(classBean1.getFullQualifiedName());
            }

            String risposta = "";
            while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
                System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
                risposta = bufferedReader.readLine();
                if(risposta.equals("n")) {
                    return;
                }
            }

            DivergentChangeRefactoringStrategyCli refactoringStrategy = new DivergentChangeRefactoringStrategyCli(classBean,obtainedClassBeans,project);
            refactoringStrategy.doRefactor();

        }else if(codeSmell instanceof ParallelInheritanceCodeSmell) {
            System.out.println("Superclasse: " + classBean.getSuperclass());
            ClassBean superClass = getSuperClassBean(classBean.getSuperclass());
            System.out.println("Parallel inheritance class: " + classBean.getParallelInheritanceClass().getFullQualifiedName());
            ClassBean parallelHineritanceClass = classBean.getParallelInheritanceClass();
            //il quarto parametro è proprtio this.packageBeans


            String risposta = "";
            while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
                System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
                risposta = bufferedReader.readLine();
                if(risposta.equals("n")) {
                    return;
                }
            }

            ParallelInheritanceRefactoringStrategyCli refactoringStrategy = new ParallelInheritanceRefactoringStrategyCli(superClass,parallelHineritanceClass,project,packageBeans);
            refactoringStrategy.doRefactor();

        }else if(codeSmell instanceof ShotgunSurgeryCodeSmell) {
            String risposta = "";
            while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
                System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
                risposta = bufferedReader.readLine();
                if(risposta.equals("n")) {
                    return;
                }
            }

            ShotgunSurgeryRefactoringStrategyCli refactoringStrategy = new ShotgunSurgeryRefactoringStrategyCli(classBean,project);
            refactoringStrategy.doRefactor();

        }
    }

    private static void methodLevelRefactoring(MethodBean methodBean, CodeSmell codeSmell, String project) throws Exception{
        String risposta = "";
        while (!(risposta.equals("s")) && !(risposta.equals("n"))) {
            System.out.println("\nVuoi effettuare il refactoring (s/n)? ");
            risposta = bufferedReader.readLine();
            if(risposta.equals("n")) {
                return;
            }
        }

        FeatureEnvyRefactoringStrategyCli refactoringStrategy = new FeatureEnvyRefactoringStrategyCli(methodBean,project);
        refactoringStrategy.doRefactor();
    }

    private static ClassBean getSuperClassBean(String name){
        for(PackageBean p : packageBeans){
            for(ClassBean c : p.getClassList()){
                if(c.getFullQualifiedName().equalsIgnoreCase(name)){
                    return c;
                }
            }
        }
        return null;
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
                String packName=path.substring(path.indexOf("src")+4, path.lastIndexOf('/'));
                pack.add(packName.replace('/', '.'));
            } else if (file.isDirectory()) {
                GeneralPackage generalPackage1 = new GeneralPackage(file.getName());
                generalPackages.add(generalPackage1);
                listOfPackage(file.getAbsolutePath(), pack, generalPackage1);
            }
        }
        return generalPackages;
    }

    public static void setSystem() {
        smell = new ArrayList<String>();
        smell.add("Feature");
        smell.add("Misplaced");
        smell.add("Blob");
        smell.add("Promiscuous");
        sogliaStructural = new ArrayList<Integer>();
        nameDir = System.getProperty("user.home")  + File.separator + ".casper";

        File dir = new File(nameDir);
        dir.mkdir();

        /*//crea i file .py per la detection dei code smell con analisi storica
        HistoryAnalysisStartup startup = new HistoryAnalysisStartup(nameDir);
        startup.writeScripts();
        //crea singleton per il path di python.exe
        PythonExeSingleton singleton = PythonExeSingleton.getIstance(nameDir);*/

        try {
            FileReader f = new FileReader(nameDir + File.separator + "threshold.txt");
            BufferedReader b = new BufferedReader(f);

            String[] list = null;
            double sogliaCoseno;
            sogliaStructural.add(0);
            int app = 0;

            for (String s : smell) {
                list = b.readLine().split(",");
                sogliaCoseno = Double.parseDouble(list[0]);
                if (sogliaCoseno < minC) {
                    minC = sogliaCoseno;
                }
                app = Integer.parseInt(list[1]);
                if (s.equalsIgnoreCase("promiscuous")) {
                    sogliaStructural.add(app);
                    sogliaStructural.add(Integer.parseInt(list[2]));
                } else {
                    if (!s.equalsIgnoreCase("blob")) {
                        if (app < sogliaStructural.get(0)) {
                            sogliaStructural.add(0, app);
                        }
                        algoritmo = list[2];
                    } else {
                        sogliaStructural.add(app);
                        sogliaStructural.add(Integer.parseInt(list[2]));
                        sogliaStructural.add(Integer.parseInt(list[3]));
                    }
                }
            }
        } catch (Exception e) {
            try {
                FileWriter f = new FileWriter(nameDir + File.separator + "threshold.txt");
                BufferedWriter out = new BufferedWriter(f);
                out.write("00.0,00,All\n" +
                        "00.0,00,All\n" +
                        "00.5,0350,020,0500,All\n" +
                        "00.5,050,050,All");
                out.flush();
                out.newLine();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            minC = 0.0;
            sogliaStructural.add(0);
            sogliaStructural.add(350);
            sogliaStructural.add(20);
            sogliaStructural.add(500);
            sogliaStructural.add(50);
            sogliaStructural.add(50);
            algoritmo = "All";
        }
    }

    private static BufferedReader bufferedReader;
    private static ArrayList<PackageBean> packageBeans = new ArrayList<>();
    private static String algoritmo, nameDir;
    private static ArrayList<String> smell;
    private static double minC = 0.5;
    private static ArrayList<Integer> sogliaStructural;
}
