package it.unisa.casper.parser;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import it.unisa.casper.adapters.ClassBeanListAdapter;
import it.unisa.casper.adapters.GeneralPackage;
import it.unisa.casper.adapters.MethodBeanListAdapter;
import it.unisa.casper.adapters.PackageAdapter;
import it.unisa.casper.analysis.code_smell.*;
import it.unisa.casper.analysis.code_smell_detection.blob.StructuralBlobStrategy;
import it.unisa.casper.analysis.code_smell_detection.blob.TextualBlobStrategy;
import it.unisa.casper.analysis.code_smell_detection.feature_envy.StructuralFeatureEnvyStrategy;
import it.unisa.casper.analysis.code_smell_detection.feature_envy.TextualFeatureEnvyStrategy;
import it.unisa.casper.analysis.code_smell_detection.misplaced_class.StructuralMisplacedClassStrategy;
import it.unisa.casper.analysis.code_smell_detection.misplaced_class.TextualMisplacedClassStrategy;
import it.unisa.casper.analysis.code_smell_detection.promiscuous_package.StructuralPromiscuousPackageStrategy;
import it.unisa.casper.analysis.code_smell_detection.promiscuous_package.TextualPromiscuousPackageStrategy;
import it.unisa.casper.core.FileUtility;
import it.unisa.casper.beans.*;
import it.unisa.casper.storage.beans.InstanceVariableList;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//import it.unisa.casper.storage.beans.*;

public class PsiParser implements Parser {
    private final List<it.unisa.casper.storage.beans.PackageBean> projectPackages;
    private static String path;
    //Lista di threads
    private List<Thread> threadList;
    private ArrayList<GeneralPackage> generalPackages;

    public PsiParser(String path,ArrayList<GeneralPackage> generalPackages) throws ParsingException {
        this.path = path;
        projectPackages = new ArrayList<it.unisa.casper.storage.beans.PackageBean>();
        threadList = new ArrayList<>();
        this.generalPackages = generalPackages;
    }

