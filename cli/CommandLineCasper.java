package it.unisa.casper.cli;

import org.apache.commons.cli.*;

public class CommandLineCasper {

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(new Option("op",true,"Opzione di prova"));
        CommandLineParser parser = new DefaultParser();
        System.out.println(args[0]);
        CommandLine cmd = parser.parse(options,args);
        if(cmd.hasOption("op")) {
            System.out.println("Ha la op option");
            String optionValue = cmd.getOptionValue("op");
            if(optionValue == null) {
                System.out.println("Nessun valore per op");
            }else {
                System.out.println("Valore di op: " + optionValue);
            }
        }else {
            System.out.println("Non ha l'opzione op");
        }
    }

}
