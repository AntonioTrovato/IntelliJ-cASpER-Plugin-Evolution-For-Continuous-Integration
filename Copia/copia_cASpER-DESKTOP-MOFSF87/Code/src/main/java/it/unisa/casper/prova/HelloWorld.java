package it.unisa.casper.prova;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        System.out.println("Vuoi lanciare cASpER(s\\n)?");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        if(input.equals("s")) {
            runProcess("java -classpath C:\\Users\\anton\\Documents\\GitHub\\Tirocinio\\Copia\\copia_cASpER-DESKTOP-MOFSF87\\Code\\out\\" +
                    "production\\classes\\;C:\\Users\\anton\\Documents\\GitHub\\Tirocinio\\Copia\\copia_cASpER-DESKTOP-MOFSF87\\Code\\build\\idea-sandbox\\plugins\\casper\\lib\\commons-cli-1.4.jar it.unisa.casper.cli.Comm" +
                    "andLineCasper -op 5");
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }
}
