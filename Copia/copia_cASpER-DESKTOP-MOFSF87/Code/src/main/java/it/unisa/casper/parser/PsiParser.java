package it.unisa.casper.parser;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
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
import org.eclipse.jdt.core.dom.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//import it.unisa.casper.storage.beans.*;

public class PsiParser implements Parser {
    private Project project;
    private final List<PackageBean> projectPackages;
    private static String path;
    //Lista di threads
    private List<Thread> threadList;

    public PsiParser(Project project) {
        this.project = project;
        path = project.getBasePath();
        projectPackages = new ArrayList<PackageBean>();
        threadList = new ArrayList<>();
    }

    @Override
    public List<PackageBean> parse() throws ParsingException {
        try {
            PackageBean parsedPackageBean;
            Set<PsiPackage> listPackages = getAllPackagesBeans();
            for (PsiPackage psiPackage : listPackages) {
                parsedPackageBean = parse(psiPackage);
                projectPackages.add(parsedPackageBean);
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

    /**
     * Creates a PackageBean from an intellij PsiPackage.
     * The method obtains all the information from the
     * PsiPackage and uses them to create a PackageBean.
     *
     * @param psiPackage
     * @return
     */
    private PackageBean parse(PsiPackage psiPackage) {
        //Package da parsare
        PackageAdapter packageAdapter = new PackageAdapter(psiPackage);
        return PackageParser.parse(packageAdapter);
    }

    /**
     * Creates an ClassBean from an intellij psiClass.
     *
     * @param psiClass
     * @return ClassBean
     */
    public ClassBean parse(PsiClass psiClass, String contentForPackage) {
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
        TypeDeclaration typeDeclaration = null;
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
        /*builder.setImports(imports);
        return builder.build();*/
    }

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
        return MethodParser.parse((MethodDeclaration)psiMethod.getNode(),list);
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
        return InstanceVariableParser.parse((FieldDeclaration)psiField.getNode());
    }

    /**
     * CODICE DI MANUEL ATTENZIONE
     * non toccare qui sennò Gesù si arrabbia
     *
     * @return Restituisce la lista dei Package
     */
    private Set<PsiPackage> getAllPackagesBeans() {

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
    }
}