    @Override
    public List<it.unisa.casper.storage.beans.PackageBean> parse() throws ParsingException {
        try {
            PackageBean parsedPackageBean;
            ArrayList<PackageBean> newProjectPackages = new ArrayList<PackageBean>();
            for (GeneralPackage generalPackage : generalPackages) {
                parsedPackageBean = parse(generalPackage);
                newProjectPackages.add(parsedPackageBean);
            }

            for(PackageBean packageBean : newProjectPackages) {
                it.unisa.casper.storage.beans.PackageBean.Builder packageBeanBuilder = new it.unisa.casper.storage.beans.PackageBean.Builder(packageBean.getName(),packageBean.getTextContent());
                ArrayList<it.unisa.casper.storage.beans.ClassBean> oldClassBeans = new ArrayList<it.unisa.casper.storage.beans.ClassBean>();
                for(ClassBean classBean : packageBean.getClasses()) {
                    it.unisa.casper.storage.beans.ClassBean.Builder classBeanBuilder = new it.unisa.casper.storage.beans.ClassBean.Builder(packageBean.getName()+"."+classBean.getName(),classBean.getTextContent());
                    ArrayList<it.unisa.casper.storage.beans.MethodBean> oldMethodBean = new ArrayList<it.unisa.casper.storage.beans.MethodBean>();
                    for(MethodBean methodBean : classBean.getMethods()) {
                        it.unisa.casper.storage.beans.MethodBean.Builder methodBeanBuilder = new it.unisa.casper.storage.beans.MethodBean.Builder(methodBean.getName(),methodBean.getTextContent());
                        methodBeanBuilder.setBelongingClass(new it.unisa.casper.storage.beans.ClassBean.Builder(classBean.getName(),classBean.getTextContent()).build());

                        ArrayList<it.unisa.casper.storage.beans.InstanceVariableBean> instanceVariableBeans = new ArrayList<it.unisa.casper.storage.beans.InstanceVariableBean>();
                        for(InstanceVariableBean instanceVariableBean : methodBean.getUsedInstanceVariables()) {
                            it.unisa.casper.storage.beans.InstanceVariableBean instanceVariableBean1 = new it.unisa.casper.storage.beans.InstanceVariableBean(instanceVariableBean.getName(),instanceVariableBean.getType(),instanceVariableBean.getInitialization(),instanceVariableBean.getVisibility());
                            instanceVariableBeans.add(instanceVariableBean1);
                        }
                        InstanceVariableList instanceVariableList = new InstanceVariableList();
                        instanceVariableList.setList(instanceVariableBeans);
                        methodBeanBuilder.setInstanceVariableList(instanceVariableList);

                        HashMap<String, it.unisa.casper.storage.beans.ClassBean> map = new HashMap<>();
                        for(SingleVariableDeclaration singleVariableDeclaration : methodBean.getParameters()) {
                            map.put(singleVariableDeclaration.getName().getIdentifier(),new it.unisa.casper.storage.beans.ClassBean.Builder(singleVariableDeclaration.getType().toString(),"").build());
                        }
                        methodBeanBuilder.setParameters(map);

                        Type type = methodBean.getReturnType();
                        methodBeanBuilder.setReturnType(new it.unisa.casper.storage.beans.ClassBean.Builder(type.toString(),"").build());

                        methodBeanBuilder.setVisibility(String.valueOf(methodBean.getVisibility()));

                        ArrayList<it.unisa.casper.storage.beans.MethodBean> methodBeanList = new ArrayList<>();
                        for(MethodBean methodBeanz : methodBean.getMethodCalls()) {
                            it.unisa.casper.storage.beans.MethodBean methodBean1x = new it.unisa.casper.storage.beans.MethodBean.Builder(methodBeanz.getName(),methodBeanz.getTextContent()).build();
                            methodBeanList.add(methodBean1x);
                        }
                        MethodBeanListAdapter methodBeanListAdapter = new MethodBeanListAdapter(methodBeanList);
                        methodBeanBuilder.setMethodsCalls(methodBeanListAdapter);

                        it.unisa.casper.storage.beans.MethodBean methodBean1 = methodBeanBuilder.build();

                        oldMethodBean.add(methodBean1);
                    }
                    MethodBeanListAdapter methodBeanListAdapter = new MethodBeanListAdapter(oldMethodBean);
                    classBeanBuilder.setMethods(methodBeanListAdapter);
                    classBeanBuilder.setBelongingPackage(new it.unisa.casper.storage.beans.PackageBean.Builder(packageBean.getName(),packageBean.getTextContent()).build());
                    //ottengo il path assoluto
                    String name = classBean.getBelongingPackage() + "/" + classBean.getName();
                    String[] list = name.split("/");
                    String root = list[0];
                    File source = new File(path);
                    boolean controllo = false;
                    int j = 0, i = 0;
                    File[] all = source.listFiles();
                    File[] in;
                    while (j < all.length && !controllo) {
                        in = all[j].listFiles();
                        if (in != null) {
                            while (i < in.length && !controllo) {
                                if (in[i].getName().equalsIgnoreCase(root)) {
                                    controllo = true;
                                    root = path + "/" + all[j].getName();
                                }
                                i++;
                            }
                        }
                        i = 0;
                        j++;
                    }
                    if (controllo) {
                        for (j = 0; j < list.length - 1; j++) {
                            root += "/" + list[j];
                        }
                    }
                    classBeanBuilder.setPathToFile(root);
                    if(classBean.getSuperclass() != null) {
                        classBeanBuilder.setSuperclass(classBean.getSuperclass());
                    }
                    classBeanBuilder.setLOC(classBean.getLOC());
                    ArrayList<it.unisa.casper.storage.beans.InstanceVariableBean> instanceVariableBeans = new ArrayList<it.unisa.casper.storage.beans.InstanceVariableBean>();
                    for(InstanceVariableBean instanceVariableBean : classBean.getInstanceVariables()) {
                        it.unisa.casper.storage.beans.InstanceVariableBean instanceVariableBean1 = new it.unisa.casper.storage.beans.InstanceVariableBean(instanceVariableBean.getName(),instanceVariableBean.getType(),instanceVariableBean.getInitialization(),instanceVariableBean.getVisibility());
                        instanceVariableBeans.add(instanceVariableBean1);
                    }
                    InstanceVariableList instanceVariableList = new InstanceVariableList();
                    instanceVariableList.setList(instanceVariableBeans);
                    classBeanBuilder.setInstanceVariables(instanceVariableList);

                    ArrayList<String> imports = new ArrayList<String>();
                    for(String imported : classBean.getImports()) {
                        imports.add(imported);
                    }
                    classBeanBuilder.setImports(imports);

                    ArrayList<it.unisa.casper.storage.beans.InstanceVariableBean> instanceVariableBeansArrayList = new ArrayList<>();


                    it.unisa.casper.storage.beans.ClassBean classBean1 = classBeanBuilder.build();

                    oldClassBeans.add(classBean1);
                }
                ClassBeanListAdapter classBeanListAdapter = new ClassBeanListAdapter(oldClassBeans);
                packageBeanBuilder.setClassList(classBeanListAdapter);
                it.unisa.casper.storage.beans.PackageBean packageBean1 = packageBeanBuilder.build();
                projectPackages.add(packageBean1);
            }

            HashMap<String, Double> coseno = new HashMap<String, Double>();
            HashMap<String, Integer> dipendence = new HashMap<String, Integer>();

            ArrayList<String> smell = new ArrayList<String>();
            smell.add("Feature");
            smell.add("Misplaced");
            smell.add("Blob");
            smell.add("Promiscuous");
            try {
                FileReader f = new FileReader(System.getProperty("user.home") + File.separator + ".casper" + File.separator + "threshold.txt");
                BufferedReader b = new BufferedReader(f);

                String[] list = null;
                for (String s : smell) {
                    list = b.readLine().split(",");
                    coseno.put("coseno" + s, Double.parseDouble(list[0]));
                    dipendence.put("dip" + s, Integer.parseInt(list[1]));
                    if (s.equalsIgnoreCase("promiscuous")) {
                        dipendence.put("dip" + s + "2", Integer.parseInt(list[2]));
                    }
                    if (s.equalsIgnoreCase("blob")) {
                        dipendence.put("dip" + s + "2", Integer.parseInt(list[2]));
                        dipendence.put("dip" + s + "3", Integer.parseInt(list[3]));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (it.unisa.casper.storage.beans.PackageBean packageBean : projectPackages) {

                packageAnalysis(coseno, dipendence, packageBean);

                for (it.unisa.casper.storage.beans.ClassBean classBean : packageBean.getClassList()) {

                    classAnalysis(coseno, dipendence, classBean);

                    for (it.unisa.casper.storage.beans.MethodBean methodBean : classBean.getMethodList()) {

                        methosAnalysis(coseno, dipendence, methodBean);
                    }
                }
            }

            //join sui thread
            for(Thread t : threadList){
                t.join();
            }

            return projectPackages;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParsingException();
        }
    }

    private void methosAnalysis(HashMap<String, Double> coseno, HashMap<String, Integer> dipendence, it.unisa.casper.storage.beans.MethodBean methodBean) {

        /*//ANALISI STORICA
        //feature envy

        HistoryFeatureEnvyStrategy historyFeatureEnvyStrategy = new HistoryFeatureEnvyStrategy(projectPackages);
        FeatureEnvyCodeSmell hFeatureEnvyCodeSmell = new FeatureEnvyCodeSmell(historyFeatureEnvyStrategy, "History");
        methodBean.isAffected(hFeatureEnvyCodeSmell);*/


        TextualFeatureEnvyStrategy textualFeatureEnvyStrategy = new TextualFeatureEnvyStrategy(projectPackages, coseno.get("cosenoFeature"));
        FeatureEnvyCodeSmell tFeatureEnvyCodeSmell = new FeatureEnvyCodeSmell(textualFeatureEnvyStrategy, "Textual");
        methodBean.isAffected(tFeatureEnvyCodeSmell);

        StructuralFeatureEnvyStrategy structuralFeatureEnvyStrategy = new StructuralFeatureEnvyStrategy(projectPackages, dipendence.get("dipFeature"));
        FeatureEnvyCodeSmell sFeatureEnvyCodeSmell = new FeatureEnvyCodeSmell(structuralFeatureEnvyStrategy, "Structural");
        methodBean.isAffected(sFeatureEnvyCodeSmell);

    }

    private void classAnalysis(HashMap<String, Double> coseno, HashMap<String, Integer> dipendence, it.unisa.casper.storage.beans.ClassBean classBean) {
        /*//ANALISI STORICA
        //blob
       HistoryBlobStrategy historyBlobStrategy = new HistoryBlobStrategy();
        BlobCodeSmell hBlobCodeSmell = new BlobCodeSmell(historyBlobStrategy, "History");
        Thread t = new Thread(new AnalyzerThread(classBean, hBlobCodeSmell));
        threadList.add(t);
        t.start();


        //Shotgun surgery
        HistoryShotgunSurgeryStrategy historyShotgunSurgeryStrategy = new HistoryShotgunSurgeryStrategy(projectPackages);
        ShotgunSurgeryCodeSmell shotgunSurgeryCodeSmell = new ShotgunSurgeryCodeSmell(historyShotgunSurgeryStrategy, "History");
        classBean.isAffected(shotgunSurgeryCodeSmell);



        //Divergent change
        HistoryDivergentChangeStrategy historyDivergentChangeStrategy = new HistoryDivergentChangeStrategy();
        DivergentChangeCodeSmell divergentChangeCodeSmell = new DivergentChangeCodeSmell(historyDivergentChangeStrategy, "History");
        classBean.isAffected(divergentChangeCodeSmell);



        //Parallel Inheritance
        HistoryParallelInheritanceStrategy historyParallelInheritanceStrategy = new HistoryParallelInheritanceStrategy(projectPackages);
        ParallelInheritanceCodeSmell parallelInheritanceCodeSmell = new ParallelInheritanceCodeSmell(historyParallelInheritanceStrategy, "History");
        classBean.isAffected(parallelInheritanceCodeSmell);*/


        TextualBlobStrategy textualBlobStrategy = new TextualBlobStrategy(coseno.get("cosenoBlob"));
        BlobCodeSmell tBlobCodeSmell = new BlobCodeSmell(textualBlobStrategy, "Textual");
        TextualMisplacedClassStrategy textualMisplacedClassStrategy = new TextualMisplacedClassStrategy(projectPackages, coseno.get("cosenoMisplaced"));
        MisplacedClassCodeSmell tMisplacedClassCodeSmell = new MisplacedClassCodeSmell(textualMisplacedClassStrategy, "Textual");
        classBean.isAffected(tBlobCodeSmell);
        classBean.isAffected(tMisplacedClassCodeSmell);
        classBean.setSimilarity(0);

        StructuralBlobStrategy structuralBlobStrategy = new StructuralBlobStrategy(dipendence.get("dipBlob"), dipendence.get("dipBlob2"), dipendence.get("dipBlob3"));
        BlobCodeSmell sBlobCodeSmell = new BlobCodeSmell(structuralBlobStrategy, "Structural");
        StructuralMisplacedClassStrategy structuralMisplacedClassStrategy = new StructuralMisplacedClassStrategy(projectPackages, dipendence.get("dipMisplaced"));
        MisplacedClassCodeSmell sMisplacedClassCodeSmell = new MisplacedClassCodeSmell(structuralMisplacedClassStrategy, "Structural");
        classBean.isAffected(sBlobCodeSmell);
        classBean.isAffected(sMisplacedClassCodeSmell);
        classBean.setSimilarity(0);

    }

    private void packageAnalysis(HashMap<String, Double> coseno, HashMap<String, Integer> dipendence, it.unisa.casper.storage.beans.PackageBean packageBean) {


        TextualPromiscuousPackageStrategy textualPromiscuousPackageStrategy = new TextualPromiscuousPackageStrategy(coseno.get("cosenoPromiscuous"));
        PromiscuousPackageCodeSmell tPromiscuousPackagecodeSmell = new PromiscuousPackageCodeSmell(textualPromiscuousPackageStrategy, "Textual");
        packageBean.isAffected(tPromiscuousPackagecodeSmell);
        packageBean.setSimilarity(0);

        StructuralPromiscuousPackageStrategy structuralPromiscuousPackageStrategy = new StructuralPromiscuousPackageStrategy(projectPackages, dipendence.get("dipPromiscuous") / 100, dipendence.get("dipPromiscuous2") / 100);
        PromiscuousPackageCodeSmell sPromiscuousPackagecodeSmell = new PromiscuousPackageCodeSmell(structuralPromiscuousPackageStrategy, "Structural");
        packageBean.isAffected(sPromiscuousPackagecodeSmell);
        packageBean.setSimilarity(0);

    }

    /**
     * Creates a PackageBean from an intellij PsiPackage.
     * The method obtains all the information from the
     * PsiPackage and uses them to create a PackageBean.
     *
     * @param generalPackage
     * @return
     */
    private PackageBean parse(GeneralPackage generalPackage) {
        //Package da parsare
        PackageAdapter packageAdapter = new PackageAdapter(generalPackage);
        return PackageParser.parse(packageAdapter);
        //return new PackageBean();
    }

    /**
     * Creates an ClassBean from an intellij psiClass.
     *
     * @param psiClass
     * @return ClassBean
     */
    /*public ClassBean parse(PsiClass psiClass, String contentForPackage) {
        ClassParser classParser = new ClassParser();
        CodeParser codeParser = new CodeParser();
        //Classe da parsare
        PsiJavaFile file = (PsiJavaFile) psiClass.getContainingFile();
        PsiPackage psiPackage = JavaPsiFacade.getInstance(project).findPackage(file.getPackageName());
        String pkgName = psiPackage.getQualifiedName();
        //PackageBean per supporto
        //PackageBean packageBean = new PackageBean.Builder(pkgName, contentForPackage).build();
        //analizzo la classe
        String name = psiClass.getQualifiedName();

        //creo la lista delle variabili d'istanza
        /*ClassBean.Builder builder = new ClassBean.Builder(name, text);
        builder.setBelongingPackage(packageBean);*/

        //ottengo il path assoluto
        /*TypeDeclaration typeDeclaration = null;
                name = name.replace(".", "/");
        try {
            CompilationUnit parsed = codeParser.createParser(FileUtility.readFile(name));
            typeDeclaration = (TypeDeclaration) parsed.types().get(0);
        }catch (IOException e) {
            e.printStackTrace();
        }

        List<String> imports = new ArrayList<>();
        for (PsiElement e : ((PsiJavaFile) psiClass.getContainingFile()).getImportList().getChildren()) {
            if (!e.getText().contains("\n")) {
                imports.add(e.getText());
            }
        }

        return classParser.parse(typeDeclaration,pkgName,imports);
        //builder.setImports(imports);
        //return builder.build();
    }*/

    /**
     * Creates a MethodBean from an intellij PsiMethod.
     * The method obtains all the information from the
     * PsiMethod and uses them to create a MethodBean.
     *
     * @param psiMethod the method to convert
     * @return MethodBean
     */
    public MethodBean parse(PsiMethod psiMethod, String textContent) {
        //MethodBan da Parsare

        //Genero la Lista di Variabili d'istanza utilizzate
        ArrayList<InstanceVariableBean> list = new ArrayList<>();
        // controllo se nel metodo ci sono variabili d'istanza
        try {
            PsiField[] fields = psiMethod.getContainingClass().getFields();
            String methodBody;
            if (psiMethod.getBody() == null) {
                methodBody = "";
            } else {
                methodBody = psiMethod.getBody().getText();
            }
            for (PsiField field : fields) {
                if (methodBody.contains(field.getName()))
                    list.add(parse(field));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return MethodParser.parse((MethodDeclaration)psiMethod,list);
    }

    /**
     * Creates an InstanceVariableBean from an intellij PsiField.
     * The method obtains the name,the visibility, the type and
     * the initialization value from the PsiField and uses them to
     * set InstanceVariableBean properties.
     *
     * @param psiField to convert.
     * @return the InstanceVariableBean corresponding to the given PsiField
     */
    public InstanceVariableBean parse(PsiField psiField) {
        return InstanceVariableParser.parse((FieldDeclaration)psiField);
    }

    /**
     * CODICE DI MANUEL ATTENZIONE
     * non toccare qui sennò Gesù si arrabbia
     *
     * @return Restituisce la lista dei Package
     */
    /*private Set<PsiPackage> getAllPackagesBeans() {

        final Set<PsiPackage> foundPackages = new HashSet<>();

        AnalysisScope scope = new AnalysisScope(this.project);
        scope.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitFile(final PsiFile file) {
                if (file instanceof PsiJavaFile) {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) file;
                    final PsiPackage aPackage = JavaPsiFacade.getInstance(project).findPackage(psiJavaFile.getPackageName());
                    if (aPackage != null) {
                        foundPackages.add(aPackage);
                    }
                }
            }
        });
        return foundPackages;
    }*/
}